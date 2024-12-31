package app.microservice.client.controller;

import app.microservice.client.dto.ClientStoreDTO;
import app.microservice.client.dto.ClientUpdateDTO;
import app.microservice.client.entity.Client;
import app.microservice.client.request.ClientStoreRequest;
import app.microservice.client.request.ClientUpdateRequest;
import app.microservice.client.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    private final MessageSource messageSource;

    @GetMapping
    public Iterable<Client> index(@RequestParam(name = "fullName", required = false) String fullName) {
        return clientService.getAllClients(fullName);
    }

    @GetMapping("/{id}")
    public Client show(@PathVariable Integer id) {
        return clientService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("error.404.title"));
    }

    @PostMapping
    public ResponseEntity<Client> store(
            @Valid @RequestBody ClientStoreRequest storeRequest,
            BindingResult bindingResult
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }

        var clientStoreDTO = new ClientStoreDTO(storeRequest.fullName(), storeRequest.phone(), storeRequest.address(), storeRequest.email());
        return ResponseEntity.created(null).body(clientService.createClient(clientStoreDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@Valid @RequestBody ClientUpdateRequest updateRequest,
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

        var updateDTO = new ClientUpdateDTO(id, updateRequest.fullName(), updateRequest.phone(), updateRequest.address(), updateRequest.email());
        clientService.updateClient(updateDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void destroy(@PathVariable Integer id) {
        clientService.deleteClient(id);
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
