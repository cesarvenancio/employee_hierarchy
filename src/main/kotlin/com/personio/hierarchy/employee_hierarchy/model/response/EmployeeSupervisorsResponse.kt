package com.personio.hierarchy.employee_hierarchy.model.response

data class EmployeeSupervisorsResponse(var employee: String, var supervisor: String, var supervisorOfSupervisor: String)