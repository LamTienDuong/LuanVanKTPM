package vn.luanvan.ktpm.config;

import org.springframework.context.annotation.Configuration;
import vn.luanvan.ktpm.util.VNPayUtil;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
public class VNPAYConfig {
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_ReturnUrl = "http://localhost:8080/api/v1/payment/vn-pay-callback";
    public static String vnp_TmnCode = "2SWH0PZU"; // kiểm tra email sau
    public static String secretKey = "E8EC65NH4L4EHAZOKMI15VUIFVHI61HB"; // khi đăng ký Test
    public static String vnp_apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";
    public static String vnp_Version = "2.1.0";
    public static String vnp_Command = "pay";
    public static String orderType = "other";

    public static String getVnp_PayUrl() {
        return vnp_PayUrl;
    }

    public static void setVnp_PayUrl(String vnp_PayUrl) {
        VNPAYConfig.vnp_PayUrl = vnp_PayUrl;
    }

    public static String getVnp_ReturnUrl() {
        return vnp_ReturnUrl;
    }

    public static void setVnp_ReturnUrl(String vnp_ReturnUrl) {
        VNPAYConfig.vnp_ReturnUrl = vnp_ReturnUrl;
    }

    public static String getVnp_TmnCode() {
        return vnp_TmnCode;
    }

    public static void setVnp_TmnCode(String vnp_TmnCode) {
        VNPAYConfig.vnp_TmnCode = vnp_TmnCode;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static void setSecretKey(String secretKey) {
        VNPAYConfig.secretKey = secretKey;
    }

    public static String getVnp_apiUrl() {
        return vnp_apiUrl;
    }

    public static void setVnp_apiUrl(String vnp_apiUrl) {
        VNPAYConfig.vnp_apiUrl = vnp_apiUrl;
    }

    public static String getVnp_Version() {
        return vnp_Version;
    }

    public static void setVnp_Version(String vnp_Version) {
        VNPAYConfig.vnp_Version = vnp_Version;
    }

    public static String getVnp_Command() {
        return vnp_Command;
    }

    public static void setVnp_Command(String vnp_Command) {
        VNPAYConfig.vnp_Command = vnp_Command;
    }

    public static String getOrderType() {
        return orderType;
    }

    public static void setOrderType(String orderType) {
        VNPAYConfig.orderType = orderType;
    }

    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnp_Version);
        vnpParamsMap.put("vnp_Command", this.vnp_Command);
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_TxnRef",  VNPayUtil.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" +  VNPayUtil.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderType", this.orderType);
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
        return vnpParamsMap;
    }

}