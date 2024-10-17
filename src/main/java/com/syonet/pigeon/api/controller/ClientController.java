package com.syonet.pigeon.api.controller;

import com.syonet.pigeon.api.dto.client.ClientDTO;
import com.syonet.pigeon.api.dto.client.ClientPageDTO;
import com.syonet.pigeon.api.dto.client.ClientRequestDTO;
import com.syonet.pigeon.domain.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/clients")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "List", description = "Returning a list of customers.")
    @GetMapping
    public List<ClientDTO> listAll() {
        return clientService.listAll();
    }

    @Operation(summary = "Find", description = "Returning a customer.")
    @GetMapping("/{id}")
    public ClientDTO findById(@PathVariable @Positive @NotNull Long id) {
        return clientService.findById(id);
    }

    @Operation(summary = "Save", description = "Save a client.")
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ClientDTO create(@RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        return clientService.create(clientRequestDTO);
    }

    @Operation(summary = "Update", description = "Update a client.")
    @PutMapping(value = "/{id}")
    public ClientDTO update(@PathVariable @Positive @NotNull Long id,
                            @RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        return clientService.update(id, clientRequestDTO);
    }

    @Operation(summary = "Delete", description = "Delete a client.")
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive @NotNull Long id) {
        clientService.delete(id);
    }

}
