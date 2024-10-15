package vn.luanvan.ktpm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Product;
import vn.luanvan.ktpm.domain.Reviews;
import vn.luanvan.ktpm.domain.User;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.domain.response.reviews.ResReviewsDTO;
import vn.luanvan.ktpm.repository.ProductRepository;
import vn.luanvan.ktpm.repository.ReviewsRepository;
import vn.luanvan.ktpm.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewsService {
    private final ReviewsRepository reviewsRepository;
    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public ReviewsService(ReviewsRepository reviewsRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewsRepository = reviewsRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Reviews create(Reviews reviews) {
        if (reviews.getProduct() != null) {
            Optional<Product> productOptional = this.productRepository.findById(reviews.getProduct().getId());
            reviews.setProduct(productOptional.get());
        }

        if (reviews.getUser() != null) {
            Optional<User> userOptional = this.userRepository.findById(reviews.getUser().getId());
            reviews.setUser(userOptional.get());
        }
        return this.reviewsRepository.save(reviews);
    }

    public Page<ResReviewsDTO> convertListToPage(List<ResReviewsDTO> list, Pageable pageable) {
        int total = list.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);

        List<ResReviewsDTO> pagedList = list.subList(start, end);
        return new PageImpl<>(pagedList, pageable, total);
    }

    public ResultPaginationDTO findAll(Specification<Reviews> spec, Pageable pageable, long id) {
        Optional<Product> productOptional = this.productRepository.findById(id);

        List<Reviews> reviewsList = this.reviewsRepository.findAll().stream().filter(
                item -> item.getProduct().equals(productOptional.get())
        ).collect(Collectors.toList());

        List<ResReviewsDTO> reviewsDTOS = reviewsList.stream().map(
                item -> new ResReviewsDTO(
                        item.getId(),
                        new ResReviewsDTO.ReviewsProduct(item.getProduct().getId(), item.getProduct().getName()),
                        new ResReviewsDTO.ReviewsUser(item.getUser().getId(), item.getUser().getName(), item.getUser().getAvatar()),
                        item.getRate(), item.getContent(),
                        item.getCreatedAt(), item.getCreatedBy())
        ).collect(Collectors.toList());

        Page<ResReviewsDTO> reviewsPage = this.convertListToPage(reviewsDTOS, pageable);

        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(reviewsPage.getTotalPages());
        mt.setTotal(reviewsPage.getTotalElements());

        res.setMeta(mt);
        res.setResult(reviewsPage.getContent());
        return res;
    }

    public Reviews findById(long id) {
        Optional<Reviews> reviewsOptional = this.reviewsRepository.findById(id);
        return reviewsOptional.orElse(null);
    }

    public Reviews update(Reviews reviews) {
        return this.reviewsRepository.save(reviews);
    }
}
