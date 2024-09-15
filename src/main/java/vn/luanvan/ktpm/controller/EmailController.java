package vn.luanvan.ktpm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.luanvan.ktpm.service.EmailService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/gmail")
    @ApiMessage("Send simple email")
    public String sendSimpleEmail() {
//        this.emailService.sendSimpleEmail();
//        this.emailService.sendEmailSync("duonglt.dev@gmail.com", "test send email",
//                "<h1><b>Hello</b></h1>", false, true);
//        this.emailService.sendEmailFromTemplateSync("duonglt.dev@gmail.com", "test send email", "job");
        return "OKE";
    }
}
