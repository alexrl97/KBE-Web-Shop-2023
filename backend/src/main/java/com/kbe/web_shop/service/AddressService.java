package com.kbe.web_shop.service;

import com.kbe.web_shop.dto.user.SignUpDto;
import com.kbe.web_shop.model.Address;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.AddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    AddressRepo addressRepository;

    public Address getAddressFromSignUpDto(SignUpDto signupDto, User user){
        Address address = new Address();
        address.setFirstName(signupDto.getFirstName());
        address.setLastName(signupDto.getLastName());
        address.setCompany(signupDto.getCompany());
        address.setStreet(signupDto.getStreet());
        address.setHouseNumber(signupDto.getHouseNumber());
        address.setCity(signupDto.getCity());
        address.setZip(signupDto.getZip());
        address.setCountry(signupDto.getCountry());
        address.setUser(user);
        return address;
    }

    public void createUpdateAddress(Address address){
        Address updatedAddress = new Address();
        address.setId(updatedAddress.getId());
        address.setCreatedDate(updatedAddress.getCreatedDate());
        addressRepository.save(address);
    }

    public Address getLatestAdressForUser(User user){
        return addressRepository.findTopByUserOrderByCreatedDateDesc(user);
    }
}
