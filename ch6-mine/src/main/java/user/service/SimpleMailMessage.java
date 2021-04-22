package user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;

class MockMailSender implements MailSender {
    @Override
    public void send(org.springframework.mail.SimpleMailMessage simpleMessage) throws MailException {

    }

    @Override
    public void send(org.springframework.mail.SimpleMailMessage[] simpleMessages) throws MailException {

    }
}
