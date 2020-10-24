package com.personio.hierarchy.employee_hierarchy.service

import com.personio.hierarchy.employee_hierarchy.model.entity.Employee
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeNode
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeSupervisorsResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup

@SpringBootTest
class EmployeeServiceTest(@Autowired val employeeService: EmployeeService) {

    @SqlGroup(
            Sql(scripts = arrayOf("classpath:data/sql/insert_employees.sql"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            Sql(scripts = arrayOf("classpath:data/sql/delete_employees.sql"), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD))
    @Test
    fun getEmployeeTest() {
        val employee: Employee? = employeeService.getEmployee("TEST_EMPLOYEE");
        Assertions.assertNotNull(employee);
        Assertions.assertEquals("TEST_EMPLOYEE", employee?.name);
        Assertions.assertEquals(9998, employee?.supervisorId);
    }

    @SqlGroup(
            Sql(scripts = arrayOf("classpath:data/sql/insert_employees.sql"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            Sql(scripts = arrayOf("classpath:data/sql/delete_employees.sql"), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD))
    @Test
    fun getSupervisorsTest() {
        val employeeSupervisors: EmployeeSupervisorsResponse? = employeeService.getEmployeeSupervisors("TEST_EMPLOYEE");
        Assertions.assertNotNull(employeeSupervisors);
        Assertions.assertEquals("TEST_EMPLOYEE", employeeSupervisors?.employee);
        Assertions.assertEquals("SUPERVISOR2", employeeSupervisors?.supervisor);
        Assertions.assertEquals("SUPERVISOR1", employeeSupervisors?.supervisorOfSupervisor);
    }

    @Sql(scripts = arrayOf("classpath:data/sql/delete_employees.sql"), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    fun saveEmployeeHierarchyTest() {
        var employee: Employee? = employeeService.getEmployee("TEST_EMPLOYEE");
        Assertions.assertNull(employee);

        employeeService.saveEmployeeHierarchy(getEmployeeHierarchyRootTest())

        employee = employeeService.getEmployee("TEST_EMPLOYEE");
        Assertions.assertNotNull(employee);
        Assertions.assertEquals("TEST_EMPLOYEE", employee?.name);
        Assertions.assertEquals(1, employee?.supervisorId);
    }

    private fun getEmployeeHierarchyRootTest(): EmployeeNode?{
        val rootEmployeeNodeResponse: EmployeeNode? = EmployeeNode("SUPERVISOR1", null, arrayListOf<EmployeeNode>());
        val employeeNodeResponse: EmployeeNode = EmployeeNode("TEST_EMPLOYEE", rootEmployeeNodeResponse, arrayListOf<EmployeeNode>());
        rootEmployeeNodeResponse?.employees?.add(employeeNodeResponse)

        return rootEmployeeNodeResponse
    }

}