package com.syonet.pigeon.domain.service;

import com.syonet.pigeon.api.dto.news.NewsDTO;
import com.syonet.pigeon.api.dto.news.NewsPageDTO;
import com.syonet.pigeon.api.dto.news.NewsRequestDTO;
import com.syonet.pigeon.domain.exception.BusinessException;
import com.syonet.pigeon.domain.exception.RecordNotFoundException;
import com.syonet.pigeon.domain.mapper.NewsMapper;
import com.syonet.pigeon.domain.model.News;
import com.syonet.pigeon.domain.model.enums.StatusNews;
import com.syonet.pigeon.domain.repository.NewsRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    public List<NewsDTO> listAll() {
        return newsRepository.findAll().stream()
                .map(newsMapper::toDTO)
                .toList();
    }

    public NewsPageDTO findAll(@PositiveOrZero int page, @Positive @Max(1000) int pageSize){
        Page<News> newsPage = newsRepository.findAll(PageRequest.of(page, pageSize));
        List<NewsDTO> newsDTOS = newsPage.getContent().stream().map(newsMapper::toDTO).toList();
        return new NewsPageDTO(newsDTOS, newsPage.getTotalElements(), newsPage.getTotalPages());
    }

    public List<NewsDTO> findByNewsNotProcessed() {
        return newsRepository.findByStatusNews(StatusNews.NO_PROCESSED).stream().map(newsMapper::toDTO).toList();
    }

    public NewsDTO findById(@Positive @NotNull Long id) {
        return newsRepository.findById(id).map(newsMapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public NewsDTO save(@Valid NewsRequestDTO newsRequestDTO) {
        News news = newsMapper.toEntity(newsRequestDTO);
        return newsMapper.toDTO(newsRepository.save(news));
    }

    public NewsDTO update(@Positive @NotNull Long id, @Valid NewsRequestDTO newsRequestDTO) {
        return newsRepository.findById(id).map(actual -> {
                    actual.setTitle(newsRequestDTO.title());
                    actual.setDescription(newsRequestDTO.description());
                    actual.setLink(newsRequestDTO.link());
                    return newsMapper.toDTO(newsRepository.save(actual));
                })
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@Positive @NotNull Long id) {
        newsRepository.delete(newsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

}
