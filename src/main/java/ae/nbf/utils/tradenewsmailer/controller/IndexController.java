package ae.nbf.utils.tradenewsmailer.controller;

import ae.nbf.utils.tradenewsmailer.service.SMTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @Autowired
    private SMTPService smtpService;

    @GetMapping("/")
    public String index(Model model) {
        boolean smtpStatus = smtpService.checkSMTPConnection();
        model.addAttribute("smtpStatus", smtpStatus ? "Connected" : "Disconnected");
        return "index";
    }
}

