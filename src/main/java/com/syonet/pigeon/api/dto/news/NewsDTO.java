package com.syonet.pigeon.api.dto.news;

import java.time.LocalDateTime;

public record NewsDTO(
        Long id,
        String title,
        String description,
        String link
) {
}
