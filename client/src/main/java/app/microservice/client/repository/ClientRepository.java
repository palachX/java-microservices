package app.microservice.client.repository;


import app.microservice.client.entity.Client;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Integer> {

    @Query(value = "SELECT c FROM Client c WHERE c.fullName ILIKE %:fullName%")
    Iterable<Client> findAllByFullNameLikeIgnoreCase(@Size(min = 1, max = 50) String fullName);

}
