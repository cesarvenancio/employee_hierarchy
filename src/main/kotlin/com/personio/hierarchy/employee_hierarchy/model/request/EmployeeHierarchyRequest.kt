package com.personio.hierarchy.employee_hierarchy.model.request

import com.fasterxml.jackson.annotation.JsonAnySetter
import java.util.ArrayList
import java.util.LinkedHashMap

class EmployeeHierarchyRequest {

    var employeesMap: LinkedHashMap<String, MutableList<String>> = LinkedHashMap()

    @JsonAnySetter
    fun buildEmployeeHierarchyMap(employee: String, supervisor: String) {
        var values: MutableList<String>? = null
        if (!employeesMap.containsKey(employee)) {
            values = ArrayList()
        } else {
            values = employeesMap.get(employee)
        }
        values!!.add(supervisor.trim())
        employeesMap.put(employee.trim(), values)
    }

}