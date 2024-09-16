package vn.luanvan.ktpm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Category;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.repository.CategoryRepository;

import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(Category category) {
        return this.categoryRepository.save(category);
    }

    public Category findById(long id) {
        Optional<Category> categoryOptional = this.categoryRepository.findById(id);
        return categoryOptional.orElse(null);
    }

    public ResultPaginationDTO findAll(Specification<Category> spec, Pageable pageable) {
        Page<Category> categoryPage = this.categoryRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(categoryPage.getTotalPages());
        mt.setTotal(categoryPage.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(categoryPage.getContent());

        return rs;
    }

    public Category update(Category category) {
        Category categoryDB = this.findById(category.getId());
        if (category != null) {
            categoryDB.setName(category.getName());
        }
        return categoryDB;
    }

    public void delete(long id) {
        this.categoryRepository.deleteById(id);
    }
    public boolean isExistByName(String name) {
        return this.categoryRepository.existsByName(name);
    }
}
