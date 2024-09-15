package vn.luanvan.ktpm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Skill;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.repository.SkillRepository;
import vn.luanvan.ktpm.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill handleCreateSkill(Skill skill) throws IdInvalidException {
        return this.skillRepository.save(skill);
    }

    public Skill handleGetSkillById(Long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        return skillOptional.orElse(null);
    }

    public Skill handleUpdateSkill(Skill skill) {
        Skill currentSkill = this.handleGetSkillById(skill.getId());
        if (currentSkill != null) {
            currentSkill.setName(skill.getName());
            currentSkill = this.skillRepository.save(currentSkill);
        }
        return currentSkill;
    }

    public ResultPaginationDTO handleGetAllSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());

        res.setMeta(mt);

        res.setResult(pageSkill.getContent());
        return res;
    }

    public void handleDeleteSkill(long id) {
        // delete job inside job_skill table
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        // delete subscriber (inside subscriber_skill table)
        currentSkill.getSubscribers().forEach(sub -> sub.getSkills().remove(currentSkill));

        // xoa skill ben con
        this.skillRepository.delete(currentSkill);
    }

    public List<Skill> findByIdIn(List<Long> id) {
        return this.skillRepository.findByIdIn(id);
    }

    public boolean isNameExist(String name) {
        return  this.skillRepository.existsByName(name);
    }
}
