package com.example.geocoding.screenplay

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Performable

open class RequestTheSameLocationAgain : Performable {
  override fun <T : Actor> performAs(actor: T) = actor.attemptsTo(RequestGeoCode.of(actor.recall("latest query")))
}