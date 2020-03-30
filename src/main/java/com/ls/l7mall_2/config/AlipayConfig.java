package com.ls.l7mall_2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author laijs
 * @date 2020-3-30-14:16
 */
@Configuration
public class AlipayConfig {
    /**
     * 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
     */
    @Value("${alipay.appid}")
    private String appID;

    /**
     * 商户私钥，您的PKCS8格式RSA2私钥
     */
    @Value("${alipay.private_key}")
    private String merchantPrivateKey;

    /**
     * 支付宝公钥
     */
    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;

    /**
     * 签名方式
     */
    @Value("${alipay.sign_type}")
    private String signType;

    /**
     * 网关
     */
    @Value("${alipay.open_api_domain}")
    private String gatewayUrl;

    /**
     * 编码
     */
    private String  charset= "utf-8";

    /**
     * 异步通知地址
     */
    @Value("${alipay.notifyUrl}")
    private String notifyUrl;

    /**
     * 类型
     */
    private String format="json";

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getMerchantPrivateKey() {
        return merchantPrivateKey;
    }

    public void setMerchantPrivateKey(String merchantPrivateKey) {
        this.merchantPrivateKey = merchantPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
