package com.example.geocoding.screenplay

import net.serenitybdd.core.exceptions.TestCompromisedException
import net.serenitybdd.screenplay.Ability
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.RefersToActor
import org.springframework.cache.CacheManager

class ManageTheCache(val cacheManager: CacheManager) : Ability, RefersToActor {
  private var actor: Actor? = null

  override fun <T : Ability?> asActor(actor: Actor): T {
    this.actor = actor
    return this as T
  }

  companion object {
    fun `as`(actor: Actor): ManageTheCache {
      if (actor.abilityTo(ManageTheCache::class.java) == null) {
        throw TestCompromisedException(actor.name + " misses the ability")
      }
      return actor.abilityTo(ManageTheCache::class.java).asActor(actor)
    }
  }
}