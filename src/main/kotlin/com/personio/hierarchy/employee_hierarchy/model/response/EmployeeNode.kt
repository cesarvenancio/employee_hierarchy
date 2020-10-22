package com.personio.hierarchy.employee_hierarchy.model.response

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.HashMap

@JsonIgnoreProperties("name", "employees", "supervisor")
class EmployeeNode(var name: String, var supervisor: EmployeeNode?, var employees: ArrayList<EmployeeNode>? = arrayListOf<EmployeeNode>()) {

    @JsonAnyGetter
    fun getForJson(): Map<String, ArrayList<EmployeeNode>?> {
        val map: MutableMap<String, ArrayList<EmployeeNode>?> = HashMap<String, ArrayList<EmployeeNode>?>()
        map[name] = employees
        return map
    }

    fun findEmployeeWithValue(nodeName: String): EmployeeNode? {
            if(this.name == nodeName){
                return this;
            } else{
                if(this.employees != null){
                    for (employeesNode in this.employees!!) {
                        val result: EmployeeNode? = employeesNode.findEmployeeWithValue(nodeName);
                        if(result != null){
                            return result;
                        }
                    }
                }
            }

        return null;
    }
}