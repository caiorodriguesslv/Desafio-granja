package com.devilish.granja.services.impl;

import com.devilish.granja.dto.request.DuckRequestDTO;
import com.devilish.granja.dto.response.DuckResponseDTO;
import com.devilish.granja.entities.Duck;
import com.devilish.granja.exceptions.InvalidMaeException;
import com.devilish.granja.repository.DuckRepository;
import com.devilish.granja.services.DuckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DuckServiceImpl implements DuckService {

    private final DuckRepository duckRepository;

    @Override
    public DuckResponseDTO save(DuckRequestDTO duckRequestDTO) {
        log.info("Iniciando método save");

        if (duckRequestDTO.getName() == null || duckRequestDTO.getName().trim().isEmpty()) {
            throw new RuntimeException("O nome do pato não pode estar vazio!");
        }

        if (duckRequestDTO.getMaeId() == null) {
            throw new RuntimeException("O ID da mãe não pode ser nulo!");
        }

        if (duckRequestDTO.getPrice() <= 0) {
            throw new RuntimeException("O preço do pato deve ser maior que zero!");
        }

        log.info("Salvando pato com mãe de ID: {}", duckRequestDTO.getMaeId());


        Duck duck = Duck.builder()
                .name(duckRequestDTO.getName())
                .mae(Duck.builder().id(duckRequestDTO.getMaeId()).build())
                .price(duckRequestDTO.getPrice())
                .sold(duckRequestDTO.isSold())
                .build();


        Duck savedDuck = duckRepository.save(duck);

        log.info("Pato salvo com sucesso: {}", savedDuck);


        return DuckResponseDTO.builder()
                .id(savedDuck.getId())
                .name(savedDuck.getName())
                .maeId(savedDuck.getMae() != null ? savedDuck.getMae().getId() : null)
                .price(savedDuck.getPrice())
                .sold(savedDuck.isSold())
                .build();
    }


    @Override
    public DuckResponseDTO findById(Long id) {
        log.info("Buscando pato com ID: {}", id);

        Duck duck = duckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pato não encontrado com o ID: " + id));

        return DuckResponseDTO.builder()
                .id(duck.getId())
                .name(duck.getName())
                .maeId(duck.getMae() != null ? duck.getMae().getId() : null)
                .price(duck.getPrice())
                .sold(duck.isSold())
                .build();
    }

    @Override
    public List<DuckResponseDTO> findAll() {
        log.info("Listando todos os patos");

        List<Duck> ducks = duckRepository.findAll();

        return ducks.stream()
                .map(duck -> DuckResponseDTO.builder()
                        .id(duck.getId())
                        .name(duck.getName())
                        .maeId(duck.getMae() != null ? duck.getMae().getId() : null)
                        .price(duck.getPrice())
                        .sold(duck.isSold())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public DuckResponseDTO update(Long id, DuckRequestDTO duckRequestDTO) {
        log.info("Atualizando pato com ID: {}", id);


        Duck duck = duckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pato não encontrado com o ID: " + id));


        if (duckRequestDTO.getMaeId() == null) {
            throw new InvalidMaeException("O ID da mãe não pode ser nulo!");
        }

        if (duckRequestDTO.getPrice() <= 0) {
            throw new RuntimeException("O preço do pato deve ser maior que zero!");
        }


        Duck mae = duckRepository.findById(duckRequestDTO.getMaeId())
                .orElseThrow(() -> new InvalidMaeException("Mãe não encontrada com o ID: " + duckRequestDTO.getMaeId()));


        duck.setName(duckRequestDTO.getName());
        duck.setMae(mae);
        duck.setPrice(duckRequestDTO.getPrice());
        duck.setSold(duckRequestDTO.isSold());


        Duck updatedDuck = duckRepository.save(duck);

        log.info("Pato atualizado com sucesso: {}", updatedDuck);


        return DuckResponseDTO.builder()
                .id(updatedDuck.getId())
                .name(updatedDuck.getName())
                .maeId(updatedDuck.getMae() != null ? updatedDuck.getMae().getId() : null)
                .price(updatedDuck.getPrice())
                .sold(updatedDuck.isSold())
                .build();
    }

    @Override
    public void delete(Long id) {
        log.info("Excluindo pato com ID: {}", id);

        Duck duck = duckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pato não encontrado com o ID: " + id));

        duckRepository.delete(duck);

        log.info("Pato excluído com sucesso: {}", duck);
    }
}