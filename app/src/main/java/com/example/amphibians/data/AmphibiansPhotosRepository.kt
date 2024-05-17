package com.example.amphibians.data

import com.example.amphibians.model.AmphibiansPhoto
import com.example.amphibians.network.AmphibiansApiService

interface AmphibiansPhotosRepository {

    suspend fun getAmphibiansPhotos(): List<AmphibiansPhoto>
}

class NetworkAmphibiansRepository(
    private val amphibiansApiService: AmphibiansApiService
) : AmphibiansPhotosRepository {
    override suspend fun getAmphibiansPhotos(): List<AmphibiansPhoto> = amphibiansApiService.getPhotos()
}
