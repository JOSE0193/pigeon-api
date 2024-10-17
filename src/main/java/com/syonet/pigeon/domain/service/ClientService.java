package com.syonet.pigeon.domain.service;

import com.syonet.pigeon.api.dto.client.ClientDTO;
import com.syonet.pigeon.api.dto.client.ClientRequestDTO;
import com.syonet.pigeon.domain.exception.BusinessException;
import com.syonet.pigeon.domain.exception.RecordNotFoundException;
import com.syonet.pigeon.domain.mapper.ClientMapper;
import com.syonet.pigeon.domain.model.Client;
import com.syonet.pigeon.domain.repository.ClientRepository;
import com.syonet.pigeon.util.DateUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public List<ClientDTO> listAll() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDTO)
                .toList();
    }

    public ClientDTO findById(@Positive @NotNull Long id) {
        return clientRepository.findById(id).map(clientMapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public ClientDTO save(@Valid ClientRequestDTO clientRequestDTO) {
        clientRepository.findByEmail(clientRequestDTO.email()).stream()
                .findAny().ifPresent(c -> {
                    throw new BusinessException("A client with email " + clientRequestDTO.email() + " already exists.");
                });
        Client client = clientMapper.toEntity(clientRequestDTO);
        return clientMapper.toDTO(clientRepository.save(client));
    }

    public ClientDTO update(@Positive @NotNull Long id, @Valid ClientRequestDTO clientRequestDTO) {
        LocalDate dateBirth = DateUtil.parseDateFormaterBr(clientRequestDTO.dateBirth());
        clientRepository.findByEmail(clientRequestDTO.email()).stream()
                .findAny().ifPresent(c -> {
                    throw new BusinessException("A client with email " + clientRequestDTO.email() + " already exists.");
                });
        return clientRepository.findById(id).map(actual -> {
                    actual.setName(clientRequestDTO.name());
                    actual.setEmail(clientRequestDTO.email());
                    actual.setDateBirth(dateBirth);
                    return clientMapper.toDTO(clientRepository.save(actual));
                })
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@Positive @NotNull Long id) {
        clientRepository.delete(clientRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

}
