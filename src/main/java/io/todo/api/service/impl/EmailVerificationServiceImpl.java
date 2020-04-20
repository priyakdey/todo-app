package io.todo.api.service.impl;

import io.todo.api.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EmailVerificationServiceImpl implements EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailVerificationServiceImpl.class);

    private final JavaMailSender mailSender;
    private final String subject;

    @Autowired
    public EmailVerificationServiceImpl(JavaMailSender mailSender,
                                        @Value("${email.verification.subject}") String subject) {
        this.mailSender = mailSender;
        this.subject = subject;
    }


    @Override
    public void sendNotification(String recipient, String username, String token, String name) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setTo(recipient);
        mimeMessageHelper.setSubject(subject);
        String body = null;
        try {
             body = generateBody(username, token, name);
        } catch (IOException e) {
            LOGGER.error("Error parsing verification-email.html file");
            throw new MessagingException("Error parsing the file", e);
        }

        if (body != null) {
            mimeMessageHelper.setText(body, true);
            mailSender.send(mimeMessage);
        }
    }

    private String generateBody(String username, String token, String name) throws IOException {
        String fileContent = readFile();
        String verificationLink = generateVerificationLink(username, token);
        fileContent = fileContent
                            .replace("{name}", name)
                            .replace("{verification-link}", verificationLink);

        return fileContent;
    }

    private String generateVerificationLink(String username, String token) {
        final String verificationLink = "http://localhost:8080/verify?username=" + username + "&token=" + token;
        return verificationLink;
    }

    private String readFile() throws IOException {
        Path path = Paths.get("/Users/priyakdey/Projects/Simple Todo App/todo-app/src/main/resources/email",
                "verification-email.html");

        StringBuilder builder = new StringBuilder();

        Files.lines(path).forEach(builder::append);

        return builder.toString();
    }

}
