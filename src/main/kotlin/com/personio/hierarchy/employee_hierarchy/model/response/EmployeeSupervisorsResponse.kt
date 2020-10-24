package com.personio.hierarchy.employee_hierarchy.model.response

interface EmployeeSupervisorsResponse{
    val employee: String
    val supervisor: String
    val supervisorOfSupervisor: String
}