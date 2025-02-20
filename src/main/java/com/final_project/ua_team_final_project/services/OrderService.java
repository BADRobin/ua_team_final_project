package com.final_project.ua_team_final_project.services;

import com.final_project.ua_team_final_project.models.*;
import com.final_project.ua_team_final_project.repositories.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service
@RequiredArgsConstructor
public class OrderService {

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
        order.setUser(user);

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

    public void setSelectedProductsModel(Map<Long, Integer> products, Model model) {

        List<OrderedProduct>  orderedProductList = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : products.entrySet()) {
            AvailableProducts product = availableProductsRepository.findById(entry.getKey()).get();
            OrderedProduct orderedProduct = new OrderedProduct();
            orderedProduct.setOrderedProductId(entry.getKey());
            orderedProduct.setName(product.getName());
            orderedProduct.setProductCode(product.getProductCode());
            orderedProduct.setItemPrice(product.getPrice());
            orderedProduct.setCategory(product.getCategory());
            orderedProduct.setSupplier(product.getSupplier());
            orderedProduct.setAmount(Long.valueOf(entry.getValue()));

            orderedProductList.add(orderedProduct);
        }

        model.addAttribute("orderedProducts", orderedProductList);
    }

    public void setHeadOfDepModel(Model model, Integer urlPageNumber, Integer pageSize, String order, User user) {

        int pageNumber = urlPageNumber - 1;

        Sort.Direction direction = Sort.Direction.ASC;

        if (order.endsWith("desc")) {
            direction = Sort.Direction.DESC;
            order = order.substring(0, order.length() - 5);
        }
        Sort sort = Sort.by(direction, order);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = null;

        try {
            page = orderRepository.findByApprovedByHeadAndApprovedByFinDeptAndStatusNot(false,
                    false, orderStatusRepository.findById(4L).orElse(null), pageable);
        } catch (PropertyReferenceException e) {
            setHeadOfDepModel(model, 1, 10, "orderId", user);
        }
        model.addAttribute("user", user);
        model.addAttribute("pageNumber", urlPageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("orders", page.getContent());
        model.addAttribute("order", sort.toString());
    }
    public List<Order> getOrdersForCurrentUser() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByLogin(username).orElseThrow(() -> new RuntimeException("User not found: " + username));

        return orderRepository.findByDept(user.getDepartment());
    }

    public List<OrderedProduct> getOrderedProductsForOrders(List<Long> orderIds) {
        return orderedProductRepository.findByOrderOrderIdIn(orderIds);
    }

}

