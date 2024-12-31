package app.microservice.client.service;


import app.microservice.client.dto.ClientStoreDTO;
import app.microservice.client.dto.ClientUpdateDTO;
import app.microservice.client.entity.Client;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Iterable<Client> getAllClients(String fullName);

    Client createClient(ClientStoreDTO storeDTO);

    void updateClient(ClientUpdateDTO updateDTO);

    void deleteClient(Integer id);

    Optional<Client> findById(Integer id);
}
