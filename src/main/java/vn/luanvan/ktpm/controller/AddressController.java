package vn.luanvan.ktpm.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.domain.Address;
import vn.luanvan.ktpm.domain.User;
import vn.luanvan.ktpm.domain.response.address.ResAddressDTO;
import vn.luanvan.ktpm.domain.response.address.ResUpdateAddressDTO;
import vn.luanvan.ktpm.service.AddressService;
import vn.luanvan.ktpm.service.UserService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;
import vn.luanvan.ktpm.util.error.CustomizeException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AddressController {
    private final AddressService addressService;
    private final UserService userService;


    public AddressController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    @PostMapping("/addresses")
    @ApiMessage("Create a new address")
    public ResponseEntity<Address> createAddress(@Valid @RequestBody Address address) {
        Address addressDB = this.addressService.create(address);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressDB);
    }

    @GetMapping("/addresses/{id}")
    @ApiMessage("Get a address")
    public ResponseEntity<ResAddressDTO> findAddressById(@PathVariable long id) throws CustomizeException {
        Address addressDB = this.addressService.findById(id);
        if (addressDB == null) {
            throw new CustomizeException("Address voi id =" + id + " khong ton tai");
        }
        ResAddressDTO res = this.addressService.convertToResAddressDTO(addressDB);
        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

    @GetMapping("/addresses")
    @ApiMessage("Get all address by email")
    public ResponseEntity<List<ResAddressDTO>> findAllAddressByEmail(@RequestBody User user) {
        List<ResAddressDTO> res = this.addressService.findByUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/addresses")
    @ApiMessage("Update a address")
    public ResponseEntity<Address> updateAddress(@RequestBody Address address) throws CustomizeException {
        Address addressDB = this.addressService.findById(address.getId());
        if (addressDB == null) {
            throw new CustomizeException("Address voi id = " + address.getId() + " khong ton tai");
        }
        Address res = this.addressService.update(address);
        return  ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/addresses/{id}")
    @ApiMessage("Delete a address")
    public ResponseEntity<Void> deleteAddress(@PathVariable long id) throws CustomizeException {
        Address address = this.addressService.findById(id);
        if (address == null) {
            throw new CustomizeException("Address voi id = " + id + " khong ton tai");
        }
        this.addressService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
