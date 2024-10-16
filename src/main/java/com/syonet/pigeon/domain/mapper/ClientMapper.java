package com.syonet.pigeon.domain.mapper;

import com.syonet.pigeon.api.dto.client.ClientDTO;
import com.syonet.pigeon.api.dto.client.ClientRequestDTO;
import com.syonet.pigeon.domain.model.Client;
import com.syonet.pigeon.util.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ClientMapper {

    public ClientDTO toDTO(Client client){
        if (client == null){
            return null;
        }
        String dateOfBirth = DateUtil.formatBrazilianDate(client.getDateBirth());
        return new ClientDTO(client.getId(), client.getName(), client.getEmail(), dateOfBirth);
    }

    public Client toEntity(ClientRequestDTO dto){
        if(dto == null) {
            return null;
        }
        LocalDate dateOfBirth = DateUtil.parseDateFormaterBr(dto.dateBirth());
        Client client = Client.builder()
                .name(dto.name())
                .email(dto.email())
                .dateBirth(dateOfBirth)
                .build();
        return client;
    }

}
