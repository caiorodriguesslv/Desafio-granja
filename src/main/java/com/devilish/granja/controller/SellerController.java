package com.devilish.granja.controller;

import com.devilish.granja.dto.request.SellerRequestDTO;
import com.devilish.granja.dto.response.SellerRankingResponseDTO;
import com.devilish.granja.dto.response.SellerResponseDTO;
import com.devilish.granja.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PostMapping
    public ResponseEntity<SellerResponseDTO> saveSeller(@RequestBody SellerRequestDTO sellerRequestDTO) {
        SellerResponseDTO savedSeller = sellerService.save(sellerRequestDTO);
        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/ranking/sales-count")
    public ResponseEntity<List<SellerRankingResponseDTO>> getTopSellersBySalesCount(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Boolean sold) {
        List<SellerRankingResponseDTO> ranking = sellerService.getTopSellersBySalesCount(startDate, endDate, sold);
        return new ResponseEntity<>(ranking, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerResponseDTO> findSellerById(@PathVariable Long id) {
        SellerResponseDTO seller = sellerService.findById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<SellerResponseDTO>> findAllSellers() {
        List<SellerResponseDTO> sellers = sellerService.findAll();
        return new ResponseEntity<>(sellers, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<SellerResponseDTO> updateSeller(
            @PathVariable Long id,
            @RequestBody SellerRequestDTO sellerRequestDTO) {
        SellerResponseDTO updatedSeller = sellerService.update(id, sellerRequestDTO);
        return new ResponseEntity<>(updatedSeller, HttpStatus.OK);
    }
}