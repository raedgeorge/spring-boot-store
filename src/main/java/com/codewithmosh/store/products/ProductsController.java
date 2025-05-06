package com.codewithmosh.store.products;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(required = false, name = "categoryId") Byte categoryId){

        List<Product> products;

        products = categoryId == null ? productRepository.findAllWithCategory() :
                                        productRepository.findAllByCategoryId(categoryId);

        var productsDtos = products.stream().map(productMapper::toDto).toList();

        return ResponseEntity.ok(productsDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id){

        var product = productRepository.findById(id).orElse(null);

        return product == null ? ResponseEntity.notFound().build() :
                                 ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto data,
            UriComponentsBuilder uriBuilder){

        var category = categoryRepository.findById(data.getCategoryId()).orElse(null);
        if (category == null)
            return ResponseEntity.badRequest().build();

        var product = productMapper.toEntity(data);
        product.setCategory(category);
        productRepository.save(product);

        var productDto = productMapper.toDto(product);

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();

        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable (name = "id") Long id,
            @RequestBody ProductDto productDto
    ){

        var product = productRepository.findById(id).orElse(null);
        if (product == null)
            return ResponseEntity.notFound().build();

        if (product.getCategory().getId() != productDto.getCategoryId()){

            var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
            if (category == null)
                return ResponseEntity.badRequest().build();

            product.setCategory(category);
        }

        productMapper.update(productDto, product);
        productRepository.save(product);
        productDto.setId(product.getId());

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable (name = "id") Long id){

        var product = productRepository.findById(id).orElse(null);
        if (product == null)
            return ResponseEntity.notFound().build();

        productRepository.delete(product);

        return ResponseEntity.noContent().build();
    }
}
