package uk.co.logiccache.test.foodapi

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.greaterThan
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import uk.co.logiccache.test.BaseTest
import uk.co.logiccache.test.foodApi
import uk.co.logiccache.test.smoke

@Tag(foodApi)
@Tag(smoke)
class FoodApiSmokeTest : BaseTest(foodApi) {

    @Test
    fun `get all UK products`() {
        Given {
            spec(requestSpecification)
        } When {
            get("/v3/UK/products")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("$.size()", greaterThan(0))
        }
    }
}
