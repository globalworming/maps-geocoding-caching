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

  @GetMapping("/geoCode")
  fun geoCode(@RequestParam query: String) =
      googleGeoCodingServiceFacade.coordinates(query)


}