package com.devilish.granja.controller;

import com.devilish.granja.dto.request.SaleRequestDTO;
import com.devilish.granja.dto.response.SaleResponseDTO;
import com.devilish.granja.dto.response.SoldDuckResponseDTO;
import com.devilish.granja.services.SaleService;
import com.devilish.granja.services.impl.SaleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;


    @PostMapping
    public ResponseEntity<SaleResponseDTO> createSale(@RequestBody SaleRequestDTO saleRequestDTO) {
        SaleResponseDTO saleResponseDTO = saleService.save(saleRequestDTO);
        return new ResponseEntity<>(saleResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> getSaleById(@PathVariable Long id) {
        SaleResponseDTO saleResponseDTO = saleService.findById(id);
        return new ResponseEntity<>(saleResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/sold/ducks")
    public List<SoldDuckResponseDTO> findAllSoldDucks() {
        return saleService.findAllSoldDucks();
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> getAllSales() {
        List<SaleResponseDTO> sales = saleService.findAll();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> updateSale(@PathVariable Long id, @RequestBody SaleRequestDTO saleRequestDTO) {
        SaleResponseDTO saleResponseDTO = saleService.update(id, saleRequestDTO);
        return new ResponseEntity<>(saleResponseDTO, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/report")
    public ResponseEntity<ByteArrayResource> downloadSalesReport() throws IOException {
        byte[] reportBytes = saleService.generateSalesReport();

        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sales_report.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }

}