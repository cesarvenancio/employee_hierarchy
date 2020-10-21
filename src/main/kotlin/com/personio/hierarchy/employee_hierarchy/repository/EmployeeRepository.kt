package com.personio.hierarchy.employee_hierarchy.repository

import com.personio.hierarchy.employee_hierarchy.model.entity.Employee
import org.springframework.data.repository.CrudRepository

interface EmployeeRepository : CrudRepository<Employee, Long> {

    fun findByName(name: String): Employee?
}