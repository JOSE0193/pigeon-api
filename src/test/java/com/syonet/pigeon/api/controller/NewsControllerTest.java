package com.syonet.pigeon.api.controller;

import com.syonet.pigeon.api.dto.client.ClientDTO;
import com.syonet.pigeon.api.dto.client.ClientRequestDTO;
import com.syonet.pigeon.api.dto.news.NewsDTO;
import com.syonet.pigeon.api.dto.news.NewsRequestDTO;
import com.syonet.pigeon.domain.service.NewsService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class NewsControllerTest {

    @InjectMocks
    private NewsController controller;

    @Mock
    private NewsService service;

    @BeforeEach
    void setUp() {
        BDDMockito.when(service.findByNewsNotProcessed())
                .thenReturn(List.of(NewsCreator.createValidNewsDTO()));

        BDDMockito.when(service.findById(ArgumentMatchers.anyLong()))
                .thenReturn(NewsCreator.createValidNewsDTO());
        BDDMockito.when(service.findById(2L)).thenReturn(null);

        BDDMockito.when(service.save(ArgumentMatchers.any(NewsRequestDTO.class)))
                .thenReturn(NewsCreator.createValidNewsDTO());

        BDDMockito.when(service.update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(NewsRequestDTO.class)))
                .thenReturn(NewsCreator.createValidNewsDTO());

        BDDMockito.doNothing().when(service).delete(ArgumentMatchers.anyLong());

    }

    @Test
    @DisplayName("findAllNotProcessed returns list of news not processed when successful")
    void findAllNotProcessed_ReturnsListOfNews_WhenSuccessful(){
        String expectedTitle = NewsCreator.createValidNewsDTO().title();

        List<NewsDTO> news = controller.findAllNotProcessed();

        Assertions.assertThat(news)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(news.get(0).title()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("findById returns news when successful")
    void findById_ReturnsNews_WhenSuccessful(){
        Long expectedId = NewsCreator.createValidNewsDTO().id();

        NewsDTO client = controller.findById(1L);

        Assertions.assertThat(client).isNotNull();

        Assertions.assertThat(client.id()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById returns an empty list of news when id is not found")
    void findById_ReturnstNull_WhenNewsIsNotFound(){
        NewsDTO news = controller.findById(2L);

        Assertions.assertThat(news).isNull();

    }

    @Test
    @DisplayName("save returns news when successful")
    void save_ReturnsNews_WhenSuccessful(){

        NewsDTO newsDTO = controller.create(NewsCreator.createValidNewsRequestDTO());

        Assertions.assertThat(newsDTO).isNotNull().isEqualTo(NewsCreator.createValidNewsDTO());

    }

    @Test
    @DisplayName("replace updates news when successful")
    void replace_UpdatesNews_WhenSuccessful(){

        Assertions.assertThatCode(() ->controller.update(1L, NewsCreator.createValidNewsRequestDTO()))
                .doesNotThrowAnyException();

        ResponseEntity<NewsDTO> entity = controller.update(1L, NewsCreator.createValidNewsRequestDTO());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


    @Test
    @DisplayName("delete removes news when successful")
    void delete_RemovesNews_WhenSuccessful(){

        Assertions.assertThatCode(() ->controller.delete(1L))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = controller.delete(1L);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}