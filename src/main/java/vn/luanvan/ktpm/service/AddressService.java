package vn.luanvan.ktpm.service;

import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Address;
import vn.luanvan.ktpm.domain.User;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.domain.response.address.ResAddressDTO;
import vn.luanvan.ktpm.domain.response.address.ResUpdateAddressDTO;
import vn.luanvan.ktpm.repository.AddressRepository;
import vn.luanvan.ktpm.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public Address create(Address address) {
        if (address.getUser() != null) {
            Optional<User> userOptional = userRepository.findById(address.getUser().getId());
            // neu nguoi dung ton tai
            if (userOptional.isPresent()) {
                // lay ra nguoi dung
                User userDB = this.userRepository.findByEmail(userOptional.get().getEmail());
                address.setUser(userDB);
                this.addressRepository.save(address);
//                List<Address> addressList = new ArrayList<>();
//                addressList.add(address);
//                userDB.setAddress(addressList);
//                this.userRepository.save(userDB);
            }
        }
        return address;
    }

    public Address findById(long id) {
        Optional<Address> addressOptional = this.addressRepository.findById(id);
        return addressOptional.orElse(null);
    }

    public ResUpdateAddressDTO update(Address address) {
        Address addressDB = this.addressRepository.save(address);

        ResUpdateAddressDTO res = new ResUpdateAddressDTO();
        res.setId(addressDB.getId());
        res.setName(addressDB.getName());
        return res;
    }

    public void delete(long id) {
        this.addressRepository.deleteById(id);
    }

    public List<ResAddressDTO> findByUser(User user) {
        List<Address> addressList = this.addressRepository.findByUser(user);

        List<ResAddressDTO> res = addressList.stream()
                .map(item -> new ResAddressDTO(item.getId(), item.getName(), item.getPhone(),
                        new ResAddressDTO.AddressUser(item.getUser().getId(), item.getUser().getName())))
                .collect(Collectors.toList());

        return res;
    }

    public ResAddressDTO convertToResAddressDTO(Address address) {
        ResAddressDTO res = new ResAddressDTO();
        res.setId(address.getId());
        res.setName(address.getName());
        res.setPhone(address.getPhone());
        if (address.getUser() != null) {
            ResAddressDTO.AddressUser addressUser = new ResAddressDTO.AddressUser();
            addressUser.setId(address.getUser().getId());
            addressUser.setName(address.getUser().getName());
            res.setUser(addressUser);
        }
        return res;
    }
}
