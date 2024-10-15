package vn.luanvan.ktpm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.luanvan.ktpm.domain.Product;
import vn.luanvan.ktpm.domain.Reviews;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long>, JpaSpecificationExecutor<Reviews> {
    List<Reviews> findAllByProduct(Product product);
}
