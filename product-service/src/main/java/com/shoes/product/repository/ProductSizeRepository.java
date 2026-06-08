package com.shoes.product.repository;

import com.shoes.product.entity.ProductSize;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {
    @Query("SELECT ps FROM ProductSize ps WHERE ps.product.id = :productId AND ps.sizeVn = :sizeVn")
    Optional<ProductSize> findByProductIdAndSizeVn(
            @Param("productId") Long productId,
            @Param("sizeVn") Integer sizeVn
    );
}
