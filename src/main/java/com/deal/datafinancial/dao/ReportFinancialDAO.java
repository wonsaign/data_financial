package com.deal.datafinancial.dao;

import com.deal.datafinancial.config.mybatis.typehandler.ListIntegerTypeHandler;
import com.deal.datafinancial.config.mybatis.typehandler.ListStringTypeHandler;
import com.deal.datafinancial.model.DataApportion;
import com.deal.datafinancial.model.DataChannel;
import com.deal.datafinancial.model.DataCustomer;
import com.deal.datafinancial.model.DataFinancial;
import com.deal.datafinancial.model.DataFinancialDeal;
import com.deal.datafinancial.model.DataFinancialSuit;
import com.deal.datafinancial.model.DataMaterialPrice;
import com.deal.datafinancial.model.dto.DataFinancialDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangsai
 */
@Mapper
@Repository
public interface ReportFinancialDAO {


    @Results({
            @Result(property = "orgCode", column = "org_code"),
            @Result(property = "name", column = "Unitcode"),
            @Result(property = "billType", column = "bill_type"),
            @Result(property = "cusType", column = "cus_type"),
            @Result(property = "ecommercePlatform", column = "ecommerce_platform"),
            @Result(property = "channel", column = "channel"),
            @Result(property = "ncaltaxmnyRestore", column = "NCALTAXMNY_restore"),
            @Result(property = "incomeShare", column = "Income_share"),
            @Result(property = "reportIncome", column = "report_income")
    })
    @Select(value = "<script> " +
            "  SELECT * FROM `dp_financial`.`data_channel`  </script> ")
    List<DataChannel> getAllChannels();



