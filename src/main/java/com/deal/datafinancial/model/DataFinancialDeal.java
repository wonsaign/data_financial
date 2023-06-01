package com.deal.datafinancial.model;

import lombok.Data;

@Data
public class DataFinancialDeal {
    private Integer id;

    private String orgCode;

    private String orgName;

    private Integer dateInt;

    private String ordertrantype;

    private String name;

    private String cusCode;

    private String materialcode;

    private String materialname;

    private String nnum;

    private String norigmny;

    private String ncaltaxmny;

    private String channel;

    private String priceGini;

    private String discountReduction;

    private String taxRate;

    private String reportIncome;

    private String cusType;

    private String appScly;

    private String appDsfx;

    private String appDszy;

    private String appMdjm;

    private String appDyks;

    private String appFdfl;

    private String appHyjf;

    private String appTotal;

    private String originSuitCode;

    private String originSuitName;

}