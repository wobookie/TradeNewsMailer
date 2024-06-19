package ae.nbf.utils.tradenewsmailer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

class ExcelServiceTest {

    @InjectMocks
    private ExcelService excelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExtractEmailAddresses() throws Exception {
        ClassPathResource resource = new ClassPathResource("test.xlsx");
        InputStream inputStream = resource.getInputStream();
        MultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);

        List<String> emailAddresses = excelService.extractEmailAddresses(file);

        assertEquals(2, emailAddresses.size());
        assertTrue(emailAddresses.contains("test.email@email.com"));
        assertTrue(emailAddresses.contains("test2.email@email.com"));
    }
}