package com.example.moviedb.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.core.util.URLConstant
import com.example.moviedb.core.util.Utilities
import com.example.moviedb.data.model.GeneralResponse
import com.example.moviedb.data.model.UIState
import com.example.moviedb.data.usecase.VideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val useCase: VideoUseCase
) : ViewModel() {

    private var msfResponse = MutableStateFlow<UIState>(UIState.IsEmpty)
    var lsfResponse = msfResponse.asStateFlow()

    fun getVideoService(context: Context, id: String) {
        viewModelScope.launch {
            if (Utilities.checkForInternet(context)) {
                val video = async {
                    useCase.getServiceVideo(URLConstant.video.replace("movie_id", id))
                }
                msfResponse.value = resulUIState(video.await())
            }
        }
    }

    private fun <T> resulUIState(response: GeneralResponse<T>) =
        if (response.entity != null) {
            UIState.Success(response.entity)
        } else {
            UIState.Error(response.messageError)
        }

    fun clearUIState() = UIState.IsEmpty
}