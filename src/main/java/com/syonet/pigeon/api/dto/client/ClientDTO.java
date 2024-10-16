package com.syonet.pigeon.api.dto.client;

import java.time.LocalDateTime;

public record ClientDTO(
        Long id,
        String name,
        String email,
        String dateBirth
) {
}
