package de.thm.mobiletech.hideandguess.rest

import org.apache.commons.text.StringEscapeUtils

fun String.escape(): String {
    return StringEscapeUtils.escapeJson(this)
}