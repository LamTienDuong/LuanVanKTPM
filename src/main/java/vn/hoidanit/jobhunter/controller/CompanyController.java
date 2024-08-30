package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;


    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        Company localCompany = this.companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(localCompany);
    }

    @GetMapping("companies/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable Long id) {
        Company localCompany = this.companyService.handleGetCompany(id);
        return ResponseEntity.status(HttpStatus.OK).body(localCompany);
    }

    @GetMapping("companies")
    @ApiMessage("fetch all company")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(
            @Filter Specification<Company> spec, Pageable pageable
            ) {
        ResultPaginationDTO resultPaginationDTO = this.companyService.handleGetAllCompany(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }

    @PutMapping("companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody  Company company) {
        Company localCompany = this.companyService.handleUpdateCompany(company);
        return ResponseEntity.status(HttpStatus.OK).body(localCompany);
    }

    @DeleteMapping("companies/{id}")
    public void deleteCompany(@PathVariable Long id) {
        this.companyService.handleDeleteCompany(id);
    }
}