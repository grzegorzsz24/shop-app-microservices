package org.example.notificationservice.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
class MailConfig {

    @Value("${spring.mail.host}")
    String host;
    @Value("${spring.mail.port}")
    String port;
    @Value("${spring.mail.username}")
    String username;
    @Value("${spring.mail.password}")
    String password;

    @Bean
    JavaMailSender createMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        return mailSender;
    }
}
