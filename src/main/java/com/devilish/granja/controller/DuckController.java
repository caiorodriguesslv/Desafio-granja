package com.devilish.granja.controller;

import com.devilish.granja.dto.request.DuckRequestDTO;
import com.devilish.granja.dto.response.DuckResponseDTO;
import com.devilish.granja.services.DuckService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/ducks")
@Validated
public class DuckController {

    @Autowired
    private DuckService duckService;

    @PostMapping
    public ResponseEntity<DuckResponseDTO> save(@Valid @RequestBody DuckRequestDTO duckRequestDTO) {
        DuckResponseDTO duckResponseDTO = duckService.save(duckRequestDTO);
        return ResponseEntity.ok(duckResponseDTO);
    }
}