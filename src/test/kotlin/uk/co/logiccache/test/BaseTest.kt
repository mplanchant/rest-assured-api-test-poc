package uk.co.logiccache.test

import com.ufoscout.properlty.Properlty
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseTest(private val api: String) {

    private val env: String = System.getProperty("env")

    companion object {
        lateinit var requestSpecification: RequestSpecification
    }

    @BeforeAll
    fun setup() {

        val properties: Properlty = Properlty.builder()
            .add("classpath:$env.properties")
            .build()

        val logConfig = LogConfig.logConfig()
            .enablePrettyPrinting(true)
            .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)

        val config = RestAssuredConfig.config().logConfig(logConfig)

        val requestSpecificationBuilder = RequestSpecBuilder()
            .setBaseUri(properties["api.$api.baseUri"])
            .setBasePath(properties["api.$api.basePath"])
            .setContentType(ContentType.JSON)
            .setRelaxedHTTPSValidation()
            .setConfig(config)
            .addFilters(listOf(RequestLoggingFilter(), ResponseLoggingFilter()))

        properties.getList("api.$api.headers") { Pair(it.substringBefore(":").trim(), it.substringAfter(":").trim()) }
            .forEach { requestSpecificationBuilder.addHeader(it.first, it.second) }

        requestSpecification = requestSpecificationBuilder.build()
    }

    @AfterAll
    fun tearDown() {
        RestAssured.reset()
    }
}
