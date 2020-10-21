package com.personio.hierarchy.employee_hierarchy.security

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityAccessTest(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun unauthorizedAccessTest() {
        val entity = restTemplate.getForEntity<String>("/employee/test")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun basicAuthAccessTest() {
        val entity = restTemplate.withBasicAuth("admin", "pass").getForEntity<String>("/employee/test")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    }

}