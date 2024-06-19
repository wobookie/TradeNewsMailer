package ae.nbf.utils.tradenewsmailer.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

    public List<String> extractEmailAddresses(MultipartFile file) throws Exception {
        List<String> emailAddresses = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            String email = row.getCell(0).getStringCellValue();
            emailAddresses.add(email);
        }
        workbook.close();
        return emailAddresses;
    }
}