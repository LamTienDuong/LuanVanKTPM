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
        long countCategory = this.categoryRepository.count();

        if (countCategory == 0) {
            ArrayList<Category> listCategory = new ArrayList<>();
            listCategory.add(new Category("áo sơ mi nam"));
            listCategory.add(new Category("áo khoác nam"));
            listCategory.add(new Category("quần dài nam"));
            this.categoryRepository.saveAll(listCategory);
        }

        if (countPermissions == 0) {
            ArrayList<Permission> arr = new ArrayList<>();
            arr.add(new Permission("Create a address", "/api/v1/addresses", "POST", "ADDRESSES"));
            arr.add(new Permission("Update a address", "/api/v1/addresses", "PUT", "ADDRESSES"));
            arr.add(new Permission("Delete a address", "/api/v1/addresses/{id}", "DELETE", "ADDRESSES"));
            arr.add(new Permission("Get a address by id", "/api/v1/addresses/{id}", "GET", "ADDRESSES"));
            arr.add(new Permission("Get address with pagination", "/api/v1/addresses", "GET", "ADDRESSES"));

            arr.add(new Permission("Create a category", "/api/v1/categories", "POST", "CATEGORIES"));
            arr.add(new Permission("Update a category", "/api/v1/categories", "PUT", "CATEGORIES"));
            arr.add(new Permission("Delete a category", "/api/v1/categories/{id}", "DELETE", "CATEGORIES"));
            arr.add(new Permission("Get a category by id", "/api/v1/categories/{id}", "GET", "CATEGORIES"));
            arr.add(new Permission("Get category with pagination", "/api/v1/categories", "GET", "CATEGORIES"));

            arr.add(new Permission("Create a history", "/api/v1/history", "POST", "HISTORY"));
            arr.add(new Permission("Update a history", "/api/v1/history", "PUT", "HISTORY"));
            arr.add(new Permission("Delete a history", "/api/v1/history/{id}", "DELETE", "HISTORY"));
            arr.add(new Permission("Get a history by id", "/api/v1/history/{id}", "GET", "HISTORY"));
            arr.add(new Permission("Get history with pagination", "/api/v1/history", "GET", "HISTORY"));

            arr.add(new Permission("Create a item", "/api/v1/items", "POST", "ITEMS"));
            arr.add(new Permission("Update a item", "/api/v1/items", "PUT", "ITEMS"));
            arr.add(new Permission("Delete a item", "/api/v1/items/{id}", "DELETE", "ITEMS"));
            arr.add(new Permission("Get a item by id", "/api/v1/items/{id}", "GET", "ITEMS"));
            arr.add(new Permission("Get item with pagination", "/api/v1/items", "GET", "ITEMS"));

            arr.add(new Permission("Create a order", "/api/v1/orders", "POST", "ORDERS"));
            arr.add(new Permission("Update a order", "/api/v1/orders/status", "PUT", "ORDERS"));
            arr.add(new Permission("Delete a order", "/api/v1/orders/{id}", "DELETE", "ORDERS"));
            arr.add(new Permission("Get a order by id", "/api/v1/orders/{id}", "GET", "ORDERS"));
            arr.add(new Permission("Get order with pagination", "/api/v1/orders", "GET", "ORDERS"));


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
            adminRole.setName("ADMIN");
            adminRole.setDescription("Admin thì full permissions");
            adminRole.setActive(true);
            adminRole.setPermissions(allPermissions);
            this.roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName("USER");
            userRole.setDescription("User là khách hàng");
            userRole.setActive(true);
            userRole.setPermissions(allPermissions);
            this.roleRepository.save(userRole);
        }

        if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setName("Quản trị viên");
            adminUser.setActive(true);
            adminUser.setAvatar("avatar.jpg");
            adminUser.setPassword(this.passwordEncoder.encode("Abcd1234"));

            Role adminRole = this.roleRepository.findByName("ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }
            // tao tai khoan admin
            this.userRepository.save(adminUser);


            User user = new User();
            user.setEmail("duong@gmail.com");
            user.setName("Lam Tien Duong");
            user.setActive(true);
            user.setAvatar("avatar.jpg");
            user.setPassword(this.passwordEncoder.encode("Abcd1234"));

            Role userRole = this.roleRepository.findByName("USER");
            if (userRole != null) {
                user.setRole(userRole);
            }
            // tao tai khoan user
            this.userRepository.save(user);
        }

        if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        } else
            System.out.println(">>> END INIT DATABASE");

    }
}
