package com.devilish.granja;

import com.devilish.granja.dto.request.SaleRequestDTO;
import com.devilish.granja.dto.response.SaleResponseDTO;
import com.devilish.granja.entities.*;
import com.devilish.granja.exceptions.InvalidDataException;
import com.devilish.granja.exceptions.OperationNotAllowedException;
import com.devilish.granja.exceptions.ResourceNotFoundException;
import com.devilish.granja.repository.*;
import com.devilish.granja.services.impl.SaleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private DuckRepository duckRepository;

    @InjectMocks
    private SaleServiceImpl saleService;

    private Client client;
    private Seller seller;
    private Duck duck;
    private Sale sale;
    private SaleRequestDTO saleRequestDTO;


    @Test
    void shouldSaveSaleSuccessfully() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(seller));
        when(duckRepository.findAllById(any())).thenReturn(List.of(duck));
        when(saleRepository.save(any())).thenReturn(sale);

        SaleResponseDTO response = saleService.save(saleRequestDTO);

        assertNotNull(response);
        assertEquals(1L, response.getClientId());
        assertEquals(80.0, response.getTotalValue());
        verify(saleRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowExceptionWhenClientNotFound() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> saleService.save(saleRequestDTO));
    }

    @Test
    void shouldThrowExceptionWhenSellerNotFound() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> saleService.save(saleRequestDTO));
    }

    @Test
    void shouldThrowExceptionWhenNoDucksFound() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(seller));
        when(duckRepository.findAllById(any())).thenReturn(List.of());
        assertThrows(InvalidDataException.class, () -> saleService.save(saleRequestDTO));
    }

    @Test
    void shouldThrowExceptionWhenDuckAlreadySold() {
        duck.setSold(true);
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(seller));
        when(duckRepository.findAllById(any())).thenReturn(List.of(duck));
        assertThrows(OperationNotAllowedException.class, () -> saleService.save(saleRequestDTO));
    }

    @Test
    void shouldFindSaleById() {
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
        SaleResponseDTO response = saleService.findById(1L);
        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void shouldThrowExceptionWhenSaleNotFound() {
        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> saleService.findById(1L));
    }
}
