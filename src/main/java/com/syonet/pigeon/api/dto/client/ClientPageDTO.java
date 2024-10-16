package com.syonet.pigeon.api.dto.client;

import java.util.List;

public record ClientPageDTO(
        List<ClientDTO> clients,
        long totalElements,
        int totalPages
) {
}
