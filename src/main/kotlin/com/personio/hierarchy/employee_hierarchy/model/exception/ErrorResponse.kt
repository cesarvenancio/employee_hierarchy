package com.personio.hierarchy.employee_hierarchy.model.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class ApiError(var timestamp: LocalDateTime?, var status: HttpStatus, var message: String?, var errors: List<*>?)