package org.sportsnow.tv.data.model

/**
 * PUBLIC_INTERFACE
 * Highlight clip metadata.
 */
data class Highlight(
    val title: String,
    val url: String,
    val thumbnail: String? = null,
    val durationSec: Int? = null
)
