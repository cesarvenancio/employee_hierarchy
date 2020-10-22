package com.personio.hierarchy.employee_hierarchy.model.request

import com.fasterxml.jackson.annotation.JsonAnySetter
import java.util.ArrayList
import java.util.LinkedHashMap

class EmployeeHierarchyRequest {

    val employeesMap: LinkedHashMap<String, MutableList<String>> = LinkedHashMap()

    @JsonAnySetter
    fun duplicateKeyValues(key: String, value: String) {
        var values: MutableList<String>? = null
        if (!employeesMap.containsKey(key)) {
            values = ArrayList()
        } else {
            values = employeesMap.get(key)
        }
        values!!.add(value)
        employeesMap.put(key, values)
    }

}