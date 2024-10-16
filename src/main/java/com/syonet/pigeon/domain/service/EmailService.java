package com.syonet.pigeon.domain.service;

import com.syonet.pigeon.api.dto.news.NewsDTO;
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
    private final NewsRepository newsRepository;
    private final ClientRepository clientRepository;
    private final NewsService newsService;
    private final NewsMapper newsMapper;

    @Scheduled(cron = "0 38 18 * * ?")
    public void sendDailyEmail(){
        List<Client> clients = clientRepository.findAll();

        clients.forEach(client -> {
            if(client.getDateBirth() == LocalDate.now()){
                sendEmail(client, true);
            }
            sendEmail(client, false);
        });

    }

    private void sendEmail(Client client, boolean isBirthday){
        var messageEmail = new SimpleMailMessage();
        messageEmail.setFrom("jose01.11.93@gmail.com");
        messageEmail.setTo(client.getEmail());
        messageEmail.setSubject("Notícias do dia!");
        messageEmail.setText(bodyMessage(client, isBirthday));
        mailSender.send(messageEmail);
    }

    private String bodyMessage(Client client, boolean isBirthday) {
        var listNews = newsService.findByNewsNotProcessed();
        String nameClient = client.getName();
        String messageBirthday = "*****    Feliz Aniversário!   *****";
        StringBuilder sbHeader = new StringBuilder();
        StringBuilder sbNews = new StringBuilder();
        String novaLinha = System.lineSeparator();

        if(isBirthday){
            sbHeader.append("Bom dia ").append(nameClient).append(novaLinha)
                    .append(messageBirthday).append(novaLinha).append(novaLinha);
        } else {
            sbHeader.append("Bom dia ").append(nameClient).append(novaLinha).append(novaLinha);
        }

        sbNews.append("Lista de Notícias: ").append(novaLinha).append(novaLinha);
        for (NewsDTO news: listNews) {
            sbNews.append(news.title()).append(novaLinha)
                    .append(news.description()).append(novaLinha).append(novaLinha)
                    .append(news.link()).append(novaLinha).append(novaLinha);
            newsService.updateStatus(newsMapper.toEntity(news));
        }
        return sbHeader.append(novaLinha).append(sbNews).toString();
    }

    private List<Client> clientsBirthday(List<Client> clients) {
        LocalDate dataAtual = LocalDate.now();
        List<Client> clientsBirthday = clientRepository.findByDateBirth(dataAtual);
        return clientsBirthday;
    }

    private List<Client> clientsNoBirthday(List<Client> clients){
        LocalDate dataAtual = LocalDate.now();
        List<Client> clientsBirthday = clientRepository.findByDateBirthNot(dataAtual);
        return clientsBirthday;
    }

}
