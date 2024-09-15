package vn.luanvan.ktpm.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.domain.Job;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.domain.response.job.ResCreateJobDTO;
import vn.luanvan.ktpm.domain.response.job.ResUpdateJobDTO;
import vn.luanvan.ktpm.service.JobService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;
import vn.luanvan.ktpm.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a job")
    public ResponseEntity<ResCreateJobDTO> createNewJob(@Valid @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.handleCreateJob(job));
    }

    @PutMapping("/jobs")
    @ApiMessage("Update a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) throws IdInvalidException {
        Job currentJob = this.jobService.handleGetJobById(job.getId());
        if (currentJob == null) {
            throw new IdInvalidException("Job not found");
        }
        ResUpdateJobDTO resUpdateJobDTO = this.jobService.handleUpdateJob(job, currentJob);
        return ResponseEntity.status(HttpStatus.OK).body(resUpdateJobDTO);
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> deleteJob(@PathVariable long id) throws IdInvalidException {
        Job job = this.jobService.handleGetJobById(id);
        if (job == null) {
            throw new IdInvalidException("Job not found");
        }
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @GetMapping("/jobs")
    @ApiMessage("Get all job")
    public ResponseEntity<ResultPaginationDTO> getAllJob(
            @Filter Specification<Job> spec,
            Pageable pageable
            ) {
        ResultPaginationDTO res = this.jobService.handleGetAllJob(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get a job")
    public ResponseEntity<Job> getJob(@PathVariable long id) throws IdInvalidException {
        Job job = this.jobService.handleGetJobById(id);
        if (job == null) {
            throw new IdInvalidException("Job not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.handleGetJobById(id));
    }
}
