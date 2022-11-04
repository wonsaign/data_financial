package com.deal.datafinancial.model;

import lombok.Data;

@Data
public class DataCustomer {
    private String orgBelong;

    private Integer dateInt;

    private String orgCode;

    private String cusName;

    private String cusType;

    private String taxRate;
}