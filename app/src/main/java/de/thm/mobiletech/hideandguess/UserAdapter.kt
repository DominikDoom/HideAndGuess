package de.thm.mobiletech.hideandguess

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val dataSet: List<User>, private val onClick: (User) -> Unit): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(view: View, val onClick: (User) -> Unit) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val imageView: ImageView
        var currentUser: User? = null

        init {
            textView = view.findViewById(R.id.textViewUser)
            imageView = view.findViewById(R.id.imageViewUser)

            view.setOnClickListener {
                currentUser?.let {
                    onClick(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_user, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.currentUser = dataSet[position]
        holder.textView.text = dataSet[position].username
        holder.imageView.setImageResource(R.drawable.example_user_image1)
    }

    override fun getItemCount() = dataSet.size

}