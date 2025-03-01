package datn.com.cosmetics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import datn.com.cosmetics.entity.Address;
import datn.com.cosmetics.entity.User;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Custom query methods can be added here
    List<Address> findByUser(User user);
}
