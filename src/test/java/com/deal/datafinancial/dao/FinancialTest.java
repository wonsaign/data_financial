package com.deal.datafinancial.dao;

import com.alibaba.fastjson.JSON;

import com.deal.datafinancial.DataFinancialApplicationTest;
import com.deal.datafinancial.model.DataApportion;
import com.deal.datafinancial.model.DataChannel;
import com.deal.datafinancial.model.DataCustomer;
import com.deal.datafinancial.model.DataFinancial;
import com.deal.datafinancial.model.DataMaterialPrice;
import com.deal.datafinancial.model.dto.DataFinancialDTO;
import com.deal.datafinancial.model.dto.DataFinancialItemWeightDTO;
import com.deal.datafinancial.service.DealDataImpl;
import com.google.common.collect.Lists;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringJUnitConfig(value= DataFinancialApplicationTest.class)
public class FinancialTest {

    private static final Logger logger = LoggerFactory.getLogger(FinancialTest.class);

    ExecutorService executor = Executors.newFixedThreadPool(10);

    private final static int deal_month = 202206;
    private final static int deal_month_channel = 20221;

    @Autowired
    ReportFinancialDAO reportFinancialDAO;

    @Autowired
    DealDataImpl impl;

    @Test
    public void testGetAllChannels(){
        List<DataChannel> d = reportFinancialDAO.getAllChannels();
        System.out.println(JSON.toJSONString(d, true));
    }

      @Test
    public void testGetAllMaterialPrices(){
        List<DataMaterialPrice> d = reportFinancialDAO.getAllMaterialPricesByDate(202206);
        System.out.println(JSON.toJSONString(d, true));
    }

    @Test
    public void testGetAllCustomers(){
        List<DataCustomer> d = reportFinancialDAO.getAllCustomersByDate(202206);
        System.out.println(JSON.toJSONString(d, true));
    }

    @Test
    public void testGetCustomer(){
        DataCustomer d = reportFinancialDAO.getCustomersByDateAndCode(202206,"00150001").get(0);
        System.out.println(JSON.toJSONString(d, true));
    }

    @Test
    public void testGetAllFinancial(){
        List<DataFinancial> d = reportFinancialDAO.getAllFinancial();
        List<DataFinancial> d2 = reportFinancialDAO.getFinancialByIds(419030,419034);
        System.out.println(JSON.toJSONString(d, true));
        System.err.println(JSON.toJSONString(d2, true));
    }

    @Test
    public void testPriceGini(){
        String gini = reportFinancialDAO.getGini(202206
                ,"010630276");
        System.out.println(gini);
    }

    @Test
    public void testGetCondition(){
        DataChannel d = reportFinancialDAO.getByCondi("!",
                "","子公司门店",20221);
        System.out.println(JSON.toJSONString(d, true));
    }

    @Test
    public void testGetApportion(){
        List<DataApportion> d = reportFinancialDAO.getApportion("9010104",
                "02030000011","",2022,"01");
        System.out.println(JSON.toJSONString(d, true));
    }

    @Test
    public void testUpdateOne(){
        // 419034 没找到
        int b = 39171;
        impl.dealOne(b,b);
    }

    @Test
    public void testGetMaxId(){
        long end = reportFinancialDAO.getMaxFinancialId() + 1;
        System.out.println(end);

    }

    @Test
    public void testGetAllFinancialGroupByCusCodeAndChannel(){
        List<DataFinancialDTO> d = reportFinancialDAO.getAllFinancialGroupByCusCodeAndChannel();
        System.out.println(JSON.toJSONString(d.get(0), true));
    }

    @Test
    public void testGetAllWeight(){
        List<DataFinancialItemWeightDTO> d = impl.getAllWeight();
        System.out.println(JSON.toJSONString(d, true));
    }

    @Test
    public void testUpdate()  {

        int begin = 1;
        int gap = 10000;
        long end = reportFinancialDAO.getMaxFinancialId() + 1;
        List<Future<?>> f = Lists.newArrayList();
        while (begin < end){
            int finalBegin = begin;
            logger.info("work begin:{},end:{}",finalBegin,finalBegin + gap);
            Future<?> submit = executor.submit(() -> impl.updateFinancial(finalBegin, finalBegin + gap));
            f.add(submit);
            begin = begin + gap;
        }
        for (Future<?> future : f) {
            try {
                future.get();
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }


    @Test
    public void testUpdateApp()  {

        int begin = 1;
        int gap = 10000;
        long end = reportFinancialDAO.getMaxFinancialId() + 1;
        Map<Integer, String> idWeight = impl.getAllWeight().stream().collect(Collectors.toMap(DataFinancialItemWeightDTO::getId, DataFinancialItemWeightDTO::getWeight, (v1, v2) -> v2));
        List<Future<?>> f = Lists.newArrayList();
        while (begin < end){
            int finalBegin = begin;
            logger.info("work begin:{},end:{}",finalBegin,finalBegin + gap);
            Future<?> submit = executor.submit(() -> impl.updateFinancialApp(finalBegin, finalBegin + gap, idWeight));
            f.add(submit);
            begin = begin + gap;
        }
        for (Future<?> future : f) {
            try {
                future.get();
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        // 导出丢失的
        impl.exportLost();
    }

    @Test
    public void testUpdateOneApp(){
        // 419034 没找到
        Map<Integer, String> idWeight = impl.getAllWeight().stream().collect(Collectors.toMap(DataFinancialItemWeightDTO::getId, DataFinancialItemWeightDTO::getWeight, (v1, v2) -> v2));
        int b = 723347;
        impl.updateFinancialApp(b,b,idWeight);
    }

}
