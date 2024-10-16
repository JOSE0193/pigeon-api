package com.syonet.pigeon.api.dto.news;

public record NewsRequestDTO(
        String title,
        String description,
        String link
) {
}
