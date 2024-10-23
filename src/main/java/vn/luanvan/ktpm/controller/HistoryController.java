package vn.luanvan.ktpm.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.domain.Order;
import vn.luanvan.ktpm.domain.User;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.service.OrderService;
import vn.luanvan.ktpm.service.UserService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;
import vn.luanvan.ktpm.util.error.CustomizeException;

@RestController
@RequestMapping("/api/v1")
public class HistoryController {
    private final OrderService orderService;
    private final UserService userService;

    public HistoryController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/history")
    @ApiMessage("Get all order bu use id")
    public ResponseEntity<ResultPaginationDTO> getOrderByUserId(
            @Filter Specification<Order> spec,
            Pageable pageable) throws CustomizeException {
        ResultPaginationDTO res = this.orderService.findAll(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
