package com.syonet.pigeon.api.dto.client;

public record ClientRequestDTO (
        String name,
        String email,
        String dateBirth
) {
}
