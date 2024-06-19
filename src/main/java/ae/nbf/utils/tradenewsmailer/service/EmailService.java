package ae.nbf.utils.tradenewsmailer.service;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.activation.MimetypesFileTypeMap;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public JavaMailSender getMailSender() {
        return this.mailSender;
    }

    public void sendEmails(List<String> emailAddresses, File attachment) throws Exception {
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        fileTypeMap.addMimeTypes("application/pdf pdf");
        String mimeType = fileTypeMap.getContentType(attachment.getName());

        MimeMessage message = mailSender.createMimeMessage();

        // Create the text part
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Please find the attached market information.", "UTF-8");

        // Create the attachment part
        MimeBodyPart attachmentPart = new MimeBodyPart();
        FileDataSource dataSource = new FileDataSource(attachment);
        attachmentPart.setDataHandler(new DataHandler(dataSource));
        attachmentPart.setFileName(attachment.getName());
        attachmentPart.setHeader("Content-Type", mimeType);

        // Combine the parts
        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(textPart);
        mimeMultipart.addBodyPart(attachmentPart);

        message.setContent(mimeMultipart);
        message.setSubject("Market Information");

        // Set BCC recipients
        InternetAddress[] bccAddresses = emailAddresses.stream()
                .map(email -> {
                    try {
                        return new InternetAddress(email);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .toArray(InternetAddress[]::new);
        message.setRecipients(MimeMessage.RecipientType.BCC, bccAddresses);

        mailSender.send(message);
    }
}