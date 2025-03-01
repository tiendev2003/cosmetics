package datn.com.cosmetics.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import datn.com.cosmetics.bean.request.BrandRequest;
import datn.com.cosmetics.entity.Brand;
import datn.com.cosmetics.repository.BrandRepository;
import datn.com.cosmetics.services.IBrandService;

@Service
public class BrandServiceImpl implements IBrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Brand createBrand(BrandRequest brandRequest) {
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
            Brand updatedBrand = existingBrand.get();
            updatedBrand.setName(brandRequest.getName());
            updatedBrand.setDescription(brandRequest.getDescription());
            updatedBrand.setImage(brandRequest.getImage());
            updatedBrand.setStatus(brandRequest.getStatus());
            return brandRepository.save(updatedBrand);
        }
        return null;
    }

    @Override
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.findById(id).orElse(null);
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
