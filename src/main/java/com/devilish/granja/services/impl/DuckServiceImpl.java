package com.devilish.granja.services.impl;

import com.devilish.granja.dto.request.DuckRequestDTO;
import com.devilish.granja.dto.response.DuckResponseDTO;
import com.devilish.granja.entities.Duck;
import com.devilish.granja.repository.DuckRepository;
import com.devilish.granja.services.DuckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class DuckServiceImpl implements DuckService {


    private final DuckRepository duckRepository;

    @Override
    public DuckResponseDTO save(DuckRequestDTO duckRequestDTO) {
        log.info("Iniciando método save");

        Duck mae = null;
        if (duckRequestDTO.getMaeId() != null) {
            mae = duckRepository.findById(duckRequestDTO.getMaeId()).orElse(null);
        }

        Duck duck = Duck.builder()
                .name(duckRequestDTO.getName())
                .mae(mae)
                .build();

        Duck savedDuck = duckRepository.save(duck);

        return DuckResponseDTO.builder()
                .id(savedDuck.getId())
                .name(savedDuck.getName())
                .maeId(savedDuck.getMae() != null ? savedDuck.getMae().getId() : null)
                .build();

    }

}
