package com.example.moviedb.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.core.util.URLConstant
import com.example.moviedb.core.util.Utilities
import com.example.moviedb.data.model.GeneralResponse
import com.example.moviedb.data.model.UIState
import com.example.moviedb.data.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase
) : ViewModel() {

    var isLoading = MutableLiveData<Boolean>()
    private var msfResponse = MutableStateFlow<UIState>(UIState.IsEmpty)
    var lsfResponse = msfResponse.asStateFlow()

    fun getMultiService(context: Context){
        viewModelScope.launch {
            viewModelScope.launch {
                isLoading.postValue(true)
//                val room = roomRepository.getAllNowPlaySelectRepository()

                if(Utilities.checkForInternet(context)) {
                    val upComing = async {
                        useCase.getServiceUpComing(URLConstant.upComing)
                    }
                    msfResponse.value = resulUIState(upComing.await())

                    val topRate = async {
                        useCase.getServiceTopRate(URLConstant.topRated)
                    }
                    msfResponse.value = resulUIState(topRate.await())

//                } else {
//                    room.let {
//                        val entity = NowPlayEntityResponse(
//                            results = roomRepository.getAllNowPlaySelectRepository(),
//                            page = 1
//                        )
//
//                        mldNowPlayViewModel.postValue(entity)
//                    }
                }

                isLoading.postValue(false)
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