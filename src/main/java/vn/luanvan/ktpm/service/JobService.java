package vn.luanvan.ktpm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Company;
import vn.luanvan.ktpm.domain.Job;
import vn.luanvan.ktpm.domain.Skill;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.domain.response.job.ResCreateJobDTO;
import vn.luanvan.ktpm.domain.response.job.ResUpdateJobDTO;
import vn.luanvan.ktpm.repository.CompanyRepository;
import vn.luanvan.ktpm.repository.JobRepository;
import vn.luanvan.ktpm.repository.SkillRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, CompanyRepository companyRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.skillRepository = skillRepository;
    }

    public Job handleGetJobById(long id) {
        Optional<Job> jobOptional = this.jobRepository.findById(id);
        return jobOptional.orElse(null);
    }

    public ResCreateJobDTO handleCreateJob(Job job) {
        //check skills
        if (job.getSkills() != null) {
            // Lay ra danh sach id cua job
            List<Long> id = job.getSkills().stream().map(item -> item.getId()).toList();
            // Tim kiem skill ton tai trong DB
            List<Skill> skills = this.skillRepository.findByIdIn(id);
            // set skills lai cho job
            job.setSkills(skills);
        }
        // check company
        if (job.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(job.getCompany().getId());
            if (companyOptional.isPresent()) {
                job.setCompany(companyOptional.get());
            }
        }

        //create job
        Job currentJob = this.jobRepository.save(job);

        // convert response
        ResCreateJobDTO dto = new ResCreateJobDTO();

        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setLocation(currentJob.getLocation());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setCreatedAt(currentJob.getCreatedAt());
        dto.setCreateBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills().stream().map(
                    item -> item.getName()
            ).toList();
            dto.setSkills(skills);
        }
        return dto;
    }

    public ResUpdateJobDTO handleUpdateJob(Job job, Job jobInDB) {
        // check skills
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills().stream().map(
                    item -> item.getId()
            ).collect(Collectors.toList());

            List<Skill> skills = this.skillRepository.findByIdIn(reqSkills);
            jobInDB.setSkills(skills);
        }

        if (job.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(job.getCompany().getId());

            if (companyOptional.isPresent()) {
                jobInDB.setCompany(companyOptional.get());
            }
        }

        // update correct info
        jobInDB.setName(job.getName());
        jobInDB.setSalary(job.getSalary());
        jobInDB.setQuantity(job.getQuantity());
        jobInDB.setLocation(job.getLocation());
        jobInDB.setLevel(job.getLevel());
        jobInDB.setStartDate(job.getStartDate());
        jobInDB.setEndDate(job.getEndDate());
        jobInDB.setActive(job.isActive());

        // update job
        Job updateJob = this.jobRepository.save(jobInDB);

        // convert response
        ResUpdateJobDTO res = new ResUpdateJobDTO();
        res.setId(updateJob.getId());
        res.setName(updateJob.getName());
        res.setSalary(updateJob.getSalary());
        res.setQuantity(updateJob.getQuantity());
        res.setLocation(updateJob.getLocation());
        res.setLevel(updateJob.getLevel());
        res.setStartDate(updateJob.getStartDate());
        res.setEndDate(updateJob.getEndDate());
        res.setActive(updateJob.isActive());
        res.setUpdatedAt(updateJob.getUpdatedAt());
        res.setUpdateBy(updateJob.getUpdatedBy());

        if (updateJob.getSkills() != null) {
            List<String> skills = updateJob.getSkills().stream().map(
                    item -> item.getName()
            ).toList();
            res.setSkills(skills);
        }

        return res;
    }

    public void handleDeleteJob(long id) {
        this.jobRepository.deleteById(id);
    }

    public ResultPaginationDTO handleGetAllJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());

        res.setMeta(mt);
        res.setResult(pageJob.getContent());

        return res;
    }
}
