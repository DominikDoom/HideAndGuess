package de.thm.mobiletech.hideandguess.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import androidx.lifecycle.MutableLiveData
import com.google.android.renderscript.Toolkit
import de.thm.mobiletech.hideandguess.util.ImagePHash
import kotlinx.coroutines.*
import kotlin.math.abs
import kotlin.math.log10


class BlurDrawView : AppCompatImageView {

    // Settings for blur painting
    private val blurFactor = 3
    private val radius = 100f

    // The Points to draw circles on
    private val points = arrayListOf<PointF>()

    // Inverse matrix to scale the canvas operations back to the bitmap
    private val inverse = Matrix()

    // Buffers to get modified bitmap
    private lateinit var bufferCanvas: Canvas
    private lateinit var bufferBitmap: Bitmap

    // PHash buffers
    // These sizes are necessary to retain enough high frequency information
    // for full blurs to be evaluated as less similar than partial blurs
    private val pHash = ImagePHash(64, 16) // (32,8) or (42,12) or (64,16) are good values
    private var originalPHash: Long? = null

    private lateinit var original: Bitmap
    private lateinit var blurred: Bitmap
    private val paint = Paint()

    private var shouldDraw = false

    // Constructors
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (drawable == null) return
        if(isInEditMode) {
            // Skip toolkit blur to make the view work in the editor
            blurred = Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888)
            bufferBitmap = Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888)
            bufferCanvas = Canvas(bufferBitmap)
            return
        }

        initialize()
    }

    /**
     * Blurs the given bitmap by the global blur factor.
     * @return the blurred bitmap
     * */
    private fun getBlurredBitmap(source: Bitmap): Bitmap {
        var tempBitmap = source

        // Downsample the source before blurring to improve performance and blur amount
        tempBitmap = tempBitmap.scale(source.width / blurFactor, source.height / blurFactor)

        // Blur the bitmap, doing multiple iterations to increase the blur amount
        for (i in 0 until blurFactor)
            tempBitmap = Toolkit.blur(tempBitmap, 25)

        // Upsample the bitmap to the original size again
        tempBitmap = tempBitmap.scale(source.width, source.height)

        return tempBitmap
    }

    override fun onDraw(canvas: Canvas) {
        // Apply transforms so that the blurred bitmap is drawn at the right position
        paint.shader?.setLocalMatrix(imageMatrix)
        imageMatrix.invert(inverse)
        bufferCanvas.setMatrix(inverse)
        // Draw the original image
        super.onDraw(canvas)

        for (point in points) {
            canvas.drawCircle(point.x, point.y, radius, paint) // draw with transforms for visible outcome
            bufferCanvas.drawCircle(point.x, point.y, radius, paint) // draw with correct source measurements for calculations
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action: Int = event.action

        shouldDraw = action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE
        val x = event.x
        val y = event.y

        // Check if another point is already near the current one to prevent multiple points on the same spot
        var redundant = false
        for (point in points) {
            if (abs(point.x - x) < radius/4 && abs(point.y - y) < radius/4) {
                redundant = true
                break
            }
        }

        // Add the point if it is not redundant
        if (shouldDraw && !redundant) {
            points.add(PointF(x, y))
            invalidate()
        }

        // Update percentage of the image that is covered by the mask
        if (action == MotionEvent.ACTION_UP) {
           updateScoreMultiplier()
        }
        return true
    }

    val currentMultiplier: MutableLiveData<Double> = MutableLiveData(24.16)

    private fun updateScoreMultiplier() {
        var multiplier = 0.0
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            withContext(Dispatchers.Default) {
                val distance = async { getPHashDistance() }
                multiplier = 1.0 / log10(1.1 + distance.await())
                Log.d("BlurDrawView", "Multiplier: $multiplier")
            }
            currentMultiplier.value = multiplier
        }
    }

    private fun getPHashDistance(): Int {
        val partialBlurPHash = pHash.calcPHash(bufferBitmap)
        return ImagePHash.distance(originalPHash!!, partialBlurPHash)
    }

    fun reset() {
        points.clear()
        bufferBitmap = original.copy(Bitmap.Config.ARGB_8888, true)
        bufferCanvas = Canvas(bufferBitmap)
        updateScoreMultiplier()
        invalidate()
    }

    fun initialize() {
        if (drawable == null) return

        original = drawable.toBitmap()
        originalPHash = pHash.calcPHash(original)

        blurred = getBlurredBitmap(original)
        paint.shader = BitmapShader(blurred, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        bufferBitmap = original.copy(Bitmap.Config.ARGB_8888, true)
        bufferCanvas = Canvas(bufferBitmap)
    }
}