package vn.luanvan.ktpm.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.domain.response.payment.VNPayResponse;
import vn.luanvan.ktpm.service.PaymentService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {
    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @GetMapping("/vn-pay")
    public ResponseEntity<VNPayResponse> pay(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.createVnPayPayment(request));
    }

    @GetMapping("/payment/vn-pay-callback")
    public ResponseEntity<VNPayResponse> payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String status = request.getParameter("vnp_ResponseCode");


        if (status.equals("00")) {
            response.sendRedirect("http://localhost:3000/order?status=success");
            return ResponseEntity.status(HttpStatus.OK).body(new VNPayResponse("00", "Success", ""));
        } else {
            response.sendRedirect("http://localhost:3000/order?status=failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}
