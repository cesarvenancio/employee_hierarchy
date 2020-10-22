package com.personio.hierarchy.employee_hierarchy.repository

import com.personio.hierarchy.employee_hierarchy.model.entity.Employee
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : CrudRepository<Employee, Long> {

    fun findByName(name: String): Employee?

    @Modifying
    @Query("DELETE FROM Employee")
    fun truncateTable()
}