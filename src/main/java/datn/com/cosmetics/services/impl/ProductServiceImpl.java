package datn.com.cosmetics.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import datn.com.cosmetics.bean.request.ProductRequest;
import datn.com.cosmetics.entity.Brand;
import datn.com.cosmetics.entity.Category;
import datn.com.cosmetics.entity.Product;
import datn.com.cosmetics.entity.ProductImage;
import datn.com.cosmetics.repository.BrandRepository;
import datn.com.cosmetics.repository.CategoryRepository;
import datn.com.cosmetics.repository.ProductImageRepository;
import datn.com.cosmetics.repository.ProductRepository;
import datn.com.cosmetics.services.IProductService;
import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductRequest productRequest) {
        validateProductRequest(productRequest);
        Product product = new Product();
        mapProductRequestToProduct(productRequest, product);
        product = productRepository.save(product);
        saveProductImages(productRequest, product);
        return product;
    }

    @Transactional
    @Override
    public Product updateProduct(Long id, ProductRequest productRequest) {
        validateProductRequest(productRequest);
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        mapProductRequestToProduct(productRequest, product);
        updateProductImages(productRequest, product);
        productRepository.save(product);
        // Refresh the product entity to ensure updated data is returned
        product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Page<Product> getAllProducts(Double minPrice, Double maxPrice, Long brandId, Long categoryId, String sortBy,
            String sortDirection, Pageable pageable) {
        if (minPrice != null || maxPrice != null || brandId != null || categoryId != null) {
            return productRepository.findByFilters(minPrice, maxPrice, brandId, categoryId, sortBy, sortDirection,
                    pageable);
        }
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getProductsByFilter(Double minPrice, Double maxPrice, Long brandId, Long categoryId,
            Pageable pageable) {
        return productRepository.findByPriceBetweenAndBrandIdAndCategoryId(minPrice, maxPrice, brandId, categoryId,
                pageable);
    }

    @Override
    public List<Product> getNewArrivals() {
        return productRepository.findNewArrivals();
    }

    @Override
    public List<Product> getTopSellingProducts() {
        return productRepository.findTopSellingProducts();
    }

    @Override
    public List<Product> getTopDiscountedProducts() {
        return productRepository.findTopDiscountedProducts();
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    private void mapProductRequestToProduct(ProductRequest productRequest, Product product) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setSalePrice(productRequest.getSalePrice());
        product.setStock(productRequest.getStock());
        product.setIngredients(productRequest.getIngredients());
        product.setProductUsage(productRequest.getProductUsage());
        product.setStatus(productRequest.getStatus());

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        Brand brand = brandRepository.findById(productRequest.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        product.setBrand(brand);

    }

    private void validateProductRequest(ProductRequest productRequest) {
        if (productRequest.getName() == null || productRequest.getName().isEmpty()) {
            throw new RuntimeException("Product name is required");
        }
        if (productRequest.isSale() && productRequest.getSalePrice() == null) {
            throw new RuntimeException("Product price must be greater than zero");
        }
        if (productRequest.getCategoryId() == null) {
            throw new RuntimeException("Category ID is required");
        }
        if (productRequest.getBrandId() == null) {
            throw new RuntimeException("Brand ID is required");
        }
    }

    private void saveProductImages(ProductRequest productRequest, Product product) {
        if (productRequest.getImages() != null && !productRequest.getImages().isEmpty()) {
            for (String imageUrl : productRequest.getImages()) {
                ProductImage productImage = new ProductImage();
                productImage.setImage(imageUrl);
                productImage.setProduct(product);
                productImageRepository.save(productImage);
            }
        }
    }

    private void updateProductImages(ProductRequest productRequest, Product product) {
        // Xóa tất cả ảnh cũ của sản phẩm
        productImageRepository.deleteByProductId(product.getId());

        // Thêm danh sách ảnh mới từ request
        if (productRequest.getImages() != null && !productRequest.getImages().isEmpty()) {
            for (String imageUrl : productRequest.getImages()) {
                ProductImage productImage = new ProductImage();
                productImage.setImage(imageUrl);
                productImage.setProduct(product);
                productImageRepository.save(productImage);
            }
        }
    }
    

}
