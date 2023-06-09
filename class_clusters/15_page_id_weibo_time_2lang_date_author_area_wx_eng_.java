package com.cssweb.payment.posp.client;

import com.cssweb.payment.posp.common.*;
import com.cssweb.payment.posp.network.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by chenhf on 2014/10/30.
 */
public class TestGetBalance {

    public CustomMessage getRequest() {
        Date now = new Date();
        StringBuffer sb = new StringBuffer();

        String AC_ID = "00000000000";
        String SW_ID = "11111111111";
        String IS_ID = "22222222222";
        String referNo = "181030414357"; // 参考号
        String terminalNo = "63856007"; // 终端号
        String batchNo = "000216"; // 批次号
        String authNo = "571462"; // 授权码
        String voucherNo = "005572"; // 凭证号
        String merchantNo = "102310058125081"; // 商户编号


        CustomMessage request = new CustomMessage();

        MsgHeader msgHeader = new MsgHeader();
        MessageType msgType = new MessageType();
        BitFieldMap bitFieldMap = new BitFieldMap();
        FieldData fieldData = new FieldData();
        List<Field> fields = new ArrayList<Field>();

        try {


            // 主账号
            Field2 field2 = new Field2();
            field2.setData("6226090217946181");
            System.out.println(field2.toString());
            fields.add(field2);


            //交易处理码
            Field3 field3 = new Field3();
            //301000
            //String f3 = Field3.TRD_TYPE_QUERY_SERVICE + Field3.FROM_DEPOSIT + Field3.FROM_DEFAULT + Field3.TO_DEFAULT + Field3.TO_DEFAULT;
            field3.setData("30X000");
            //field3.setTradeType(Field3.TRD_TYPE_QUERY_SERVICE);
            //field3.setFrom(Field3.FROM_DEPOSIT + Field3.FROM_DEFAULT);
            //field3.setTo(Field3.TO_DEFAULT + Field3.TO_DEFAULT);
            System.out.println(field3.toString());
            fields.add(field3);

            // 交易传输时间
            Field7 field7 = new Field7();
            SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
            String tranTime = sdf.format(now);
            field7.setData(tranTime);
            System.out.println(field7.toString());
            fields.add(field7);


            //系统跟踪号
            Field11 field11 = new Field11();
            String traceNo = "";
            Random random = new Random();
            for (int i = 0; i < 6; i++) {
                traceNo += random.nextInt(10);
            }
            field11.setData(traceNo);
            System.out.println(field11.toString());
            fields.add(field11);

            //受卡方所在地时间
            Field12 field12 = new Field12();
            SimpleDateFormat time = new SimpleDateFormat("HHmmss");
            String t = time.format(now);
            field12.setData(t);
            System.out.println(field12.toString());
            fields.add(field12);

            //受卡方所在地日期
            Field13 field13 = new Field13();
            SimpleDateFormat date = new SimpleDateFormat("MMdd");
            String d = date.format(now);
            field13.setData(d);
            System.out.println(field13.toString());
            fields.add(field13);

            // 卡有效期
            Field14 field14 = new Field14();
            field14.setData("1612"); // 2016年12月份
            System.out.println(field14.toString());
            fields.add(field14);

            // 清算日期
            Field15 field15 = new Field15();
            //SimpleDateFormat date = new SimpleDateFormat("MMdd");
            //String d = date.format(now);
            field15.setData(d);
            System.out.println(field15.toString());
            fields.add(field15);

            // 商户类型
            //取值请参见GB/T 20548-2006《金融零售业务 商户类别代码》
            Field18 field18 = new Field18();
            field18.setData("1234");
            System.out.println(field18.toString());
            fields.add(field18);

            //商户国家代码
            //参见世界各国和地区名称代码（GB/T 2659-94）
            Field19 field19 = new Field19();
            field19.setData("156");

            //服务点输入方式码
            Field22 field22 = new Field22();
            String f22 = Field22.PAN_IC + Field22.PIN_INCLUDE;
            field22.setData(f22);
            //field22.setPAN(Field22.PAN_IC);
            //field22.setPIN(Field22.PIN_INCLUDE);
            System.out.println(field22.toString());
            fields.add(field22);

            //服务点条件码
            Field25 field25 = new Field25();
            field25.setData(Field25.COMMIT);
            System.out.println(field25.toString());
            fields.add(field25);

            //服务点PIN获取码
            Field26 field26 = new Field26();
            field26.setData("06"); // 6位长度密码
            System.out.println(field26.toString());
            fields.add(field26);

            //受理机构标识码
            Field32 field32 = new Field32();
            field32.setData(AC_ID);
            System.out.println(field32.toString());
            fields.add(field32);

            //发送机构标识码
            Field33 field33 = new Field33();
            field33.setData(IS_ID);
            System.out.println(field33.toString());
            fields.add(field33);


            //第二磁道数据
            Field35 field35 = new Field35();
            //第三磁道数据
            Field36 field36 = new Field36();

            //检索参考号
            Field37 field37 = new Field37();
            field37.setData(referNo);
            System.out.println(field37.toString());
            fields.add(field37);

            //应答码
            Field39 field39 = new Field39();
            //field39.setFieldValue();

            //受卡机终端标识码
            Field41 field41 = new Field41();
            field41.setData(terminalNo);
            System.out.println(field41.toString());
            fields.add(field41);

            //受卡方标识码
            Field42 field42 = new Field42();
            field42.setData(merchantNo);
            System.out.println(field42.toString());
            fields.add(field42);

            //受卡方名称地址
            Field43 field43 = new Field43();
            String addr = field43.appendSpace("地址", field43.getDataLen());

            field43.setData(addr);
            System.out.println(field43.toString());
            fields.add(field43);


            //第一磁道数据
            Field45 field45 = new Field45();

            //附加数据－私有
            Field48 field48 = new Field48();

            Field48_AA field48_aa = new Field48_AA();


            //fields.add(field48);

            //交易货币代码
            //参见ISO 4217标准
            Field49 field49 = new Field49();
            field49.setData("CNY");
            System.out.println(field49.toString());
            fields.add(field49);

            //个人标识码数据，存放加密后的PIN
            Field52 field52 = new Field52();
            field52.setData("enc  pin");
            System.out.println(field52.toString());
            fields.add(field52);

            //安全控制信息
            Field53 field53 = new Field53();

            String f53 = Field53.PIN_FORMAT_PAN + Field53.ENCRYPTION_METHOD_DOUBLE + Field53.RESERVED;
            field53.setData(f53);
            //field53.setPinFormat(Field53.PIN_FORMAT_PAN);
            //field53.setEncAlgo(Field53.ENCRYPTION_METHOD_DOUBLE);
            System.out.println(field53.toString());
            fields.add(field53);

            //实际余额
            Field54 field54 = new Field54();

            //附加交易信息
            Field57 field57 = new Field57();

            //自定义域
            Field60 field60 = new Field60();
            //fields.add(field60);

            //持卡人身份认证信息
            Field61 field61 = new Field61();
            //fields.add(field61);

            //接收机构标识码
            Field100 field100 = new Field100();
            field100.setData(SW_ID);
            System.out.println(field100.toString());
            fields.add(field100);


            //预付卡发卡机构保留
            //预付卡发卡机构保留
            Field121 field121 = new Field121();

            //应答/应答原因码
            Field121_1 field121_1 = new Field121_1();
            field121_1.setData(Field121_1.RC_ISSUER_RESPONSE);
            System.out.println(field121_1.toString());


            //单/双或双/单转换码
            Field121_2 field121_2 = new Field121_2();
            field121_2.setData(Field121_2.CC_UNKNOWN);
            System.out.println(field121_2.toString());

            //卡性质
            Field121_3 field121_3 = new Field121_3();
            field121_3.setData(Field121_3.CARD_TYPE_CUP_DEBIT);
            System.out.println(field121_3.toString());

            //预付卡发卡机构保留
            Field121_4 field121_4 = new Field121_4();
            sb.delete(0, sb.length());
            for (int i = 0; i < field121_4.getDataLen(); i++) {
                sb.append('0');
            }
            field121_4.setData(sb.toString().getBytes());
            System.out.println(field121_4.toString());


            Field121_5_ID field121_5_id = new Field121_5_ID();
            sb.delete(0, sb.length());
            for (int i = 0; i < field121_5_id.getDataLen(); i++) {
                sb.append('1');
            }
            field121_5_id.setData(sb.toString().getBytes());
            System.out.println(field121_5_id.toString());

            //转入和转出方标识代码/手续费信息
            Field121_5 field121_5 = new Field121_5();
            field121_5.addSubFieldByTag(field121_5_id);
            //field121_5.addField(field121_5_id);
            System.out.println(field121_5.toString());



            field121.addSubField(field121_1);
            field121.addSubField(field121_2);
            field121.addSubField(field121_3);
            field121.addSubField(field121_4);
            field121.addSubField(field121_5);
            field121.encode();

            System.out.println(field121.toString());

            fields.add(field121);


            //报文鉴别码
            Field128 field128 = new Field128();
            field128.setData("MAC45378");
            System.out.println(field128.toString());

            fields.add(field128);

            int totalLen = 0;
            for (Field field : fields) {
                totalLen += field.getFieldLength();
            }
            System.out.println("实际总长度=" + totalLen);

            // 合并各个域的值
            fieldData.encode(fields);
            System.out.println(fieldData.toString());

            // 设置位图
            bitFieldMap.setFields(fields);
            System.out.println(bitFieldMap.showBitFieldMap());

            // 开始处理消息类型
            msgType.setMsgType("0200");

            // 设置消息头
            totalLen = MsgHeader.MSG_HEADER_SIZE + MessageType.MSG_TYPE_SIZE + bitFieldMap.getBitFieldMapLen() + fieldData.getFieldDataLen();
            msgHeader.encode(totalLen, "48020000", "B0210029", (byte) 0, "00000000", (byte) 0, "00000");

            // 消息编码,这一步非常重要，把msgHeader, msgType, bitFieldMap, fieldData合成msgContent
            request.setMsgHeader(msgHeader);
            request.setMsgType(msgType);
            request.setBitFieldMap(bitFieldMap);
            request.setFieldData(fieldData);

            //
            request.encode();

            return request;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (FieldLengthException e) {
            e.printStackTrace();
        } catch (OverflowMaxLengthException e) {
            e.printStackTrace();
        }

        return null;
    }
}


--------------------

package org.yy.paipai.request.evaluation;

/*
* 文 件 名:  GetShopEvalListRequest.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:   查询店铺评价列表  ,http://pop.paipai.com/api/paipai/evaluation/getShopEvalList请求
* 修 改 人:  zhouliang
* 修改时间:  2014年11月28日
* 修改内容:  <修改内容>
*/
import org.yy.paipai.request.AbstractPaiPaiRequest;
import org.yy.paipai.response.evaluation.GetShopEvalListResponse;

/**
*  查询店铺评价列表  ,http://pop.paipai.com/api/paipai/evaluation/getShopEvalList请求
* 
* @author  zhouliang
* @version  [0.1, 2014年11月28日]
* @since  [paipai-base/0.1]
*/
public class GetShopEvalListRequest extends AbstractPaiPaiRequest<GetShopEvalListResponse> {
    
