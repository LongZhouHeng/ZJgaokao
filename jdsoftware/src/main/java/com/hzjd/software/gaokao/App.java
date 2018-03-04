package com.hzjd.software.gaokao;


import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Vibrator;




/**
 * 全局Application类
 * 通常做一些初始化工作，
 * Notice：记得在AndroidManife.xml清单文件中配置APP路径
 *
 * Created by xiongmc on 2016/4/26.
 */
public class App extends Application {

    /**
     * 全局上下文，方便使用
     */
	 public static final String CHACHE_FILE = "JDlot";
	private SharedPreferences mPrefs;
	// 版本名
    private String versionName;
	public static boolean SKIP_WELCOME;
    public static Context appliction;
//    public static RequestQueue queue;
 //   public static ImageLoader IL;
    public static Handler handler;
    public Vibrator mVibrator;
    private static App instance;
	public static String jgId;

	public static App getInst() {
        return instance;
    }
    
    @Override
    public void onCreate() {
    	 // 首次启动不跳过欢迎界面；        
    	SKIP_WELCOME = false;        
        super.onCreate();
        
        appliction = this;
        handler= new Handler();
   //     queue = Volley.newRequestQueue(getApplicationContext()); // 实例化RequestQueue对象
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        instance = this;
        try {
            versionName = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mPrefs = this.getSharedPreferences(CHACHE_FILE, Context.MODE_PRIVATE);
  /*   // 融云
     		RongIM.init(this);
     		RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener());
     		RongIM.setConnectionStatusListener(new MyConnectionStatusListener());
     	//shareSDK
     		ShareSDK.initSDK(this);
     	
     		 SDKInitializer.initialize(this);
     		 
     		setCheck("1");
//		极光推送
		//初始化sdk
		JPushInterface.setDebugMode(true);//正式版的时候设置false，关闭调试
		JPushInterface.init(this);
		*//*String registrationId = JPushInterface.getRegistrationID(this);
		Log.i("1099", "run:--------->registrationId： "+registrationId );*//*
		//建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
		Set<String> set = new HashSet<>();
		set.add("andfixdemo");//名字任意，可多添加几个,能区别就好了
		JPushInterface.setTags(this, set, null);//设置标签
*/
	/*	PrefUtils pUtils = new PrefUtils();

		String id = pUtils.getString(getContext(),
				"member_id", null);
		jgId = registrationId + id;

		JPushInterface.setAlias(this,jgId,null);*/
        
    }
   /* public static RequestQueue getHttpQueue() {
        return queue;
    }*/
    /**
     * 新安装应用或者更新版本时，需要显示导航页面一次
     * */
/*
    public boolean showGuide() {
        String version = mPrefs.getString(ConnUrls.CURRENT_VERSION, "");
        return !versionName.equals(version);
    }

*/
    /**
     * 导航页面启动过一次之后，不在显示，除非更新版本。
     * */
   /* public void finishGuide() {
        mPrefs.edit().putString(ConnUrls.CURRENT_VERSION, versionName).commit();
    }*/
  /*  private class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

	    *//**
	     * 收到消息的处理。
	     *
	     * @param message 收到的消息实体。
	     * @param left    剩余未拉取消息数目。
	     * @return 收到消息是否处理完成，true 表示自己处理铃声和后台通知，false 走融云默认处理方式。
	     *//*
	    @Override
	    public boolean onReceived(Message message, int left) {
	        //开发者根据自己需求自行处理
	        return false;
	    }
	}
	
	private class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {

		  @Override
		  public void onChanged(ConnectionStatus connectionStatus) {

		      switch (connectionStatus){

		          case CONNECTED://连接成功。
		        	  	Log.e("连接", "连接");
		              break;
		          case DISCONNECTED://断开连接。
		        	  Log.e("连接", "断开连接");
		              break;
		          case CONNECTING://连接中。
		        	  Log.e("连接", "连接中");
		              break;
		          case NETWORK_UNAVAILABLE://网络不可用。
		        	  Log.e("连接", "网络不可用");
		              break;
		          case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线

		              break;
		      }
		  }
		}*/
	
	
	 private String Check;

	public String getCheck() {
		return Check;
	}

	public void setCheck(String check) {
		Check = check;
	}

	public static Context getContext(){
		return appliction;
	}

	
	
}