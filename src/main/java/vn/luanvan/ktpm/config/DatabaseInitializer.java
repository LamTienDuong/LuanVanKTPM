package vn.luanvan.ktpm.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.*;
import vn.luanvan.ktpm.repository.CategoryRepository;
import vn.luanvan.ktpm.repository.PermissionRepository;
import vn.luanvan.ktpm.repository.RoleRepository;
import vn.luanvan.ktpm.repository.UserRepository;
import vn.luanvan.ktpm.util.constant.GenderEnum;

import java.util.ArrayList;
import java.util.List;


@Service
public class DatabaseInitializer implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(CategoryRepository categoryRepository, PermissionRepository permissionRepository, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.categoryRepository = categoryRepository;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");

        long countPermissions = this.permissionRepository.count();
        long countRoles = this.roleRepository.count();
        long countUsers = this.userRepository.count();

        if (countPermissions == 0) {
            ArrayList<Permission> arr = new ArrayList<>();
            arr.add(new Permission("Create a product", "/api/v1/products", "POST", "PRODUCTS"));
            arr.add(new Permission("Update a product", "/api/v1/products", "PUT", "PRODUCTS"));
            arr.add(new Permission("Delete a product", "/api/v1/products/{id}", "DELETE", "PRODUCTS"));
            arr.add(new Permission("Get a product by id", "/api/v1/products/{id}", "GET", "PRODUCTS"));
            arr.add(new Permission("Get products with pagination", "/api/v1/products", "GET", "PRODUCTS"));

            arr.add(new Permission("Create a user", "/api/v1/users", "POST", "USERS"));
            arr.add(new Permission("Update a user", "/api/v1/users", "PUT", "USERS"));
            arr.add(new Permission("Delete a user", "/api/v1/users/{id}", "DELETE", "USERS"));
            arr.add(new Permission("Get a user by id", "/api/v1/users/{id}", "GET", "USERS"));
            arr.add(new Permission("Get users with pagination", "/api/v1/users", "GET", "USERS"));

            this.permissionRepository.saveAll(arr);
        }

        if (countRoles == 0) {
            List<Permission> allPermissions = this.permissionRepository.findAll();

            Role adminRole = new Role();
            adminRole.setName("SUPER_ADMIN");
            adminRole.setDescription("Admin thì full permissions");
            adminRole.setActive(true);
            adminRole.setPermissions(allPermissions);

            this.roleRepository.save(adminRole);
        }

        if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setGender(GenderEnum.MALE);
            adminUser.setName("I'm super admin");
            adminUser.setPassword(this.passwordEncoder.encode("123456"));

            Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }

            this.userRepository.save(adminUser);
        }

        if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        } else
            System.out.println(">>> END INIT DATABASE");

    }
}
