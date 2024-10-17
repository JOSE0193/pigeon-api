package com.syonet.pigeon.api.controller;

import com.syonet.pigeon.api.dto.client.ClientDTO;
import com.syonet.pigeon.api.dto.client.ClientRequestDTO;
import com.syonet.pigeon.domain.service.ClientService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class ClientControllerTest {

    @InjectMocks
    private ClientController controller;

    @Mock
    private ClientService service;

    @BeforeEach
    void setUp() {
        BDDMockito.when(service.listAll())
                .thenReturn(List.of(ClientCreator.createValidClientDTO()));

        BDDMockito.when(service.findById(ArgumentMatchers.anyLong()))
                .thenReturn(ClientCreator.createValidClientDTO());
        BDDMockito.when(service.findById(2L)).thenReturn(null);

        BDDMockito.when(service.save(ArgumentMatchers.any(ClientRequestDTO.class)))
                .thenReturn(ClientCreator.createValidClientDTO());

        BDDMockito.when(service.update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(ClientRequestDTO.class)))
                .thenReturn(ClientCreator.createValidClientDTO());

        BDDMockito.doNothing().when(service).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("listAll returns list of clients when successful")
    void listAll_ReturnsListOfClients_WhenSuccessful(){
        String expectedName = ClientCreator.createValidClientDTO().name();

        List<ClientDTO> clients = controller.listAll();

        Assertions.assertThat(clients)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(clients.get(0).name()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns client when successful")
    void findById_ReturnsClient_WhenSuccessful(){
        Long expectedId = ClientCreator.createValidClientDTO().id();

        ClientDTO client = controller.findById(1L);

        Assertions.assertThat(client).isNotNull();

        Assertions.assertThat(client.id()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById returns an empty list of client when id is not found")
    void findById_ReturnstNull_WhenClientIsNotFound(){
        ClientDTO client = controller.findById(2L);

        Assertions.assertThat(client).isNull();

    }

    @Test
    @DisplayName("save returns client when successful")
    void save_ReturnsClient_WhenSuccessful(){

        ClientDTO clientDTO = controller.create(ClientCreator.createValidRequestClient());

        Assertions.assertThat(clientDTO).isNotNull().isEqualTo(ClientCreator.createValidClientDTO());

    }

    @Test
    @DisplayName("replace updates client when successful")
    void replace_UpdatesClient_WhenSuccessful(){

        Assertions.assertThatCode(() ->controller.update(1L, ClientCreator.createValidRequestClient()))
                .doesNotThrowAnyException();

        ResponseEntity<ClientDTO> entity = controller.update(1L, ClientCreator.createValidRequestClient());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


    @Test
    @DisplayName("delete removes client when successful")
    void delete_RemovesClient_WhenSuccessful(){

        Assertions.assertThatCode(() ->controller.delete(1L))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = controller.delete(1L);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}