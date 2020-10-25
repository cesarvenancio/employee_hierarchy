package com.personio.hierarchy.employee_hierarchy.service

import com.personio.hierarchy.employee_hierarchy.model.entity.Employee
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeHierarchyResponse
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeNode
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeSupervisorsResponse
import com.personio.hierarchy.employee_hierarchy.repository.EmployeeRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.LinkedHashMap


class EmployeeServiceTest {

    private val employeeRepository: EmployeeRepository = Mockito.mock(EmployeeRepository::class.java)
    private val employeeService: EmployeeService = EmployeeService(employeeRepository)

    @Test
    fun processHierarchyNoValidationMessagesTest(){

        val employee:Employee = Employee(1, "mockTest", null)

        given(employeeRepository.save(any(Employee::class.java))).willReturn(employee);

        val employeeResponse: EmployeeHierarchyResponse = employeeService.processHierarchy(getEmployeeHierarchyMap());

        Assertions.assertNotNull(employeeResponse.hierarchy);
        Assertions.assertEquals(employeeResponse.hierarchy?.name, "Jonas");
        Assertions.assertEquals(employeeResponse.hierarchy?.employees?.get(0)?.name, "Sophie");

        verify(employeeRepository, times(5)).save(any(Employee::class.java))
        verify(employeeRepository, times(1)).truncateEmployeeTable()
        verifyNoMoreInteractions(employeeRepository)

        Assertions.assertEquals(employeeResponse.validationMessages.size, 0);
    }

    @Test
    fun processHierarchyValidationDuplicateReferenceTest(){

        val employee:Employee = Employee(1, "mockTest", null)

        given(employeeRepository.save(any(Employee::class.java))).willReturn(employee);

        val employeeResponse: EmployeeHierarchyResponse = employeeService.processHierarchy(getEmployeeHierarchyMapDuplicateReference());

        verify(employeeRepository, times(5)).save(any(Employee::class.java))
        verify(employeeRepository, times(1)).truncateEmployeeTable()
        verifyNoMoreInteractions(employeeRepository)

        Assertions.assertEquals(employeeResponse.validationMessages.size, 1);
        Assertions.assertEquals(employeeResponse.validationMessages.get(0),
                "Could not link the employee Pete to supervisor Sophie, employee already linked to supervisor Nick");
    }

    @Test
    fun processHierarchyValidationIndependentRootTest(){

        val employee:Employee = Employee(1, "mockTest", null)

        given(employeeRepository.save(any(Employee::class.java))).willReturn(employee);

        val employeeResponse: EmployeeHierarchyResponse = employeeService.processHierarchy(getEmployeeHierarchyMapIndependentRoot());

        verify(employeeRepository, times(5)).save(any(Employee::class.java))
        verify(employeeRepository, times(1)).truncateEmployeeTable()
        verifyNoMoreInteractions(employeeRepository)

        Assertions.assertEquals(employeeResponse.validationMessages.size, 1);
        Assertions.assertEquals(employeeResponse.validationMessages.get(0),
                "Could not link Eoin to employee Trevor independent root");
    }

    @Test
    fun getEmployeeTest(){

        val employee:Employee = Employee(1, "mockTest", null)

        given(employeeRepository.findByName(eq("mockTest"))).willReturn(employee);

        val employeeResponse: Employee? = employeeService.getEmployeeByName("mockTest");

        Assertions.assertNotNull(employeeResponse)
        verify(employeeRepository, times(1)).findByName(eq("mockTest"))
        verifyNoMoreInteractions(employeeRepository)
    }

    @Test
    fun getEmployeeSupervisorsTest(){

        val mockEmployeeSupervisor: EmployeeSupervisorsResponse = mock(EmployeeSupervisorsResponse::class.java)
        given(employeeRepository.findSupervisorsByEmployeeName(eq("mockTest"))).willReturn(mockEmployeeSupervisor);

        val employeeSupervisorResponse: EmployeeSupervisorsResponse? = employeeService.getEmployeeSupervisorsByEmployeeName("mockTest");

        Assertions.assertNotNull(employeeSupervisorResponse)
        verify(employeeRepository, times(1)).findSupervisorsByEmployeeName(eq("mockTest"))
        verifyNoMoreInteractions(employeeRepository)
    }

    @Test
    fun saveEmployeeHierarchyTest(){
        val employee:Employee = Employee(1, "TEST", null)
        given(employeeRepository.save(any(Employee::class.java))).willReturn(employee);

        employeeService.saveEmployeeHierarchy(EmployeeNode("TEST", null, arrayListOf()));

        verify(employeeRepository, times(1)).truncateEmployeeTable()
        verify(employeeRepository, times(1)).save(any())
        verifyNoMoreInteractions(employeeRepository)
    }

    private fun getEmployeeHierarchyMap(): LinkedHashMap<String, MutableList<String>>{
        val employeeMap: LinkedHashMap<String, MutableList<String>> = LinkedHashMap()

        employeeMap["Pete"] = mutableListOf("Nick")
        employeeMap["Barbara"] = mutableListOf("Nick")
        employeeMap["Nick"] = mutableListOf("Sophie")
        employeeMap["Sophie"] = mutableListOf("Jonas")

        return employeeMap
    }

    private fun getEmployeeHierarchyMapDuplicateReference(): LinkedHashMap<String, MutableList<String>>{
        val employeeMap: LinkedHashMap<String, MutableList<String>> = getEmployeeHierarchyMap()

        employeeMap["Pete"]?.add("Sophie")

        return employeeMap
    }

    private fun getEmployeeHierarchyMapIndependentRoot(): LinkedHashMap<String, MutableList<String>>{
        val employeeMap: LinkedHashMap<String, MutableList<String>> = getEmployeeHierarchyMap()

        employeeMap["Trevor"] = mutableListOf("Eoin")

        return employeeMap
    }

}