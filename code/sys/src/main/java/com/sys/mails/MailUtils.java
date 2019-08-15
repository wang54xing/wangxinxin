package com.sys.mails;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

import com.sys.props.Resources;

/**
 * 邮件处理类
 * @author fourer
 *
 */
public class MailUtils {

	private static final Logger logger =Logger.getLogger(MailUtils.class);

	// xml文件中字段对应的javabean对象
	private static MailInfo mailInfo = new MailInfo();

	private void parseXML() {
		
		mailInfo.setHost(Resources.getProperty("smtp.server"));

		// 设置发件人
		
		mailInfo.setFrom(Resources.getProperty("smtp.from"));

		// 设置收件人，对多个收件人的处理放在后面

		// 设置抄送，对多个抄送人的处理放在后面

		// 设置发件人用户名
		
		mailInfo.setUsername(Resources.getProperty("smtp.userName"));

		
		// 设置发件人密码
		mailInfo.setPassword(Resources.getProperty("smtp.password"));

	}

	/**
	 * <p>
	 * Title: sendMailOfValidate
	 * </p>
	 * <p>
	 * Description:发送邮件的方法,Authenticator类验证
	 * </p>
	 */
	private void sendMailOfValidate() {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", mailInfo.getHost());// 设置邮件服务器的域名或IP
		props.put("mail.smtp.auth", "true");// 授权邮件,mail.smtp.auth必须设置为true

		String password = mailInfo.getPassword();// 密码
		// try {
		// password = Encrypt.DoDecrypt(password);// 如果密码经过加密用此方法对密码进行解密
		// } catch (NumberFormatException e1) {
		// // 如果密码未经过加密,则对密码不做任何处理
		// }
		// 传入发件人的用户名和密码,构造MyAuthenticator对象
		MyAuthenticator myauth = new MyAuthenticator(mailInfo.getUsername(), password);

		// 传入props、myauth对象,构造邮件授权的session对象
		Session session = Session.getDefaultInstance(props, myauth);

		// 将Session对象作为MimeMessage构造方法的参数传入构造message对象
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(mailInfo.getFrom()));// 发件人

			// 对多个收件人的情况进行处理,配置文件SendMail.xml中每个收件人之间必须用逗号隔开的
			if ((mailInfo.getTo() != null) && !"".equals(mailInfo.getTo())) {
				String to[] = mailInfo.getTo().split(",");
				for (String element : to) {
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(element));// 收件人
				}
			}

			// 对多个抄送人的情况进行处理,每个抄送人之间用逗号隔开的
			if ((mailInfo.getCc() != null) && !"".equals(mailInfo.getCc())) {
				String cc[] = mailInfo.getCc().split(",");
				for (String element : cc) {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(element));// 抄送
				}
			}

			// 构造Multipart
			Multipart mp = new MimeMultipart();
			// 向Multipart添加正文
			MimeBodyPart mbpContent = new MimeBodyPart();
			mbpContent.setContent(mailInfo.getContent(), "text/html;charset=gb2312");
			// 向MimeMessage添加（Multipart代表正文）
			mp.addBodyPart(mbpContent);
			// 向Multipart添加附件
			Enumeration efile = mailInfo.getFile().elements();
			while (efile.hasMoreElements()) {

				MimeBodyPart mbpFile = new MimeBodyPart();
				mailInfo.setFilename(efile.nextElement().toString());
				FileDataSource fds = new FileDataSource(mailInfo.getFilename());
				mbpFile.setDataHandler(new DataHandler(fds));
				// 这个方法可以解决附件乱码问题。
				// String filename= new String(fds.getName().getBytes(),"ISO-8859-1");
				// 转码 文件名乱码问题解决
				BASE64Encoder enc = new BASE64Encoder();
				mbpFile.setFileName("=?GBK?B?" + enc.encode(fds.getName().getBytes("GBK")) + "?=");
				// mbpFile.setFileName(filename);
				// 向MimeMessage添加（Multipart代表附件）
				mp.addBodyPart(mbpFile);

			}
			mailInfo.getFile().removeAllElements(); // 移除所有附件

			message.setSubject(mailInfo.getTitle());// 主题

			message.setContent(mp);
			// message.setContent(mailInfo.getContent(), "text/html;charset=gb2312");

			Transport.send(message);// 调用发送邮件的方法

		}  catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * <p>
	 * Title: sendMail
	 * </p>
	 * <p>
	 * Description:外部程序调用的入口
	 * </p>
	 * 
	 * @param type 邮件的类型,目前有三种，即logmessage、checkmessage、ordermessage,type只能传这三个值中一个,传其他的任何值都不行
	 * @param content 邮件的内容
	 * @throws Exception
	 */
	public void sendMail(String title, String to, String content) throws Exception {

		parseXML();// 解析xml字符串，把对应字段的值放入到+对象中
		mailInfo.setTitle(title);
		mailInfo.setTo(to);
		mailInfo.setContent(content);// 设置发送的内容

		sendMailOfValidate();// 发送邮件

	}

	/**
	 * <p>
	 * Title: sendMail
	 * </p>
	 * <p>
	 * Description:外部程序调用的入口
	 * </p>
	 * 
	 * @param type 邮件的类型,目前有三种，即logmessage、checkmessage、ordermessage,type只能传这三个值中一个,传其他的任何值都不行
	 * @param content 邮件的内容
	 * @throws Exception
	 */
	public void sendMail(String title, String to, String content, Vector file) throws Exception {

		parseXML();// 解析xml字符串，把对应字段的值放入到+对象中
		mailInfo.setTitle(title);
		mailInfo.setTo(to);
		mailInfo.setContent(content);// 设置发送的内容

		mailInfo.setFile(file); // 附件

		sendMailOfValidate();// 发送邮件

	}

	/**
	 * 为了方便直接用main方法测试
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		MailUtils mail = new MailUtils();

		// type类型,根据此字段去配置文件SendMail.xml中匹配,然后发到相应的邮箱

		// 邮件的内容,实际开发中这个内容是前台传到后台
		String content = "<h4><font color=red>Welcome!</font></h4><br>";

		// 在其他类中调用时只能看到sendMail方法,为了保护内部细节,其它的方法都声明为私有的
		mail.sendMail("测试", "fourer.yin@fulaan.com", content);
		// 这个项目中没有日志文件,所以我打印一句话来告诉自己程序已经成功运行
		System.out.println("****success****");

	}
	
	
	
}
