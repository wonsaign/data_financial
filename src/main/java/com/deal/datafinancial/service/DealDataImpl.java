package com.deal.datafinancial.service;

import com.alibaba.fastjson.JSON;
import com.deal.datafinancial.dao.ReportFinancialDAO;
import com.deal.datafinancial.model.DataApportion;
import com.deal.datafinancial.model.DataChannel;
import com.deal.datafinancial.model.DataCustomer;
import com.deal.datafinancial.model.DataFinancial;
import com.deal.datafinancial.model.DataFinancialDeal;
import com.deal.datafinancial.model.DataFinancialSuit;
import com.deal.datafinancial.model.DataMaterialPrice;
import com.deal.datafinancial.model.dto.DataFinancialDTO;
import com.deal.datafinancial.model.dto.DataFinancialItemWeightDTO;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * All rights Reserved, Designed By www.drplant.com.cn
 *
 * @ProjectName: data-financial
 * @Package: com.deal.datafinancial.service
 * @ClassName: DealDataImpl
 * @Description: []
 * @Author: [wang sai]
 * @Date: 2022/11/2 15:30
 * @Version: V1.0
 * @Copyright: 2019 www.drplant.com.cn Inc. All rights reserved.
 **/
@Service
public class DealDataImpl {

    private static final Logger logger = LoggerFactory.getLogger(DealDataImpl.class);

    private final static int deal_year_month_custom = 20214;
    // 不变
    private final static int deal_year_month_channel = 20214;

    private final static int deal_year = 2020;
    private final static String deal_month = "1";

    private final static Set<Integer> appsUsedIds = Sets.newHashSet();

    @Autowired
    ReportFinancialDAO reportFinancialDAO;

    public void updateFinancial(int begin, int end){
        List<DataFinancial> d = reportFinancialDAO.getFinancialByIds(begin, end);
        for (DataFinancial e : d) {
            // e.setDateInt(deal_year_month_custom);
            e.setYear("2022");
            e.setMonth("12");
            // 客户类型和税率
            setTaxRateAndCusType(e);
            // 一口价
            setDataMaterialPrice(e);
            // 渠道和折扣还原
            setChannel(e);
            // 除税收入
            setReportIncome(e);
            // 依赖数据库的结果，现在还没有
//             setApportion(e, idWeight);
            reportFinancialDAO.updateFinancial(e);
        }
    }

    public // synchronized
    void copyIntoFinancial(int begin, int end, Map<String, List<DataFinancialSuit>> fatherSons){
        List<DataFinancial> d = reportFinancialDAO.getFinancialByIds(begin, end);
        for (DataFinancial e : d) {

            String materialcode = e.getMaterialcode();
            if(!Strings.isNullOrEmpty(materialcode)){
                List<DataFinancialSuit> dataFinancialSuits = fatherSons.get(materialcode);
                if (!CollectionUtils.isEmpty(dataFinancialSuits)) {
                    for (DataFinancialSuit dfs : dataFinancialSuits) {
                        DataFinancialDeal deal = new DataFinancialDeal();
                        BeanUtils.copyProperties(e, deal);
                        deal.setMaterialcode(dfs.getChildCode());
                        deal.setMaterialname(dfs.getChildName());
                        deal.setNnum(String.valueOf(dfs.getChildNum() * Integer.parseInt(deal.getNnum())));

                        deal.setOriginSuitCode(e.getMaterialcode());
                        deal.setOriginSuitName(e.getMaterialname());
                        if(!Strings.isNullOrEmpty(deal.getMaterialcode())){
                            reportFinancialDAO.insertDataFinancialDeal(deal);
                        }
                    }
//                    continue;
                }
            }
            DataFinancialDeal deal = new DataFinancialDeal();
            BeanUtils.copyProperties(e, deal);
            if(fatherSons.containsKey(deal.getMaterialcode())){
                deal.setOriginSuitCode("Origin");
                deal.setOriginSuitName("原套盒");
            }
            if(!Strings.isNullOrEmpty(deal.getMaterialcode())){
                reportFinancialDAO.insertDataFinancialDeal(deal);
            }
        }
    }

    public void updateFinancialApp(int begin, int end, Map<Integer, String> idWeight){
        List<DataFinancial> d = reportFinancialDAO.getFinancialByIds(begin, end);
        for (DataFinancial e : d) {
            setApportion(e, idWeight);
            reportFinancialDAO.updateFinancial(e);
        }
    }

    public void exportLost(){
        List<DataApportion> apps
                = reportFinancialDAO.getApportionByYear(deal_year, deal_month);
        apps.removeIf(a -> appsUsedIds.contains(a.getId()));
        writeToJsonFile(apps);
    }


