package org.sportsnow.tv

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import org.sportsnow.tv.databinding.ActivityPlayerBinding

/**
 * PUBLIC_INTERFACE
 * PlayerActivity plays a single media URL using Media3 ExoPlayer.
 */
class PlayerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MEDIA_URL = "extra_media_url"
    }

    private lateinit var binding: ActivityPlayerBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra(EXTRA_MEDIA_URL)
        if (url.isNullOrBlank()) {
            finish()
            return
        }

        player = ExoPlayer.Builder(this).build().also { exo ->
            binding.playerView.player = exo
            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            exo.setMediaItem(mediaItem)
            exo.prepare()
            exo.playWhenReady = true
        }
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}
