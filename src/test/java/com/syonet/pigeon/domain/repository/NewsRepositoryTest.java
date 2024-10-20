package com.syonet.pigeon.domain.repository;

import com.syonet.pigeon.domain.model.News;
import com.syonet.pigeon.domain.model.enums.StatusNews;
import com.syonet.pigeon.util.NewsCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class NewsRepositoryTest {

    @Autowired
    NewsRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @DisplayName("Should return a list of news by status NO_PROCESSED")
    void findByStatusNews_ReturnsList_WhenUnSuccessfull() {
        News news = NewsCreator.createNewsToBeSaved();
        entityManager.persist(news);

        List<News> newsList = repository.findByStatusNews(StatusNews.NO_PROCESSED);

        assertThat(newsList).isNotEmpty().contains(news);
        assertThat(newsList.get(0).getStatusNews()).isEqualTo(news.getStatusNews());
    }

    @Test
    @DisplayName("Should return a list empty of news by status PROCESSED")
    void findByStatusNews_ReturnsEmpty_WhenUnSuccessfull() {
        News news = NewsCreator.createNewsToBeSaved();
        entityManager.persist(news);

        List<News> newsList = repository.findByStatusNews(StatusNews.PROCESSED);

        assertThat(newsList).isEmpty();
    }

    @Test
    @DisplayName("Should save a news when record is valid")
    void testSaveSuccessful() {
        News news = NewsCreator.createNewsToBeSaved();
        final News newsSaved = repository.save(news);

        final News actual = entityManager.find(News.class, newsSaved.getId());

        assertThat(newsSaved.getId()).isPositive();
        assertThat(newsSaved).isNotNull();
        assertThat(newsSaved.getTitle()).isEqualTo(news.getTitle());
        assertThat(newsSaved.getDescription()).isEqualTo(news.getDescription());
        assertThat(newsSaved.getLink()).isEqualTo(news.getLink());
        assertThat(actual).isEqualTo(newsSaved);
    }

    @Test
    @DisplayName("Should removes a client when record is valid")
    void testDeleteSuccessful() {
        News news = NewsCreator.createNewsToBeSaved();
        entityManager.persist(news);
        this.repository.delete(news);

        Optional<News> newsOptional = repository.findById(news.getId());

        assertThat(newsOptional).isEmpty();
    }

}