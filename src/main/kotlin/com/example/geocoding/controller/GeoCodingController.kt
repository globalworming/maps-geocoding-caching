package com.example.geocoding.controller

import com.example.geocoding.service.GoogleGeoCodingServiceFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GeoCodingController {


  @Autowired
  private lateinit var googleGeoCodingServiceFacade: GoogleGeoCodingServiceFacade

  // TODO bulk request?
  @GetMapping("/coordinates")
  fun coordinates(@RequestParam query: String, @RequestParam(required=false) apiKey: String?) =
      googleGeoCodingServiceFacade.coordinates(query, apiKey)


}