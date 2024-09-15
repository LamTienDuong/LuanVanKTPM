package vn.luanvan.ktpm.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.domain.Permission;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.service.PermissionService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;
import vn.luanvan.ktpm.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createNewPermission(@Valid @RequestBody Permission permission) throws IdInvalidException {
        // check exist
        if (this.permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException("Permission da ton tai");
        }
        // create a new permission

        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.permissionService.handleCreatePermission(permission)
        );
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission permission) throws IdInvalidException {
        // check exist by id
        if (this.permissionService.handleGetPermissionById(permission.getId()) == null) {
            throw new IdInvalidException("Permission voi id = " + permission.getId() + " khong ton tai");
        }

        // check exist by module, apiPath and method
        if (this.permissionService.isPermissionExist(permission)) {
            if (this.permissionService.isSameName(permission)) {
                throw new IdInvalidException("Permission da ton tai");
            }
        }

        Permission permissionDB = this.permissionService.handleUpdatePermission(permission);
        return ResponseEntity.status(HttpStatus.OK).body(permissionDB);
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> deletePermission(@PathVariable long id) throws IdInvalidException {
        // check id exist
        Permission permission = this.permissionService.handleGetPermissionById(id);
        if (permission == null) {
            throw new IdInvalidException("Permission void id = " + id + " khong ton tai");
        }

        this.permissionService.handleDeletePermission(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/permissions")
    @ApiMessage("Get all permission")
    public ResponseEntity<ResultPaginationDTO> getAllPermission(
            @Filter Specification<Permission> spec,
            Pageable pageable
            ) {
        ResultPaginationDTO resultPaginationDTO = this.permissionService.handleGetAllPermission(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }
}
