package com.personio.hierarchy.employee_hierarchy.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerTest(@Autowired var restTemplate: TestRestTemplate) {

    val simpleFileRequestPath: Path = Paths.get(this.javaClass.getResource("/hierarchies/request/simpleHierarchy.json").toURI());
    val simpleFileResponsePath: Path = Paths.get(this.javaClass.getResource("/hierarchies/response/simpleHierarchy.json").toURI());

    val complexFileRequestPath: Path = Paths.get(this.javaClass.getResource("/hierarchies/request/complexHierarchy.json").toURI());
    val complexFileResponsePath: Path = Paths.get(this.javaClass.getResource("/hierarchies/response/complexHierarchy.json").toURI());

    val cyclicFileRequestPath: Path = Paths.get(this.javaClass.getResource("/hierarchies/request/cyclicHierarchy.json").toURI());
    val cyclicFileResponsePath: Path = Paths.get(this.javaClass.getResource("/hierarchies/response/cyclicHierarchy.json").toURI());

    val duplicateReferenceSupervisorFileRequestPath: Path = Paths.get(this.javaClass.getResource("/hierarchies/request/duplicateEmployeeReferenceToSupervisor.json").toURI());
    val duplicateReferenceSupervisorFileResponsePath: Path = Paths.get(this.javaClass.getResource("/hierarchies/response/duplicateEmployeeReferenceToSupervisor.json").toURI());

    val getEmployeeFileResponsePath: Path = Paths.get(this.javaClass.getResource("/hierarchies/response/getEmployee.json").toURI());

    val getEmployeeSupervisorFileResponsePath: Path = Paths.get(this.javaClass.getResource("/hierarchies/response/getEmployeeSupervisor.json").toURI());

    val badFormatFileRequestPath: Path = Paths.get(this.javaClass.getResource("/hierarchies/request/badFormat.json").toURI());

    @BeforeEach
    fun initAll() {
        restTemplate = restTemplate.withBasicAuth("admin", "pass")
    }

    @Test
    fun simpleHierarchyTest() {

        val payloadBody: String = buildStringFromJsonFile(simpleFileRequestPath)

        val request = buildJsonStringRequestBody(payloadBody);

        val entity = restTemplate.postForEntity<String>("/employee/processEmployeesHierarchy", request, String::class.java)

        val payloadResponse: String = buildStringFromJsonFile(simpleFileResponsePath)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(entity.body).isEqualTo(payloadResponse);
    }

    @Test
    fun complexHierarchyTest() {

        val payloadBody: String = buildStringFromJsonFile(complexFileRequestPath)

        val request = buildJsonStringRequestBody(payloadBody);

        val entity = restTemplate.postForEntity<String>("/employee/processEmployeesHierarchy", request, String::class.java)

        val payloadResponse: String = buildStringFromJsonFile(complexFileResponsePath)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(entity.body).isEqualTo(payloadResponse);
    }

    @Test
    fun cyclicDependencyHierarchyTest() {

        val payloadBody: String = buildStringFromJsonFile(cyclicFileRequestPath)

        val request = buildJsonStringRequestBody(payloadBody);

        val entity = restTemplate.postForEntity<String>("/employee/processEmployeesHierarchy", request, String::class.java)

        val payloadResponse: String = buildStringFromJsonFile(cyclicFileResponsePath)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(entity.body).isEqualTo(payloadResponse);
    }

    @Test
    fun duplicateReferenceToSupervisorHierarchyTest() {

        val payloadBody: String = buildStringFromJsonFile(duplicateReferenceSupervisorFileRequestPath)

        val request = buildJsonStringRequestBody(payloadBody);

        val entity = restTemplate.postForEntity<String>("/employee/processEmployeesHierarchy", request, String::class.java)

        val payloadResponse: String = buildStringFromJsonFile(duplicateReferenceSupervisorFileResponsePath)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(entity.body).isEqualTo(payloadResponse);
    }

    @SqlGroup(
            Sql(scripts = arrayOf("classpath:data/sql/insert_employees.sql"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            Sql(scripts = arrayOf("classpath:data/sql/delete_employees.sql"), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD))
    @Test
    fun getEmployeeByNameTest() {

        val entity = restTemplate.getForEntity<String>("/employee/TEST_EMPLOYEE")

        val payloadResponse: String = buildStringFromJsonFile(getEmployeeFileResponsePath)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(entity.body).isEqualTo(payloadResponse);
    }

    @Test
    fun getEmployeeNotFoundTest() {

        val entity = restTemplate.getForEntity<String>("/employee/TET_EMPLOYEE")

        val mapper = ObjectMapper()
        val actualObj = mapper.readTree(entity.body)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(actualObj.get("message").textValue()).isEqualTo("Resource Not Found");
        Assertions.assertThat(actualObj.get("errors")[0].textValue()).isEqualTo("Employee not found TET_EMPLOYEE");
    }

    @SqlGroup(
            Sql(scripts = arrayOf("classpath:data/sql/insert_employees.sql"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            Sql(scripts = arrayOf("classpath:data/sql/delete_employees.sql"), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD))
    @Test
    fun getEmployeeSupervisorTest() {

        val entity = restTemplate.getForEntity<String>("/employee/TEST_EMPLOYEE/supervisor")

        val payloadResponse: String = buildStringFromJsonFile(getEmployeeSupervisorFileResponsePath)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(entity.body).isEqualTo(payloadResponse);
    }

    @Test
    fun getEmployeeSupervisorNotFoundTest() {

        val entity = restTemplate.getForEntity<String>("/employee/TET_EMPLOYEE/supervisor")

        val mapper = ObjectMapper()
        val actualObj = mapper.readTree(entity.body)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(actualObj.get("message").textValue()).isEqualTo("Resource Not Found");
        Assertions.assertThat(actualObj.get("errors")[0].textValue()).isEqualTo("Employee not found TET_EMPLOYEE");
    }

    @Test
    fun badFormatRequestTest() {

        val payloadBody: String = buildStringFromJsonFile(badFormatFileRequestPath)

        val request = buildJsonStringRequestBody(payloadBody);

        val entity = restTemplate.postForEntity<String>("/employee/processEmployeesHierarchy", request, String::class.java)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(entity.body).isEqualTo("Invalid JSON Format");
    }

    private fun buildStringFromJsonFile(pathFile: Path): String{
        return Files.lines(pathFile)
                .parallel()
                .collect(Collectors.joining())
    }

    private fun buildJsonStringRequestBody(payloadBody: String): HttpEntity<String>{
        val headers: HttpHeaders = HttpHeaders ()
        headers.contentType = MediaType.APPLICATION_JSON

        return HttpEntity<String>(payloadBody, headers)
    }

}