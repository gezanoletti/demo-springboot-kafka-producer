package com.gezanoletti.demospringbootkafkaproducer

import com.github.javafaker.service.FakeValuesService
import com.github.javafaker.service.RandomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Locale

@Configuration
class JavaFakerConfig {

    @Bean
    fun fakeValuesService(): FakeValuesService = FakeValuesService(Locale.ENGLISH, RandomService())
}
