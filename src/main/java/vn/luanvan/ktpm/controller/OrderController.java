package vn.luanvan.ktpm.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.domain.Order;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.service.OrderService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;
import vn.luanvan.ktpm.util.error.CustomizeException;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    @ApiMessage("Create a order")
    public ResponseEntity<Order> create(@RequestBody Order order) {
        Order orderDB = this.orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDB);
    }

    @GetMapping("/orders/{id}")
    @ApiMessage("Get order by id")
    public ResponseEntity<Order> findById(@PathVariable Long id) throws CustomizeException {
        Order order = this.orderService.findById(id);
        if (order == null) {
            throw new CustomizeException("Order voi id = " + id + " khong ton tai");
        }
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @GetMapping("/orders")
    @ApiMessage("Get all orders")
    public ResponseEntity<ResultPaginationDTO> findAll(
            @Filter Specification<Order> spec,
            Pageable pageable
            ) {
        ResultPaginationDTO res = this.orderService.findAll(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    @PutMapping("/orders/status")
    @ApiMessage("Update status order")
    public ResponseEntity<Order> updateStatus(@RequestBody Order order) throws CustomizeException {
        Order orderDB = this.orderService.findById(order.getId());
        if(orderDB == null) {
            throw new CustomizeException("Order voi id = " + order.getId()+ " khong ton tai");
        }
        orderDB = this.orderService.updateStatus(order);
        return ResponseEntity.status(HttpStatus.OK).body(orderDB);
    }


}
