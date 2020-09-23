package com.example.geocoding.atdd

import com.example.geocoding.screenplay.actorName
import net.serenitybdd.junit.runners.SerenityRunner
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule
import net.serenitybdd.screenplay.Actor
import net.serenitybdd.screenplay.rest.abilities.CallAnApi
import net.serenitybdd.screenplay.rest.interactions.Get
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence.*
import org.apache.http.HttpStatus.*
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*
import org.hamcrest.core.IsIterableContaining
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort


@RunWith(SerenityRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GeoCoderApiTestExternal {


  @LocalServerPort
  internal var port: Int = 0


  @Value("\${maps.api.key}")
  private lateinit var apiKey: String

  @Rule
  @JvmField
  var springIntegrationMethodRule = SpringIntegrationMethodRule()

  private val apiUser = Actor(actorName.get())

  @Before
  fun setUp() {
    apiUser.can(CallAnApi.at("http://localhost:${port}"))
  }

  @Test
  fun `0 - did application start?`() {
    assertThat(port, greaterThan(0))
  }

  @Test
  fun `0 - got your maps api key?`() {
    assertThat(apiKey, not(`is`("FIXME")))
    assertThat(apiKey, hasLength(39))
  }

  @Test
  fun `1 - when searching an address, I get a result`() {
    apiUser.attemptsTo(Get.resource("/geoCode").with{ it.param("query", "1600 Amphitheatre Parkway Mountain View, CA 94043") })
    apiUser.should(seeThatResponse("has place id") {
      it.statusCode(SC_OK).body("placeId", IsIterableContaining.hasItem("ChIJtYuu0V25j4ARwu5e4wwRYgE"))
    })
  }

}

