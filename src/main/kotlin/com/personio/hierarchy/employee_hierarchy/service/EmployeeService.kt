package com.personio.hierarchy.employee_hierarchy.service

import com.personio.hierarchy.employee_hierarchy.model.entity.Employee
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeHierarchyResponse
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeNode
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeSupervisorsResponse
import com.personio.hierarchy.employee_hierarchy.repository.EmployeeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmployeeService @Autowired constructor(private val employeeRepository: EmployeeRepository){

    fun processHierarchy(employeeHierarchyMap: LinkedHashMap<String, MutableList<String>>): EmployeeHierarchyResponse {
        var response: EmployeeHierarchyResponse = EmployeeHierarchyResponse();

        var rootEmployeeNode: EmployeeNode? = null;

        for ((employeeName, supervisorListName) in employeeHierarchyMap) {

            val supervisorName = supervisorListName.removeAt(0);

            if(rootEmployeeNode == null){
                rootEmployeeNode = EmployeeNode(supervisorName, null, arrayListOf<EmployeeNode>());
                val employee = EmployeeNode(employeeName, rootEmployeeNode, arrayListOf<EmployeeNode>());
                rootEmployeeNode.employees?.add(employee);
            } else{
                var supervisor: EmployeeNode? = rootEmployeeNode.findEmployeeWithValue(supervisorName);
                var employee: EmployeeNode? = rootEmployeeNode.findEmployeeWithValue(employeeName);

                if(supervisor == null && employee != rootEmployeeNode){
                    //TODO MESSAGE INDEPENDENT ROOT
                }else if(supervisor == null && employee == rootEmployeeNode){
                    supervisor = EmployeeNode(supervisorName, null, arrayListOf<EmployeeNode>());
                    supervisor.employees?.add(employee);
                    rootEmployeeNode = supervisor;
                } else if(supervisor != null && employee == null){
                    employee = EmployeeNode(employeeName, supervisor, arrayListOf<EmployeeNode>())
                    supervisor.employees?.add(employee)
                }
            }
        }

        response.hierarchy = rootEmployeeNode;

        return  response;
    }

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
