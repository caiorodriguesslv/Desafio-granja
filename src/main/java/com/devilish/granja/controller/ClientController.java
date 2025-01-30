package com.devilish.granja.controller;

import com.devilish.granja.dto.request.ClientRequestDTO;
import com.devilish.granja.dto.response.ClientResponseDTO;
import com.devilish.granja.services.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponseDTO> saveClient(@RequestBody ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO savedClient = clientService.save(clientRequestDTO);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findClientById(@PathVariable Long id) {
        ClientResponseDTO client = clientService.findById(id);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> findAllClients() {
        List<ClientResponseDTO> clients = clientService.findAll();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(
            @PathVariable Long id,
            @RequestBody ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO updatedClient = clientService.update(id, clientRequestDTO);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

