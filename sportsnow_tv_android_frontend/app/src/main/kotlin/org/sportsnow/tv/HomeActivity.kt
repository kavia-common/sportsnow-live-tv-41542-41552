package org.sportsnow.tv

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.sportsnow.tv.data.Repository
import org.sportsnow.tv.data.model.Match
import org.sportsnow.tv.databinding.ActivityHomeBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * HomeActivity renders a simple RecyclerView list of matches (non-Compose).
 * Selecting an item opens DetailsActivity.
 */
class HomeActivity : AppCompatActivity(), MatchAdapter.MatchClickListener {

    private lateinit var binding: ActivityHomeBinding
    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.matchList.layoutManager = LinearLayoutManager(this)
        val adapter = MatchAdapter(this)
        binding.matchList.adapter = adapter

        lifecycleScope.launch {
            repository.matches.collectLatest { list ->
                adapter.submitList(list)
            }
        }
    }

    override fun onMatchClicked(match: Match) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_MATCH_ID, match.id)
        startActivity(intent)
    }
}
