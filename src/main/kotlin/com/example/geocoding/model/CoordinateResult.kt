package com.example.geocoding.model

import com.google.maps.model.GeocodingResult

data class CoordinateResult(
    val query: String,
    val placeId: String,
    val latitude: Double,
    val longitude: Double
) {
  companion object {
    fun from(query: String, geocodingResult: GeocodingResult): CoordinateResult {
      return CoordinateResult(
          query,
          geocodingResult.placeId,
          geocodingResult.geometry.location.lat,
          geocodingResult.geometry.location.lng
      )
    }

  }
}

