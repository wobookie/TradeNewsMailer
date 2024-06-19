package ae.nbf.utils.tradenewsmailer.controller;

import ae.nbf.utils.tradenewsmailer.model.SMTPConfig;
import ae.nbf.utils.tradenewsmailer.service.SMTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConfigurationController {

    @Autowired
    private SMTPService smtpService;

    @GetMapping("/configuration")
    public String configurationPage(Model model) {
        SMTPConfig smtpConfig = smtpService.getSMTPConfiguration();
        boolean smtpStatus = smtpService.checkSMTPConnection();
        model.addAttribute("smtpConfig", smtpConfig);
        model.addAttribute("smtpStatus", smtpStatus ? "Connected" : "Disconnected");
        return "configuration";
    }

    @PostMapping("/configure-smtp")
    public String configureSmtp(SMTPConfig smtpConfig) {
        smtpService.configureSMTP(smtpConfig);
        return "redirect:/configuration";
    }
}