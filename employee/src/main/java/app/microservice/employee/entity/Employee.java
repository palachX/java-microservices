package app.microservice.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "employee", name = "users")
@NamedQueries(
        @NamedQuery(
                name = "Employee.findAllByFullNameLikeIgnoringCase",
                query = "SELECT e FROM Employee e WHERE e.fullName ILIKE CONCAT('%', :fullName, '%')"
        )
)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 5, max = 50)
    private String fullName;

    @NotNull
    @Size(min = 11, max = 11)
    @Column(unique = true)
    private String phone;

}
