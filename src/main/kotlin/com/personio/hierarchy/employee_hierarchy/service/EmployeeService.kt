package com.personio.hierarchy.employee_hierarchy.service

import com.personio.hierarchy.employee_hierarchy.model.entity.Employee
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeSupervisorsResponse
import com.personio.hierarchy.employee_hierarchy.repository.EmployeeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmployeeService @Autowired constructor(private val employeeRepository: EmployeeRepository){

    fun getEmployee(name:String): Employee?{
        return employeeRepository.findByName(name);
    }

    fun getEmployeeSupervisors(name:String): EmployeeSupervisorsResponse? {

        var employee: Employee? = getEmployee(name);
        if(employee != null){
            var employeeSupervisorName: String = employee?.supervisor?.name ?: "";
            var supervisorOfSupervisorName: String = employee?.supervisor?.supervisor?.name ?: "";
            return EmployeeSupervisorsResponse(employee.name, employeeSupervisorName, supervisorOfSupervisorName);
        }

        return null;
    }

}
