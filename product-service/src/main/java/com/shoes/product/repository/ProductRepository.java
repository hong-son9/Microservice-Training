package com.shoes.product.repository;

import com.shoes.product.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findBySku(String sku);


    Optional<Product> findByName(String name);

    @EntityGraph(attributePaths = {"brand", "categories"})
    Optional<Product> findById(Long id);

    @EntityGraph(attributePaths = {"brand", "categories"})
    List<Product> findAll();
    @EntityGraph(attributePaths = {"brand", "categories"})
    List<Product> findByIdIn(List<Long> ids);

    boolean existsBySku(String sku);

    boolean existsByName(String name);
}
