package datn.com.cosmetics.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import datn.com.cosmetics.entity.Address;
import datn.com.cosmetics.entity.User;
import datn.com.cosmetics.repository.AddressRepository;
import datn.com.cosmetics.services.IAddressService;

@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Long id, Address address) {
        if (addressRepository.existsById(id)) {
            address.setId(id);
            return addressRepository.save(address);
        }
        return null;
    }

    @Override
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public List<Address> getAllAddressesByUser(User user) {
        return addressRepository.findByUser(user);
    }
}
