package com.example.amphibians.ui.screens

import android.text.Editable.Factory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.amphibians.AmphibiansPhotosApplication
import com.example.amphibians.data.AmphibiansPhotosRepository
import com.example.amphibians.model.AmphibiansPhoto
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface AmphibiansUiState {
    data class Success(val amphibians: List<AmphibiansPhoto>): AmphibiansUiState
    object Error : AmphibiansUiState
    object  Loading : AmphibiansUiState
}

class AmphibiansViewModel(private val amphibiansPhotosRepository: AmphibiansPhotosRepository) : ViewModel(){

    var amphibiansUiState : AmphibiansUiState by mutableStateOf(AmphibiansUiState.Loading)
        private  set

    init {
        getAmphibiansPhotos()
    }

    fun getAmphibiansPhotos(){
        viewModelScope.launch {
            amphibiansUiState = AmphibiansUiState.Loading
            amphibiansUiState = try {
                AmphibiansUiState.Success(amphibiansPhotosRepository.getAmphibiansPhotos())
            } catch (e: IOException){
                AmphibiansUiState.Error
            } catch (e: HttpException){
                AmphibiansUiState.Error
            }
        }
    }

    /**
     * Factory for [AmphibiansViewModel] that takes [AmphibiansPhotosRepository] as a dependency
     */

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as AmphibiansPhotosApplication)
                val amphibiansPhotosRepository = application.container.amphibiansPhotosRepository
                AmphibiansViewModel(amphibiansPhotosRepository = amphibiansPhotosRepository)
            }
        }
    }
}