package com.example.geocoding.screenplay

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.GivenWhenThen.*
import net.serenitybdd.screenplay.Performable
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers
import kotlin.test.fail

open class EnsuresThatTheResultsAreValid: Performable {
  override fun <T : Actor> performAs(actor: T) {

    actor.should(seeThat(
        { it.asksFor(LatestGeoCodingResults())[0].formattedAddress },
        `is`(actor.recall("latest query") as String)))
    actor.should(seeThat(
        { it.asksFor(LatestGeoCodingResults())[0].placeId },
        not(nullValue())))
  }
}