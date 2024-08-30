package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.ResCreateUserDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("users")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User user) throws IdInvalidException {
        boolean isEmailExist = this.userService.isEmailExist(user.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email " + user.getEmail() + " da ton tai, vui long su dung email khac");
        }

        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User localUser = userService.handleCreateUser(user);

        ResCreateUserDTO resCreateUserDTO = this.userService.convertToResCreateUserDTO(localUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateUserDTO);
    }

    @PutMapping("users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        User updateUser = this.userService.handleUpdateUser(user);
        if (updateUser == null) {
            throw new IdInvalidException("User voi id = " + user.getId() + " khong ton tai");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(updateUser));
    }


    @GetMapping("users/{id}")
    @ApiMessage("Fetch user by id")
    public ResponseEntity<ResUserDTO> getUser(@PathVariable Long id) throws IdInvalidException  {
        User user = this.userService.handleGetUser(id);
        if (user == null) {
            throw new IdInvalidException("User voi id = " + id + " khong ton tai");
        }
        ResUserDTO resUserDTO = new ResUserDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(resUserDTO);
    }

    @GetMapping("users")
    @ApiMessage("Fetch all user")
    public ResponseEntity<ResultPaginationDTO>  getAllUser(
            @Filter Specification<User> spec,
            Pageable pageable
            ) {
        ResultPaginationDTO resultPaginationDTO = this.userService.handleGetAllUser(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }

    @DeleteMapping("users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) throws IdInvalidException {
        User user = this.userService.handleGetUser(id);
        if (user == null) {
            throw new IdInvalidException(
                    "User voi id = " + id + " khong ton tai"
            );
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}