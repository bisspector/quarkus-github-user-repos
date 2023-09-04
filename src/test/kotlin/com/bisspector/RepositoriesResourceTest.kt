package com.bisspector

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test

@QuarkusTest
class RepositoriesResourceTest {

    @Test
    fun testUserEndpoint() {
        given()
                .`when`().get("/user/bisspector")
                .then()
                .statusCode(200)
    }

}