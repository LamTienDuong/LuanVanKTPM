package vn.luanvan.ktpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.luanvan.ktpm.domain.Reviews;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long>, JpaSpecificationExecutor<Reviews> {
}
