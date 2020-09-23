package com.example.geocoding.screenplay

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Performable

open class ClearsCache: Performable {


  override fun <T : Actor> performAs(actor: T) {
    ManageTheCache.`as`(actor).cacheManager.getCache("geoCodingResults")!!.clear()
  }
}