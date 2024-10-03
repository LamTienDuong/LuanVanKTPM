package vn.luanvan.ktpm.service;

import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Reviews;
import vn.luanvan.ktpm.repository.ReviewsRepository;

import java.util.Optional;

@Service
public class ReviewsService {
    private final ReviewsRepository reviewsRepository;

    public ReviewsService(ReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }

    public Reviews create(Reviews reviews) {
        return this.reviewsRepository.save(reviews);
    }

    public Reviews findById(long id) {
        Optional<Reviews> reviewsOptional = this.reviewsRepository.findById(id);
        return reviewsOptional.orElse(null);
    }

    public Reviews update(Reviews reviews) {
        return this.reviewsRepository.save(reviews);
    }
}
