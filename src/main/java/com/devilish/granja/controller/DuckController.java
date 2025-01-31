package com.devilish.granja.controller;

import com.devilish.granja.dto.request.DuckRequestDTO;
import com.devilish.granja.dto.response.DuckResponseDTO;
import com.devilish.granja.services.DuckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/ducks")
@Validated
@RequiredArgsConstructor
public class DuckController {


    private final DuckService duckService;

    @PostMapping
    public ResponseEntity<DuckResponseDTO> save(@Valid @RequestBody DuckRequestDTO duckRequestDTO) {
        DuckResponseDTO duckResponseDTO = duckService.save(duckRequestDTO);
        return ResponseEntity.ok(duckResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DuckResponseDTO> findDuckById(@PathVariable Long id) {
        DuckResponseDTO duck = duckService.findById(id);
        return new ResponseEntity<>(duck, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DuckResponseDTO>> findAllDucks() {
        List<DuckResponseDTO> ducks = duckService.findAll();
        return new ResponseEntity<>(ducks, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DuckResponseDTO> updateDuck(
            @PathVariable Long id,
            @RequestBody DuckRequestDTO duckRequestDTO) {
        DuckResponseDTO updatedDuck = duckService.update(id, duckRequestDTO);
        return new ResponseEntity<>(updatedDuck, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDuck(@PathVariable Long id) {
        duckService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}