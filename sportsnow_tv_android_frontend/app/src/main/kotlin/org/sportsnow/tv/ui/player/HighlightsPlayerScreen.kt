package org.sportsnow.tv.ui.player

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import org.sportsnow.tv.data.Repository

/**
 * PUBLIC_INTERFACE
 * HighlightsPlayerScreen plays a list of highlights for a given match.
 */
@Composable
fun HighlightsPlayerScreen(
    matchId: String,
    startIndex: Int,
    repository: Repository,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val match by repository.match(matchId).collectAsState(initial = null)
    val items = match?.highlights ?: emptyList()

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
        }
    }

    DisposableEffect(matchId) {
        onDispose {
            exoPlayer.release()
        }
    }

    LaunchedEffect(items) {
        exoPlayer.clearMediaItems()
        items.forEach { h ->
            if (h.url.isNotBlank()) {
                exoPlayer.addMediaItem(MediaItem.fromUri(h.url))
            }
        }
        if (items.isNotEmpty()) {
            exoPlayer.prepare()
            exoPlayer.seekTo(startIndex.coerceIn(0, items.lastIndex), 0L)
            exoPlayer.play()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = onBack, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )) {
                Text("Back")
            }
            Button(onClick = {
                if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
            }) {
                Text("Play/Pause")
            }
            Button(onClick = {
                val next = (exoPlayer.currentMediaItemIndex + 1).coerceAtMost((items.size - 1).coerceAtLeast(0))
                exoPlayer.seekTo(next, 0)
                exoPlayer.play()
            }) {
                Text("Next")
            }
        }
    }
}
