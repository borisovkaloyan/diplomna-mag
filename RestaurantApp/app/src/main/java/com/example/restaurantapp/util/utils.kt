package com.example.restaurantapp.util

import org.openapitools.client.models.MenuItem
import java.security.MessageDigest

fun String.sha256(): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}

fun MenuItem.fullImageUrl(baseUrl: String): String {
    return baseUrl.trimEnd('/') + image.toString()
}