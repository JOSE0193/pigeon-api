package com.syonet.pigeon.util;

import com.syonet.pigeon.api.dto.client.ClientDTO;
import com.syonet.pigeon.api.dto.client.ClientRequestDTO;
import com.syonet.pigeon.domain.model.Client;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClientCreator {

    public static Client createClientToBeSaved(){
        return Client.builder()
                .name("Tanjiro Kamado")
                .email("tanjiro@teste.com")
                .dateBirth(LocalDate.of(1999, 10, 17))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ClientDTO createValidClientDTO(){
        return new ClientDTO(1L, "Tanjiro Kamado", "tanjiro@teste.com", "17/10/1995");
    }

    public static Client createValidClient(){
        return Client.builder()
                .id(1L)
                .name("Tanjiro Kamado")
                .email("tanjiro@teste.com")
                .dateBirth(LocalDate.of(1999, 10, 17))
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }

    public static ClientRequestDTO createValidRequestClient(){
        return new ClientRequestDTO(ClientCreator.createValidClientDTO().name(), ClientCreator.createValidClientDTO().email(),
                ClientCreator.createValidClientDTO().dateBirth());
    }

}
