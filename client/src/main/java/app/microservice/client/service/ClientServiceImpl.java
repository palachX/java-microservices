package app.microservice.client.service;

import app.microservice.client.dto.ClientStoreDTO;
import app.microservice.client.dto.ClientUpdateDTO;
import app.microservice.client.entity.Client;
import app.microservice.client.repository.ClientRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository employeeRepository;
    private final KafkaProducerClient kafkaProducerClient;

    @Override
    public Iterable<Client> getAllClients(String fullName) {
        if (fullName != null) {
            return employeeRepository.findAllByFullNameLikeIgnoreCase(fullName);
        }
        return employeeRepository.findAll();
    }

    @Override
    public Client createClient(ClientStoreDTO storeDTO) {
        var client = employeeRepository.save(new Client(null, storeDTO.getFullName(), storeDTO.getPhone(), storeDTO.getAddress(), storeDTO.getEmail()));

        ObjectMapper mapper = new ObjectMapper();
        try {
            var clientJson = mapper.writeValueAsString(client);
            this.kafkaProducerClient.send("clients", clientJson, client.getId().toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return client;
    }

    @Override
    public void updateClient(ClientUpdateDTO updateDTO) {
        employeeRepository.findById(updateDTO.getId())
                .ifPresentOrElse(client -> {
                    client.setFullName(updateDTO.getFullName());
                    client.setPhone(updateDTO.getPhone());
                    client.setAddress(updateDTO.getAddress());
                    client.setEmail(updateDTO.getEmail());
                }, () -> {
                    throw new NoSuchElementException();
                });
    }

    @Override
    public void deleteClient(Integer id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return employeeRepository.findById(id);
    }

}
