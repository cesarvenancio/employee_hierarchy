package com.personio.hierarchy.employee_hierarchy.model

import com.personio.hierarchy.employee_hierarchy.model.request.EmployeeHierarchyRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EmployeeHierarchyRequestTest {

    @Test
    fun buildEmployeeHierarchyMapTest(){
        val employeeHierarchyRequest: EmployeeHierarchyRequest = EmployeeHierarchyRequest()
        employeeHierarchyRequest.buildEmployeeHierarchyMap("TEST_EMPLOYEE", "TEST_SUPERVISOR")

        Assertions.assertNotNull(employeeHierarchyRequest.employeesMap);
        Assertions.assertEquals(1, employeeHierarchyRequest.employeesMap.size);
        Assertions.assertEquals("TEST_SUPERVISOR", employeeHierarchyRequest.employeesMap["TEST_EMPLOYEE"]?.get(0))
    }

    @Test
    fun buildEmployeeHierarchyMapTrimKeyValuesTest(){
        val employeeHierarchyRequest: EmployeeHierarchyRequest = EmployeeHierarchyRequest()
        employeeHierarchyRequest.buildEmployeeHierarchyMap(" TEST_EMPLOYEE", "   TEST_SUPERVISOR   ")

        Assertions.assertNotNull(employeeHierarchyRequest.employeesMap);
        Assertions.assertEquals(1, employeeHierarchyRequest.employeesMap.size);
        Assertions.assertEquals("TEST_SUPERVISOR", employeeHierarchyRequest.employeesMap["TEST_EMPLOYEE"]?.get(0))
    }

}