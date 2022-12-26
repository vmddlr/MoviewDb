package com.example.moviedb.ui.view.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.moviedb.core.util.Utilities
import com.example.moviedb.data.model.UIState
import com.example.moviedb.data.model.VideoEntity
import com.example.moviedb.databinding.FragmentVideoBinding
import com.example.moviedb.ui.viewmodel.VideoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentVideoBinding

    private val viewModel: VideoViewModel by viewModels()
    private var idMovie = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arg: VideoFragmentArgs by navArgs()
        arg.idMovie.let {
            idMovie = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val maxHeight = Utilities.displayMetrics(this, 0.35)
        val dialog = BottomSheetDialog(requireContext(), theme)
        return Utilities.dialogBottom(dialog, maxHeight)
    }

    private fun initFragment() {
        this.serviceVideo()
        this.serviceResponse()
    }

    private fun serviceResponse() {
        lifecycleScope.launchWhenStarted {
            viewModel.lsfResponse.collect { state ->
                when (state) {
                    is UIState.Success<*> -> {
                        when (state.entity) {
                            is VideoEntity -> serviceVideoResponse(state.entity)
                        }
                    }
                    is UIState.Error -> errorResponse(state.message)
                    is UIState.IsEmpty -> Unit
                }
            }
        }
    }

    private fun serviceVideo() {
        viewModel.getVideoService(this.requireContext(), idMovie)
    }

    private fun serviceVideoResponse(entity: VideoEntity) {
        if (entity.results[0].site == "YouTube") {
            try {
                binding.videoView.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        super.onReady(youTubePlayer)
                        youTubePlayer.loadVideo(entity.results[0].key, 0F)
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(
                    this.requireContext(),
                    "Error al consultar el vide",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        } else {
            Toast.makeText(
                this.requireContext(),
                "Error al consultar el vide",
                Toast.LENGTH_SHORT
            ).show()
            this.dismiss()
        }
    }

    private fun errorResponse(message: String) {
        Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}