package com.final_project.ua_team_final_project.services;

import com.final_project.ua_team_final_project.models.*;
import com.final_project.ua_team_final_project.repositories.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    @Autowired
    private EntityManager entityManager;

    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final AvailableProductsRepository availableProductsRepository;
    private final OrderStatusRepository orderStatusRepository;

    private final OrderRepository orderRepository;
    private final OrderedProductRepository orderedProductRepository;

    public OrderService(UserRepository userRepository, SupplierRepository supplierRepository, CategoryRepository categoryRepository, AvailableProductsRepository availableProductsRepository, OrderStatusRepository orderStatusRepository, OrderRepository orderRepository, OrderedProductRepository orderedProductRepository) {
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
        this.categoryRepository = categoryRepository;
        this.availableProductsRepository = availableProductsRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.orderRepository = orderRepository;
        this.orderedProductRepository = orderedProductRepository;
    }

    @Transactional
    public void saveOrder(Order order, List<OrderedProduct> orderedProducts) {
        orderRepository.save(order);


    }

    @Transactional
    public void saveNewOrder(Map<Long, Long> orderedProducts) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Department department = user.getDepartment();

        Order order = new Order();
        OrderStatus orderStatus = orderStatusRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("OrderStatus not found: 1"));
        order.setDept(department);
        order.setStatus(orderStatus);
        order.setApprovedByHead(false);
        order.setApprovedByFinDept(false);

        double totalPrice = 0.0;
        List<OrderedProduct> orderedProductList = new ArrayList<>();
        orderRepository.save(order);
        for (Map.Entry<Long, Long> entry : orderedProducts.entrySet()) {

            Long productId = entry.getKey();
            Long quantity = entry.getValue();

            AvailableProducts product = availableProductsRepository
                    .findById(productId).orElseThrow(() -> new RuntimeException("Product not found " + productId));
            Supplier supplierId = product.getSupplier();
            if (supplierId == null) {
                throw new RuntimeException("Supplier ID not found for product: " + productId);
            }

            Supplier supplier = supplierRepository.findById(supplierId.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found: " + supplierId));

            Category categoryId = product.getCategory();
            if (categoryId == null) {
                throw new RuntimeException("Category ID not found for product: " + productId);
            }

            Category category = categoryRepository.findById(categoryId.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));

            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());

            OrderedProduct orderedProduct = new OrderedProduct();
//            orderedProduct.setOrderedProductId(1L);
            orderedProduct.setOrder(order);
            orderedProduct.setName(product.getName());
            orderedProduct.setProductCode(product.getProductCode());
            orderedProduct.setItemPrice(product.getPrice());
            orderedProduct.setCategory(category);
            orderedProduct.setSupplier(supplier);
            orderedProduct.setAmount(quantity);

            order.setTotalPrice(order.getTotalPrice() + orderedProduct.getItemPrice());
            System.out.println("Order saved: " + order);
            System.out.println("Order saved: " + orderedProduct);
//            order.addOrderedProduct(orderedProduct);
            orderedProductList.add(orderedProduct);
            double itemTotal = product.getPrice() * quantity;

            totalPrice += itemTotal;


        }

        orderedProductRepository.saveAll(orderedProductList);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);


    }

    public boolean setSelectedProductsModel(List<Long> selectedProducts, Map<String, String> quantities, Model model) {
        if (selectedProducts == null || selectedProducts.isEmpty()) {
            return true;
        }

        List<OrderedProduct> orderedProducts = new ArrayList<>();

        for (Long productId : selectedProducts) {
            String quantityStr = quantities.get("quantities[" + productId + "]");
            if (quantityStr != null && !quantityStr.isEmpty()) {
                AvailableProducts product = availableProductsRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

                OrderedProduct orderedProduct = new OrderedProduct();
                orderedProduct.setOrderedProductId(product.getProductId());
                orderedProduct.setProductCode(product.getProductCode());
                orderedProduct.setName(product.getName());
                orderedProduct.setItemPrice(product.getPrice());
                orderedProduct.setAmount((long) Integer.parseInt(quantityStr));
                orderedProducts.add(orderedProduct);
            }
        }

        if (orderedProducts.isEmpty()) {
            return true;
        }

        model.addAttribute("orderedProducts", orderedProducts);
        return false;
    }

    public List<Order> getOrdersForCurrentUser() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByLogin(username).orElseThrow(() -> new RuntimeException("User not found: " + username));

        return orderRepository.findByDept(user.getDepartment());
    }

}

