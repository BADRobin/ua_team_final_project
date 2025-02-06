package com.final_project.ua_team_final_project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "ordered_products")
public class OrderedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ordered_product_id", nullable = false, updatable = false)
    private Long orderedProductId;

    @ManyToOne
    @JoinColumn(name = "orders_order_id", nullable = false)
    private Order order;

    private String name;
    private String productCode;

    @Column(nullable = false)
    private Double itemPrice;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category categoryId;
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplierId;

    @Column(nullable = false)
    private Long amount;

    public OrderedProduct() {
    }

    public OrderedProduct(Long orderedProductId, Order order, String name, String productCode, Double itemPrice, Category categoryId, Supplier supplierId, Long amount) {
        this.orderedProductId = orderedProductId;
        this.order = order;
        this.name = name;
        this.productCode = productCode;
        this.itemPrice = itemPrice;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OrderedProduct that)) return false;
        return Objects.equals(orderedProductId, that.orderedProductId) && Objects.equals(order, that.order) && Objects.equals(name, that.name) && Objects.equals(productCode, that.productCode) && Objects.equals(itemPrice, that.itemPrice) && Objects.equals(categoryId, that.categoryId) && Objects.equals(supplierId, that.supplierId) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderedProductId, order, name, productCode, itemPrice, categoryId, supplierId, amount);
    }

    @Override
    public String toString() {
        return "OrderedProduct{" +
                "orderedProductId=" + orderedProductId +
                ", order=" + order +
                ", name='" + name + '\'' +
                ", productCode='" + productCode + '\'' +
                ", itemPrice=" + itemPrice +
                ", categoryId=" + categoryId +
                ", supplierId=" + supplierId +
                ", amount=" + amount +
                '}';
    }
}