    /** {@inheritDoc} */
    @Override
    public String getApiMethodName() {
        return "/evaluation/getShopEvalList.xhtml";
    }
    
    /** {@inheritDoc} */
    @Override
    public Class<GetShopEvalListResponse> getResponseClass() {
        return GetShopEvalListResponse.class;
    }
    
    /**
    * >=1	查询第几页
    */
    public Long getPageIndex() {
        return Long.valueOf(textMap.get("pageIndex"));
    }
    
    /**
     * 
    */
    public void setPageIndex(Long pageIndex) {
        textMap.put("pageIndex", String.valueOf(pageIndex));
    }
    
    /**
    * 每页显示的记录数
    */
    public Long getPageSize() {
        return Long.valueOf(textMap.get("pageSize"));
    }
    
    /**
     * 
    */
    public void setPageSize(Long pageSize) {
        textMap.put("pageSize", String.valueOf(pageSize));
    }
    
    /**
    * [10001,2000000000]	卖家QQ号
    */
    public Long getSellerUin() {
        return Long.valueOf(textMap.get("sellerUin"));
    }
    
    /**
     * 
    */
    public void setSellerUin(Long sellerUin) {
        textMap.put("sellerUin", String.valueOf(sellerUin));
    }
    
    /**
    * 0（默认）	是否显示历史评价 0：只返回六个月内评价列表（默认） 1：只返回六个月以前评价列表
    */
    public Long getHistory() {
        return Long.valueOf(textMap.get("history"));
    }
    
