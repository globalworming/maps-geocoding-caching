package com.example.geocoding.screenplay

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Performable
import net.serenitybdd.screenplay.Tasks
import net.thucydides.core.annotations.Step

open class RequestGeoCode(var query: String?) : Performable {

  @Step("{0} requests geocoding for \"#query\"")
  override fun <T : Actor> performAs(actor: T) {
    actor.remember("latest query", query)
    actor.remember("latest response", UseTheGeoCoder.`as`(actor).googleGeoCodingServiceFacade.geoCode(query!!))
  }

  companion object {
    fun of(query: String): RequestGeoCode = Tasks.instrumented(RequestGeoCode::class.java, query)
  }
}