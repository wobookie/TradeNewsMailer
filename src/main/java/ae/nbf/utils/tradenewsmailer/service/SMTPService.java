package ae.nbf.utils.tradenewsmailer.service;

import ae.nbf.utils.tradenewsmailer.model.SMTPConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class SMTPService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    public void configureSMTP(SMTPConfig smtpConfig) {
        mailSender.setHost(smtpConfig.getHost());
        mailSender.setPort(smtpConfig.getPort());
        mailSender.setUsername(smtpConfig.getUsername());
        mailSender.setPassword(smtpConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", smtpConfig.getHost());
    }

    public SMTPConfig getSMTPConfiguration() {
        SMTPConfig smtpConfig = new SMTPConfig();
        smtpConfig.setHost(mailSender.getHost());
        smtpConfig.setPort(mailSender.getPort());
        smtpConfig.setUsername(mailSender.getUsername());
        smtpConfig.setPassword(mailSender.getPassword());
        return smtpConfig;
    }

    public boolean checkSMTPConnection() {
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.host", mailSender.getHost());
        properties.put("mail.smtp.port", mailSender.getPort());
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", mailSender.getHost());

        try {
            mailSender.testConnection();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}