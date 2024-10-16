package com.syonet.pigeon.api.controller;

import com.syonet.pigeon.api.dto.news.NewsDTO;
import com.syonet.pigeon.api.dto.news.NewsRequestDTO;
import com.syonet.pigeon.domain.service.NewsService;
import jakarta.validation.Valid;
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
@RequestMapping("api/noticias")
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public List<NewsDTO> listAll() {
        return newsService.listAll();
    }

    @GetMapping("/findAllNotProcessed")
    public List<NewsDTO> findAllNotProcessed() {
        return newsService.findByNewsNotProcessed();
    }

    @GetMapping("/{id}")
    public NewsDTO findById(@PathVariable @Positive @NotNull Long id) {
        return newsService.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public NewsDTO create(@RequestBody @Valid NewsRequestDTO newsRequestDTO) {
        return newsService.save(newsRequestDTO);
    }

    @PutMapping(value = "/{id}")
    public NewsDTO update(@PathVariable @Positive @NotNull Long id,
                            @RequestBody @Valid NewsRequestDTO newsRequestDTO) {
        return newsService.update(id, newsRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive @NotNull Long id) {
        newsService.delete(id);
    }

}
