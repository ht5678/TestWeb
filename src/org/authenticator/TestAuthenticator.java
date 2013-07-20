package org.authenticator;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

public class TestAuthenticator extends Authenticator{

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		// TODO Auto-generated method stub
		return new PasswordAuthentication("username", "password".toCharArray());
	}

	@Override
	protected URL getRequestingURL() {
		// TODO Auto-generated method stub
		return super.getRequestingURL();
	}

	@Override
	protected RequestorType getRequestorType() {
		// TODO Auto-generated method stub
		return super.getRequestorType();
	}

}
