package com.gezanoletti.demospringbootkafkaproducer

import com.github.javafaker.service.FakeValuesService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutionException
import kotlin.random.Random.Default.nextInt

@Component
class Producer(
        private val kafkaTemplate: KafkaTemplate<String, String>,
        private val fakeValuesService: FakeValuesService
) {

    private val logger: Logger = LoggerFactory.getLogger(Producer::class.java)

    @Scheduled(fixedRate = 500)
    fun sendMessage() {
        val id = nextInt(1, 11).toString()
        val message = fakeValuesService.bothify("????##@gmail.com")

        try {
            kafkaTemplate.send(TOPIC_NAME_NORMAL, id, message)
                    .addCallback(
                            { onSuccess(it) },
                            { onFailure(it) }
                    )

            kafkaTemplate.send(TOPIC_NAME_COMPACT, id, message)
                    .addCallback(
                            { onSuccess(it) },
                            { onFailure(it) }
                    )

        } catch (e: ExecutionException) {
            logger.error("Error in sending record", e)

        } catch (e: InterruptedException) {
            logger.error("Error in sending record", e)
        }
    }

    private fun onSuccess(result: SendResult<String, String>?) {
        logger.info("Record sent to topic ${result?.recordMetadata?.topic()}, " +
                "to partition ${result?.recordMetadata?.partition()}, " +
                "with offset ${result?.recordMetadata?.offset()}")
    }

    private fun onFailure(ex: Throwable) {
        logger.error("Record fails: ${ex.message}")
    }

    companion object {
        const val TOPIC_NAME_NORMAL = "demo-basic-kafka-partitions"
        const val TOPIC_NAME_COMPACT = "demo-basic-kafka-partitions-compact"
    }
}
