/**
 * Author: S.J.H
 * 
 * Date: 2016/7/1
 */
package com.hzjd.software.gaokao;

/**
 * 常数
 */
public class Constants {

	//IP地址
	public static final String API_SERVER = "http://www.bkbaodian.com/app";
	/**
	 * 注册
	 */
	public final static String REGISTER = API_SERVER+"/user/register";

	/*
	验证码
	* */
	public final static String GER_CODE = API_SERVER+"/user/get_smscode";

	/**
	 * 登录
	 */
	public final static String LOGIN =API_SERVER+ "/user/login";

	//检查手机号
	public final static String CHECK_PHONE= API_SERVER+"/user/check_register";

	//重置密码
	public final static String RESET_PWD= API_SERVER+"/user/reset_password";

	//首页信息
	public final static String HOME_PAGE= API_SERVER+"/user/index";

	//修改分数名次
	public static final String MODIFY_GRADE_RANK = API_SERVER+"/user/update";

	//根据条件查询院校
	public static final String QUERY_RESULT = API_SERVER+"/info/pageAll";

	//支付宝支付信息
	public final static String ALIAPY_ORDER= API_SERVER+"/payment/sign";

	//微信支付信息
	public final static String ORDER_WECHAT= API_SERVER+"/weixin/sign";


	//订单信息
	public final static String ORDER_INFO= API_SERVER+"/user/order/list";


	//意见反馈 微信，QQ交流群信息
	public final static String WEICHA_MESS= API_SERVER+"/dict/group/list";

	//意见反馈 微信，QQ交流群信息
	public final static String FEED_BACK= API_SERVER+"/user/advise/add";

	//意见反馈 微信，QQ交流群信息
	public final static String CALCULATOR= API_SERVER+"/lot/count/list";

	//取消订单
	public final static String CANCEL_ORDER= API_SERVER+"/order/cancel";


	//appid 微信分配的公众账号ID
	public static final String APP_ID = "wx8cd6b3025822aceb";

	//商户号 微信分配的公众账号ID
	public static final String MCH_ID = "1494530282";

	//判断
	/**
	 * 日志
	 */
	public static final String TAG = "hhdebug";

	/**
	 * 开发者模式
	 */
	public static final boolean DEVELOPER_MODE = true;
	/**
	 * app cache key
	 */
	public static final String CURRENT_VERSION = "";// 当前版本

	/**
	 * action
	 */
	public static final String NOTIFICATION_REMAIN_CHANGE = "";
	/**
	 * image cache
	 */
	public static final String IMAGE_CACHE_SDCARD_PATH = "";
	/**
	 * error log
	 */
	public static final String LOG_CACHE_SDCARD_PATH = "";
	public static final String CURRENT_USER = "";// 当前登录用户
	public static final String CURRENT_USER_INFO = "";// 当前登录用户详细信息
	public static final String CURRENT_SHOPS = "";
	public static final String CURRENT_USER_LIST = "";// 用户列表
	public static final String CURRENT_CLASSLIST = "";

}
