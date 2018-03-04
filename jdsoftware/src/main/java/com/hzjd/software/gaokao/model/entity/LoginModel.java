package com.hzjd.software.gaokao.model.entity;

import java.io.Serializable;

/**
 * Created by Longlong on 2017/12/7.
 */

public class LoginModel implements Serializable{


    /**
     * status : 0
     * data : {"uid":"4","utoken":"a8ac859788d140278bf5c7c907548b34","score":"623","ranking":"23000","updateStatus":"1","extensionCode":"11111"}
     */

    public String status;
    public DataBean data;
    public String msg;
    public static class DataBean {
        /**
         * uid : 4
         * utoken : a8ac859788d140278bf5c7c907548b34
         * score : 623     分数
         * ranking : 23000  排名
         * updateStatus : 1   是否已经修改改过分数名次，0--未修改 1--修改过
         * extensionCode : 11111
         */

        public String uid;
        public String utoken;
        public String score;
        public String ranking;
        public String updateStatus;
        public String extensionCode;
    }
}
