package com.devilish.granja.services;

import com.devilish.granja.dto.request.ClientRequestDTO;
import com.devilish.granja.dto.response.ClientResponseDTO;

import java.util.List;

public interface ClientService {
    ClientResponseDTO save(ClientRequestDTO clientRequestDTO);
    ClientResponseDTO findById(Long id);
    List<ClientResponseDTO> findAll();
    ClientResponseDTO update(Long id, ClientRequestDTO clientRequestDTO);
    void delete(Long id);
}