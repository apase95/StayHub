package com.stayhub.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender(@Value("${app.mail.host:localhost}") String host,
                                         @Value("${app.mail.port:25}") int port,
                                         @Value("${app.mail.username:}") String username,
                                         @Value("${app.mail.password:}") String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        if (!username.isBlank()) {
            mailSender.setUsername(username);
        }
        if (!password.isBlank()) {
            mailSender.setPassword(password);
        }

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", String.valueOf(!username.isBlank()));
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.connectiontimeout", "3000");
        properties.put("mail.smtp.timeout", "3000");
        properties.put("mail.smtp.writetimeout", "3000");
        return mailSender;
    }
}
