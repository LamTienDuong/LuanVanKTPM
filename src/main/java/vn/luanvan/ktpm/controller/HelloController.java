package vn.luanvan.ktpm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String getHelloWorld() {
        return "Luan Van tot nghiep dai hoc cua Lam Tien Duong";
    }
}
