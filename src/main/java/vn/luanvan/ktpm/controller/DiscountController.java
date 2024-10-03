package vn.luanvan.ktpm.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.domain.Discount;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.service.DiscountService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;
import vn.luanvan.ktpm.util.error.CustomizeException;

@RestController
@RequestMapping("/api/v1")
public class DiscountController {
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping("/discounts")
    @ApiMessage("Create a new discount")
    public ResponseEntity<Discount> createNewDiscount(@Valid @RequestBody Discount discount) throws CustomizeException {
        boolean isExistByName = this.discountService.isExistByName(discount.getName());
        if (isExistByName) {
            throw new CustomizeException("Discount voi name = " + discount.getName()+ " da ton tai");
        }
        Discount discountDB = this.discountService.create(discount);
        return ResponseEntity.status(HttpStatus.CREATED).body(discountDB);
    }

    @GetMapping("/discounts/{id}")
    @ApiMessage("Get discount by id")
    public ResponseEntity<Discount> findById(@PathVariable long id) throws CustomizeException {
        Discount discount = this.discountService.findById(id);
        if (discount == null) {
            throw new CustomizeException("Discount voi id = " + id +" khong ton tai");
        }
        return ResponseEntity.status(HttpStatus.OK).body(discount);
    }

    @GetMapping("/discounts")
    @ApiMessage("Get all discount")
    public ResponseEntity<ResultPaginationDTO> findAll(
            @Filter Specification<Discount> spec,
            Pageable pageable) {
        ResultPaginationDTO res = this.discountService.findAll(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/discounts")
    @ApiMessage("Update a discount")
    public ResponseEntity<Discount> update(@Valid @RequestBody Discount discount) throws CustomizeException {
        Discount discountDB = this.discountService.findById(discount.getId());
        if (discountDB == null) {
            throw new CustomizeException("Discount voi id = " + discount.getId() + " khong ton tai");
        }

        boolean isExistByName = this.discountService.isExistByName(discount.getName());
        if (isExistByName) {
            throw new CustomizeException("Discount voi name = " + discount.getName()+ " da ton tai");
        }
        discountDB = this.discountService.update(discount);
        return ResponseEntity.status(HttpStatus.OK).body(discountDB);
    }

    @DeleteMapping("/discounts/{id}")
    @ApiMessage("Delete a discount")
    public ResponseEntity<Void> delete(@PathVariable long id) throws CustomizeException {
        Discount discount = this.discountService.findById(id);
        if (discount == null) {
            throw new CustomizeException("Discount voi id = " + id + " khong ton tai");
        }
        this.discountService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
