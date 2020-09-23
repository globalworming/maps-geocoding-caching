package com.example.geocoding.controller

import com.example.geocoding.model.CoordinateResult
import com.example.geocoding.service.GoogleGeoCodingServiceFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class GeoCodingController {

  @Autowired
  private lateinit var googleGeoCodingServiceFacade: GoogleGeoCodingServiceFacade

  // TODO bulk request?
  @GetMapping("/coordinates")
  fun coordinates(@RequestParam query: String, @RequestParam(required=false) apiKey: String?): Array<CoordinateResult> {

    try {
      return googleGeoCodingServiceFacade.coordinates(query, apiKey)
    } catch (e: IllegalStateException) {
      e.message?.let {
        if (it.contains("API key")) throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message, e)
      }
      throw e
    }

  }


}