package com.final_project.ua_team_final_project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private List<Long> productIds;
    private List<Integer> quantities;
}
