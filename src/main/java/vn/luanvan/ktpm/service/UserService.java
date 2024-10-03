package vn.luanvan.ktpm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Address;
import vn.luanvan.ktpm.domain.Role;
import vn.luanvan.ktpm.domain.User;
import vn.luanvan.ktpm.domain.response.ResCreateUserDTO;
import vn.luanvan.ktpm.domain.response.ResUpdateUserDTO;
import vn.luanvan.ktpm.domain.response.ResUserDTO;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.repository.AddressRepository;
import vn.luanvan.ktpm.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, RoleService roleService, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.addressRepository = addressRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }
    public User handleGetUser(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        return userOptional.orElse(null);
    }

    public ResultPaginationDTO handleGetAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt =  new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> this.convertToResUserDTO(item))
                .collect(Collectors.toList());

        rs.setResult(listUser);
        return rs;
    }

    public User handleUpdateUser(User user) {
        User userDB = this.handleGetUser(user.getId());
        if (userDB != null) {
            userDB.setName(user.getName());
            userDB.setPhone(user.getPhone());
            userDB.setGender(user.getGender());

            if (user.getAddress() != null) {
                List<Long> listId = user.getAddress().stream()
                        .map(item -> item.getId()).collect(Collectors.toList());

                List<Address> addressList = this.addressRepository.findByIdIn(listId);
                userDB.setAddress(addressList);
            }


            this.userRepository.save(userDB);
        }
        return userDB;
    }

    public void handleDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public Long countUserByRoleName(String roleName) {
        return this.userRepository.countByRole_Name(roleName);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();
        resCreateUserDTO.setId(user.getId());
        resCreateUserDTO.setName(user.getName());
        resCreateUserDTO.setPhone(user.getPhone());
        resCreateUserDTO.setEmail(user.getEmail());
        resCreateUserDTO.setGender(user.getGender());
        return resCreateUserDTO;
    }


    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setPhone(user.getPhone());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        if (user.getAddress() != null) {
            List<Address> addressList = user.getAddress();
            res.setAddressList(addressList);
        }

        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setPhone(user.getPhone());
        res.setGender(user.getGender());
        return res;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

}
