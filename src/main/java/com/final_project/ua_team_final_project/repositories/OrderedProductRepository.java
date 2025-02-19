package com.final_project.ua_team_final_project.repositories;

import com.final_project.ua_team_final_project.models.Category;
import com.final_project.ua_team_final_project.models.OrderedProduct;
import com.final_project.ua_team_final_project.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {
    Optional<OrderedProduct> findByOrderedProductId(Long orderedProductId);
    Optional<OrderedProduct> findByName(String name);
    Optional<OrderedProduct> findByProductCode(String productCode);
    Optional<OrderedProduct> findAllByItemPrice(double itemPrice);
    Optional<OrderedProduct> findAllByCategory(Category category);
    Optional<OrderedProduct> findAllBySupplier(Supplier supplier);
    Optional<OrderedProduct> findAllByAmount(Long amount);
    List<OrderedProduct> findByOrderOrderIdIn(List<Long> orderIds);

}