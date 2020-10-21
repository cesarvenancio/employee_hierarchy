package com.personio.hierarchy.employee_hierarchy.repository

import com.personio.hierarchy.employee_hierarchy.model.entity.Employee
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup

@SpringBootTest
class EmployeeRepositoryTest(@Autowired val employeeRepository: EmployeeRepository) {

    @SqlGroup(
            Sql(scripts = arrayOf("classpath:data/insert_employees.sql"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            Sql(scripts = arrayOf("classpath:data/delete_employees.sql"), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD))
    @Test
    fun testGetEmployee() {
        val employee: Employee? = employeeRepository.findByName("TEST_EMPLOYEE");
        Assertions.assertNotNull(employee);
        Assertions.assertEquals("TEST_EMPLOYEE", employee?.name);
        Assertions.assertNotNull(employee?.supervisor);
        Assertions.assertEquals("SUPERVISOR2", employee?.supervisor?.name);
        Assertions.assertNotNull(employee?.supervisor?.supervisor);
        Assertions.assertEquals("SUPERVISOR1", employee?.supervisor?.supervisor?.name);
    }

}