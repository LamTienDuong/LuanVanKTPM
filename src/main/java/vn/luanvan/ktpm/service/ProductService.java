package vn.luanvan.ktpm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Category;
import vn.luanvan.ktpm.domain.Product;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.domain.response.product.ResProductDTO;
import vn.luanvan.ktpm.repository.CategoryRepository;
import vn.luanvan.ktpm.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product create(Product product) {
        if (product.getCategory() != null) {
            Optional<Category> categoryOptional = this.categoryRepository.findById(product.getCategory().getId());
            product.setCategory(categoryOptional.orElse(null));
        }
        return this.productRepository.save(product);
    }

    public Product findById(long id) {
        Optional<Product> productOptional = this.productRepository.findById(id);
        return productOptional.orElse(null);
    }

    public ResultPaginationDTO findAll(Specification<Product> spec, Pageable pageable) {
        Page<Product> productPage = this.productRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        List<Product> productList = productPage.getContent().stream().filter(
                Product::isActive
        ).collect(Collectors.toList());

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(productPage.getTotalPages());
        mt.setTotal(productList.size());

        res.setMeta(mt);


        res.setResult(productList);
        return res;
    }

    public Product update(Product product) {
        Product productDB = this.findById(product.getId());

        productDB.setName(product.getName());
        productDB.setPrice(product.getPrice());
        productDB.setQuantity(product.getQuantity());
        if (!product.getThumbnail().isEmpty()) {
            productDB.setThumbnail(product.getThumbnail());
        }

        if (product.getSlider() != null) {
            productDB.setSlider(product.getSlider());
        }

        if (product.getCategory() != null) {
            Category category = this.categoryRepository.findByName(product.getCategory().getName());

            productDB.setCategory(category);
        }
        productDB.setActive(product.isActive());
        return this.productRepository.save(productDB);
    }

    public void delete(long id) {
        Product product = this.findById(id);
        if (product != null) {
            product.setActive(false);
        }
    }

    public boolean existByName(String name) {
        return this.productRepository.existsByName(name);
    }

    public int countProductByCategory(Category category) {
        Optional<Category> categoryDB = this.categoryRepository.findById(category.getId());
        return this.productRepository.countByCategory(categoryDB.get());
    }

    public long countProduct() {
        return this.productRepository.count();
    }

}
