package com.minfo.quanmei.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by fei on 15/10/28.
 */
public class Pay_Utils {

    public String notify_url = "http://114.119.41.119/api/zfb/notify_url.php";

    // 商户PID
    public static final String PARTNER = "2088121000537625";
    // 商户收款账号
    public static final String SELLER = "846846@qq.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMWuQ1WaXHjQcp3EOEfWhIezMJ0006VIsYNaaEyle4MoooWhk00vctcBYTdd6VQjIIeQdk2u5oDJCPAG a0fL0mesdF31dlw4l6zUoDS5eSA5wVYtNkFeYcKt972XClaeBivjdZA6ZghfstKEYz+d4WTn2gOvChB5Zm6iWlfVUqQTAgMBAAECgYB75sbTf8XX/6bnVdaEyGMW/uxI jJTfcxm4H9FhwRMSWUTMh0JhTY0oT/gUEOuvTbkU3yoXdLmLHPZaI1vYi1sbfcXb F/FotGmkfSKoQSk/lokQxfcW8i4LOJLOfyKoEQhqhAedG5Q96TMiaHAbOPdwq6oC 7tuN4sw1zqnRTmxoUQJBAP95kdPuq0TDt3egirBaUUf7UenCwySPtifKJduMlcDg S2dIIkss3Sm1D5++dzOrAtFb2lYC1zteP5+2AK7ocakCQQDGFkg/F6tujNy1yBuj I8bpl8DMvB+oJJeF8oRrTosQL8hdGlh4NgmEUs7uIDnj6rn+C79CBBJbgOD75qt+wHVbAkBN2LuI+tcRcxn6x9668iqGZpyFQKW6BFibM0vp5KLVTQNtC1v30EnsJZIH OUCVa+zF4tlbEC6JlqSIhCsdIRNRAkBsa7/JgMwda05W1RuDdM6oBp7JsOJm5vhk oXQnQ8tL5ct2YjgwO+uDmMuYfN0SyeRZj9Z0bMQbf3QljIErlG3nAkEAo0bBRYWJ NDT7qYbLI5zaMDthr9E+K9/x8fLBRlTd38ghUxlzfg2i1fwoGUgCLtMBcjG8RuGI Q7/yxavY7RuJMQ==";
    /**
     * create the order info. 创建订单信息
     *
     */
    public static String getOrderInfo(String subject, String body, String price ,String key) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + key + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://114.119.41.119/api/zfb/notify_url.php"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    public static String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public static String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
