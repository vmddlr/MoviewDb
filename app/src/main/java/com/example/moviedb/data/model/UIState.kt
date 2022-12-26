package com.example.moviedb.data.model

sealed class UIState {
    data class Success<T>(val entity: T) : UIState()
    data class Error(val message: String) : UIState()
    object IsEmpty : UIState()
}
