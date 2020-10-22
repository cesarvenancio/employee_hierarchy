package com.personio.hierarchy.employee_hierarchy.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String?) : RuntimeException(message)