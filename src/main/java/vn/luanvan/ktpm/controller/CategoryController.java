package vn.luanvan.ktpm.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.domain.Category;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.service.CategoryService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;
import vn.luanvan.ktpm.util.error.CustomizeException;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    @ApiMessage("Create a new category")
    public ResponseEntity<Category> createNewCategory(@Valid @RequestBody Category category) throws CustomizeException {
        boolean isExistByName = this.categoryService.isExistByName(category.getName());
//        if (isExistByName) {
//            throw new CustomizeException("Category voi name = " + category.getName() + " đã tồn tại");
//        }
        Category categoryDB = this.categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDB);
    }

    @GetMapping("/categories/{id}")
    @ApiMessage("Get category by id")
    public ResponseEntity<Category> getCategoryById(@PathVariable long id) throws CustomizeException {
        Category category = this.categoryService.findById(id);
        if (category == null) {
            throw new CustomizeException("Category voi id = " + id + " khong ton tai");
        }
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @GetMapping("/categories")
    @ApiMessage("Get all category")
    public ResponseEntity<ResultPaginationDTO> getAllCategory(
            @Filter Specification<Category> spec, Pageable pageable) {
        ResultPaginationDTO res = this.categoryService.findAll(spec, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/categories")
    @ApiMessage("Update category")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody Category category) throws CustomizeException {
        Category categoryDB = this.categoryService.findById(category.getId());
        if (categoryDB == null) {
            throw new CustomizeException("Category voi id = " + category.getId() + " khong ton tai");
        }

        boolean isExistByName = this.categoryService.isExistByName(category.getName());
        if (isExistByName) {
            throw new CustomizeException("Category với name = " + category.getName() + " đã tồn tại");
        }

        categoryDB = this.categoryService.update(category);
        return ResponseEntity.status(HttpStatus.OK).body(categoryDB);
    }

    @DeleteMapping("/categories/{id}")
    @ApiMessage("Delete category")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) throws CustomizeException {
        Category categoryDB = this.categoryService.findById(id);
        if (categoryDB == null) {
            throw new CustomizeException("Category voi id = " + id + " khong ton tai");
        }
        this.categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
