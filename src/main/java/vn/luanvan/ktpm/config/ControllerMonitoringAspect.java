package vn.luanvan.ktpm.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerMonitoringAspect {
    private static final Logger logger = LoggerFactory.getLogger(ControllerMonitoringAspect.class);

    @Around("execution(* vn.luanvan.ktpm.controller.PaymentController.paymentCompleted())") // Điều chỉnh package cho phù hợp
    public Object monitorController(ProceedingJoinPoint joinPoint) throws Throwable {
        // Ghi lại tham số
        Object[] args = joinPoint.getArgs();
        logger.info("Gọi phương thức: {}", joinPoint.getSignature().getName());
        logger.info("Tham số: {}", args);

        // Thực thi phương thức
        Object result = joinPoint.proceed();

        // Ghi lại kết quả
        logger.info("Kết quả: {}", result);
        return result;
    }
}
