package org.sportsnow.tv.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sportsnow.tv.data.model.Highlight
import org.sportsnow.tv.data.model.Match

/**
 * PUBLIC_INTERFACE
 * Repository provides reactive streams of matches and a match details stream.
 */
class Repository {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val service = FirestoreService()

    private val _fallbackMatches = MutableStateFlow(sampleMatches())
    val matches: StateFlow<List<Match>> = _fallbackMatches

    init {
        // Bridge Firestore into fallback state; if service emits, overwrite
        scope.launch {
            service.observeMatches()
                .catch { /* ignore, keep fallback */ }
                .collect { list -> _fallbackMatches.value = list.ifEmpty { sampleMatches() } }
        }
    }

    // PUBLIC_INTERFACE
    fun match(matchId: String): Flow<Match?> {
        return service.observeMatch(matchId)
            .map { it ?: sampleMatches().find { m -> m.id == matchId } }
            .catch { emit(sampleMatches().find { it.id == matchId }) }
    }

    private fun sampleMatches(): List<Match> {
        return listOf(
            Match(
                id = "match_1",
                homeTeam = "Falcons",
                awayTeam = "Tigers",
                homeScore = 2,
                awayScore = 1,
                status = "LIVE",
                startTime = System.currentTimeMillis() - 3600_000,
                highlights = listOf(
                    Highlight("Goal 1", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
                    Highlight("Goal 2", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
                )
            ),
            Match(
                id = "match_2",
                homeTeam = "Wolves",
                awayTeam = "Eagles",
                homeScore = 0,
                awayScore = 0,
                status = "UPCOMING",
                startTime = System.currentTimeMillis() + 7200_000
            )
        )
    }
}
