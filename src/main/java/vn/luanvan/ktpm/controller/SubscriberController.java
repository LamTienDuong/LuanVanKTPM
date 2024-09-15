package vn.luanvan.ktpm.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.domain.Subscriber;
import vn.luanvan.ktpm.service.EmailService;
import vn.luanvan.ktpm.service.SubscriberService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;
import vn.luanvan.ktpm.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public SubscriberController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a new subscriber")
    public ResponseEntity<Subscriber> createNewSubscriber(@Valid @RequestBody Subscriber subscriber) throws IdInvalidException {
        // check email
        boolean isExist = this.subscriberService.isExistByEmail(subscriber.getEmail());
        if (isExist) {
            throw new IdInvalidException("Email " + subscriber.getEmail() + " da ton tai");
        }
        Subscriber res = this.subscriberService.handleCreateSubscriber(subscriber);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber subscriber) throws IdInvalidException {
        // check id
        Subscriber dbSubscriber = this.subscriberService.handleGetSubcriberById(subscriber.getId());
        if (dbSubscriber == null) {
            throw new IdInvalidException("Subscriber voi id = " + subscriber.getId() + " khong ton tai");
        }
        Subscriber res = this.subscriberService.handleUpdateSubscriber(dbSubscriber, subscriber);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    public String sendSimpleEmail() {
        this.subscriberService.sendSubscribersEmailJobs();
        return "oke";
    }

}
