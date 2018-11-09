package com.aishangxuejie.javamail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


/**
 * @author CuiGM
 * @date 2018年6月14日 下午4:42:15
 * @param PROPERTIES
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
	private static final String PROPERTIES_DEFAULT = "\\resource\\mailConfig.properties";
    public static Properties PROPERTIES;
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
        PROPERTIES = new Properties();
        try{
        	String realpath = System.getProperty("user.dir");
        	String path = realpath + PROPERTIES_DEFAULT;
        	System.out.println("配置文件路径 " + path);
        	FileReader filereader = null;
        	try {
        		filereader = new FileReader(path);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        	BufferedReader br = new BufferedReader(filereader);
    		String line;
    		Object[] obj =null;
    		while ((line = br.readLine()) != null) {
    			if(!line.startsWith("#")&&!line.startsWith(" ")){
    				System.out.println(line);
    				obj = line.toString().trim().split("=");
    				if(obj.length == 2) {
    					PROPERTIES.put(obj[0]==null?"":obj[0].toString(),obj[1]==null?"":obj[1].toString());
    				}
    			}
    		}
    		
    		filereader.close();
            MAIL_SMTOHOST = PROPERTIES.getProperty("mail.smtp.host");	//	发件人的邮箱的 SMTP 服务器地址
        	SMTP_TYPE = PROPERTIES.getProperty("mail.transport.protocol");	//	使用的协议（JavaMail规范要求）
        	MAIL_PORT = PROPERTIES.getProperty("mail.smtp.port");	//	端口
        	ENABLE = PROPERTIES.getProperty("mail.smtp.starttls.enable");	//tsl加密
        	AUTH = PROPERTIES.getProperty("mail.smtp.auth");	//	请求认证
        	SSL = PROPERTIES.getProperty("mail.smtp.socketFactory.class");	//如果需要ssl
        	FALLBACK = PROPERTIES.getProperty("mail.smtp.socketFactory.fallback");
        	MAIL_ACCOUNT = PROPERTIES.getProperty("mail.sender.username");	//	邮箱服务器发邮件账户
        	MAIL_PASSOWORD = PROPERTIES.getProperty("mail.sender.password");	//	密码或授权码
        	RECEIVE_MAILACCOUNT = PROPERTIES.getProperty("mail.receiver.account");	//	主收件人
        	RECEIVE_MAILCOPYTO1 = PROPERTIES.getProperty("mail.receiver.copyTo");	//	抄送人
        	RECEIVE_MAILCOPYTO2 = PROPERTIES.getProperty("mail.receiver.copyTo");	//	抄送人
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
    	//启动时先加载初始化代码块，再执行main线程。
		System.out.println("end：---------------------邮件服务器配置文件--------------------");
	}
}

