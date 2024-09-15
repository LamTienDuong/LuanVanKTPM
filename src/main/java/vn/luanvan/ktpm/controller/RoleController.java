package vn.luanvan.ktpm.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.domain.Role;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.service.RoleService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;
import vn.luanvan.ktpm.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a new role")
    public ResponseEntity<Role> createNewRole(@RequestBody Role role) throws IdInvalidException {
        boolean isExistByName = this.roleService.isExistByName(role.getName());
        if (isExistByName) {
            throw new IdInvalidException("Role voi name = " + role.getName() + " da ton tai");
        }
        Role currentRole = this.roleService.handleCreateRole(role);
        return ResponseEntity.status(HttpStatus.OK).body(currentRole);

    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) throws IdInvalidException {
        // check id
        Role currentRole = this.roleService.handleGetRoleById(role.getId());
        if (currentRole == null) {
            throw new IdInvalidException("Role voi id = " + role.getId() + " khong ton tai");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.roleService.handleUpdateRole(role));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> deleteRole(@PathVariable long id) throws IdInvalidException {
        Role role = this.roleService.handleGetRoleById(id);
        if (role == null) {
            throw new IdInvalidException("Role voi id = " + id + " khong ton tai");
        }
        this.roleService.handledeleteRole(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/roles")
    @ApiMessage("Get all role")
    public ResponseEntity<ResultPaginationDTO> getAllRole(
            @Filter Specification<Role> spec,
            Pageable pageable
            ) {
        ResultPaginationDTO resultPaginationDTO = this.roleService.handleGetAllRole(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Get role by id")
    public ResponseEntity<Role> getRoleById(@PathVariable long id) throws IdInvalidException {
        Role role = this.roleService.handleGetRoleById(id);
        if (role == null) {
            throw new IdInvalidException("Role voi id = " + id + " khong ton tai");
        }
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }
}
