package com.syonet.pigeon.api.controller;

import com.syonet.pigeon.api.dto.news.NewsDTO;
import com.syonet.pigeon.api.dto.news.NewsRequestDTO;
import com.syonet.pigeon.domain.service.EmailService;
import com.syonet.pigeon.domain.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/news")
public class NewsController {

    private final NewsService newsService;
    private final EmailService emailService;

    @Operation(summary = "List of unprocessed news", description = "Return unprocessed news.")
    @GetMapping("/findAllNotProcessed")
    public List<NewsDTO> findAllNotProcessed() {
        return newsService.findByNewsNotProcessed();
    }

    @Operation(summary = "Find", description = "Returning a news.")
    @GetMapping("/{id}")
    public NewsDTO findById(@PathVariable @Positive @NotNull Long id) {
        return newsService.findById(id);
    }

    @Operation(summary = "Save", description = "Save a news.")
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public NewsDTO create(@RequestBody @Valid NewsRequestDTO newsRequestDTO) {
        return newsService.save(newsRequestDTO);
    }

    @Operation(summary = "Send Email", description = "Send daily unprocessed news")
    @PostMapping("/sendEmailDailyNews")
    public void setNewsService(){
        emailService.sendDailyEmail();
    }

    @Operation(summary = "Update", description = "Update a news.")
    @PutMapping(value = "/{id}")
    public ResponseEntity<NewsDTO> update(@PathVariable @Positive @NotNull Long id,
                                          @RequestBody @Valid NewsRequestDTO newsRequestDTO) {
        return new ResponseEntity<>(newsService.update(id, newsRequestDTO), HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete", description = "Delete a news.")
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable @Positive @NotNull Long id) {
        newsService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
