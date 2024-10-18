package vn.luanvan.ktpm.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.service.VNPAYService;

@RestController
public class PaymentController {
    private final VNPAYService vnPayService;
    private static String Status = "No";

    public PaymentController(VNPAYService vnPayService) {
        this.vnPayService = vnPayService;
    }

    @GetMapping( "/payment")
    public String home(){
        return "createOrder";
    }

    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping("/payment/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
        Status = "processing";
        return "redirect:" + vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model){
        int paymentStatus =vnPayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);
        if (paymentStatus == 1) {
            Status = "success";
        } else {
            Status = "fail";
        }

        return paymentStatus == 1 ? "orderSuccess" : "orderFail";
    }

    @GetMapping("/payment/status")
    public String getStatus() {
        return Status;
    }

    @PostMapping("/payment/status")
    public String setStatus() {
        Status = "No";
        return Status;
    }
}
