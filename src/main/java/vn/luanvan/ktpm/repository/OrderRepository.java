package vn.luanvan.ktpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.luanvan.ktpm.domain.Order;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findByUserIdAndStatus(long id, String status);

    @Query("SELECT o FROM Order o WHERE FUNCTION('MONTH', o.createdAt) = :month AND FUNCTION('YEAR', o.createdAt) = :year")
    List<Order> findAllByCreatedAtMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.createdAt) = :date")
    List<Order> findAllByCreatedAt(@Param("date") LocalDate date);

}
