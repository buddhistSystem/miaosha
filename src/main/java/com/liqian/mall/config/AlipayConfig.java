package com.liqian.mall.config;

import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author liqian
 */
@Component
public class AlipayConfig {

    /**
     * appid,收款账号是APPID对应支付宝账号
     */
    public static String app_id = "2016092000555546";

    /**
     * 商户私钥，您的PKCS8格式RSA2私钥
     * 私钥不要写在配置文件中或者代码中
     */
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCvOuYEMktoICfwjOba6a77pqX+RcGpesvtlJim+ictTYb3PsI1Ww/ARMxlNVqMck5QMoz4++1AgEihIC2my" +
            "V22qPp2biNiEv8NTm/tRelk3cylC2s0e8gA/DUKIoe7IRJY74thTfgxxCw9jlJCDNqqEmVpk12Qq/zoNNf5SsicTLEOGF5hE18YqSGWsRCMusj1o/G7a7emmogP1AbDP5EbvFJRSpc5gmlZ7UDdJZukyhn9Xs1mnvglaQNEXO" +
            "nh4cCjN0BtqLcocgoQM9+scYP0z3fRAMxuuSbiXF62fi+N/OaJNL33wa5EewybS1geOfysb5Uv+zsuhpTaX8RJ+WADAgMBAAECggEAZdMhBIFBZmGBM/x8zesLPrrJLKfWcKilxgoaZrVofJ/XdOcoLZ8b2QthhtJpKsHllh6" +
            "54plqdZfWHKM5Q/kxlso8uQMpVn1Zbp8M1WFRzcZBfArVoirnvwLPw3LrUqdWJQ4V/VY0ip7HYoHIdQ9JJZkJCz+SGFaI4VqLwhZd2syR8KKdp5PFZaJLMib8I+Q4XYPBTIZHXKIZAL9QuvYsewuoEEXf33ahf+kBHfWvyIHP" +
            "YesCuzMSTFL2nKlCdrHUi2TesqPKT4eTHB6pzfIXwFfoFTL8igC2gKm+/HVqOEXL+cKKKiKzRHQVNivfa1rl7oGqEtJKEPsmzXqhuausCQKBgQDvrv0pLQlKC197rWVwseMuXBXFtBpGjBE84fLKTEuq1xgCS8MkRJG8iHeVC" +
            "gm8/j6LSFPC9ZItYO5q7JsK6SWbzpDzX79WLbOpRPQANpwbtc1dNj+A5wn/nqLaH6dvn0MMz0S9UWhFyVoO9yLrdo2oL5Y57T2c2FhrhA3lhBaARwKBgQC7KKqfhsCwm08PgbgazBkcEE0u82qqA5jigm+MPct7np31m+kzo3" +
            "UbzCOX66qOVvGuZbSfgQsWh8GAODDfh6Vk/EYiFu+rzTqLqA9+eAhPN3K17sgWD+FS/Vr+R3KNJ8pPt3XVowr4pLP8izfXUhdaArXK2GTJMgAMhZLb0eccZQKBgEiE5Joxwy7BFCnCg/++AuPC/t9rp8uSk6CdKK2mufo9zEj" +
            "djXSKvqGRlMTmXZ3uOxQ/VQCW9px+SITXg+2gLz3eGSNZ4/V6LHLZpw3td/w2XKWIRVyGCxNpdxKSI0H1qP7WM5EocWk0C+idQ9aEb0Xk6AosysiK+O3G7cF03q3/AoGAbQoGTibOAJabeydSQZqDToxYy2/Zxu+NlPn1Tzl5" +
            "Ya5JzRxmw0UKJ4XJfus4VPFmoPp0PzGJajjxjWgvYSvAOpOXsKi4Pdm1sEPbswzvmmdtl31kaSYbWqRs7RRnMsHp5oO2TR9OdwpOKmZZxEL5g+FsVJ+6KFXPHXx80UD7atkCgYBgsYioIpeM4XOesNiRF1S++4SovSEK6nZzZ" +
            "3xMgo31iLTIIKYB7WX0uj/za9z81ILz9sz84CeoN7US9DYTF/2g8qixVAviXUsHD+j02wJ+c7w3amSw8b2xGR9Gs0AD1bFPk3/lVI5vJcF0bso+X3/d4oJ2H/vbYFeZzXtKtWfFHw==";

    /**
     * 支付宝公钥
     */
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1OLiyJkeVXD0hBR9uAYrZFiCIYPHuTPkZ5lvU/sIURf+bkeu+sekYEyaOHoMjlNW4E88bMnpLuTqmkhAIJlTxFfEi1iM" +
            "sGwhJzcMBgskIEQgQM4HQ/303IEXOp8Inubqb8jpMGFWV54ZDpBx8nS1A1BAQN/rRkWYoQ3jMETudzri6B28hhTRbBIVjL6T6KoG7id96H0K21cmA38D9wPavLyZ4Bzy12nFKBdkmgO5MkKmJ7ajsmV+QYn7VrJw98Ykv/5Ak" +
            "cThjpGMbaRtA6Ct/S77YF9lhZG4ZxHG2YG2jGkE5nGoEge3Mxk255ltJI36CnkopEMjJ87mSN6+BMfz4wIDAQAB";

    /**
     * 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     * 返回的时候此页面不会返回到用户页面，只会执行你写到控制器里的地址
     * 可使用natapp工具进行内网穿透 natapp -authtoken
     */
    public static String notify_url = "http://869mwi.natappfree.cc/order/notifyUrl";

    /**
     * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     * 此页面是同步返回用户页面，也就是用户支付后看到的页面，上面的notify_url是异步返回商家操作
     */
    public static String return_url = "http://192.168.12.223:8080/returnUrl";

    /**
     * 签名方式
     */
    public static String sign_type = "RSA2";

    /**
     * 字符编码格式
     */
    public static String charset = "utf-8";

    /**
     * 支付宝网关
     */
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    /**
     * 日志地址
     */
    private static String log_path = "D:/logs/";


    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_"
                    + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
