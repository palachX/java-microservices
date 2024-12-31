package app.microservice.employee.repository;

import app.microservice.employee.entity.Employee;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    @Query(value = "SELECT e FROM Employee e WHERE e.phone = :phone")
    Employee findByPhone(@Param("phone") @Size(min = 11, max = 11) String phone);


    /**
     * 1. Написание SQL запроса с использованием HQL
     * 2. Написание чистого SQL запрос
     * 3. Написание SQL запроса с использованием JPA
     */
      @Query(value = "SELECT e FROM Employee e WHERE e.fullName ILIKE %:fullName%")
    //  @Query(value = "select * from employee.users where full_name ILIKE CONCAT('%', :fullName, '%')", nativeQuery = true)
    //  @Query(name = "Employee.findAllByFullNameLikeIgnoringCase", nativeQuery = true)
    Iterable<Employee> findAllByFullNameLikeIgnoreCase(@Param("fullName") @Size(min = 1, max = 50) String fullName);

}
