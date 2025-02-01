package com.devilish.granja;

import com.devilish.granja.dto.request.SellerRequestDTO;
import com.devilish.granja.dto.response.SellerResponseDTO;
import com.devilish.granja.entities.Seller;
import com.devilish.granja.exceptions.OperationNotAllowedException;
import com.devilish.granja.exceptions.ResourceNotFoundException;
import com.devilish.granja.repository.SaleRepository;
import com.devilish.granja.repository.SellerRepository;
import com.devilish.granja.services.impl.SellerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceImplTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private SellerServiceImpl sellerService;

    private Seller seller;
    private SellerRequestDTO sellerRequestDTO;

    @BeforeEach
    void setUp() {
        seller = Seller.builder()
                .id(1L)
                .name("João Silva")
                .cpf("12345678900")
                .registration("REG123")
                .build();

        sellerRequestDTO = SellerRequestDTO.builder()
                .name("João Silva")
                .cpf("12345678900")
                .registration("REG123")
                .build();
    }

    @Test
    void save_Success() {
        when(sellerRepository.findByCpf(sellerRequestDTO.getCpf())).thenReturn(Optional.empty());
        when(sellerRepository.findByRegistration(sellerRequestDTO.getRegistration())).thenReturn(Optional.empty());
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        SellerResponseDTO response = sellerService.save(sellerRequestDTO);

        assertNotNull(response);
        assertEquals(seller.getId(), response.getId());
        assertEquals(seller.getName(), response.getName());
        verify(sellerRepository, times(1)).save(any(Seller.class));
    }

    @Test
    void save_ThrowsOperationNotAllowedException_WhenCpfExists() {
        when(sellerRepository.findByCpf(sellerRequestDTO.getCpf())).thenReturn(Optional.of(seller));

        assertThrows(OperationNotAllowedException.class, () -> sellerService.save(sellerRequestDTO));
    }

    @Test
    void findById_Success() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        SellerResponseDTO response = sellerService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void findById_ThrowsResourceNotFoundException() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sellerService.findById(1L));
    }

    @Test
    void findAll_ReturnsSellerList() {
        when(sellerRepository.findAll()).thenReturn(List.of(seller));

        List<SellerResponseDTO> responseList = sellerService.findAll();

        assertEquals(1, responseList.size());
        assertEquals("João Silva", responseList.get(0).getName());
    }

    @Test
    void update_Success() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        SellerResponseDTO response = sellerService.update(1L, sellerRequestDTO);

        assertNotNull(response);
        assertEquals("João Silva", response.getName());
    }

    @Test
    void update_ThrowsResourceNotFoundException() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sellerService.update(1L, sellerRequestDTO));
    }
}
