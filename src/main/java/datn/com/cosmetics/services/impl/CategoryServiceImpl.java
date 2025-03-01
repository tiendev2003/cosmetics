package datn.com.cosmetics.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import datn.com.cosmetics.bean.request.CategoryRequest;
import datn.com.cosmetics.entity.Category;
import datn.com.cosmetics.repository.CategoryRepository;
import datn.com.cosmetics.services.ICategoryService;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setImage(categoryRequest.getImage());
        category.setStatus(categoryRequest.getStatus());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, CategoryRequest categoryRequest) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            Category updatedCategory = existingCategory.get();
            updatedCategory.setName(categoryRequest.getName());
            updatedCategory.setDescription(categoryRequest.getDescription());
            updatedCategory.setImage(categoryRequest.getImage());
            updatedCategory.setStatus(categoryRequest.getStatus());
            return categoryRepository.save(updatedCategory);
        }
        return null;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> getAllCategories(String name, Pageable pageable) {
        if (name != null && !name.isEmpty()) {
            return categoryRepository.findByNameContaining(name, pageable);
        }
        return categoryRepository.findAll(pageable);
    }
}
