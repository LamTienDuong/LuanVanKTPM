package vn.luanvan.ktpm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Permission;
import vn.luanvan.ktpm.domain.Role;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.repository.PermissionRepository;
import vn.luanvan.ktpm.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean isExistByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role handleCreateRole(Role role) {
        // check permission
        if (role.getPermissions() != null) {
            List<Long> reqPermission = role.getPermissions().stream().map(
                    item -> item.getId()
            ).collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermission);
            role.setPermissions(dbPermissions);
        }
        return this.roleRepository.save(role);
    }

    public Role handleGetRoleById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        return roleOptional.orElse(null);
    }

    public Role handleUpdateRole(Role role) {
        Role roleDB = this.handleGetRoleById(role.getId());
        // check permission
        if (role.getPermissions() != null) {
            List<Long> reqPermission = role.getPermissions().stream().map(
                    item -> item.getId()
            ).collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermission);
            role.setPermissions(dbPermissions);
        }

        roleDB.setName(role.getName());
        roleDB.setDescription(role.getDescription());
        roleDB.setActive(role.isActive());
        roleDB.setPermissions(role.getPermissions());
        roleDB = this.roleRepository.save(roleDB);

        return this.roleRepository.save(roleDB);
    }

    public void handledeleteRole(long id) {
        this.roleRepository.deleteById(id);
    }

    public ResultPaginationDTO handleGetAllRole(Specification<Role> spec, Pageable pageable) {
        Page<Role> rolePage = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(rolePage.getTotalPages());
        mt.setTotal(rolePage.getTotalElements());
        res.setMeta(mt);

        res.setResult(rolePage.getContent());
        return res;
    }

}
