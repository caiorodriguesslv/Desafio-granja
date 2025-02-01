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
            throw new RuntimeException("Verifique os dados fornecidos.");
        }

        if (duckRequestDTO.getMotherId() == null) {
            throw new RuntimeException("Verifique os dados fornecidos.");
        }

        if (duckRequestDTO.getPrice() <= 0) {
            throw new RuntimeException("Verifique os dados fornecidos.");
        }

        log.info("Salvando pato com mãe de ID: {}", duckRequestDTO.getMotherId());

        Duck duck = Duck.builder()
                .name(duckRequestDTO.getName())
                .duckMother(Duck.builder().id(duckRequestDTO.getMotherId()).build())
                .price(duckRequestDTO.getPrice())
                .sold(duckRequestDTO.isSold())
                .build();

        Duck savedDuck = duckRepository.save(duck);

        log.info("Pato salvo com sucesso.");

        return DuckResponseDTO.builder()
                .id(savedDuck.getId())
                .name(savedDuck.getName())
                .motherId(savedDuck.getDuckMother() != null ? savedDuck.getDuckMother().getId() : null)
                .price(savedDuck.getPrice())
                .sold(savedDuck.isSold())
                .build();
    }

    @Override
    public DuckResponseDTO findById(Long id) {
        log.info("Buscando pato com ID: {}", id);

        Duck duck = duckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operação não permitida."));

        return DuckResponseDTO.builder()
                .id(duck.getId())
                .name(duck.getName())
                .motherId(duck.getDuckMother() != null ? duck.getDuckMother().getId() : null)
                .price(duck.getPrice())
                .sold(duck.isSold())
                .build();
    }

    @Override
    public List<DuckResponseDTO> findAll() {
        log.info("Listando todos os patos.");

        List<Duck> ducks = duckRepository.findAll();

        return ducks.stream()
                .map(duck -> DuckResponseDTO.builder()
                        .id(duck.getId())
                        .name(duck.getName())
                        .motherId(duck.getDuckMother() != null ? duck.getDuckMother().getId() : null)
                        .price(duck.getPrice())
                        .sold(duck.isSold())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public DuckResponseDTO update(Long id, DuckRequestDTO duckRequestDTO) {
        log.info("Atualizando pato com ID: {}", id);

        Duck duck = duckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operação não permitida."));

        if (duckRequestDTO.getMotherId() == null) {
            throw new InvalidMaeException("Verifique os dados fornecidos.");
        }

        if (duckRequestDTO.getPrice() <= 0) {
            throw new RuntimeException("Verifique os dados fornecidos.");
        }

        Duck mae = duckRepository.findById(duckRequestDTO.getMotherId())
                .orElseThrow(() -> new InvalidMaeException("Operação não permitida."));

        duck.setName(duckRequestDTO.getName());
        duck.setDuckMother(mae);
        duck.setPrice(duckRequestDTO.getPrice());
        duck.setSold(duckRequestDTO.isSold());

        Duck updatedDuck = duckRepository.save(duck);

        log.info("Pato atualizado com sucesso.");

        return DuckResponseDTO.builder()
                .id(updatedDuck.getId())
                .name(updatedDuck.getName())
                .motherId(updatedDuck.getDuckMother() != null ? updatedDuck.getDuckMother().getId() : null)
                .price(updatedDuck.getPrice())
                .sold(updatedDuck.isSold())
                .build();
    }

    @Override
    public void delete(Long id) {
        log.info("Excluindo pato com ID: {}", id);

        Duck duck = duckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operação não permitida."));

        duckRepository.delete(duck);

        log.info("Pato excluído com sucesso.");
    }
}