package jaosn.app.momo.crawler;

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

import jaosn.app.momo.crawler.model.MomoRoom;
import jaosn.app.momo.crawler.model.RecommendResponse;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A example that demonstrates how HttpClient APIs can be used to perform
 * form-based logon.
 */
public class GetRoomListApp {
	private static final Logger logger = LoggerFactory.getLogger(GetRoomListApp.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
		// 创建一个HttpClient
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.setDefaultCookieStore(cookieStore).build();
		logger.info("init http client");
		try {
			HttpGet httpget = new HttpGet("https://web.immomo.com");
			CloseableHttpResponse response1 = httpclient.execute(httpget);
			try {
//				HttpEntity entity = response1.getEntity();
//				EntityUtils.consume(entity);
				if(response1.getStatusLine().getStatusCode()!=200) {
					logger.error("failed to request for cookie");
					return;
				}
				logger.info("Initial set of cookies:");
				List<Cookie> cookies = cookieStore.getCookies();
				if (cookies.isEmpty()) {
					logger.info("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						logger.info("- " + cookies.get(i).toString());
					}
				}
			} finally {
				response1.close();
			}
			int page = 1;
			int cnt = 0;
			logger.info("start to fetch pages");
			while (true) {
				logger.info("current page:"+page);
				HttpPost post = new HttpPost(
						"https://web.immomo.com/webmomo/api/scene/recommend/lists");
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("page", String
						.valueOf(page)));
				parameters.add(new BasicNameValuePair("item", "-3"));
				parameters.add(new BasicNameValuePair("live", "false"));
				post.addHeader("Referer",
						"https://web.immomo.com/live/220908702");
				post.addHeader("Origin", "https://web.immomo.com");
				post.addHeader("Host", "web.immomo.com");
				post.addHeader("X-Requested-With", "XMLHttpRequest");
				post.addHeader(
						"User-Agent",
						"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36");

				HttpEntity entityPost = new UrlEncodedFormEntity(parameters);
				post.setEntity(entityPost);
				CloseableHttpResponse response2 = httpclient.execute(post);
				try {
					HttpEntity entity = response2.getEntity();
					String content = EntityUtils.toString(entity);
				//	System.out.println(content);
					RecommendResponse resp = mapper.readValue(content,
							RecommendResponse.class);
					if (resp.getEc() == 200 && resp.getData() != null) {
						if (resp.getData().getR_infos() != null) {
							for (MomoRoom room : resp.getData().getR_infos()) {
								logger.info(room.getRid() + ":"
										+ room.getTitle() + ":" + room.isLive()
										+ ":" + room.getOn());
								cnt++;
							}
							if (resp.getData().getR_infos().size() == 0)
								break;
						} else {
							break;
						}
					}else {
						logger.warn(resp.getEc()+":"+resp.getEm());
						break;
					}
					EntityUtils.consume(entity);

				} finally {
					response2.close();
				}
				page++;
			}
			logger.info("rooms total:"+cnt);
		} finally {
			httpclient.close();
		}
	}
}