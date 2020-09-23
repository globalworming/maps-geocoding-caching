package com.example.geocoding.screenplay

import com.google.maps.model.GeocodingResult
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Question

class LatestGeoCodingResults: Question<List<GeocodingResult>> {
  override fun answeredBy(actor: Actor): List<GeocodingResult> =
      actor.recall<Array<GeocodingResult>>("latest response").asList()

}