    public void dealOne(int begin, int end){
        List<DataFinancial> d = reportFinancialDAO.getFinancialByIds(begin, end);
        Map<Integer, String> idWeight
                = getAllWeight().stream().collect(Collectors.toMap(DataFinancialItemWeightDTO::getId, DataFinancialItemWeightDTO::getWeight, (v1, v2) -> v2));

        for (DataFinancial e : d) {
            e.setDateInt(deal_year_month_custom);
            // 客户类型和税率
            setTaxRateAndCusType(e);
            // 一口价
            setDataMaterialPrice(e);
            // 渠道和折扣还原
            setChannel(e);
            // 除税收入
            setReportIncome(e);

            setApportion(e, idWeight);
//            reportFinancialDAO.updateFinancial(e);
            logger.info("跟新的id是:{}", e.getId());
            System.out.println(JSON.toJSONString(e, true));
        }
    }

    enum ChannelType{
        BZ("北植","0101"),
        MD("美动","9010103"),
        GC("工厂","9010116");

        private String name;
        private String code;

        ChannelType(String name, String code) {
            this.name = name;
            this.code = code;
        }
        public String getCode() {
            return code;
        }
    }

    enum  BillType {
        /*** 整单 */
        one("30-Cxx-01"),
        four("30-Cxx-04"),
        five("30-Cxx-05"),
        seven("30-Cxx-07"),
        nine("30-Cxx-09"),
        ten("30-Cxx-10");

        private final String code;

        BillType( String code) {
            this.code = code;
        }
    }

    private void setChannel(DataFinancial origin){
        String orgCode = origin.getOrgCode();
        String ordertrantype = origin.getOrdertrantype();
        String cusType = origin.getCusType();
        String def27Name = Strings.isNullOrEmpty(origin.getDef27Name()) ? null : origin.getDef27Name();// 电商平台
        if(Strings.isNullOrEmpty(orgCode)){
            return;
        }
        DataChannel d;
        if(!ChannelType.BZ.getCode().equalsIgnoreCase(orgCode) &&
                !ChannelType.MD.getCode().equalsIgnoreCase(orgCode) &&
                !ChannelType.GC.getCode().equalsIgnoreCase(orgCode)){
            // FIXME: 2023/6/1 新增一个条件，电商平台 原始数据会有，ePlatform
            d = reportFinancialDAO.getByCondi("!", null, cusType, def27Name, origin.getYear(), origin.getMonth());
        }else {
            d = reportFinancialDAO.getByCondi(orgCode, ordertrantype, cusType, def27Name, origin.getYear(), origin.getMonth());
        }
        if(d == null){
            // logger.error("内部数据跳过，orgCode:{}, ordertrantype:{}, cusType:{}",orgCode, ordertrantype, cusType);
            return;
        }
        origin.setChannel(d.getChannel());
        String priceGini = origin.getPriceGini();
        BigDecimal discountReduction = BigDecimal.valueOf(Double.parseDouble(origin.getNorigtaxmny())).divide(
                BigDecimal.valueOf(Double.parseDouble(d.getNcaltaxmnyRestore())), 2, RoundingMode.HALF_UP);
        // 小植系数不同
        if("小植-直营".equalsIgnoreCase(d.getChannel()) &&
                !"非独店终端".equalsIgnoreCase(origin.getCusType()) &&
                !Strings.isNullOrEmpty(priceGini) && Double.parseDouble(priceGini) > 0){
            discountReduction = BigDecimal.valueOf(Double.parseDouble(origin.getNorigtaxmny())).divide(
                    BigDecimal.valueOf(Double.parseDouble(priceGini)), 2, RoundingMode.HALF_UP);
        }
        origin.setDiscountReduction(discountReduction.toString());
    }

    private void setTaxRateAndCusType(DataFinancial origin){
        if(Strings.isNullOrEmpty(origin.getCusCode())){
            return;
        }
        // FIXME: 2023/6/1 更改为year+month处理
        List<DataCustomer> list = reportFinancialDAO.getCustomersByDateAndCode(
                origin.getYear(),
                origin.getMonth(),
                origin.getCusCode());
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        DataCustomer d = list.get(0);
        origin.setCusType(d.getCusType());

        origin.setTaxRate(d.getTaxRate());
    }

    private void setDataMaterialPrice(DataFinancial origin){
        DataMaterialPrice p = reportFinancialDAO.getGini(origin.getYear(), origin.getMonth()
                ,origin.getMaterialcode());
        if (p != null) {
            origin.setPriceGini(p.getGini());
            origin.setFenlei1(p.getFenlei1());
            origin.setFenlei2(p.getFenlei2());
            origin.setGuige(p.getGuige());
        }

    }


