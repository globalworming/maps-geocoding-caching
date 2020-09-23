package com.example.geocoding.service

import com.example.geocoding.model.CoordinateResult
import com.google.maps.GeoApiContext
import com.google.maps.GeoApiContext.*
import com.google.maps.GeocodingApi
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

  private lateinit var ourContext: GeoApiContext

  @PostConstruct
  open fun init() {
    if (apiKey == "FIXME") {
      throw RuntimeException("no system property ")
    }
    ourContext = Builder().apiKey(apiKey).build()
  }


  @Cacheable(cacheName)
  open fun coordinates(query: String, apiKey: String?): Array<CoordinateResult> {
    val apiContext = if (apiKey != null) apiContext(apiKey) else ourContext;
    return GeocodingApi.geocode(apiContext, query).await()
        .map {CoordinateResult.from(query, it)}
        .toTypedArray()
  }

  @Cacheable("apiContext")
  open fun apiContext(apiKey: String): GeoApiContext = Builder().apiKey(apiKey).build()


}