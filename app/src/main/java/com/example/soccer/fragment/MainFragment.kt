package com.example.soccer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soccer.*
import com.example.soccer.adapter.SoccerAdapter
import com.example.soccer.databinding.FragmentMainBinding
import com.example.soccer.adapter.Paging
import com.example.soccer.adapter.addHorizontalSpaceDecoration
import com.example.soccer.adapter.addPaginationScrollListener
import com.example.soccer.model.Player
import com.example.soccer.retrofit.ApiSoccerService
import kotlinx.android.synthetic.main.fragment_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val adapter = SoccerAdapter { player ->
        findNavController().navigate(
            MainDirections.toDetails(player.playerName)
        )
    }

    private var isLoading = false
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMainBinding.inflate(inflater, container, false)
            .also { binding -> _binding = binding }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPlayer()

        with(binding) {
            toolbar
                .menu
                .findItem(R.id.search)
                .let { it.actionView as SearchView }
                .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        return true
                    }
                })

            layoutSwipeRefresh.setOnRefreshListener {
                adapter.submitList(emptyList())
                initPlayer() {
                    layoutSwipeRefresh.isRefreshing = false
                }
            }

            val linearLayoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )

            recyclerView.adapter = adapter
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.addHorizontalSpaceDecoration(RECYCLER_ITEM_SPACE)
            recyclerView.addPaginationScrollListener(linearLayoutManager, COUNT_TO_LOAD) {
                initPlayer()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initPlayer(onLoadingFinished: () -> Unit = {}) {
        if (isLoading) return

        isLoading = true

        val loadingFinishedCallback = {
            isLoading = false
            onLoadingFinished()
        }

        val since = currentPage * PAGE_SIZE
        ApiSoccerService.soccerApi.getPlayers(since, PAGE_SIZE)
            .enqueue(object : Callback<List<Player>> {
                override fun onResponse(
                    call: Call<List<Player>>,
                    response: Response<List<Player>>
                ) {
                    if (response.isSuccessful) {
                        val newList = adapter.currentList
                            .dropLastWhile { it == Paging.Loading }
                            .plus(response.body()?.map { Paging.Content(it) }.orEmpty())
                            .plus(Paging.Loading)
                        adapter.submitList(newList)
                        currentPage++
                    } else {
                        processingException(Throwable())
                    }

                    loadingFinishedCallback()
                }

                override fun onFailure(call: Call<List<Player>>, t: Throwable) {
                    processingException(t)
                    loadingFinishedCallback()
                }
            })
    }

    private fun processingException(e: Throwable) {
        Toast.makeText(requireContext(), e.message ?: "Error", Toast.LENGTH_SHORT)
            .show()
    }

    companion object {
        private const val RECYCLER_ITEM_SPACE = 20

        private const val PAGE_SIZE = 30
        private const val COUNT_TO_LOAD = 15

    }
}