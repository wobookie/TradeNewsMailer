package ae.nbf.utils.tradenewsmailer.service;

import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmails() throws Exception {
        File attachment = new File("src/test/resources/test.txt");
        Files.writeString(attachment.toPath(), "attachment content");

        // Create a real MimeMessage instance with a valid session
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage mimeMessage = new MimeMessage(session);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Call the method to test
        List<String> emailAddresses = Arrays.asList("test@example.com", "test2@example.com");
        emailService.sendEmails(emailAddresses, attachment);

        // Verify the MimeMessage details directly
        InternetAddress[] bccRecipients = (InternetAddress[]) mimeMessage.getRecipients(MimeMessage.RecipientType.BCC);
        assertEquals(2, bccRecipients.length);
        assertEquals("test@example.com", bccRecipients[0].getAddress());
        assertEquals("test2@example.com", bccRecipients[1].getAddress());
        assertEquals("Market Information", mimeMessage.getSubject());

        // Verify the email content
        MimeMultipart mimeMultipart = (MimeMultipart) mimeMessage.getContent();
        boolean textFound = false;
        boolean attachmentFound = false;

        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            MimeBodyPart part = (MimeBodyPart) mimeMultipart.getBodyPart(i);
            if (part.isMimeType("text/plain") && part.getFileName() == null) {
                String textContent = (String) part.getContent();
                if (textContent.contains("Please find the attached market information.")) {
                    textFound = true;
                }
            }
            if (part.isMimeType("text/plain") && "test.txt".equals(part.getFileName())) {
                String attachmentContent = new String(part.getInputStream().readAllBytes());
                if (attachmentContent.equals("attachment content")) {
                    attachmentFound = true;
                }
            }
        }
        assertTrue(textFound, "Text content not found in the email.");
        assertTrue(attachmentFound, "Attachment content not found in the email.");

        // Clean up the test file
        Files.delete(attachment.toPath());
    }

    @Test
    void testSendEmailsWithPdfAttachment() throws Exception {
        File pdfAttachment = new File("src/test/resources/test.pdf");

        // Create a real MimeMessage instance with a valid session
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage mimeMessage = new MimeMessage(session);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Call the method to test
        List<String> emailAddresses = Arrays.asList("test@example.com", "test2@example.com");
        emailService.sendEmails(emailAddresses, pdfAttachment);

        // Verify the MimeMessage details directly
        InternetAddress[] bccRecipients = (InternetAddress[]) mimeMessage.getRecipients(MimeMessage.RecipientType.BCC);
        assertEquals(2, bccRecipients.length);
        assertEquals("test@example.com", bccRecipients[0].getAddress());
        assertEquals("test2@example.com", bccRecipients[1].getAddress());
        assertEquals("Market Information", mimeMessage.getSubject());

        // Verify the email content
        MimeMultipart mimeMultipart = (MimeMultipart) mimeMessage.getContent();
        boolean textFound = false;
        boolean pdfAttachmentFound = false;

        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            MimeBodyPart part = (MimeBodyPart) mimeMultipart.getBodyPart(i);
            if (part.isMimeType("text/plain") && part.getFileName() == null) {
                String textContent = (String) part.getContent();
                if (textContent.contains("Please find the attached market information.")) {
                    textFound = true;
                }
            }
            if (part.isMimeType("application/pdf") && "test.pdf".equals(part.getFileName())) {
                byte[] attachmentContent = part.getInputStream().readAllBytes();
                byte[] expectedContent = Files.readAllBytes(pdfAttachment.toPath());
                if (Arrays.equals(attachmentContent, expectedContent)) {
                    pdfAttachmentFound = true;
                }
            }
        }
        assertTrue(textFound, "Text content not found in the email.");
        assertTrue(pdfAttachmentFound, "PDF attachment content not found in the email.");
    }
}
