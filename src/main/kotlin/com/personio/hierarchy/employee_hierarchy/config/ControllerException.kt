package com.personio.hierarchy.employee_hierarchy.config

import com.personio.hierarchy.employee_hierarchy.exception.ResourceNotFoundException
import com.personio.hierarchy.employee_hierarchy.model.exception.ApiError
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.util.CollectionUtils
import org.springframework.web.HttpMediaTypeNotSupportedException
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

    @ExceptionHandler(Exception::class)
    fun handleGenericException(exception: Exception): ResponseEntity<ApiError> {
        val details: MutableList<String?> = ArrayList()
        details.add(exception.message)

        val err = ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Exception when processing",
                details)

        return ResponseEntity(err, err.status)
    }

    //Overriding default spring handling behaviour, just showing an easy way to deal with the default errors
    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, "Invalid JSON Format", headers, HttpStatus.BAD_REQUEST, request)
    }

    override fun handleHttpMediaTypeNotSupported(ex: HttpMediaTypeNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val mediaTypes = ex.supportedMediaTypes
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            headers.accept = mediaTypes
        }

        return handleExceptionInternal(ex, ex.message, headers, status, request)
    }

}