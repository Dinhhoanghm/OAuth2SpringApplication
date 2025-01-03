package vn.aivhub.oauth.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import vn.aivhub.oauth.data.dto.EmailDetails;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Properties;


@Log4j2
@Service
public class GoogleServiceImpl {
  public void sendSimpleMail(EmailDetails emailDetails) throws MessagingException, UnsupportedEncodingException {
    String sender = "contact@reborn.vn";
    String password = "ksdhrtrdwvxhtekw";
    sendMail(sender, password, emailDetails.getRecipient(), emailDetails.getSubject(), emailDetails.getBody());
  }

  private void sendMail(String sender, String password, String receiver, String subject, String body) throws MessagingException, UnsupportedEncodingException {
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "465");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.starttls.required", "true");
    props.put("mail.smtp.ssl.protocols", "TLSv1.2");
    props.put("mail.smtp.ssl.enable", "true");
    props.put("mail.smtp.quitwait", "false");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.debug", "true");
    Session session = Session.getInstance(props, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(sender, password);
      }
    });
    MimeMessage message = new MimeMessage(session);
    Multipart multipart = new MimeMultipart(); //1
    BodyPart bodyPart = new MimeBodyPart();
    bodyPart.setText(body);
    multipart.addBodyPart(bodyPart);
    message.setFrom(new InternetAddress(sender, sender));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
    message.setContent(multipart);
    message.setSubject(subject);
    log.info("Before send email to {}", receiver);
    Transport.send(message);
    log.info("Send email to {} done", receiver);
  }
}

