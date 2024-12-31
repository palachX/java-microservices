package app.microservice.employee.repository;

import app.microservice.employee.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/sql/employees.sql")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryIT {


    @Autowired
    EmployeeRepository employeeRepository;


    @Test
    void findAllByFullNameLikeIgnoreCase_ReturnsFilteredEmployeesList() {
        //given
        var fullName = "Jane Doe";

        //when
        var employees = employeeRepository.findAllByFullNameLikeIgnoreCase(fullName);

        //then
        assertEquals(List.of(new Employee(2, "Jane Doe", "70952756595")), employees);
    }

}
