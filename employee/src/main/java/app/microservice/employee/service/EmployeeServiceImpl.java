package app.microservice.employee.service;

import lombok.RequiredArgsConstructor;
import app.microservice.employee.dto.EmployeeStoreDTO;
import app.microservice.employee.dto.EmployeeUpdateDTO;
import app.microservice.employee.entity.Employee;
import app.microservice.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Iterable<Employee> getAllEmployees(String fullName) {
        if (fullName != null) {
            return employeeRepository.findAllByFullNameLikeIgnoreCase(fullName);
        }
        return employeeRepository.findAll();
    }

    @Override
    @Transactional
    public Employee createEmployee(EmployeeStoreDTO storeDTO) {
        return employeeRepository.save(new Employee(null,storeDTO.getFullName(), storeDTO.getPhone()));
    }

    @Override
    @Transactional
    public void updateEmployee(EmployeeUpdateDTO updateDTO) {
        employeeRepository.findById(updateDTO.getId())
                .ifPresentOrElse(employee -> {
                 employee.setFullName(updateDTO.getFullName());
                },() -> {
                    throw new NoSuchElementException();
                });
    }

    @Override
    @Transactional
    public void deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Optional<Employee> findById(Integer id) {
        return employeeRepository.findById(id);
    }

}
