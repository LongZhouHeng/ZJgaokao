package com.hzjd.software.gaokao.model.entity;

import java.io.Serializable;

/**
 * Created by Longlong on 2017/12/11.
 */

public class AliPayModel implements Serializable {


    /**
     * status : 0
     * data : {"out_trade_no":"20171212152056103005","sign":"alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017120700430992&biz_content=%7B%22body%22%3A%22%E8%AE%A2%E5%8D%95%E6%94%AF%E4%BB%98%22%2C%22out_trade_no%22%3A%2220171212152056103005%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E5%8A%9F%E8%83%BD%E5%85%A8%E8%A7%A3%E9%94%81%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F47.96.172.217%3A8080%2FCee%2Fapp%2Fpayment%2Fnotify&sign=kMBnc%2ByylktHFiG2jUM7KCzEeblYZEIul4%2FE6qAFjNJ4gGEtpEw5pBwkMHucw9EJxnlkNZMnDNVCKolhr7XoLoRlP5gAXJ8eXnAB%2BunvGRDh24ovb490c59X7oyHL6Vu%2F%2F5%2FaM83I6LF3zXQh0orZWN%2FKuEwCmLT7FzjEddnMOcvAuqBKyryVH1qXihh%2Bpki7BRubESOP7GIZnJ9wN84d%2FmdLJpk%2FdRCbjFVi3l0DwLZID8PDUlsQeh%2BSIzAz8NLJPxEgBlIzSFqWStEOsDTJkNZTYsRurpl3n1ml6VRj1KUasz9oqD2A1Qt73O%2BRqHpQTvytdBHciEinfPXT6z3zQ%3D%3D&sign_type=RSA2&timestamp=2017-12-12+15%3A20%3A56&version=1.0"}
     */

    public String status;
    public DataBean data;
    public String msg;
    public static class DataBean {
        /**
         * out_trade_no : 20171212152056103005
         * sign : alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017120700430992&biz_content=%7B%22body%22%3A%22%E8%AE%A2%E5%8D%95%E6%94%AF%E4%BB%98%22%2C%22out_trade_no%22%3A%2220171212152056103005%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E5%8A%9F%E8%83%BD%E5%85%A8%E8%A7%A3%E9%94%81%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F47.96.172.217%3A8080%2FCee%2Fapp%2Fpayment%2Fnotify&sign=kMBnc%2ByylktHFiG2jUM7KCzEeblYZEIul4%2FE6qAFjNJ4gGEtpEw5pBwkMHucw9EJxnlkNZMnDNVCKolhr7XoLoRlP5gAXJ8eXnAB%2BunvGRDh24ovb490c59X7oyHL6Vu%2F%2F5%2FaM83I6LF3zXQh0orZWN%2FKuEwCmLT7FzjEddnMOcvAuqBKyryVH1qXihh%2Bpki7BRubESOP7GIZnJ9wN84d%2FmdLJpk%2FdRCbjFVi3l0DwLZID8PDUlsQeh%2BSIzAz8NLJPxEgBlIzSFqWStEOsDTJkNZTYsRurpl3n1ml6VRj1KUasz9oqD2A1Qt73O%2BRqHpQTvytdBHciEinfPXT6z3zQ%3D%3D&sign_type=RSA2&timestamp=2017-12-12+15%3A20%3A56&version=1.0
         */

        public String out_trade_no;
        public String sign;
    }
}
