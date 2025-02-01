package com.devilish.granja;

import com.devilish.granja.dto.request.ClientRequestDTO;
import com.devilish.granja.dto.response.ClientResponseDTO;
import com.devilish.granja.entities.Client;
import com.devilish.granja.exceptions.InvalidDataException;
import com.devilish.granja.exceptions.ResourceNotFoundException;
import com.devilish.granja.repository.ClientRepository;
import com.devilish.granja.services.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;
    private ClientRequestDTO clientRequestDTO;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setName("João Silva");
        client.setDiscountEligible(true);

        clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setName("João Silva");
        clientRequestDTO.setDiscountEligible(true);
    }

    @Test
    void save_ShouldReturnClientResponseDTO_WhenValidDataIsProvided() {

        when(clientRepository.save(any(Client.class))).thenReturn(client);


        ClientResponseDTO result = clientService.save(clientRequestDTO);


        assertNotNull(result);
        assertEquals(client.getId(), result.getId());
        assertEquals(client.getName(), result.getName());
        assertEquals(client.isDiscountEligible(), result.isDiscountEligible());

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void save_ShouldThrowInvalidDataException_WhenNameIsEmpty() {

        clientRequestDTO.setName("");


        assertThrows(InvalidDataException.class, () -> clientService.save(clientRequestDTO));

        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void findById_ShouldReturnClientResponseDTO_WhenClientExists() {

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));


        ClientResponseDTO result = clientService.findById(1L);


        assertNotNull(result);
        assertEquals(client.getId(), result.getId());
        assertEquals(client.getName(), result.getName());
        assertEquals(client.isDiscountEligible(), result.isDiscountEligible());

        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenClientDoesNotExist() {

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> clientService.findById(1L));

        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void findAll_ShouldReturnListOfClientResponseDTO_WhenClientsExist() {

        when(clientRepository.findAll()).thenReturn(Collections.singletonList(client));


        List<ClientResponseDTO> result = clientService.findAll();


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(client.getId(), result.get(0).getId());
        assertEquals(client.getName(), result.get(0).getName());
        assertEquals(client.isDiscountEligible(), result.get(0).isDiscountEligible());

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void update_ShouldReturnUpdatedClientResponseDTO_WhenValidDataIsProvided() {

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);


        ClientResponseDTO result = clientService.update(1L, clientRequestDTO);


        assertNotNull(result);
        assertEquals(client.getId(), result.getId());
        assertEquals(client.getName(), result.getName());
        assertEquals(client.isDiscountEligible(), result.isDiscountEligible());

        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void update_ShouldThrowInvalidDataException_WhenNameIsEmpty() {

        clientRequestDTO.setName("");


        assertThrows(InvalidDataException.class, () -> clientService.update(1L, clientRequestDTO));

        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void delete_ShouldDeleteClient_WhenClientExists() {

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).delete(client);


        clientService.delete(1L);

        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenClientDoesNotExist() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clientService.delete(1L));

        verify(clientRepository, never()).delete(any(Client.class));
    }
}