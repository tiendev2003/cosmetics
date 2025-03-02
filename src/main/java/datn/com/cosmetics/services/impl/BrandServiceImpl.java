package datn.com.cosmetics.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import datn.com.cosmetics.bean.request.BrandRequest;
import datn.com.cosmetics.entity.Brand;
import datn.com.cosmetics.exceptions.DuplicateResourceException;
import datn.com.cosmetics.exceptions.ResourceNotFoundException;
import datn.com.cosmetics.exceptions.ValidationException;
import datn.com.cosmetics.repository.BrandRepository;
import datn.com.cosmetics.services.IBrandService;

@Service
public class BrandServiceImpl implements IBrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Brand createBrand(BrandRequest brandRequest) {
        if (brandRepository.existsByName(brandRequest.getName())) {
            throw new DuplicateResourceException("Brand already exists with this name");
        }
        validateBrand(brandRequest);
        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brand.setDescription(brandRequest.getDescription());
        brand.setImage(brandRequest.getImage());
        brand.setStatus(brandRequest.getStatus());
        return brandRepository.save(brand);
    }

    @Override
    public Brand updateBrand(Long id, BrandRequest brandRequest) {
        Optional<Brand> existingBrand = brandRepository.findById(id);
        if (existingBrand.isPresent()) {
            validateBrand(brandRequest);
            Brand updatedBrand = existingBrand.get();
            updatedBrand.setName(brandRequest.getName());
            updatedBrand.setDescription(brandRequest.getDescription());
            updatedBrand.setImage(brandRequest.getImage());
            updatedBrand.setStatus(brandRequest.getStatus());
            return brandRepository.save(updatedBrand);
        }
        throw new ResourceNotFoundException("Brand not found");
    }

    private void validateBrand(BrandRequest brandRequest) {
        if (brandRequest.getName().isEmpty() || brandRequest.getDescription().isEmpty() ||
            brandRequest.getImage().isEmpty() || brandRequest.getStatus() == null) {
            throw new ValidationException("All fields are required");
        }
        // Add more validation logic as needed
    }

    @Override
    public void deleteBrand(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new ResourceNotFoundException("Brand not found");
        }
        brandRepository.deleteById(id);
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Page<Brand> getAllBrands(String name, Pageable pageable) {
        if (name != null && !name.isEmpty()) {
            return brandRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        return brandRepository.findAll(pageable);
    }
}
