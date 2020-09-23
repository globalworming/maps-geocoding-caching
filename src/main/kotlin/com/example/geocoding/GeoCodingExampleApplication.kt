package com.example.geocoding

import org.cache2k.extra.spring.SpringCache2kCacheManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean


@EnableCaching
@SpringBootApplication
open class GeoCodingExampleApplication {
  @Bean
  open fun cacheManager(): CacheManager {
    val springCache2kCacheManager = SpringCache2kCacheManager("spring-${hashCode()}")
    springCache2kCacheManager.isAllowUnknownCache = true
    return springCache2kCacheManager
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      runApplication<GeoCodingExampleApplication>(*args)
    }
  }
}

