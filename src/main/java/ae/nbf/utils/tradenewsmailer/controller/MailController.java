package ae.nbf.utils.tradenewsmailer.controller;

import ae.nbf.utils.tradenewsmailer.service.EmailService;
import ae.nbf.utils.tradenewsmailer.service.ExcelService;
import ae.nbf.utils.tradenewsmailer.service.SMTPService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class MailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private SMTPService smtpService;

    @GetMapping("/upload")
    public String uploadPage(Model model, @RequestParam(value = "sendStatus", required = false) String sendStatus) {
        boolean smtpStatus = smtpService.checkSMTPConnection();
        model.addAttribute("smtpStatus", smtpStatus ? "Connected" : "Disconnected");
        if (sendStatus != null) {
            model.addAttribute("sendStatus", sendStatus.equals("success") ? "Email sent successfully" : "Failed to send email");
        }
        return "upload";
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam("file") MultipartFile file,
                            @RequestParam("attachment") MultipartFile attachment) {
        File savedFile = null;
        try {
            List<String> emailAddresses = excelService.extractEmailAddresses(file);

            // Save the attachment to a file with its original filename
            savedFile = saveUploadedFile(attachment);

            emailService.sendEmails(emailAddresses, savedFile);

            return "redirect:/upload?sendStatus=success";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/upload?sendStatus=error";
        } finally {
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
        }
    }

    private File saveUploadedFile(MultipartFile multipartFile) throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir"), multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }
}