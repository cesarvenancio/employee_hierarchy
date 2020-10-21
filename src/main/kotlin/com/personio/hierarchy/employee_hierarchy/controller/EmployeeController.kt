package com.personio.hierarchy.employee_hierarchy.controller

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/employee")
class EmployeeController {

    @GetMapping("/test")
    fun getTest(): String {
        return "hello";
    }

}