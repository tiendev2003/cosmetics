package datn.com.cosmetics.services;

import datn.com.cosmetics.entity.Address;
import datn.com.cosmetics.entity.User;

import java.util.List;

public interface IAddressService {
    Address createAddress(Address address);
    Address updateAddress(Long id, Address address);
    void deleteAddress(Long id);
    Address getAddressById(Long id);
    List<Address> getAllAddresses();
    List<Address> getAllAddressesByUser(User user);
}
