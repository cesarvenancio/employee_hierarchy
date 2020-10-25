package com.personio.hierarchy.employee_hierarchy.controller

import com.personio.hierarchy.employee_hierarchy.exception.ResourceNotFoundException
import com.personio.hierarchy.employee_hierarchy.model.entity.Employee
import com.personio.hierarchy.employee_hierarchy.model.request.EmployeeHierarchyRequest
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeHierarchyResponse
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeNode
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeSupervisorsResponse
import com.personio.hierarchy.employee_hierarchy.service.EmployeeService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.LinkedHashMap

class EmployeeControllerTest() {

    private val employeeService: EmployeeService = Mockito.mock(EmployeeService::class.java)
    private val employeeController: EmployeeController = EmployeeController(employeeService)

    @Test
    fun processHierarchyTest() {

        val employeeHierarchy:EmployeeHierarchyResponse = EmployeeHierarchyResponse()
        employeeHierarchy.hierarchy = EmployeeNode("Sophie", null, arrayListOf())

        given(employeeService.processHierarchy(LinkedHashMap())).willReturn(employeeHierarchy);

        val employeeHierarchyResponse = employeeController.processHierarchy(getEmployeeHierarchyRequest())

        Assertions.assertNotNull(employeeHierarchyResponse)
        Assertions.assertEquals(employeeHierarchyResponse.hierarchy?.name, "Sophie")
        Mockito.verify(employeeService, Mockito.times(1)).processHierarchy(LinkedHashMap())
        Mockito.verifyNoMoreInteractions(employeeService)
    }

    @Test
    fun getEmployeeByNameTest() {
        val employee: Employee? = Employee(1, "TEST", null)

        given(employeeService.getEmployeeByName("TEST")).willReturn(employee);

        val employeeResponse: Employee? = employeeController.getEmployeeByName("TEST");

        Assertions.assertNotNull(employeeResponse)
        Assertions.assertEquals(employeeResponse?.name, "TEST")
        Mockito.verify(employeeService, Mockito.times(1)).getEmployeeByName("TEST")
        Mockito.verifyNoMoreInteractions(employeeService)
    }

    @Test
    fun getEmployeeByNameNotFoundTest() {

        given(employeeService.getEmployeeByName("NOT_FOUND_EMPLOYEE")).willReturn(null);

        val exceptionResponse = assertThrows(ResourceNotFoundException::class.java) {
            employeeController.getEmployeeByName("NOT_FOUND_EMPLOYEE");
        }

        Assertions.assertEquals("Employee not found NOT_FOUND_EMPLOYEE", exceptionResponse.message)
        Mockito.verify(employeeService, Mockito.times(1)).getEmployeeByName("NOT_FOUND_EMPLOYEE")
        Mockito.verifyNoMoreInteractions(employeeService)
    }

    @Test
    fun getEmployeeSupervisorsByNameTest() {
        val employeeSupervisors: EmployeeSupervisorsResponse? = Mockito.mock(EmployeeSupervisorsResponse::class.java)

        given(employeeService.getEmployeeSupervisorsByEmployeeName("TEST")).willReturn(employeeSupervisors);

        val employeeSupervisorsResponse: EmployeeSupervisorsResponse? = employeeController.getEmployeeSupervisorsByName("TEST");

        Assertions.assertNotNull(employeeSupervisorsResponse)
        Mockito.verify(employeeService, Mockito.times(1)).getEmployeeSupervisorsByEmployeeName("TEST")
        Mockito.verifyNoMoreInteractions(employeeService)
    }

    @Test
    fun getEmployeeSupervisorsNotFoundTest() {
        val employeeSupervisors: EmployeeSupervisorsResponse? = Mockito.mock(EmployeeSupervisorsResponse::class.java)

        given(employeeService.getEmployeeSupervisorsByEmployeeName("NOT_FOUND_EMPLOYEE")).willReturn(null);

        val exceptionResponse = assertThrows(ResourceNotFoundException::class.java) {
            employeeController.getEmployeeSupervisorsByName("NOT_FOUND_EMPLOYEE");
        }

        Assertions.assertEquals("Employee not found NOT_FOUND_EMPLOYEE", exceptionResponse.message)
        Mockito.verify(employeeService, Mockito.times(1)).getEmployeeSupervisorsByEmployeeName("NOT_FOUND_EMPLOYEE")
        Mockito.verifyNoMoreInteractions(employeeService)
    }

    private fun getEmployeeHierarchyRequest(): EmployeeHierarchyRequest {
        val employeeHierarchyRequest: EmployeeHierarchyRequest = EmployeeHierarchyRequest()
        employeeHierarchyRequest.employeesMap = LinkedHashMap()

        return employeeHierarchyRequest
    }

}