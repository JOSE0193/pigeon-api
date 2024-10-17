package com.syonet.pigeon.domain.service;

import com.syonet.pigeon.api.dto.client.ClientDTO;
import com.syonet.pigeon.api.dto.client.ClientRequestDTO;
import com.syonet.pigeon.domain.exception.RecordNotFoundException;
import com.syonet.pigeon.domain.mapper.ClientMapper;
import com.syonet.pigeon.domain.model.Client;
import com.syonet.pigeon.domain.repository.ClientRepository;
import com.syonet.pigeon.util.ClientCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class ClientServiceTest {

    @InjectMocks
    private ClientService service;

    @Mock
    private ClientRepository repository;

    @Mock
    private ClientMapper mapper;

    @BeforeEach
    void setUp() {
        BDDMockito.when(repository.findAll())
                .thenReturn(List.of(ClientCreator.createValidClient()));

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(ClientCreator.createValidClient()));

        BDDMockito.when(repository.save(ArgumentMatchers.any(Client.class)))
                .thenReturn(ClientCreator.createValidClient());

        BDDMockito.doNothing().when(repository).delete(ArgumentMatchers.any(Client.class));
    }

    @Test
    @DisplayName("listAll returns list of clients when successful")
    void listAll_ReturnsListOfClients_WhenSuccessful(){
        Client expectedSavedNews = ClientCreator.createValidClient();
        ClientDTO expectedNewsDTO = ClientCreator.createValidClientDTO();
        BDDMockito.when(mapper.toDTO(expectedSavedNews)).thenReturn(expectedNewsDTO);

        List<ClientDTO> clients = service.listAll();

        Assertions.assertThat(clients)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(clients).isNotEmpty();
        Assertions.assertThat(clients.get(0).id()).isEqualTo(expectedSavedNews.getId());
    }

    @Test
    @DisplayName("findById returns client when successful")
    void findById_ReturnsClient_WhenSuccessful(){
        Client expectedSavedNews = ClientCreator.createValidClient();
        ClientDTO expectedNewsDTO = ClientCreator.createValidClientDTO();
        BDDMockito.when(mapper.toDTO(expectedSavedNews)).thenReturn(expectedNewsDTO);
        Long expectedId = expectedSavedNews.getId();

        ClientDTO client = service.findById(1L);

        Assertions.assertThat(client).isNotNull();

        Assertions.assertThat(client.id()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById returns an empty list of client when id is not found")
    void findById_ReturnstNull_WhenClientIsNotFound(){
        assertThrows(RecordNotFoundException.class, () -> service.findById(2L));

    }

    @Test
    @DisplayName("save returns client when successful")
    void save_ReturnsClient_WhenSuccessful(){
        ClientRequestDTO clientRequestDTO = ClientCreator.createValidRequestClient();
        Client expectedSavedClient = ClientCreator.createValidClient();
        ClientDTO expectedClientDTO = ClientCreator.createValidClientDTO();

        BDDMockito.when(mapper.toEntity(clientRequestDTO)).thenReturn(expectedSavedClient);
        BDDMockito.when(repository.save(expectedSavedClient)).thenReturn(expectedSavedClient);
        BDDMockito.when(mapper.toDTO(expectedSavedClient)).thenReturn(expectedClientDTO);

        ClientDTO clientDTO = service.save(clientRequestDTO);

        Assertions.assertThat(clientDTO).isNotNull().isEqualTo(expectedClientDTO);

        BDDMockito.verify(mapper).toEntity(clientRequestDTO);
        BDDMockito.verify(repository).save(expectedSavedClient);
        BDDMockito.verify(mapper).toDTO(expectedSavedClient);

    }

    @Test
    @DisplayName("replace updates client when successful")
    void replace_UpdatesClient_WhenSuccessful(){
        ClientRequestDTO clientRequestDTO = ClientCreator.createValidRequestClient();
        Client expectedSavedClient = ClientCreator.createValidClient();
        ClientDTO expectedClientDTO = ClientCreator.createValidClientDTO();

        BDDMockito.when(mapper.toEntity(clientRequestDTO)).thenReturn(expectedSavedClient);
        BDDMockito.when(repository.save(expectedSavedClient)).thenReturn(expectedSavedClient);
        BDDMockito.when(mapper.toDTO(expectedSavedClient)).thenReturn(expectedClientDTO);
        ClientDTO client = service.findById(1L);

        Assertions.assertThat(client).isNotNull();

        ClientDTO entity = service.update(1L, clientRequestDTO);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity).isEqualTo(expectedClientDTO);

    }


    @Test
    @DisplayName("delete removes client when successful")
    void delete_RemovesClient_WhenSuccessful(){

        Assertions.assertThatCode(() -> service.delete(1L))
                .doesNotThrowAnyException();

    }

}