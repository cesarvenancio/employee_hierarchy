package com.personio.hierarchy.employee_hierarchy.controller

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerTest(@Autowired val restTemplate: TestRestTemplate) {

    val simpleFileRequestPath: Path = Paths.get(this.javaClass.getResource("/hierarchies/request/simpleHierarchy.json").toURI());
    val simpleFileResponsePath: Path = Paths.get(this.javaClass.getResource("/hierarchies/response/simpleHierarchy.json").toURI());

    val complexFileRequestPath: Path = Paths.get(this.javaClass.getResource("/hierarchies/request/complexHierarchy.json").toURI());
    val complexFileResponsePath: Path = Paths.get(this.javaClass.getResource("/hierarchies/response/complexHierarchy.json").toURI());

    @Test
    fun simpleHierarchy() {

        val payloadBody: String = buildStringFromJsonFile(simpleFileRequestPath)

        val headers: HttpHeaders = HttpHeaders ()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity<String>(payloadBody, headers)

        val entity = restTemplate.withBasicAuth("admin", "pass").postForEntity<String>("/employee/processEmployeesHierarchy", request, String::class.java)

        val payloadResponse: String = buildStringFromJsonFile(simpleFileResponsePath)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(entity.body).isEqualTo(payloadResponse);
    }

    @Test
    fun complexHierarchy() {

        val payloadBody: String = buildStringFromJsonFile(complexFileRequestPath)

        val headers: HttpHeaders = HttpHeaders ()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity<String>(payloadBody, headers)

        val entity = restTemplate.withBasicAuth("admin", "pass").postForEntity<String>("/employee/processEmployeesHierarchy", request, String::class.java)

        val payloadResponse: String = buildStringFromJsonFile(complexFileResponsePath)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(entity.body).isEqualTo(payloadResponse);
    }

    private fun buildStringFromJsonFile(pathFile: Path): String{
        return Files.lines(pathFile)
                .parallel()
                .collect(Collectors.joining())
    }

}