package vn.luanvan.ktpm.service;

import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
