package com.devilish.granja;

import com.devilish.granja.dto.request.DuckRequestDTO;
import com.devilish.granja.dto.response.DuckResponseDTO;
import com.devilish.granja.entities.Duck;
import com.devilish.granja.exceptions.InvalidDataException;
import com.devilish.granja.exceptions.InvalidMotherException;
import com.devilish.granja.exceptions.OperationNotAllowedException;
import com.devilish.granja.repository.DuckRepository;
import com.devilish.granja.services.impl.DuckServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DuckServiceImplTest {

    @Mock
    private DuckRepository duckRepository;

    @InjectMocks
    private DuckServiceImpl duckService;

    private Duck duck;
    private DuckRequestDTO duckRequestDTO;

    @BeforeEach
    void setUp() {
        duck = Duck.builder()
                .id(1L)
                .name("Pato Teste")
                .duckMother(Duck.builder().id(2L).build())
                .price(50.0)
                .sold(false)
                .build();

        duckRequestDTO = DuckRequestDTO.builder()
                .name("Pato Novo")
                .motherId(2L)
                .price(100.0)
                .sold(false)
                .build();
    }

    @Test
    void save_ShouldReturnDuckResponseDTO_WhenValidData() {
        when(duckRepository.save(any(Duck.class))).thenReturn(duck);

        DuckResponseDTO response = duckService.save(duckRequestDTO);

        assertNotNull(response);
        assertEquals(duck.getId(), response.getId());
        verify(duckRepository, times(1)).save(any(Duck.class));
    }

    @Test
    void save_ShouldThrowInvalidDataException_WhenNameIsNull() {
        duckRequestDTO.setName(null);
        assertThrows(InvalidDataException.class, () -> duckService.save(duckRequestDTO));
    }

    @Test
    void findById_ShouldReturnDuckResponseDTO_WhenDuckExists() {
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));

        DuckResponseDTO response = duckService.findById(1L);

        assertNotNull(response);
        assertEquals(duck.getId(), response.getId());
    }

    @Test
    void findById_ShouldThrowException_WhenDuckNotFound() {
        when(duckRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OperationNotAllowedException.class, () -> duckService.findById(1L));
    }

    @Test
    void findAll_ShouldReturnListOfDucks() {
        when(duckRepository.findAll()).thenReturn(Arrays.asList(duck));

        List<DuckResponseDTO> responseList = duckService.findAll();

        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
    }

    @Test
    void update_ShouldReturnUpdatedDuckResponseDTO_WhenValidData() {
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));
        when(duckRepository.findById(2L)).thenReturn(Optional.of(Duck.builder().id(2L).build()));
        when(duckRepository.save(any(Duck.class))).thenReturn(duck);

        DuckResponseDTO response = duckService.update(1L, duckRequestDTO);

        assertNotNull(response);
        assertEquals(duckRequestDTO.getName(), response.getName());
    }

    @Test
    void update_ShouldThrowInvalidMotherException_WhenMotherIdIsNull() {
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));
        duckRequestDTO.setMotherId(null);

        assertThrows(InvalidMotherException.class, () -> duckService.update(1L, duckRequestDTO));
    }

    @Test
    void delete_ShouldRemoveDuck_WhenDuckExists() {
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));
        doNothing().when(duckRepository).delete(duck);

        assertDoesNotThrow(() -> duckService.delete(1L));
        verify(duckRepository, times(1)).delete(duck);
    }

    @Test
    void delete_ShouldThrowException_WhenDuckNotFound() {
        when(duckRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(OperationNotAllowedException.class, () -> duckService.delete(1L));
    }
}