package vn.hoidanit.jobhunter.service;

import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;
    private final FilterParser filterParser;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeService(UserRepository userRepository, JobRepository jobRepository, ResumeRepository resumeRepository, FilterParser filterParser, FilterSpecificationConverter filterSpecificationConverter) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
        this.filterParser = filterParser;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }

    public boolean checkResumeExistByUserAndJob(Resume resume) {
        // check user by id
        if(resume.getUser() == null) {
            return false;
        }
        Optional<User>  user = this.userRepository.findById(resume.getUser().getId());
        if (user.isEmpty()) {
            return false;
        }

        if (resume.getJob() == null) {
            return false;
        }
        Optional<Job> job = this.jobRepository.findById(resume.getJob().getId());
        if (job.isEmpty()) {
            return false;
        }

        return true;
    }

    public ResCreateResumeDTO handleCreateResume(Resume resume) {
        resume = this.resumeRepository.save(resume);

        ResCreateResumeDTO res = new ResCreateResumeDTO();
        res.setId(resume.getId());
        res.setCreateAt(resume.getCreatedAt());
        res.setCreateBy(resume.getCreatedBy());

        return res;
    }

    public Resume handleGetResumeById(long id) {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(id);
        return resumeOptional.orElse(null);
    }

    public ResUpdateResumeDTO handleUpdateResume(Resume resume) {
        resume = this.resumeRepository.save(resume);

        ResUpdateResumeDTO res = new ResUpdateResumeDTO();
        res.setUpdateAt(resume.getUpdatedAt());
        res.setUpdateBy(resume.getUpdatedBy());

        return res;
    }

    public void handleDeleteResume(long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResResumeDTO convertToResResumeDTO(Resume resume) {
        ResResumeDTO res = new ResResumeDTO();
        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedAt(resume.getCreatedAt());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        res.setUpdatedBy(resume.getUpdatedBy());

        if (resume.getJob() != null) {
            res.setCompanyName(resume.getJob().getCompany().getName());
        }

        res.setUser(new ResResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        res.setJob(new ResResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));
        return  res;
    }

    public ResultPaginationDTO  handleGetAllResume(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        List<ResResumeDTO> listResumeDTO = pageResume.getContent().stream().map(
                item -> this.convertToResResumeDTO(item)
        ).toList();
        rs.setResult(listResumeDTO);
        return rs;
    }

    public ResultPaginationDTO handleGetResumeByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get() : "";

        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta  mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageResume.getContent());

        return rs;
    }
}
