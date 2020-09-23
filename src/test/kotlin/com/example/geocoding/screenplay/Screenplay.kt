package com.example.geocoding.screenplay

import com.github.javafaker.Faker
import java.util.function.Supplier

val faker: Faker = Faker()
val actorName = Supplier { "API User '" + faker.name().username()  + "'"}

