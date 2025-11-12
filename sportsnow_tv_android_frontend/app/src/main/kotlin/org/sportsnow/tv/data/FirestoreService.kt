package org.sportsnow.tv.data

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.sportsnow.tv.data.model.Highlight
import org.sportsnow.tv.data.model.Match

/**
 * Simple Firestore access wrapper.
 * If Firebase isn't initialized, emits empty results and logs warnings.
 */
internal class FirestoreService {
    private val db: FirebaseFirestore? = try {
        FirebaseFirestore.getInstance()
    } catch (t: Throwable) {
        Log.w("SportsNowTV", "Firestore unavailable: ${t.message}")
        null
    }

    fun observeMatches(): Flow<List<Match>> = callbackFlow {
        val instance = db
        if (instance == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        val reg = instance.collection("matches")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("SportsNowTV", "Firestore matches listen error", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val list = value?.documents?.mapNotNull { it.toMatch() } ?: emptyList()
                trySend(list)
            }
        awaitClose { reg.remove() }
    }

    fun observeMatch(matchId: String): Flow<Match?> = callbackFlow {
        val instance = db
        if (instance == null) {
            trySend(null)
            close()
            return@callbackFlow
        }
        val reg = instance.collection("matches").document(matchId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("SportsNowTV", "Firestore match listen error", error)
                    trySend(null)
                    return@addSnapshotListener
                }
                trySend(value?.toMatch())
            }
        awaitClose { reg.remove() }
    }
}

private fun DocumentSnapshot.toMatch(): Match? {
    val id = id
    val homeTeam = getString("homeTeam") ?: return null
    val awayTeam = getString("awayTeam") ?: return null
    val homeScore = getLong("homeScore")?.toInt() ?: 0
    val awayScore = getLong("awayScore")?.toInt() ?: 0
    val status = getString("status") ?: "UPCOMING"
    val startTime = getLong("startTime") ?: 0L
    val highlights = (get("highlights") as? List<Map<String, Any?>>)?.map {
        Highlight(
            title = it["title"] as? String ?: "Highlight",
            url = it["url"] as? String ?: "",
            thumbnail = it["thumbnail"] as? String,
            durationSec = (it["durationSec"] as? Number)?.toInt()
        )
    } ?: emptyList()
    return Match(
        id = id,
        homeTeam = homeTeam,
        awayTeam = awayTeam,
        homeScore = homeScore,
        awayScore = awayScore,
        status = status,
        startTime = startTime,
        highlights = highlights
    )
}
