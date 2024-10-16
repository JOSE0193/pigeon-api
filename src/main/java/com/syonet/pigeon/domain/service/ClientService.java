package com.syonet.pigeon.domain.service;

import com.syonet.pigeon.api.dto.client.ClientDTO;
import com.syonet.pigeon.api.dto.client.ClientPageDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public ClientPageDTO findAllPageable(@PositiveOrZero int page, @Positive @Max(1000) int pageSize){
        Page<Client> clientPage = clientRepository.findAll(PageRequest.of(page, pageSize));
        List<ClientDTO> clients = clientPage.getContent().stream().map(clientMapper::toDTO).toList();
        return new ClientPageDTO(clients, clientPage.getTotalElements(), clientPage.getTotalPages());
    }

    public ClientDTO findById(@Positive @NotNull Long id) {
        return clientRepository.findById(id).map(clientMapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public ClientDTO create(@Valid ClientRequestDTO clientRequestDTO) {
        clientRepository.findByEmail(clientRequestDTO.email()).stream()
                .findAny().ifPresent(c -> {
                    throw new BusinessException("A client with email " + clientRequestDTO.email() + " already exists.");
                });
        Client course = clientMapper.toEntity(clientRequestDTO);
        return clientMapper.toDTO(clientRepository.save(course));
    }

    public ClientDTO update(@Positive @NotNull Long id, @Valid ClientRequestDTO clientRequestDTO) {
        LocalDate dateBirth = DateUtil.parseDateFormaterBr(clientRequestDTO.dateBirth());
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
