package com.syonet.pigeon.domain.service;

import com.syonet.pigeon.domain.exception.RecordNotFoundException;
import com.syonet.pigeon.domain.model.Client;
import com.syonet.pigeon.domain.model.News;
import com.syonet.pigeon.domain.model.enums.StatusNews;
import com.syonet.pigeon.domain.repository.ClientRepository;
import com.syonet.pigeon.domain.repository.NewsRepository;
import com.syonet.pigeon.util.ClientCreator;
import com.syonet.pigeon.util.NewsCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class EmailServiceTest {


    @Mock
    private JavaMailSender mailSender;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testSendDailyEmail_WithNoNews_ShouldNotSendEmail() {
        when(newsRepository.findByStatusNews(StatusNews.NO_PROCESSED)).thenReturn(Collections.emptyList());

        emailService.sendDailyEmail();

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
        verify(newsRepository, never()).save(any(News.class));
    }


    @Test
    public void testUpdateStatus_NewsNotFound_ShouldThrowException() {
        News news = new News();
        news.setId(1L);

        when(newsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> emailService.updateStatus(news));
    }

}