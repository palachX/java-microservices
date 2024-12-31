package app.microservice.employee.controller;

import app.microservice.employee.dto.EmployeeStoreDTO;
import app.microservice.employee.dto.EmployeeUpdateDTO;
import app.microservice.employee.entity.Employee;
import app.microservice.employee.request.EmployeeStoreRequest;
import app.microservice.employee.request.EmployeeUpdateRequest;
import app.microservice.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final MessageSource messageSource;

    @GetMapping
    @Operation(
            description = "Список сотрудников",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            headers = @Header(name = "Content-Type", description = "Тип данных", example = "application/json"),
                            content = {
                                    @Content(
                                            schema = @Schema(
                                                    type = "array",
                                                    implementation = Employee.class
                                            )
                                    )
                            }
                    )
            }
    )
    public Iterable<Employee> index(@RequestParam(name = "fullName", required = false) String fullName) {
        return employeeService.getAllEmployees(fullName);
    }

    @GetMapping("/{id}")
    public Employee show(@PathVariable Integer id) {
        return employeeService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("error.404.title"));
    }

    @PostMapping
    @Operation(
            description = "Создание сотрудника",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    type = "object",
                                    properties = {
                                            @StringToClassMapItem(key = "fullName", value = String.class),
                                            @StringToClassMapItem(key = "phone", value = String.class)
                                    }
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            headers = @Header(name = "Content-Type", description = "Тип данных", example = "application/json"),
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(
                                                    type = "object",
                                                    properties = {
                                                            @StringToClassMapItem(key = "id", value = Integer.class),
                                                            @StringToClassMapItem(key = "fullName", value = String.class),
                                                            @StringToClassMapItem(key = "phone", value = String.class)
                                                    }
                                            )
                                    )
                            }
                    )
            }
    )
    public ResponseEntity<?> store(
            @Valid @RequestBody EmployeeStoreRequest storeRequest,
            BindingResult bindingResult
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }

        var employeeStoreDTO = new EmployeeStoreDTO(storeRequest.fullName(), storeRequest.phone());
        return ResponseEntity.created(null).body(employeeService.createEmployee(employeeStoreDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody EmployeeUpdateRequest updateRequest,
                                    BindingResult bindingResult,
                                    @PathVariable Integer id
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }

        var updateDTO = new EmployeeUpdateDTO(id, updateRequest.fullName());
        employeeService.updateEmployee(updateDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void destroy(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(
            NoSuchElementException ex,
            Locale locale
    ) {
        var messageError = this.messageSource.getMessage(ex.getMessage(), new Object[0], locale);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                messageError
        );
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
