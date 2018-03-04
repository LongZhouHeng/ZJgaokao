package com.hzjd.software.gaokao.model.entity;

import java.io.Serializable;

/**
 * Created by Longlong on 2017/12/7.
 */

public class HomePageModel implements Serializable{


    /**
     * status : 0
     * data : {"batchNum":"1","score":"623","ranking":"23002","role":"2","countdown":"182","lastYear":"2017","yesterYear":"2016"}
     */

    public String status;
    public DataBean data;
    public String msg;
    public static class DataBean {
        /**
         * batchNum : 1
         * score : 623
         * ranking : 23002
         * role : 2
         * countdown : 182
         * lastYear : 2017
         * yesterYear : 2016
         */

        public String batchNum;
        public String score;
        public String ranking;
        public String role;
        public String countdown;
        public String lastYear;
        public String yesterYear;
        public String price;
    }
}
