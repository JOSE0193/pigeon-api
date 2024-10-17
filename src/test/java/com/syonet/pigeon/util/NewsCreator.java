package com.syonet.pigeon.util;

import com.syonet.pigeon.api.dto.news.NewsDTO;
import com.syonet.pigeon.api.dto.news.NewsRequestDTO;
import com.syonet.pigeon.domain.model.News;
import com.syonet.pigeon.domain.model.enums.StatusNews;

import java.time.LocalDateTime;

public class NewsCreator {

    public static News createNewsToBeSaved(){
        return News.builder()
                .title("Oracle e Amazon Web Services anunciam parceria estratégica")
                .description("Os clientes agora podem acessar o Oracle Autonomous Database e o Oracle Exadata Database " +
                        "Service na AWS, simplificando a migração e a implantação de cargas de trabalho empresariais na nuvem, " +
                        "ao mesmo tempo em que melhoram a agilidade, flexibilidade e segurança.")
                .link("https://www.oracle.com/news/announcement/ocw24-oracle-and-amazon-web-services-announce-strategic-partnership-2024-09-09/")
                .statusNews(StatusNews.NO_PROCESSED)
                .build();
    }

    public static NewsDTO createValidNewsDTO(){
        return new NewsDTO(1L, "Oracle e Amazon Web Services anunciam parceria estratégica",
                "Os clientes agora podem acessar o Oracle Autonomous Database e o Oracle Exadata Database Service na AWS, " +
                        "simplificando a migração e a implantação de cargas de trabalho empresariais na nuvem, ao mesmo " +
                        "tempo em que melhoram a agilidade, flexibilidade e segurança.",
                "https://www.oracle.com/news/announcement/ocw24-oracle-and-amazon-web-services-announce-strategic-partnership-2024-09-09/");
    }

    public static NewsRequestDTO createValidNewsRequestDTO(){
        return new NewsRequestDTO( "Oracle e Amazon Web Services anunciam parceria estratégica",
                "Os clientes agora podem acessar o Oracle Autonomous Database e o Oracle Exadata Database Service na AWS, " +
                        "simplificando a migração e a implantação de cargas de trabalho empresariais na nuvem, ao mesmo " +
                        "tempo em que melhoram a agilidade, flexibilidade e segurança.",
                "https://www.oracle.com/news/announcement/ocw24-oracle-and-amazon-web-services-announce-strategic-partnership-2024-09-09/");
    }

    public static News createValidNews(){
        return News.builder()
                .id(1L)
                .title("Oracle e Amazon Web Services anunciam parceria estratégica")
                .description("Os clientes agora podem acessar o Oracle Autonomous Database e o Oracle Exadata Database Service na AWS, " +
                        "simplificando a migração e a implantação de cargas de trabalho empresariais na nuvem, ao mesmo " +
                        "tempo em que melhoram a agilidade, flexibilidade e segurança.")
                .link("https://www.oracle.com/news/announcement/ocw24-oracle-and-amazon-web-services-announce-strategic-partnership-2024-09-09/")
                .statusNews(StatusNews.NO_PROCESSED)
                .updateAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

}
