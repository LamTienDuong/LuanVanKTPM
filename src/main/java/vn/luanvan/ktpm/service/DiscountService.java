package vn.luanvan.ktpm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Discount;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.repository.DiscountRepository;

import java.util.Optional;

@Service
public class DiscountService {
    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public Discount create(Discount discount) {
        return this.discountRepository.save(discount);
    }

    public Discount findById(long id) {
        Optional<Discount> discountOptional = this.discountRepository.findById(id);
        return discountOptional.orElse(null);
    }

    public ResultPaginationDTO findAll(Specification<Discount> spec, Pageable pageable) {
        Page<Discount> discountPage = this.discountRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(discountPage.getTotalPages());
        mt.setTotal(discountPage.getTotalElements());

        res.setMeta(mt);
        res.setResult(discountPage.getContent());

        return res;
    }

    public Discount update(Discount discount) {
        Discount discountDB = this.findById(discount.getId());

        if (discountDB != null) {
            discountDB.setName(discount.getName());
            discountDB.setPercent(discount.getPercent());
            discountDB.setDateStart(discount.getDateStart());
            discountDB.setDateEnd(discount.getDateEnd());
            this.discountRepository.save(discountDB);
        }
        return discountDB;
    }

    public void delete(long id) {
        this.discountRepository.deleteById(id);
    }

    public boolean isExistByName(String name) {
        return this.discountRepository.existsByName(name);
    }
}
