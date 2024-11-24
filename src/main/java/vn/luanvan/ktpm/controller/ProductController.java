package vn.luanvan.ktpm.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.domain.Product;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.service.ProductService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;
import vn.luanvan.ktpm.util.error.CustomizeException;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    @ApiMessage("Create a product")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) throws CustomizeException {
        boolean isExistByName = this.productService.existByName(product.getName());

//        if (isExistByName) {
//            throw new CustomizeException("Product voi name = " + product.getName() + " da ton tai");
//        }
        Product productDB = this.productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDB);
    }

    @GetMapping("/products/{id}")
    @ApiMessage("Get a product")
    public ResponseEntity<Product> findProductById(@PathVariable long id) throws CustomizeException {
        Product product = this.productService.findById(id);
        if (product == null) {
            throw new CustomizeException("Product voi id = " + id + " khong ton tai");
        }
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/products")
    @ApiMessage("Get all products")
    public ResponseEntity<ResultPaginationDTO> findAll(
            @Filter Specification<Product> spec,
            Pageable pageable
    ) {
        ResultPaginationDTO res = this.productService.findAll(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/products")
    @ApiMessage("Update a product")
    public ResponseEntity<Product> update(@Valid @RequestBody Product product) throws CustomizeException {
//        boolean isExistByName = this.productService.existByName(product.getName());
//        if (isExistByName) {
//            throw new CustomizeException("Product voi name = " + product.getName() + " da ton tai");
//        }

        Product productDB = this.productService.findById(product.getId());
        if (productDB == null) {
            throw new CustomizeException("Product voi id = " + product.getId() + " khong ton tai");
        }
        productDB = this.productService.update(product);
        return ResponseEntity.status(HttpStatus.OK).body(productDB);
    }

    @DeleteMapping("/products/{id}")
    @ApiMessage("Delete a product")
    public ResponseEntity<Void> delete(@PathVariable long id) throws CustomizeException {
        Product product = this.productService.findById(id);
        if (product == null) {
            throw new CustomizeException("Product voi id = " + id + " khong ton tai");
        }
        this.productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
