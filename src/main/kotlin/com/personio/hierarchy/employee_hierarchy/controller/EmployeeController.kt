package com.personio.hierarchy.employee_hierarchy.controller

import com.personio.hierarchy.employee_hierarchy.model.entity.Employee
import com.personio.hierarchy.employee_hierarchy.model.request.EmployeeHierarchyRequest
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeHierarchyResponse
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeNode
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeSupervisorsResponse
import com.personio.hierarchy.employee_hierarchy.service.EmployeeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/employee")
class EmployeeController @Autowired constructor(private val employeeService: EmployeeService){

    @GetMapping("/test")
    fun getTest(): String {
        return "hello";
    }

    @PostMapping("/processEmployeesHierarchy")
    fun processHierarchy(@RequestBody hierarchyListRequest: EmployeeHierarchyRequest): EmployeeHierarchyResponse {

        var response: EmployeeHierarchyResponse = employeeService.processHierarchy(hierarchyListRequest.employeesMap);

        return response;
    }

    @GetMapping("/{name}")
    fun getEmployeeByName(@PathVariable name:String): Employee? {
        var employee:Employee? = employeeService.getEmployee(name);

        return employee;
    }

    @GetMapping("/{name}/supervisor")
    fun getEmployeeSupervisorsByName(@PathVariable name:String): EmployeeSupervisorsResponse? {
        return employeeService.getEmployeeSupervisors(name);
    }

}