package vn.luanvan.ktpm.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.domain.Reviews;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.service.ReviewsService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class ReviewsController {
    private final ReviewsService reviewsService;

    public ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @PostMapping("/reviews")
    @ApiMessage("Create a reviews")
    public ResponseEntity<Reviews> create(@RequestBody Reviews  reviews) {
        Reviews reviewsDB = this.reviewsService.create(reviews);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewsDB);
    }

    @GetMapping("/reviews")
    @ApiMessage("Find all reviews")
    public ResponseEntity<ResultPaginationDTO> findAll(
            @Filter Specification<Reviews> spec, Pageable pageable) {
        ResultPaginationDTO res = this.reviewsService.findAll(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
