package com.devilish.granja.services;

import com.devilish.granja.dto.request.SellerRequestDTO;
import com.devilish.granja.dto.response.SellerRankingResponseDTO;
import com.devilish.granja.dto.response.SellerResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface SellerService {
    SellerResponseDTO save(SellerRequestDTO sellerRequestDTO);
    SellerResponseDTO findById(Long id);
    List<SellerResponseDTO> findAll();
    SellerResponseDTO update(Long id, SellerRequestDTO sellerRequestDTO);
    List<SellerRankingResponseDTO> getTopSellersBySalesCount(LocalDateTime startDate, LocalDateTime endDate, Boolean sold);
}
