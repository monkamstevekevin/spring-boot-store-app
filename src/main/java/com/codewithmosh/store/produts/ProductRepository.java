package com.codewithmosh.store.produts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    <T> Optional<T> findByCategory_Id(Byte categoryId);
}