    @Results({
            @Result(property = "orgCode", column = "org_code"),
            @Result(property = "name", column = "Unitcode"),
            @Result(property = "billType", column = "bill_type"),
            @Result(property = "cusType", column = "cus_type"),
            @Result(property = "ecommercePlatform", column = "ecommerce_platform"),
            @Result(property = "channel", column = "channel"),
            @Result(property = "ncaltaxmnyRestore", column = "NCALTAXMNY_restore"),
            @Result(property = "incomeShare", column = "Income_share"),
            @Result(property = "reportIncome", column = "report_income"),
            @Result(property = "month", column = "month"),
            @Result(property = "year", column = "year")
    })
    @Select(value = "<script> " +
            "  SELECT * FROM `dp_financial`.`data_channel` " +
            "  where org_code = #{orgCode} " +
            "    <choose>" +
            "        <when test='billType !=null'> " +
            "            and bill_type = #{billType} " +
            "        </when>" +
            "        <otherwise>" +
            "            and bill_type is NULL " +
            "        </otherwise>" +
            "    </choose>" +
            "   and cus_type = #{cusType}  " +
            "    <choose>" +
            "        <when test='ecommercePlatform !=null'> " +
            "            and ecommerce_platform = #{ecommercePlatform} " +
            "        </when>" +
            "        <otherwise>" +
            "            and ecommerce_platform is NULL " +
            "        </otherwise>" +
            "    </choose>" +
            "  and year = #{year} and month = #{month} </script> ")
    DataChannel getByCondi(@Param("orgCode") String orgCode,
                           @Param("billType") String billType,
                           @Param("cusType") String cusType,
                           @Param("ecommercePlatform") String ecommercePlatform,
                           @Param("year") String year,
                           @Param("month") String month);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "orgCode", column = "org_code"),
            @Result(property = "comName", column = "com_name"),
            @Result(property = "cusName", column = "cus_name"),
            @Result(property = "cusCode", column = "cus_code"),
            @Result(property = "money", column = "money"),
            @Result(property = "channel", column = "channel"),
            @Result(property = "apportionType", column = "apportion_type"),
            @Result(property = "year", column = "year"),
            @Result(property = "month", column = "month")
    })
    @Select(value = "<script> " +
            "  SELECT * FROM `dp_financial`.`data_apportion` " +
            "  WHERE org_code = #{orgCode} and cus_code = #{cusCode} and channel = #{channel} and year = #{year} and month = #{month}  </script> ")
    List<DataApportion> getApportion(@Param("orgCode") String orgCode,
                             @Param("cusCode") String cusCode, @Param("channel") String channel,
                             @Param("year") Integer year,
                             @Param("month") String month);


    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "orgCode", column = "org_code"),
            @Result(property = "comName", column = "com_name"),
            @Result(property = "cusName", column = "cus_name"),
            @Result(property = "cusCode", column = "cus_code"),
            @Result(property = "money", column = "money"),
            @Result(property = "channel", column = "channel"),
            @Result(property = "apportionType", column = "apportion_type"),
            @Result(property = "year", column = "year"),
            @Result(property = "month", column = "month")
    })
    @Select(value = "<script> " +
            "  SELECT * FROM `dp_financial`.`data_apportion` " +
            "  WHERE cus_code = #{cusCode} and channel = #{channel} and year = #{year} and month = #{month}  </script> ")
    List<DataApportion> getApportion2(@Param("cusCode") String cusCode, @Param("channel") String channel,
                                     @Param("year") Integer year,
                                     @Param("month") String month);



    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "orgCode", column = "org_code"),
            @Result(property = "comName", column = "com_name"),
            @Result(property = "cusName", column = "cus_name"),
            @Result(property = "cusCode", column = "cus_code"),
            @Result(property = "money", column = "money"),
            @Result(property = "channel", column = "channel"),
            @Result(property = "apportionType", column = "apportion_type"),
            @Result(property = "year", column = "year"),
            @Result(property = "month", column = "month")
    })
    @Select(value = "<script> " +
            "  SELECT * FROM `dp_financial`.`data_apportion` " +
            "  WHERE year = #{year} and month = #{month}  </script> ")
    List<DataApportion> getApportionByYear(
                                     @Param("year") Integer year,
                                     @Param("month") String month);


    @Results({
            @Result(property = "dateInt", column = "date_int")
    })
    @Select(value = "<script> " +
            " SELECT * FROM `dp_financial`.`data_material_price` where date_int = #{dateInt}  </script> ")
    List<DataMaterialPrice> getAllMaterialPricesByDate(@Param("dateInt") int dateInt);


    @Results({
            @Result(property = "orgBelong", column = "org_belong"),
            @Result(property = "orgCode", column = "org_code"),
            @Result(property = "cusName", column = "cus_name"),
            @Result(property = "cusType", column = "cus_type"),
            @Result(property = "cusType", column = "tax_rate"),
            @Result(property = "dateInt", column = "date_int")
    })
    @Select(value = "<script> " +
            " SELECT * FROM `dp_financial`.`data_customer` where date_int = #{dateInt}  </script> ")
    List<DataCustomer> getAllCustomersByDate(@Param("dateInt") int dateInt);


    @Results({
            @Result(property = "orgBelong", column = "org_belong"),
            @Result(property = "orgCode", column = "org_code"),
            @Result(property = "cusName", column = "cus_name"),
            @Result(property = "cusType", column = "cus_type"),
            @Result(property = "taxRate", column = "tax_rate"),
            @Result(property = "dateInt", column = "date_int")
    })
    @Select(value = "<script> " +
            " SELECT * FROM `dp_financial`.`data_customer` where year = #{year} and month = #{month} and cus_code = #{cusCode} </script> ")
    List<DataCustomer> getCustomersByDateAndCode( @Param("year") String year,
                                                  @Param("month") String month,
                                                  @Param("cusCode") String cusCode);

    @Select(value = "<script> " +
            " SELECT * FROM `dp_financial`.`data_material_price` where year = #{year} and month = #{month} and code = #{code} </script> ")
    DataMaterialPrice getGini(@Param("year") String year,
                              @Param("month") String month,
                              @Param("code") String code);

    @Results({
            @Result(property = "orgCode", column = "ORG_CODE"),
            @Result(property = "orgName", column = "ORG_NAME"),
            @Result(property = "dateInt", column = "date_int"),
            @Result(property = "ordertrantype", column = "ORDERTRANTYPE"),
            @Result(property = "name", column = "NAME"),
            @Result(property = "cusCode", column = "CUS_CODE"),
            @Result(property = "materialcode", column = "MATERIALCODE"),
            @Result(property = "materialname", column = "MATERIALNAME"),
            @Result(property = "nnum", column = "NNUM"),
            @Result(property = "norigmny", column = "NORIGMNY"),
            @Result(property = "ncaltaxmny", column = "NCALTAXMNY"),
            @Result(property = "cusType", column = "cus_type"),
            @Result(property = "channel", column = "channel"),
            @Result(property = "priceGini", column = "price_gini"),
            @Result(property = "discountReduction", column = "discount_reduction"),
            @Result(property = "taxRate", column = "tax_rate"),
            @Result(property = "reportIncome", column = "report_income"),

            @Result(property = "appScly", column = "app_scly"),
            @Result(property = "appDsfx", column = "app_dsfx"),
            @Result(property = "appDszy", column = "app_dszy"),
            @Result(property = "appMdjm", column = "app_mdjm"),
            @Result(property = "appDyks", column = "app_dyks"),
            @Result(property = "appFdfl", column = "app_fdfl"),
            @Result(property = "appHyjf", column = "app_hyjf"),
            @Result(property = "appTotal", column = "app_total"),

            @Result(property = "year", column = "year"),
            @Result(property = "month", column = "month"),
            @Result(property = "guige", column = "guige"),
            @Result(property = "fenlei1", column = "fenlei1"),
            @Result(property = "fenlei2", column = "fenlei2"),
            @Result(property = "def27Name", column = "DEF27_NAME")
    })
    @Select(value = "<script> " +
            "  SELECT * FROM `dp_financial`.`data_financial` where id  between #{b} and #{e}  </script> ")
    List<DataFinancial> getFinancialByIds(@Param("b")int b, @Param("e")int e);


    @Select(value = "<script> " +
            "  SELECT id FROM `dp_financial`.`data_financial` ORDER BY ID DESC limit 1   </script> ")
    long getMaxFinancialId();

    @Results({
            @Result(property = "orgCode", column = "ORG_CODE"),
            @Result(property = "orgName", column = "ORG_NAME"),
            @Result(property = "dateInt", column = "date_int"),
            @Result(property = "ordertrantype", column = "ORDERTRANTYPE"),
            @Result(property = "name", column = "NAME"),
            @Result(property = "cusCode", column = "CUS_CODE"),
            @Result(property = "materialcode", column = "MATERIALCODE"),
            @Result(property = "materialname", column = "MATERIALNAME"),
            @Result(property = "nnum", column = "NNUM"),
            @Result(property = "norigmny", column = "NORIGMNY"),
            @Result(property = "ncaltaxmny", column = "NCALTAXMNY"),
            @Result(property = "cusType", column = "cus_type"),
            @Result(property = "channel", column = "channel"),
            @Result(property = "priceGini", column = "price_gini"),
            @Result(property = "discountReduction", column = "discount_reduction"),
            @Result(property = "taxRate", column = "tax_rate"),
            @Result(property = "reportIncome", column = "report_income"),


            @Result(property = "appScly", column = "app_scly"),
            @Result(property = "appDsfx", column = "app_dsfx"),
            @Result(property = "appDszy", column = "app_dszy"),
            @Result(property = "appMdjm", column = "app_mdjm"),
            @Result(property = "appDyks", column = "app_dyks"),
            @Result(property = "appFdfl", column = "app_fdfl"),
            @Result(property = "appHyjf", column = "app_hyjf"),
            @Result(property = "appTotal", column = "app_total")
    })
    @Select(value = "<script> " +
            "  SELECT * FROM `dp_financial`.`data_financial` limit  10  </script> ")
    List<DataFinancial> getAllFinancial();

    //

    @Update(value = "<script> " +
            "UPDATE `dp_financial`.`data_financial` " +
            "SET channel=#{e.channel}," +
            "   date_int=#{e.dateInt}," +
            "   price_gini=#{e.priceGini}," +
            "   discount_reduction=#{e.discountReduction}," +
            "   tax_rate=#{e.taxRate}," +
            "   cus_type=#{e.cusType}," +
            "   report_income=#{e.reportIncome}, " +
            "   app_scly=#{e.appScly}," +
            "   app_dsfx=#{e.appDsfx}," +
            "   app_dszy=#{e.appDszy}," +
            "   app_mdjm=#{e.appMdjm}," +
            "   app_dyks=#{e.appDyks}," +
            "   app_fdfl=#{e.appFdfl}, " +
            "   app_hyjf=#{e.appHyjf}," +
            "   app_total=#{e.appTotal}, " +

            "   guige=#{e.guige}," +
            "   fenlei1=#{e.fenlei1}," +
            "   fenlei2=#{e.fenlei2} " +
            "WHERE id = #{e.id}"+
            "</script> ")
    int updateFinancial(@Param("e") DataFinancial e);


    @Results({
            @Result(property = "nums", column = "nums"),
            @Result(property = "total", column = "total"),
            @Result(property = "absTotal", column = "abs_total"),
            @Result(property = "details", column = "details", typeHandler = ListStringTypeHandler.class),
            @Result(property = "ids", column = "ids",typeHandler = ListIntegerTypeHandler.class)
    })
    @Select(value = "<script> " +
            " SELECT count(1) as nums,\n" +
            "sum(report_income) as total,\n" +
            "sum(ABS(report_income)) as abs_total,\n" +
            "JSON_ARRAYAGG(report_income) as details,\n" +
            "JSON_ARRAYAGG(id) as ids,\n" +
            "CUS_CODE,channel FROM `dp_financial`.`data_financial` WHERE report_income != 0 GROUP BY CUS_CODE,channel </script> ")
    List<DataFinancialDTO> getAllFinancialGroupByCusCodeAndChannel();


    @Results({
            @Result(property = "suitCode", column = "suit_code"),
            @Result(property = "childCode", column = "child_code"),
            @Result(property = "childName", column = "child_name"),
            @Result(property = "childNum", column = "child_num")
    })
    @Select(value = "<script> " +
            " SELECT * FROM `dp_financial`.`data_financial_suit` \n" +
            "</script> ")
    List<DataFinancialSuit> getAllFinancialSuit();

    @Insert({ "insert into dp_financial.data_financial_deal" +
            "(ORG_CODE, ORG_NAME, date_int, ORDERTRANTYPE, NAME, cus_code, MATERIALCODE, " +
            "MATERIALNAME, NNUM,NORIGMNY, NCALTAXMNY,cus_type,channel,price_gini, discount_reduction, tax_rate, report_income," +
            " app_scly,app_mdjm,app_dyks,app_fdfl,app_dsfx,app_dszy,app_hyjf, app_total, origin_suit_code, origin_suit_name " +
            ") values(#{orgCode},#{orgName},#{dateInt},#{ordertrantype}, #{name}, #{cusCode}, #{materialcode}," +
            " #{materialname}, #{nnum}, #{norigmny},#{ncaltaxmny},#{cusType},#{channel},#{priceGini},#{discountReduction},#{taxRate}, #{reportIncome}," +
            " #{appScly},#{appMdjm},#{appDyks},#{appFdfl},#{appDsfx},#{appDszy},#{appHyjf},#{appTotal}, #{originSuitCode}, #{originSuitName})" })
    int insertDataFinancialDeal(DataFinancialDeal d);
}
