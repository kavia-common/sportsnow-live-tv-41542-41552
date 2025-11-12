package org.sportsnow.tv

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.sportsnow.tv.data.Repository
import org.sportsnow.tv.data.model.Highlight
import org.sportsnow.tv.databinding.ActivityDetailsBinding

/**
 * PUBLIC_INTERFACE
 * DetailsActivity displays basic details for a selected match and allows playing first highlight.
 */
class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MATCH_ID = "extra_match_id"
    }

    private lateinit var binding: ActivityDetailsBinding
    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val matchId = intent.getStringExtra(EXTRA_MATCH_ID) ?: run {
            finish()
            return
        }

        lifecycleScope.launch {
            repository.match(matchId).collectLatest { match ->
                if (match == null) {
                    binding.title.text = getString(R.string.app_name)
                    binding.subtitle.text = "Match not found"
                    binding.playButton.isEnabled = false
                } else {
                    binding.title.text = "${match.homeTeam} vs ${match.awayTeam}"
                    binding.subtitle.text = "Score: ${match.homeScore} - ${match.awayScore}  |  ${match.status}"
                    val first: Highlight? = match.highlights.firstOrNull()
                    binding.playButton.isEnabled = first != null
                    binding.playButton.setOnClickListener {
                        first?.let {
                            val i = Intent(this@DetailsActivity, PlayerActivity::class.java)
                            i.putExtra(PlayerActivity.EXTRA_MEDIA_URL, it.url)
                            startActivity(i)
                        }
                    }
                }
            }
        }
    }
}
