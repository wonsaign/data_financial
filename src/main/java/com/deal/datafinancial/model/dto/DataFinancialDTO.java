package com.deal.datafinancial.model.dto;

import lombok.Data;

import java.util.List;


@Data
public class DataFinancialDTO {

    private String nums;

    private String total;

    private String absTotal;

    private List<String> details;

    private List<Integer> ids;

}