package com.devilish.granja.services.impl;

import com.devilish.granja.dto.request.ClientRequestDTO;
import com.devilish.granja.dto.response.ClientResponseDTO;
import com.devilish.granja.entities.Client;
import com.devilish.granja.repository.ClientRepository;
import com.devilish.granja.services.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {


    private final ClientRepository clientRepository;

    @Override
    public ClientResponseDTO save(ClientRequestDTO clientResponseDTO) {
        log.info("Iniciando método save");

        Client client = Client.builder()
                .name(clientResponseDTO.getName())
                .discountEligible(clientResponseDTO.isDiscountEligible())
                .build();

        Client savedClient = clientRepository.save(client);

        log.info("Cliente Salvo com sucesso: {}" + savedClient);

        return ClientResponseDTO.builder()
                .id(savedClient.getId())
                .name(savedClient.getName())
                .discountEligible(savedClient.isDiscountEligible())
                .build();
    }

    @Override
    public ClientResponseDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + id));

        return ClientResponseDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .discountEligible(client.isDiscountEligible())
                .build();
    }

    @Override
    public List<ClientResponseDTO> findAll() {
        List<Client> clients = clientRepository.findAll();

        return clients.stream()
                .map(client -> ClientResponseDTO.builder()
                        .id(client.getId())
                        .name(client.getName())
                        .discountEligible(client.isDiscountEligible())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ClientResponseDTO update(Long id, ClientRequestDTO clientRequestDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: "+ id));

        client.setName(clientRequestDTO.getName());
        client.setDiscountEligible(clientRequestDTO.isDiscountEligible());

        Client updatedClient = clientRepository.save(client);

        return ClientResponseDTO.builder()
                .id(updatedClient.getId())
                .name(updatedClient.getName())
                .discountEligible(updatedClient.isDiscountEligible())
                .build();
    }

    @Override
    public void delete(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + id));

        clientRepository.delete(client);
    }
}
