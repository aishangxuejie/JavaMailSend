package com.aishangxuejie.javamail;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;


/**
 * @author CuiGM
 * @date 2018年6月14日 下午4:42:15
 * @param properties
 * @param MAIL_SMTOHOST 邮件服务器
 * @param SMTP_TYPE 协议类型
 * @Param MAIL_PORT 邮件服务器端口
 * @Param ENABLE tls加密设置
 * @Param AUTH 请求认证
 * @Param SSL ssl加密
 * @Param FALLBACK mail.smtp.socketFactory.fallback
 * @Param TIMEOUT 
 * @Param MAIL_ACCOUNT 邮箱服务器账号
 * @Param MAIL_PASSOWORD 邮箱服务器密码OR授权码
 * @Param RECEIVE_MAILACCOUNT 接收人，可设置多个
 * @Param RECEIVE_MAILCOPYTO 抄送人，可设置多个,可为空
 * 
 */
public class MailConfig {
	public static final String MAILCONFIG_PROPERTIES = "\\resource\\mailConfig.properties";
    public static String MAIL_SMTOHOST;
    public static String SMTP_TYPE;
    public static String MAIL_PORT;
    public static String ENABLE;
    public static String AUTH;
    public static String SSL;
    public static String FALLBACK;
    public static String TIMEOUT = "2500";
    public static String MAIL_ACCOUNT;
    public static String MAIL_PASSOWORD;
    public static String RECEIVE_MAILACCOUNT;//收件人
    public static String RECEIVE_MAILCOPYTO1;//抄送人
    public static String RECEIVE_MAILCOPYTO2;//抄送人
    
    static{
        try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * 初始化
     * @throws IOException 
     */
    private static void init() throws IOException {
    	System.out.println("start：---------------------邮件服务器配置文件--------------------");
        try{
        	Properties properties = new Properties();
        	String realpath = System.getProperty("user.dir");
        	String path = realpath + MAILCONFIG_PROPERTIES;
        	System.out.println("配置文件路径 " + path);
        	BufferedInputStream in = new BufferedInputStream (new FileInputStream(path));
    		properties.load(in);     ///加载属性列表
    		in.close();
			Iterator<String> it=properties.stringPropertyNames().iterator();
			while(it.hasNext()){
			    String value=it.next();
			    System.out.println(value+"="+properties.getProperty(value));
			}
            MAIL_SMTOHOST = properties.getProperty("mail.smtp.host");	//	发件人的邮箱的 SMTP 服务器地址
        	SMTP_TYPE = properties.getProperty("mail.transport.protocol");	//	使用的协议（JavaMail规范要求）
        	MAIL_PORT = properties.getProperty("mail.smtp.port");	//	端口
        	ENABLE = properties.getProperty("mail.smtp.starttls.enable");	//tsl加密
        	AUTH = properties.getProperty("mail.smtp.auth");	//	请求认证
        	SSL = properties.getProperty("mail.smtp.socketFactory.class");	//如果需要ssl
        	FALLBACK = properties.getProperty("mail.smtp.socketFactory.fallback");
        	MAIL_ACCOUNT = properties.getProperty("mail.sender.username");	//	邮箱服务器发邮件账户
        	MAIL_PASSOWORD = properties.getProperty("mail.sender.password");	//	密码或授权码
        	RECEIVE_MAILACCOUNT = properties.getProperty("mail.receiver.account");	//	主收件人
        	RECEIVE_MAILCOPYTO1 = properties.getProperty("mail.receiver.copyTo1");	//	抄送人
        	RECEIVE_MAILCOPYTO2 = properties.getProperty("mail.receiver.copyTo2");	//	抄送人
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
    	//启动时先加载初始化代码块，再执行main线程。
		System.out.println("end：---------------------邮件服务器配置文件--------------------");
	}
}

