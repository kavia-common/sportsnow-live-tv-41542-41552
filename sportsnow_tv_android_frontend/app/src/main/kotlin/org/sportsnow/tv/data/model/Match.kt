package org.sportsnow.tv.data.model

/**
 * PUBLIC_INTERFACE
 * Match domain model representing a live match.
 */
data class Match(
    val id: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int,
    val awayScore: Int,
    val status: String, // e.g., "LIVE", "FT", "HT"
    val startTime: Long,
    val highlights: List<Highlight> = emptyList()
)
