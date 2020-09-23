package com.example.geocoding.atdd

import com.example.geocoding.screenplay.ManageTheCache
import com.example.geocoding.screenplay.UseTheGeoCoder
import com.example.geocoding.screenplay.actorName
import com.example.geocoding.screenplay.ClearsCache
import com.example.geocoding.screenplay.EnsureCache2kIsUsed
import com.example.geocoding.screenplay.EnsuresThatTheResultsAreValid
import com.example.geocoding.screenplay.LatestGeoCodingResults
import com.example.geocoding.screenplay.RequestGeoCode
import com.example.geocoding.screenplay.RequestTheSameLocationAgain
import com.example.geocoding.service.GoogleGeoCodingServiceFacade
import com.google.maps.model.GeocodingResult
import net.serenitybdd.junit.runners.SerenityRunner
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.GivenWhenThen.*
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.CacheManager
import java.util.*


@RunWith(SerenityRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class Cache2kAndCachableAnnotationIT {

  @Rule
  @JvmField
  var springIntegrationMethodRule = SpringIntegrationMethodRule()

  @Autowired
  lateinit var cacheManager: CacheManager

  @MockBean
  private lateinit var googleGeoCodingServiceFacade: GoogleGeoCodingServiceFacade

  private lateinit var admin: Actor
  private val apiUser = Actor(actorName.get())


  @Before
  fun setUp() {
    admin = Actor("admin")
    admin.can(ManageTheCache(cacheManager))
    apiUser.can(UseTheGeoCoder(googleGeoCodingServiceFacade))
    Mockito.`when`(googleGeoCodingServiceFacade.coordinates(anyString()))
        .thenAnswer { uniqueGeoCodingResult(it.arguments[0] as String) }
  }

  private fun uniqueGeoCodingResult(query: String): Array<GeocodingResult> {
    val res = GeocodingResult()
    res.placeId = UUID.randomUUID().toString()
    res.formattedAddress = query
    return arrayOf(res)
  }

  @Test
  fun `0 - check for cache2k usage`() {
    admin.attemptsTo(EnsureCache2kIsUsed())
  }

  @Test
  fun `1 - when searching an address, I get a result`() {
    apiUser.attemptsTo(RequestGeoCode.of("1600 Amphitheatre Parkway Mountain View, CA 94043"))
    apiUser.attemptsTo(EnsuresThatTheResultsAreValid())
  }

  @Test
  fun `2 - the result is different when the cache has to load it again`() {
    apiUser.attemptsTo(RequestGeoCode.of("query 1"))
    val placeId = apiUser.asksFor(LatestGeoCodingResults())[0].placeId
    admin.attemptsTo(ClearsCache())
    apiUser.attemptsTo(RequestGeoCode.of("query 1"))
    apiUser.should(seeThat({it.asksFor(LatestGeoCodingResults())[0].placeId}, not(`is`(placeId))))
  }

  @Test
  fun `3 - when searching an address multiple times, the cache already has the result`() {
    `1 - when searching an address, I get a result`()
    val placeId = apiUser.asksFor(LatestGeoCodingResults())[0].placeId
    apiUser.attemptsTo(RequestTheSameLocationAgain())
    apiUser.should(seeThat({it.asksFor(LatestGeoCodingResults())[0].placeId}, not(`is`(placeId))))
  }

}

