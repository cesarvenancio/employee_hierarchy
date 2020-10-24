package com.personio.hierarchy.employee_hierarchy.repository

import com.personio.hierarchy.employee_hierarchy.model.entity.Employee
import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeSupervisorsResponse
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface EmployeeRepository : CrudRepository<Employee, Long> {

    fun findByName(name: String): Employee?

    @Query("SELECT " +
            "e.name as employee, supervisor.name as supervisor, supervisorOfSupervisor.name as supervisorOfSupervisor " +
            "FROM Employee e " +
            "LEFT JOIN Employee supervisor on e.supervisor_id = supervisor.id " +
            "LEFT JOIN Employee supervisorOfSupervisor on supervisor.supervisor_id = supervisorOfSupervisor.id " +
            "WHERE e.name = ?1", nativeQuery = true)
    fun findSupervisorsByEmployeeName(name: String): EmployeeSupervisorsResponse?

    @Transactional
    @Modifying
    @Query("DELETE FROM Employee")
    fun truncateEmployeeTable()
}