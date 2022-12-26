package com.example.moviedb.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.moviedb.data.model.*
import com.example.moviedb.databinding.FragmentHomeBinding
import com.example.moviedb.ui.adapter.GeneralAdapter
import com.example.moviedb.ui.adapter.UpComingAdapter
import com.example.moviedb.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setupInitFragment()
        this.setupObserver()
    }


    private fun setupInitFragment() {
        this.multiServiceRequest()
        this.servicesResponse()
    }

    private fun setupObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            this.binding.progress.isVisible = it
        }
    }


    private fun servicesResponse() {
        lifecycleScope.launchWhenStarted {
            viewModel.lsfResponse.collect { state ->
                when (state) {
                    is UIState.Success<*> -> {
                        when (state.entity) {
                            is UpComingEntity -> serviceUpComingResponse(state.entity)
                            is TopRateEntity -> serviceTopRateResponse(state.entity)
                        }
                    }
                    is UIState.Error -> errorResponse(state.message)
                    is UIState.IsEmpty -> Unit
                }
            }
        }
    }

    private fun multiServiceRequest() {
        viewModel.getMultiService(this.requireContext())
    }

    private fun serviceUpComingResponse(entity: UpComingEntity) {
        viewModel.clearUIState()
        this.binding.rvUpComing.apply {
            adapter = UpComingAdapter(entity.listUpComing) {
                clickDetailUpComing(it)
            }
        }
    }

    private fun serviceTopRateResponse(entity: TopRateEntity) {
        viewModel.clearUIState()
        this.binding.rvTopRate.apply {
            adapter = GeneralAdapter(entity.listTopRate) {
                clickDetailTopRate(it)
            }
        }

        this.movieRecommendation(entity.listTopRate)
    }

    private fun movieRecommendation(listTopRate: ArrayList<TopRateDetailEntity>) {
        this.binding.rvRecommendation.apply {
            adapter = GeneralAdapter(listTopRate.take(6)) {
                clickDetailTopRate(it)
            }
        }
    }

    private fun clickDetailUpComing(entity: UpComingDetailEntity) {
        val action = HomeFragmentDirections.toDetailFragment(entity, null)
        this.requireView().findNavController().navigate(action)
    }

    private fun clickDetailTopRate(entity: TopRateDetailEntity) {
        val action = HomeFragmentDirections.toDetailFragment(null, entity)
        this.requireView().findNavController().navigate(action)
    }

    private fun errorResponse(message: String) {
        Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}