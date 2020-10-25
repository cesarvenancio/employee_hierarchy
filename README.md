# Employee Challenge

> Hierarchy API using Gradle/Wrapper with Java 8, Spring Boot Web API/Data JPA and H2 in memory database.

## How to run
1. Fork the project
2. Build: `/gradlew build`
3. Run: `/gradlew bootRun`

## API Security

To access the endpoints, please use Basic Auth with -

`Username`: `admin`

`Password`: `pass`

### Employees Endpoints

> GET http://localhost:8080/employee_hierarchy/employee/{name} (Get Employee)

> GET http://localhost:8080/employee_hierarchy/employee/{name}/supervisor (Get Employee with Supervisors)

> POST http://localhost:8080/employee_hierarchy/employee/processEmployeesHierarchy (Process Employee Hierarchy)

> Post Body Example - 
```json
{
"Pete": "Nick",
"Barbara": "Nick",
"Nick": "Sophie",
"Sophie": "Jonas"
}
```

### H2 DB Access

http://localhost:8080/employee_hierarchy/h2-console

`JDBC URL`: `jdbc:h2:mem:employeeDb`

`Username`: `admin`

`Password`: `admin`
