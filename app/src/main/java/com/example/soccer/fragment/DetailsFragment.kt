package com.example.soccer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.soccer.databinding.FragmentDetailsBinding
import com.example.soccer.model.DetailsSoccer
import com.example.soccer.retrofit.ApiSoccerService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val args by navArgs<DetailsFragmentArgs>()

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

        init()

        with(binding) {
            toolbar.setupWithNavController(findNavController())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init() {
        ApiSoccerService
            .soccerApi.getSoccerDetails(args.username)
            .enqueue(object : Callback<DetailsSoccer> {
                override fun onResponse(
                    call: Call<DetailsSoccer>,
                    response: Response<DetailsSoccer>
                ) {
                    if (response.isSuccessful) {
                        val playerDetails = response.body() ?: return
                        with(binding) {
                            image.load(playerDetails.playerImage)
                            name.text = playerDetails.playerName
                            age.text = playerDetails.playerAge
                            number.text = playerDetails.playerNumber
                            playerMatch.text = playerDetails.playerMatchPlayed
                        }
                    } else {
                        processingException(Throwable())
                    }
                }

                override fun onFailure(call: Call<DetailsSoccer>, t: Throwable) {
                    processingException(t)
                }
            })
    }

    private fun processingException(e: Throwable) {
        Toast.makeText(requireContext(), e.message ?: "Error", Toast.LENGTH_SHORT)
            .show()
    }
}