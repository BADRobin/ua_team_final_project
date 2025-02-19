package com.final_project.ua_team_final_project.controllers;

import com.final_project.ua_team_final_project.models.OrderedProduct;
import com.final_project.ua_team_final_project.services.SupplierOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SupplierOrderController {

    private final SupplierOrderService supplierOrderService;

    @PostMapping("/generateCsv")
    public ResponseEntity<List<String>> generateCsv(@RequestParam List<Long> selectedOrders) {
        Map<Long, List<OrderedProduct>> orderedProducts = supplierOrderService.processOrders(selectedOrders);
        List<String> files = supplierOrderService.generateAndUploadCsv(orderedProducts);
        return ResponseEntity.ok(files);
    }
}

