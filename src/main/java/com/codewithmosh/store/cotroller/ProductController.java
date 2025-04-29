package com.codewithmosh.store.cotroller;

import com.codewithmosh.store.dto.ProductDto;
import com.codewithmosh.store.dto.UserDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
 private final ProductRepository productRepository;
 private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping("")
    public List<ProductDto> getAllProducts(@RequestParam (required = false, defaultValue = "", name = "categoryId") Byte categoryId) {
     var products = productRepository.findAll();
     if (categoryId != null) {
         return products.stream()
                 .filter(product -> product.getCategory() != null && categoryId.equals(product.getCategory().getId()))
                 .toList().stream().map(productMapper::toDto).collect(Collectors.toList());
     }

     return products.stream()
                .map(productMapper::toDto)
                .toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProducts(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
          return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }
    @PostMapping("")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto
            ,UriComponentsBuilder uriBuilder) {
   var category =   categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
       productRepository.save(product);
         productDto.setId(product.getId());
        var uri  =      uriBuilder.path("/products/{id}")
                .buildAndExpand(productDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(productDto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
                                                    @RequestBody ProductDto productDto) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
         productMapper.update(productDto, product);
            product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
         return  ResponseEntity.ok(productMapper.toDto(product));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }


}
