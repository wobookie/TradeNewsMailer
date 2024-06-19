package ae.nbf.utils.tradenewsmailer.controller;

import ae.nbf.utils.tradenewsmailer.service.EmailService;
import ae.nbf.utils.tradenewsmailer.service.ExcelService;
import ae.nbf.utils.tradenewsmailer.service.SMTPService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.Arrays;

public class MailControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmailService emailService;

    @Mock
    private ExcelService excelService;

    @InjectMocks
    private MailController mailController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mailController).build();
    }

    @Test
    public void testSendEmailSuccess() throws Exception {
        MockMultipartFile excelFile = new MockMultipartFile("file", "emails.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "email content".getBytes());

        MockMultipartFile attachmentFile = new MockMultipartFile("attachment", "attachment.pdf",
                "application/pdf", "attachment content".getBytes());

        when(excelService.extractEmailAddresses(any(MockMultipartFile.class)))
                .thenReturn(Arrays.asList("test1@example.com", "test2@example.com"));

        doNothing().when(emailService).sendEmails(any(), any(File.class));

        mockMvc.perform(multipart("/send-email")
                        .file(excelFile)
                        .file(attachmentFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/upload?sendStatus=success"));
    }

    @Test
    public void testSendEmailFailure() throws Exception {
        MockMultipartFile excelFile = new MockMultipartFile("file", "emails.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "email content".getBytes());

        MockMultipartFile attachmentFile = new MockMultipartFile("attachment", "attachment.pdf",
                "application/pdf", "attachment content".getBytes());

        when(excelService.extractEmailAddresses(any(MockMultipartFile.class)))
                .thenReturn(Arrays.asList("test1@example.com", "test2@example.com"));

        doThrow(new RuntimeException("Email sending failed")).when(emailService).sendEmails(any(), any(File.class));

        mockMvc.perform(multipart("/send-email")
                        .file(excelFile)
                        .file(attachmentFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/upload?sendStatus=error"));
    }
}
