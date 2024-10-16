package com.syonet.pigeon.api.dto.news;

import java.util.List;

public record NewsPageDTO(
        List<NewsDTO> newsDTOS,
        long totalElements,
        int totalPages
) {
}