    /**
     * 
    */
    public void setHistory(Long history) {
        textMap.put("history", String.valueOf(history));
    }
    
    /**
    * 0（默认） 是否显示回复 0：不显示回复（默认） 1：显示回复
    */
    public Long getNeedReply() {
        return Long.valueOf(textMap.get("needReply"));
    }
    
    /**
     * 
    */
    public void setNeedReply(Long needReply) {
        textMap.put("needReply", String.valueOf(needReply));
    }
    
}

--------------------

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinglicom.salesman.service;

import com.dinglicom.frame.sys.entity.UserInfo;
import com.dinglicom.salesman.domain.DepsaleResp;
import com.dinglicom.salesman.domain.OrgSaleDetailSampleReq;
import com.dinglicom.salesman.domain.OrgSaleSampleReq;
import com.dinglicom.salesman.domain.OrgSaleSampleResp;
import com.dinglicom.salesman.domain.ProductSaleSampleReq;
import com.dinglicom.salesman.domain.ProductSaleSampleResp;

/**
 *
 * @author panzhen
 */
public interface SalesmanService {

    /**
     * 销售员各个商品的销售情况
     * @param req
     * @param salesman
     * @return 
     */
    ProductSaleSampleResp queryByProductsample(ProductSaleSampleReq req, UserInfo salesman);

