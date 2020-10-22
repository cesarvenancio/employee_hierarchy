package com.personio.hierarchy.employee_hierarchy.config

import com.personio.hierarchy.employee_hierarchy.exception.ResourceNotFoundException
import com.personio.hierarchy.employee_hierarchy.model.exception.ApiError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime
import java.util.*

@ControllerAdvice
class ControllerAdviceRequestError : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(ResourceNotFoundException::class)])
    fun handleResourceNotFound(exception: ResourceNotFoundException, request: WebRequest): ResponseEntity<ApiError> {

        val details: MutableList<String?> = ArrayList()
        details.add(exception.message)

        val err = ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                details)

        return ResponseEntity(err, err.status)
    }

    @ExceptionHandler(Throwable::class)
    fun handleException(exception: Exception): ResponseEntity<Exception> {
        return ResponseEntity(exception, HttpStatus.BAD_REQUEST)
    }


}