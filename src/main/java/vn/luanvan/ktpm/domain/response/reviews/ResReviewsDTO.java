package vn.luanvan.ktpm.domain.response.reviews;

import jakarta.persistence.Column;

import java.time.Instant;

public class ResReviewsDTO {
    private long id;
    private ReviewsProduct product;
    private ReviewsUser user;
    private String rate;
    private String content;
    private Instant createdAt;
    private String createdBy;

    public ResReviewsDTO() {
    }

    public ResReviewsDTO(long id, ReviewsProduct product, ReviewsUser user, String rate, String content, Instant createdAt, String createdBy) {
        this.id = id;
        this.product = product;
        this.user = user;
        this.rate = rate;
        this.content = content;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public static class ReviewsProduct{
        private long id;
        private String name;

        public ReviewsProduct() {
        }

        public ReviewsProduct(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ReviewsUser{
        private long id;
        private String name;
        private String avatar;

        public ReviewsUser() {
        }

        public ReviewsUser(long id, String name, String avatar) {
            this.id = id;
            this.name = name;
            this.avatar = avatar;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ReviewsProduct getProduct() {
        return product;
    }

    public void setProduct(ReviewsProduct product) {
        this.product = product;
    }

    public ReviewsUser getUser() {
        return user;
    }

    public void setUser(ReviewsUser user) {
        this.user = user;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
