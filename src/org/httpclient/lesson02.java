/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.httpclient;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.ProxyClient;

public class lesson02 {

	public static void main(String[] args) {
	
		HttpClient httpclient = new DefaultHttpClient();
		
		try{
			HttpGet get = new HttpGet("http://www.google.com");
			System.out.println("execute request: "+get.getURI());
			//create a response hendler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpclient.execute(get,responseHandler);
			System.out.println("====================================");
			System.out.println(responseBody);
			System.out.println("====================================");
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			//关闭连接管理，以确定快速的分配所有的系统资源
			httpclient.getConnectionManager().shutdown();
		}
		
	}

}

