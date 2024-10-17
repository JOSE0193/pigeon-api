package com.syonet.pigeon.domain.mapper;

import com.syonet.pigeon.api.dto.news.NewsDTO;
import com.syonet.pigeon.api.dto.news.NewsRequestDTO;
import com.syonet.pigeon.domain.model.News;
import com.syonet.pigeon.domain.model.enums.StatusNews;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper {

    public NewsDTO toDTO(News news){
        if (news == null){
            return null;
        }
        return new NewsDTO(news.getId(), news.getTitle(), news.getDescription(), news.getLink());
    }

    public News toEntity(NewsRequestDTO dto){
        if(dto == null) {
            return null;
        }
        News news = News.builder()
                .title(dto.title())
                .description(dto.description())
                .link(dto.link())
                .statusNews(StatusNews.NO_PROCESSED)
                .build();
        return news;
    }

    public News toEntity(NewsDTO dto){
        if(dto == null) {
            return null;
        }
        News news = News.builder()
                .id(dto.id())
                .title(dto.title())
                .description(dto.description())
                .link(dto.link())
                .statusNews(StatusNews.NO_PROCESSED)
                .build();
        return news;
    }

}
