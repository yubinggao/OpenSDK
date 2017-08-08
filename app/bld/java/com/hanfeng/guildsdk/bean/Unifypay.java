package com.hanfeng.guildsdk.bean;

import java.io.Serializable;
/**
 * 
 * 银联微信签名回调类
 * @author yuqi
 *
 */
public class Unifypay implements Serializable {

	//    {
//        "sign": "F2F5C7170F2D103314D154220381E7EC",
//            "result_code": "SUCCESS",
//            "mch_id": "15121009",
//            "prepay_url": "https://api.zwxpay.com/pay/jspay?showwxpaytitle=1&prepay_id=c25c208bb734790b6fe3c79ac6ac7249",
//            "prepay_id": "c25c208bb734790b6fe3c79ac6ac7249",
//            "return_code": "SUCCESS",
//            "trade_type": "trade.weixin.h5pay"
//    }
    private String sign;
    private String result_code;
    private String mch_id;
    private String prepay_url;
    private String prepay_id;
    private String out_trade_no;
    private String return_code;
    private String trade_type;
    private String return_msg;
    private String err_code;
    private String err_code_des;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getPrepay_url() {
        return prepay_url;
    }

    public void setPrepay_url(String prepay_url) {
        this.prepay_url = prepay_url;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }
}
