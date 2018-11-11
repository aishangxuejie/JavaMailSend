package com.aishangxuejie.javamail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author aishangxuejie
 * @date 2018年6月14日 上午8:56:06
 * @param name
 * @param pathName
 */
public class SendMailForErrorService {
	
	public static String MAIL_SMTPHOST = MailConfig.MAIL_SMTOHOST;
    public static String SMTP_TYPE = MailConfig.SMTP_TYPE;
    public static String MAIL_PORT = MailConfig.MAIL_PORT;
    public static String TIMEOUT = MailConfig.TIMEOUT;
    public static String ENABLE = MailConfig.ENABLE;
    public static String AUTH = MailConfig.AUTH;
    public static String SSL = MailConfig.SSL;
    public static String FALLBACK = MailConfig.FALLBACK;
    public static String MAIL_ACCOUNT = MailConfig.MAIL_ACCOUNT;	//	服务器邮箱
    public static String MAIL_PASSWORD = MailConfig.MAIL_PASSOWORD;
    public static String RECEIVE_MAILACCOUNT = MailConfig.RECEIVE_MAILACCOUNT;
    public static String RECEIVE_MAILCOPYTO1 = MailConfig.RECEIVE_MAILCOPYTO1;
    public static String RECEIVE_MAILCOPYTO2 = MailConfig.RECEIVE_MAILCOPYTO2;
    public static void main(String[] args) throws Exception {
    	SendMailForErrorService.sendMain("aishangxuejie-进行JavaMail测试","tran_log","");
    }

    /**
     * @author aishangxuejie
     * @date 2018年6月21日15:30:32
     * 创建一封邮件
     * @param session
     * @param sendMail 发件人
     * @param receiveMail 收件人
     * @param receiveMailCopyTo 抄送人
     * @param mess 邮件内容
     * @Parma personal 发件名称
     * @Param subject 邮件主题
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,
    		String receiveMailCopyTo,String mess,String personal,String subject) throws Exception {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	new InternetAddress();
    	// 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, personal, "UTF-8"));
        // 3. To: 收件人
        InternetAddress[] internetAddressTo = InternetAddress.parse(receiveMail);
		message.setRecipients(Message.RecipientType.TO, internetAddressTo);
		// 4. CC: 抄送
        if(null != receiveMailCopyTo) {
        	 @SuppressWarnings("static-access")  
             InternetAddress[] internetAddressCC = new InternetAddress().parse(receiveMailCopyTo);  
             message.setRecipients(Message.RecipientType.CC, internetAddressCC);
        }
        // 设置多个密送地址  -需要增加配置参数
//        if(null != bccUser && !bccUser.isEmpty()){  
//         @SuppressWarnings("static-access")  
//         InternetAddress[] internetAddressBCC = new InternetAddress().parse(bccUser);  
//         message.setRecipients(Message.RecipientType.BCC, internetAddressBCC);  
//        }
        // 5. Subject: 邮件主题
        message.setSubject(subject, "UTF-8");
        String time = df.format(new Date());
        // 6. Content: 邮件正文（可以使用html标签）
        message.setContent("检测服务程序于"+time+"，发现"+mess+"请及时处理！<br>", "text/html;charset=UTF-8");
        // 7. 设置发件时间
        message.setSentDate(new Date());
        
     // 7. （文本+图片）设置 文本 和 图片"节点"的关系（将 文本 和 图片"节点"合成一个混合"节点"）
//        MimeMultipart mm_text_image = new MimeMultipart();
//        mm_text_image.addBodyPart(text);
//        mm_text_image.addBodyPart(image);
//        mm_text_image.setSubType("related");    // 关联关系
//         
//        // 8. 将 文本+图片 的混合"节点"封装成一个普通"节点"
//        // 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
//        // 上面的 mailTestPic 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
//        MimeBodyPart text_image = new MimeBodyPart();
//        text_image.setContent(mm_text_image);
//         
//        // 9. 创建附件"节点"
//        MimeBodyPart attachment = new MimeBodyPart();
//        // 读取本地文件
//        DataHandler dh2 = new DataHandler(new FileDataSource("src\\mailTestDoc.docx"));
//        // 将附件数据添加到"节点"
//        attachment.setDataHandler(dh2);
//        // 设置附件的文件名（需要编码）
//        attachment.setFileName(MimeUtility.encodeText(dh2.getName()));       
//         
//        // 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
//        MimeMultipart mm = new MimeMultipart();
//        mm.addBodyPart(text_image);
//        mm.addBodyPart(attachment);     // 如果有多个附件，可以创建多个多次添加
//        mm.setSubType("mixed");         // 混合关系
        
        // 8. 保存设置
        message.saveChanges();

        return message;
    }
    /**
     * @author aishangxuejie
     * @date 2018年6月21日15:30:32
     * @param mess 邮件内容
     * @param personal 发件人名称
     * @param subject 主题
     * @return
     */
    public static Boolean sendMain(String mess, String personal,String subject){
    	try {
			// 1、Set参数配置
			Properties props = new Properties(); // 参数配置
			props.setProperty("mail.smtp.host", MAIL_SMTPHOST); //	服务器地址
			props.setProperty("mail.transport.protocol", SMTP_TYPE); // 使用的协议（JavaMail规范要求）
			props.setProperty("mail.smtp.starttls.enable", ENABLE); //tls加密
			props.setProperty("mail.smtp.auth", AUTH); // 请求认证
//			props.setProperty("mail.smtp.socketFactory.class", SSL);//sslFALLBACK
//			props.setProperty("mail.smtp.socketFactory.fallback", FALLBACK);
			// 2. 根据配置创建会话对象, 用于和邮件服务器交互
			Session session = Session.getDefaultInstance(props);
			session.setDebug(false); // 设置为debug模式, 可以查看详细的发送 log
			if(personal.startsWith("tran_log")) {
				// 3. 创建一封邮件
				MimeMessage message = createMimeMessage(session, MAIL_ACCOUNT, RECEIVE_MAILACCOUNT,
						RECEIVE_MAILCOPYTO1, mess,personal,subject);
				// 4. 根据 Session 获取邮件传输对象
				Transport transport = session.getTransport();
				transport.connect(MAIL_ACCOUNT, MAIL_PASSWORD);
				// 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 
				//	获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
				transport.sendMessage(message, message.getAllRecipients());
				// 7. 关闭连接
				transport.close();
			}else {
				// 3. 创建一封邮件
				MimeMessage message = createMimeMessage(session, MAIL_ACCOUNT, RECEIVE_MAILACCOUNT,
						RECEIVE_MAILCOPYTO2, mess,personal,subject);
				// 4. 根据 Session 获取邮件传输对象
				Transport transport = session.getTransport();
				transport.connect(MAIL_ACCOUNT, MAIL_PASSWORD);
				// 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 
				//	获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
				transport.sendMessage(message, message.getAllRecipients());
				// 7. 关闭连接
				transport.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("邮件发送成功！关闭链接！");
		return true;
    }
}
