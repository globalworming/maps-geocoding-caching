package com.example.geocoding.service

import com.google.maps.GeoApiContext
import com.google.maps.GeoApiContext.*
import com.google.maps.GeocodingApi
import com.google.maps.model.GeocodingResult
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
open class GoogleGeoCodingServiceFacade {

  companion object {
    const val cacheName = "geoCodingResults"
  }

  @Value("\${maps.api.key}")
  private lateinit var apiKey: String

  private lateinit var context: GeoApiContext

  @PostConstruct
  open fun init() {
    if (apiKey == "FIXME") {
      throw RuntimeException("enter you api keys into the application.properties, see https://developers.google.com/maps/documentation/geocoding/get-api-key")
    }
    context = Builder().apiKey(apiKey).build()
  }


  @Cacheable(cacheName)
  open fun coordinates(query: String): Array<GeocodingResult> {
    return GeocodingApi.geocode(context, query).await()
  }



}