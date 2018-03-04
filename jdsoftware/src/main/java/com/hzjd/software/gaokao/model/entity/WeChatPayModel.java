package com.hzjd.software.gaokao.model.entity;

import java.io.Serializable;

/**
 * Created by Longlong on 2017/12/19.
 */

public class WeChatPayModel implements Serializable{


    /**
     * status : 0
     * data : {"nonce_str":"Z4LK7IQN3Q9EE4JJSYGRYXYMTC2SQ4ET","out_trade_no":"20171218171701187001","sign":"51ACC1313D3FB8154EF3FE7D269F2863","prepay_id":"wx20171218171701d76ae742cb0341106507","timestamp":"1513588620"}
     */

    public String status;
    public DataBean data;

    public static class DataBean {
        /**
         * nonce_str : Z4LK7IQN3Q9EE4JJSYGRYXYMTC2SQ4ET
         * out_trade_no : 20171218171701187001
         * sign : 51ACC1313D3FB8154EF3FE7D269F2863
         * prepay_id : wx20171218171701d76ae742cb0341106507
         * timestamp : 1513588620
         */

        public String nonce_str;
        public String out_trade_no;
        public String sign;
        public String prepay_id;
        public String timestamp;
    }
}
