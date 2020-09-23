package com.example.geocoding

import com.example.geocoding.service.GoogleGeoCodingServiceFacade
import org.cache2k.extra.spring.SpringCache2kCacheManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import java.util.concurrent.TimeUnit
import java.util.function.Function


@EnableCaching
@SpringBootApplication
open class GeoCodingExampleApplication {
  @Bean
  open fun cacheManager(): CacheManager {
    val springCache2kCacheManager = SpringCache2kCacheManager("spring-${hashCode()}")
    springCache2kCacheManager.addCaches(Function { it
        .name(GoogleGeoCodingServiceFacade.cacheName)
        .expireAfterWrite(30, TimeUnit.DAYS)
        .enableJmx(true)
    })

    return springCache2kCacheManager
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      runApplication<GeoCodingExampleApplication>(*args)
    }
  }
}

