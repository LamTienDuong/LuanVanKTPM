package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @ApiMessage("Create a skill")
    @PostMapping("/skills")
    public ResponseEntity<Skill> createNewSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        boolean isNameExist = this.skillService.isNameExist(skill.getName());
        if (isNameExist) {
            throw new IdInvalidException("Skill name: "+ skill.getName() + " da ton tai");
        }
        Skill newSkill = this.skillService.handleCreateSkill(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSkill);
    }

    @ApiMessage("Update a skill")
    @PutMapping("/skills")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        Skill currentSkill = this.skillService.handleGetSkillById(skill.getId());
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id = " + skill.getId() + " khong ton tai");
        }
        boolean isNameExist = this.skillService.isNameExist(skill.getName());
        if (isNameExist) {
            throw new IdInvalidException("Skill name = "+ skill.getName() + " da ton tai");
        }
        Skill updateSkill = this.skillService.handleUpdateSkill(skill);
        return ResponseEntity.status(HttpStatus.OK).body(updateSkill);
    }

    @ApiMessage("Get a skill")
    @GetMapping("/skills/{id}")
    public ResponseEntity<Skill> getSkill(@PathVariable Long id) throws IdInvalidException {
        Skill currentSkill = this.skillService.handleGetSkillById(id);
        if (currentSkill == null) {
            throw  new IdInvalidException("Skill voi id: " +id + " khong ton tai");
        }
        return ResponseEntity.status(HttpStatus.OK).body(currentSkill);
    }

    @GetMapping("/skills")
    @ApiMessage("Get all skill")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(
            @Filter Specification<Skill> spec,
            Pageable pageable
            ) {
        ResultPaginationDTO res = this.skillService.handleGetAllSkill(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable long id) throws IdInvalidException {
        Skill currentSkill = this.skillService.handleGetSkillById(id);
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id = " + id + " khong ton tai");
        }
//        this.skillService
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
