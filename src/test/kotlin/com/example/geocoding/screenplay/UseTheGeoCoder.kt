package com.example.geocoding.screenplay

import com.example.geocoding.service.GoogleGeoCodingServiceFacade
import net.serenitybdd.core.exceptions.TestCompromisedException
import net.serenitybdd.screenplay.Ability
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.RefersToActor

class UseTheGeoCoder(val googleGeoCodingServiceFacade: GoogleGeoCodingServiceFacade) : Ability, RefersToActor {
  private var actor: Actor? = null

  override fun <T : Ability?> asActor(actor: Actor): T {
    this.actor = actor
    return this as T
  }

  companion object {
    fun `as`(actor: Actor): UseTheGeoCoder {
      if (actor.abilityTo(UseTheGeoCoder::class.java) == null) {
        throw TestCompromisedException(actor.name + " misses the ability")
      }
      return actor.abilityTo(UseTheGeoCoder::class.java).asActor(actor)
    }
  }
}