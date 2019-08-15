package com.sys.mails;

import javax.mail.PasswordAuthentication;

public class MyAuthenticator extends javax.mail.Authenticator {
	private String strUser;
	private String strPwd;

	public MyAuthenticator(String user, String password) {
		this.strUser = user;
		this.strPwd = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(strUser, strPwd);
	}
}