    /**
     * 业务员按照奶站/经销商统计
     * @param req
     * @param salesman
     * @return 
     */
    OrgSaleSampleResp queryByOrgsample(OrgSaleSampleReq req, UserInfo salesman);

    /**
     * 业务员查看具体奶站/经销商产品销售情况
     * @param req
     * @return 
     */
    ProductSaleSampleResp queryByOrgDetailsample(OrgSaleDetailSampleReq req);

    /**
     * 销售部门按照产品统计
     * @param req
     * @param depAdmin
     * @return 
     */
    ProductSaleSampleResp queryByDepProductsample(ProductSaleSampleReq req, UserInfo depAdmin);
    
    /**
     * 销售部分按照销售员销售业绩统计
     * @param req
     * @param depman
     * @return 
     */
    DepsaleResp queryDepBySalesman(OrgSaleSampleReq req, UserInfo depman);
    
    /**
     * 具体销售员销售量统计
     * @param req
     * @return 
     */
    ProductSaleSampleResp queryDepBySalemansample(OrgSaleDetailSampleReq req);
    
    /**
     * 公司所有商品分月、季度、年度的销量统计
     * @param req
     * @return 
     */
    ProductSaleSampleResp queryByAllProductsample(ProductSaleSampleReq req);
    
    /**
     * 按照销售部门统计销量情况
     * @param req
     * @return 
     */
    DepsaleResp querySaleManagerSample(OrgSaleSampleReq req);
    
    /**
     * 销售部门各产品销售情况
     * @param req
     * @return 
     */
    ProductSaleSampleResp querySaleManagerDetailSample(OrgSaleDetailSampleReq req);
}

--------------------

