package datn.com.cosmetics.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import datn.com.cosmetics.bean.request.ProductRequest;
import datn.com.cosmetics.bean.response.ApiResponse;
import datn.com.cosmetics.entity.Product;
import datn.com.cosmetics.services.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "API for product management")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a new product with the provided details")
    public ResponseEntity<ApiResponse<Product>> createProduct(
            @Parameter(description = "Product request body", required = true) @RequestBody ProductRequest productRequest) {
        Product product = productService.createProduct(productRequest);
        ApiResponse<Product> response = ApiResponse.success(product, "Product created successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Update the details of an existing product by its ID")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id,
            @Parameter(description = "Product request body", required = true) @RequestBody ProductRequest productRequest) {
        Product product = productService.updateProduct(id, productRequest);
        ApiResponse<Product> response = ApiResponse.success(product, "Product updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Delete an existing product by its ID")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id) {
        productService.deleteProduct(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Product deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieve a product by its unique ID")
    public ResponseEntity<ApiResponse<Product>> getProductById(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id) {
        Product product = productService.getProductById(id);
        ApiResponse<Product> response = ApiResponse.success(product, "Product retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all products with optional filters, sorting, and pagination")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts(
            @Parameter(description = "Minimum price", required = false) @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Maximum price", required = false) @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Brand ID", required = false) @RequestParam(required = false) Long brandId,
            @Parameter(description = "Category ID", required = false) @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Sort by field", required = false) @RequestParam(required = false) String sortBy,
            @Parameter(description = "Sort direction", required = false) @RequestParam(required = false) String sortDirection,
            Pageable pageable) {
        Page<Product> products = productService.getAllProducts(minPrice, maxPrice, brandId, categoryId, sortBy,
                sortDirection, pageable);
        ApiResponse.Pagination pagination = new ApiResponse.Pagination(products.getNumber(), products.getTotalPages(),
                products.getTotalElements());
        ApiResponse<List<Product>> response = ApiResponse.success(products.getContent(),
                "Products retrieved successfully", pagination);
        return ResponseEntity.ok(response);
    }

}
