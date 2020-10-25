package com.personio.hierarchy.employee_hierarchy.repository

import com.personio.hierarchy.employee_hierarchy.model.entity.Employee
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeSupervisorsResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup

@SpringBootTest
class EmployeeRepositoryTest(@Autowired val employeeRepository: EmployeeRepository) {

    @SqlGroup(
            Sql(scripts = arrayOf("classpath:data/sql/insert_employees.sql"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            Sql(scripts = arrayOf("classpath:data/sql/delete_employees.sql"), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD))
    @Test
    fun getEmployeeTest() {
        val employee: Employee? = employeeRepository.findByName("TEST_EMPLOYEE");
        Assertions.assertNotNull(employee);
        Assertions.assertEquals("TEST_EMPLOYEE", employee?.name);
        Assertions.assertNotNull(employee?.supervisorId);
        Assertions.assertEquals(9998, employee?.supervisorId);
    }

    @SqlGroup(
            Sql(scripts = arrayOf("classpath:data/sql/insert_employees.sql"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            Sql(scripts = arrayOf("classpath:data/sql/delete_employees.sql"), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD))
    @Test
    fun getEmployeeSupervisorsTest() {
        val employee: EmployeeSupervisorsResponse? = employeeRepository.findSupervisorsByEmployeeName("TEST_EMPLOYEE");
        Assertions.assertNotNull(employee);
        Assertions.assertEquals("TEST_EMPLOYEE", employee?.employee);
        Assertions.assertEquals("SUPERVISOR2", employee?.supervisor);
        Assertions.assertEquals("SUPERVISOR1", employee?.supervisorOfSupervisor);
    }

    @Sql(scripts = arrayOf("classpath:data/sql/insert_employees.sql"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    fun truncateEmployeeTableTest() {
        val employeeResults: MutableIterable<Employee> = employeeRepository.findAll()

        Assertions.assertEquals(3, employeeResults.count());
        employeeRepository.truncateEmployeeTable();

        val emptyEmployeeResults: MutableIterable<Employee> = employeeRepository.findAll()
        Assertions.assertEquals(0, emptyEmployeeResults.count());
    }

}