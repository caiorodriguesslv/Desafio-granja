package com.devilish.granja.services;

import com.devilish.granja.dto.request.SellerRequestDTO;
import com.devilish.granja.dto.response.SellerResponseDTO;

import java.util.List;

public interface SellerService {
    SellerResponseDTO save(SellerRequestDTO sellerRequestDTO);
    SellerResponseDTO findById(Long id);
    List<SellerResponseDTO> findAll();
    SellerResponseDTO update(Long id, SellerRequestDTO sellerRequestDTO);
}
