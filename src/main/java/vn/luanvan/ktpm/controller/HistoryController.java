package vn.luanvan.ktpm.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
            @RequestParam(required = false) Long id,
            Pageable pageable) throws CustomizeException {
        User user = this.userService.handleGetUser(id);
        if (user == null) {
            throw new CustomizeException("User voi id = " + id + " khong ton tai");
        }
        ResultPaginationDTO res = this.orderService.findOrderByUserId(id, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
