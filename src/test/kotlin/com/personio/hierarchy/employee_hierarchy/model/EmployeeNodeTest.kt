package com.personio.hierarchy.employee_hierarchy.model

import com.personio.hierarchy.employee_hierarchy.model.response.EmployeeNode
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EmployeeNodeTest {

    @Test
    fun findNodeTest(){
        val rootEmployeeNode: EmployeeNode = getRootWithThreeEmployees();
        val employeeFound: EmployeeNode? = rootEmployeeNode.findEmployeeWithValue("EmployeeOne")

        Assertions.assertNotNull(employeeFound);
        Assertions.assertEquals("EmployeeOne", employeeFound?.name);
    }

    @Test
    fun returnNullWhenNoFoundNodeTest(){
        val rootEmployeeNode: EmployeeNode = getRootWithThreeEmployees();
        Assertions.assertEquals(null, rootEmployeeNode.findEmployeeWithValue("BossNon-existent"));
    }

    private fun getRootWithThreeEmployees(): EmployeeNode{
        val rootNode:EmployeeNode = EmployeeNode("Boss", null, arrayListOf<EmployeeNode>())
        val employeeOneNode:EmployeeNode = EmployeeNode("EmployeeOne", rootNode, arrayListOf<EmployeeNode>())
        rootNode.employees?.add(employeeOneNode);
        val employeeTwoNode:EmployeeNode = EmployeeNode("EmployeeTwo", employeeOneNode, arrayListOf<EmployeeNode>())
        employeeOneNode.employees?.add(employeeTwoNode)

        return rootNode
    }
}