    private void setReportIncome(DataFinancial origin){
        if(Strings.isNullOrEmpty(origin.getDiscountReduction()) || Strings.isNullOrEmpty(origin.getTaxRate())){
            return;
        }
        BigDecimal son = BigDecimal.valueOf(Double.parseDouble(origin.getDiscountReduction()));
        BigDecimal father = BigDecimal.valueOf(Double.parseDouble(origin.getTaxRate())).add(BigDecimal.valueOf(1));
        BigDecimal divide = son.divide(father, 2, RoundingMode.HALF_UP);

        origin.setReportIncome(divide.toString());
    }

    /**
     * @Description: [获取权重]
     * @Title: getAllWeight
     * @Author: Wang Sai
     * @Date: 2022-11-03
     * @Param:
     * @Return: java.util.List<com.deal.datafinancial.model.dto.DataFinancialItemWeightDTO>
     * @Throws:
     */
    public List<DataFinancialItemWeightDTO> getAllWeight(){
        List<DataFinancialDTO> allGroup = reportFinancialDAO.getAllFinancialGroupByCusCodeAndChannel();
        List<DataFinancialItemWeightDTO> itemWeights = Lists.newArrayList();
        for (DataFinancialDTO d : allGroup) {
            String absTotal = d.getAbsTotal();
            List<String> details = d.getDetails();

            for (int i = 0; i < details.size(); i++) {
                String money = details.get(i);
                Integer id = d.getIds().get(i);
                DataFinancialItemWeightDTO item = new DataFinancialItemWeightDTO();
                item.setId(id);
                BigDecimal weight = (BigDecimal.valueOf(Double.parseDouble(money)).divide(
                        BigDecimal.valueOf(Double.parseDouble(absTotal)) ,6,  RoundingMode.HALF_UP)).abs();
                item.setWeight(weight.toString());
                itemWeights.add(item);
            }
        }
        return itemWeights;
    }

    private void setApportion(DataFinancial origin, Map<Integer, String> idWeight){

        List<DataApportion> apps
                = reportFinancialDAO.getApportion2(origin.getCusCode(), origin.getChannel(),
                deal_year, deal_month);

//        List<DataApportion> apps
//                = reportFinancialDAO.getApportion(origin.getOrgCode(), origin.getCusCode(), origin.getChannel(),
//                deal_year, deal_month);

        if(CollectionUtils.isEmpty(apps)){
            return;
        }
        for (DataApportion app : apps) {
            String reportIncome = origin.getReportIncome();
            if(Strings.isNullOrEmpty(reportIncome)
                    || Double.parseDouble(reportIncome) == 0d){
                break;
            }
            String weight = idWeight.get(origin.getId());

            if(Strings.isNullOrEmpty(weight)){
                // logger.warn("丢失匹配项:{}",origin.getId());
                break;
            }
            appsUsedIds.add(app.getId());
            String appV
                    = BigDecimal.valueOf(Double.parseDouble(app.getMoney())).multiply(BigDecimal.valueOf(Double.parseDouble(weight))).toString();
            if(ApportionType.fdfl.getCode().equalsIgnoreCase(app.getApportionType())){
                origin.setAppFdfl(appV);
            }
            if(ApportionType.sclyd.getCode().equalsIgnoreCase(app.getApportionType())){
                origin.setAppScly(appV);
            }
            if(ApportionType.dsfx.getCode().equalsIgnoreCase(app.getApportionType())){
                origin.setAppDsfx(appV);
            }
            if(ApportionType.dszy.getCode().equalsIgnoreCase(app.getApportionType())){
                origin.setAppDszy(appV);
            }
            if(ApportionType.mdjm.getCode().equalsIgnoreCase(app.getApportionType())){
                origin.setAppMdjm(appV);
            }
            if(ApportionType.dyks.getCode().equalsIgnoreCase(app.getApportionType())){
                origin.setAppDyks(appV);
            }
            if(ApportionType.fdfl.getCode().equalsIgnoreCase(app.getApportionType())){
                origin.setAppFdfl(appV);
            }
            if(ApportionType.hyjf.getCode().equalsIgnoreCase(app.getApportionType())){
                origin.setAppHyjf(appV);
            }
        }
    }


    private <T> void  writeToJsonFile(Collection<T> v){
        String fileName = deal_year + deal_month + "lost.json";
        String path = "C:\\Users\\DP\\Downloads\\" + fileName;

        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(v);
            out.write(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    enum  ApportionType {
        /*** 整单 */
        sclyd("商场联营店"),
        dsfx("电商分销"),
        dszy("电商直营"),
        mdjm("门店加盟"),
        dyks("抖音快手"),
        fdfl("返点返利"),
        hyjf("会员积分");

        private final String code;

        ApportionType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
