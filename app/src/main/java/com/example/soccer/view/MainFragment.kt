package com.example.soccer.view

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soccer.R
import com.example.soccer.viewmodel.adapter.SoccerAdapter
import com.example.soccer.databinding.FragmentMainBinding
import com.example.soccer.model.Lce
import com.example.soccer.model.data.service.ServiceLocator
import com.example.soccer.model.data.service.SoccerService
import com.example.soccer.viewmodel.SoccerViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val adapter by lazy {
        SoccerAdapter(
            context = requireContext(),
            onSoccerClicked = {
                findNavController().navigate(
                    MainFragmentDirections
                        .actionListMainToDetailsFragment(it.playerName)
                )
            }
        )
    }

    private val viewModel by viewModels<SoccerViewModel> {
        viewModelFactory {
            initializer {
                SoccerViewModel(
                    SoccerService.provideApiSource(),
                    ServiceLocator.provideDataSource())
            }
        }
    }

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

        with(binding) {
            val linearLayoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )

            toolbarMain
                .menu
                .findItem(R.id.search)
                .actionView
                .let { it as SearchView }
                .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        viewModel.onQueryChanged(newText)
                        return true
                    }
                })

            layoutSwipeRefresh.setOnRefreshListener {
                adapter.submitList(emptyList())
                viewModel.onLoadMore()
            }

            val linearLayoutManager = LinearLayoutManager(
                view.context,
                LinearLayoutManager.VERTICAL, false
            )

            recyclerView.adapter = adapter

            recyclerView.addItemDecoration(
                MaterialDividerItemDecoration(
                    requireContext(),
                    MaterialDividerItemDecoration.VERTICAL
                )
            )

            recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount) {
                        outRect.top = resources.getDimensionPixelSize(R.dimen.value_for_decorator)
                    }
                }
            })

            viewModel
                .currentLceFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .onEach { binding.layoutSwipeRefresh.isRefreshing = false }
                .onEach { lce ->
                    when (lce) {
                        is Lce.Loading -> {
                            binding.progress.isVisible = true
                        }
                        is Lce.ContentSoccer<*> -> {
                            progress.isVisible = false
                            layoutSwipeRefresh.isVisible = true
                            adapter.submitList(lce.listPlayer)
                            binding.progress.isVisible = false
                        }
                        is Lce.Error -> {
                            Toast.makeText(
                                requireContext(),
                                lce.throwable.message,
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





