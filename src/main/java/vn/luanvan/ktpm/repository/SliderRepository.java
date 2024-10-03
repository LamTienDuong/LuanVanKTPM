package vn.luanvan.ktpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.luanvan.ktpm.domain.Slider;

@Repository
public interface SliderRepository extends JpaRepository<Slider, Long>, JpaSpecificationExecutor<Slider> {
}
