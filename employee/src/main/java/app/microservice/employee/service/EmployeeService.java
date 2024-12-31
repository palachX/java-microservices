package app.microservice.employee.service;

import app.microservice.employee.dto.EmployeeStoreDTO;
import app.microservice.employee.dto.EmployeeUpdateDTO;
import app.microservice.employee.entity.Employee;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

public interface EmployeeService {

    @GetMapping
    Iterable<Employee> getAllEmployees(String fullName);

    Employee createEmployee(EmployeeStoreDTO storeDTO);

    void updateEmployee(EmployeeUpdateDTO updateDTO);

    void deleteEmployee(Integer id);

    Optional<Employee> findById(Integer id);
}
