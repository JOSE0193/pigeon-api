package com.syonet.pigeon.domain.service;

import com.syonet.pigeon.api.dto.news.NewsDTO;
import com.syonet.pigeon.domain.exception.RecordNotFoundException;
import com.syonet.pigeon.domain.mapper.NewsMapper;
import com.syonet.pigeon.domain.model.Client;
import com.syonet.pigeon.domain.model.News;
import com.syonet.pigeon.domain.model.enums.StatusNews;
import com.syonet.pigeon.domain.repository.ClientRepository;
import com.syonet.pigeon.domain.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final ClientRepository clientRepository;
    private final NewsRepository newsRepository;

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendDailyEmail(){
        var listNews = newsRepository.findByStatusNews(StatusNews.NO_PROCESSED);
        List<Client> clients = clientRepository.findAll();
        LocalDate today = LocalDate.now();
        int day = today.getDayOfMonth();
        int month = today.getMonthValue();

        if(!listNews.isEmpty()){
            clients.forEach(client -> {
                if(client.getDateBirth().getDayOfMonth() == day && client.getDateBirth().getMonthValue() == month){
                    sendEmail(client, listNews,  true);
                } else{
                    sendEmail(client, listNews, false);
                }
            });
            for (News news: listNews) {
                this.updateStatus(news);
            }
        }
    }

    private void sendEmail(Client client, List<News> news, boolean isBirthday){
        var messageEmail = new SimpleMailMessage();
        messageEmail.setFrom("jose01.11.93@gmail.com");
        messageEmail.setTo(client.getEmail());
        messageEmail.setSubject("Notícias do dia!");
        messageEmail.setText(bodyMessage(client, news, isBirthday));
        mailSender.send(messageEmail);
    }

    private String bodyMessage(Client client, List<News> newsList, boolean isBirthday) {
        String nameClient = client.getName();
        String messageBirthday = "*****    Feliz Aniversário!   *****";
        StringBuilder sbHeader = new StringBuilder();
        StringBuilder sbNews = new StringBuilder();
        String novaLinha = System.lineSeparator();

        if(isBirthday){
            sbHeader.append("Bom dia ").append(nameClient).append(novaLinha).append(novaLinha)
                    .append(messageBirthday).append(novaLinha).append(novaLinha);
        } else {
            sbHeader.append("Bom dia ").append(nameClient).append(novaLinha).append(novaLinha);
        }
        sbNews.append("Lista de Notícias: ").append(novaLinha).append(novaLinha);
        for (News news: newsList) {
            sbNews.append(news.getTitle()).append(novaLinha).append(novaLinha)
                    .append(news.getDescription()).append(novaLinha).append(novaLinha)
                    .append(news.getLink()).append(novaLinha)
                    .append("----------------------------------------------------").append(novaLinha).append(novaLinha);
        }
        return sbHeader.append(novaLinha).append(sbNews).toString();
    }

    public void updateStatus(News news) {
        newsRepository.findById(news.getId()).map(actual -> {
                    actual.setTitle(news.getTitle());
                    actual.setDescription(news.getDescription());
                    actual.setLink(news.getLink());
                    actual.setStatusNews(StatusNews.PROCESSED);
                    return newsRepository.save(actual);
                })
                .orElseThrow(() -> new RecordNotFoundException(news.getId()));
    }

}
