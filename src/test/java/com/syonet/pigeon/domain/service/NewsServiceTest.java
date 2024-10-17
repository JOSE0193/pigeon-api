package com.syonet.pigeon.domain.service;

import com.syonet.pigeon.api.dto.client.ClientDTO;
import com.syonet.pigeon.api.dto.news.NewsDTO;
import com.syonet.pigeon.api.dto.news.NewsRequestDTO;
import com.syonet.pigeon.domain.exception.RecordNotFoundException;
import com.syonet.pigeon.domain.mapper.NewsMapper;
import com.syonet.pigeon.domain.model.Client;
import com.syonet.pigeon.domain.model.News;
import com.syonet.pigeon.domain.model.enums.StatusNews;
import com.syonet.pigeon.domain.repository.NewsRepository;
import com.syonet.pigeon.util.ClientCreator;
import com.syonet.pigeon.util.NewsCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class NewsServiceTest {

    @InjectMocks
    private NewsService service;

    @Mock
    private NewsRepository repository;

    @Mock
    private NewsMapper mapper;

    @BeforeEach
    void setUp() {

        BDDMockito.when(repository.findByStatusNews(StatusNews.NO_PROCESSED))
                .thenReturn(List.of(NewsCreator.createValidNews()));

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(NewsCreator.createValidNews()));

        BDDMockito.when(repository.save(ArgumentMatchers.any(News.class)))
                .thenReturn(NewsCreator.createValidNews());

        BDDMockito.doNothing().when(repository).delete(ArgumentMatchers.any(News.class));

    }

    @Test
    @DisplayName("findAllNotProcessed returns list of news not processed when successful")
    void findAllNotProcessed_ReturnsListOfNews_WhenSuccessful() {
        News expectedSavedNews = NewsCreator.createValidNews();
        NewsDTO expectedNewsDTO = NewsCreator.createValidNewsDTO();
        BDDMockito.when(mapper.toDTO(expectedSavedNews)).thenReturn(expectedNewsDTO);
        List<NewsDTO> news = service.findByNewsNotProcessed();

        Assertions.assertThat(news)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(news).isNotEmpty();
        Assertions.assertThat(news.get(0).id()).isEqualTo(expectedSavedNews.getId());
    }

    @Test
    @DisplayName("findById returns news when successful")
    void findById_ReturnsNews_WhenSuccessful() {
        News expectedSavedNews = NewsCreator.createValidNews();
        NewsDTO expectedNewsDTO = NewsCreator.createValidNewsDTO();
        BDDMockito.when(mapper.toDTO(expectedSavedNews)).thenReturn(expectedNewsDTO);
        Long expectedId = expectedSavedNews.getId();

        NewsDTO news = service.findById(1L);

        Assertions.assertThat(news).isNotNull();

        Assertions.assertThat(news.id()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById returns an empty list of news when id is not found")
    void findById_ReturnstNull_WhenNewsIsNotFound() {
        assertThrows(RecordNotFoundException.class, () -> service.findById(2L));
    }

    @Test
    @DisplayName("save returns news when successful")
    void save_ReturnsNews_WhenSuccessful() {
        NewsRequestDTO newsRequestDTO = NewsCreator.createValidNewsRequestDTO();
        News expectedSavedNews = NewsCreator.createValidNews();
        NewsDTO expectedNewsDTO = NewsCreator.createValidNewsDTO();

        BDDMockito.when(mapper.toEntity(newsRequestDTO)).thenReturn(expectedSavedNews);
        BDDMockito.when(repository.save(expectedSavedNews)).thenReturn(expectedSavedNews);
        BDDMockito.when(mapper.toDTO(expectedSavedNews)).thenReturn(expectedNewsDTO);

        NewsDTO newsDTO = service.save(newsRequestDTO);

        Assertions.assertThat(newsDTO).isNotNull().isEqualTo(expectedNewsDTO);

        BDDMockito.verify(mapper).toEntity(newsRequestDTO);
        BDDMockito.verify(repository).save(expectedSavedNews);
        BDDMockito.verify(mapper).toDTO(expectedSavedNews);
    }

    @Test
    @DisplayName("replace updates news when successful")
    void replace_UpdatesNews_WhenSuccessful() {
        NewsRequestDTO newsRequestDTO = NewsCreator.createValidNewsRequestDTO();
        News expectedSavedNews = NewsCreator.createValidNews();
        NewsDTO expectedNewsDTO = NewsCreator.createValidNewsDTO();

        BDDMockito.when(mapper.toEntity(newsRequestDTO)).thenReturn(expectedSavedNews);
        BDDMockito.when(repository.save(expectedSavedNews)).thenReturn(expectedSavedNews);
        BDDMockito.when(mapper.toDTO(expectedSavedNews)).thenReturn(expectedNewsDTO);

        NewsDTO newsDTO = service.findById(1L);

        Assertions.assertThat(newsDTO).isNotNull();

        NewsDTO entity = service.update(1L, newsRequestDTO);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity).isEqualTo(expectedNewsDTO);


    }


    @Test
    @DisplayName("delete removes news when successful")
    void delete_RemovesNews_WhenSuccessful() {

        Assertions.assertThatCode(() -> service.delete(1L))
                .doesNotThrowAnyException();

    }


}