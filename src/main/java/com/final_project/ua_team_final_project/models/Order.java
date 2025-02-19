package com.final_project.ua_team_final_project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "dept_id", nullable = false)
    private Department dept;

    @Column(name = "total_price")
    private double totalPrice;

    private boolean approvedByHead;
    private boolean approvedByFinDept;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderedProduct> orderedProducts = new ArrayList<>();

    public Order() { }

    public Order(Long orderId, Department dept, double totalPrice, boolean approvedByHead, boolean approvedByFinDept, LocalDateTime createdAt, LocalDateTime updatedAt, OrderStatus status, List<OrderedProduct> orderedProducts) {
        this.orderId = orderId;
        this.dept = dept;
        this.totalPrice = totalPrice;
        this.approvedByHead = approvedByHead;
        this.approvedByFinDept = approvedByFinDept;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.orderedProducts = orderedProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order order)) return false;
        return Double.compare(totalPrice, order.totalPrice) == 0 && approvedByHead == order.approvedByHead && approvedByFinDept == order.approvedByFinDept && Objects.equals(orderId, order.orderId) && Objects.equals(dept, order.dept) && Objects.equals(createdAt, order.createdAt) && Objects.equals(updatedAt, order.updatedAt) && Objects.equals(status, order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, dept, totalPrice, approvedByHead, approvedByFinDept, createdAt, updatedAt, status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", deptId=" + dept +
                ", totalPrice=" + totalPrice +
                ", approvedByHead=" + approvedByHead +
                ", approvedByFinDept=" + approvedByFinDept +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status=" + status +
                '}';
    }
}
