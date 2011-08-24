package com.mygengo.confluence;

import java.io.Serializable;

/**
 * Model to save API and Private key.
 */
public class Credentials implements Serializable
{
		private String apiKey;
		private String privKey;
 
		public String getApiKey() { return apiKey; }
		public String getPrivKey() { return privKey; }

		public void setApiKey(String a) { apiKey = a; }
		public void setPrivKey(String a) { privKey = a; }
}
