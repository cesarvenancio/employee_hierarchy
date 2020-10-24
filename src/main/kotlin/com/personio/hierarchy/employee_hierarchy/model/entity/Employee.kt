package com.personio.hierarchy.employee_hierarchy.model.entity

import javax.persistence.*

@Entity
@Table(name = "employee")
data class Employee(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = 0,

        var name: String = "",

        @Column(name = "supervisor_id")
        var supervisorId: Long? = null
)