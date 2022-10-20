package com.example.soccer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.soccer.R
import com.example.soccer.databinding.FragmentDetailsBinding
import com.example.soccer.model.Lce
import com.example.soccer.model.data.service.ServiceLocator
import com.example.soccer.model.data.service.SoccerService
import com.example.soccer.viewmodel.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.loaging.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val args by navArgs<DetailsFragmentArgs>()

    private val viewModel by viewModels<DetailsViewModel> {
        viewModelFactory {
            initializer {
                DetailsViewModel(SoccerService.provideApiSource(), args.player)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDetailsBinding.inflate(inflater, container, false)
            .also { binding -> _binding = binding }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            toolbarDetails.setNavigationOnClickListener{
                findNavController().navigate(R.id.main)
            }

            viewModel
                .detailFragmentFlow
                .onEach { lce ->
                    when (lce) {
                        Lce.Loading -> {
                            progress.isVisible = true
                        }
                        is Lce.ContentSoccer -> {
                            progress.isVisible = false
                            image.load(lce.listPlayer.playerImage)
                            name.text = lce.listPlayer.playerName
                            age.text = lce.listPlayer.playerAge
                            number.text = lce.listPlayer.playerNumber
                            playerMatch.text = lce.listPlayer.playerMatchPlayed
                        }
                        is Lce.Error -> {
                            Toast.makeText(
                                requireContext(),
                                lce.throwable.message,
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}