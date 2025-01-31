package com.devilish.granja.services;

import com.devilish.granja.dto.request.DuckRequestDTO;
import com.devilish.granja.dto.response.DuckResponseDTO;

import java.util.List;

public interface DuckService {

    DuckResponseDTO save(DuckRequestDTO duckRequestDTO);
    DuckResponseDTO findById(Long id);
    List<DuckResponseDTO> findAll();
    DuckResponseDTO update(Long id, DuckRequestDTO duckRequestDTO);
    void delete(Long id);
}
