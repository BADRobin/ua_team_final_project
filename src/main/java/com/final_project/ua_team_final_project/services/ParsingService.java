package com.final_project.ua_team_final_project.services;

import com.final_project.ua_team_final_project.models.AvailableProducts;
import com.final_project.ua_team_final_project.models.Category;
import com.final_project.ua_team_final_project.models.Supplier;
import com.final_project.ua_team_final_project.repositories.CategoryRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ParsingService {

    private final CategoryRepository categoryRepository;

    public List<AvailableProducts> parseCsv(InputStream inputStream, Supplier supplier) {
        List<AvailableProducts> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVReader csvReader = new CSVReader(reader)) {

            String[] headers = csvReader.readNext();
            if (headers == null) {
                throw new IllegalArgumentException("Empty CSV file");
            }

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                AvailableProducts product = new AvailableProducts();
                product.setProductCode(line[0]);
                product.setName(line[1]);
                product.setDescription(line[2]);
                if (categoryRepository.findByName(line[3]).isEmpty()) {
                    categoryRepository.save(new Category(line[3]));
                }
                product.setCategory(categoryRepository.findByName(line[3]).get());
                product.setSupplier(supplier);
                product.setPrice(Double.parseDouble(line[4]));

                products.add(product);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return products;
    }

}

