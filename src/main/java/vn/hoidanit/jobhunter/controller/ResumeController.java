package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;
    private final FilterBuilder filterBuilder;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeController(ResumeService resumeService, UserService userService, FilterBuilder filterBuilder, FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.filterBuilder = filterBuilder;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a new resume")
    public ResponseEntity<ResCreateResumeDTO> createNewResume(@Valid @RequestBody Resume resume) throws IdInvalidException {
        boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isIdExist) {
            throw new IdInvalidException("User id/Job id khong ton tai");
        }

        // create new resume
        ResCreateResumeDTO res = this.resumeService.handleCreateResume(resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) throws IdInvalidException {
        Resume currentResume = this.resumeService.handleGetResumeById(resume.getId());
        if (currentResume == null) {
            throw new IdInvalidException("Resume voi id = " + resume.getId() + " khong ton tai");
        }

        // cap nhat lai trang thai resume
        currentResume.setStatus(resume.getStatus());
        ResUpdateResumeDTO res = this.resumeService.handleUpdateResume(currentResume);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<Void> deleteResume(@PathVariable long id) throws IdInvalidException {
        Resume resume = this.resumeService.handleGetResumeById(id);
        if (resume == null) {
            throw new IdInvalidException("Resume voi id = " + id + " khong ton tai");
        }
        this.resumeService.handleDeleteResume(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Get a resume")
    public ResponseEntity<ResResumeDTO> getResume(@PathVariable long id) throws IdInvalidException {
        Resume resume = this.resumeService.handleGetResumeById(id);
        // check resume exist
        if (resume == null) {
            throw new IdInvalidException("Resume voi id = " + id + " khong ton tai");
        }
        ResResumeDTO res = this.resumeService.convertToResResumeDTO(resume);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/resumes")
    @ApiMessage("Get all resume")
    public ResponseEntity<ResultPaginationDTO> getAllResume(
            @Filter Specification<Resume> spec,
            Pageable pageable
            ) {
        List<Long> arrJobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.handleGetUserByUsername(email);
        if (currentUser != null) {
            Company userCompany = currentUser.getCompany();
            if (userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && companyJobs.size() > 0) {
                    arrJobIds = companyJobs.stream().map(x -> x.getId())
                            .collect(Collectors.toList());
                }
            }
        }

        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(filterBuilder.field("job")
                .in(filterBuilder.input(arrJobIds)).get());

        Specification<Resume> finalSpec = jobInSpec.and(spec);

        ResultPaginationDTO rs = this.resumeService.handleGetAllResume(finalSpec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(rs);
    }

    @PostMapping("resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> getResumeByUser(Pageable pageable) {
        ResultPaginationDTO res = this.resumeService.handleGetResumeByUser(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
