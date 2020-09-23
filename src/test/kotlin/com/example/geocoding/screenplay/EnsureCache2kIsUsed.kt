package com.example.geocoding.screenplay

import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.Performable
import org.cache2k.Cache
import kotlin.test.assertTrue

open class EnsureCache2kIsUsed: Performable {


  override fun <T : Actor> performAs(actor: T) {
    assertTrue(ManageTheCache.`as`(actor).cacheManager.getCache("geoCodingResults")!!.nativeCache is Cache<*, *>)
  }
}