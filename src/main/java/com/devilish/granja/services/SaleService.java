package com.devilish.granja.services;

import com.devilish.granja.dto.request.SaleRequestDTO;
import com.devilish.granja.dto.response.SaleResponseDTO;

import java.util.List;

public interface SaleService {
    SaleResponseDTO save(SaleRequestDTO saleRequestDTO);
    SaleResponseDTO findById(Long id);
    List<SaleResponseDTO> findAll();
    SaleResponseDTO update(Long id, SaleRequestDTO saleRequestDTO);
    void delete(Long id); // Delete
}