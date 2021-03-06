package com.namibank.gateway.yifenqi;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.alibaba.fastjson.JSONObject;
import com.namibank.df.gateway.util.DateUtil;
import com.namibank.gateway.channel.yifenqi.dto.PreauthDTO;

public class TestController {

	public void testPreauth() throws Exception {
		String now = DateUtil.format("yyyyMMddHHmmss", new Date());
		String apiUrl = "http://localhost:8080/yifenqi/service/preauth";
		PreauthDTO preauth = new PreauthDTO();
		preauth.setBankCard("6226388000000095");
		preauth.setBankPhone("18100000000");
		preauth.setBankUserId("510265790128303");
		preauth.setBankUserName("张三");
		preauth.setCvn2("248");
		preauth.setValidPeriod("1912");
		preauth.setBatchId(UUID.randomUUID().toString());
		preauth.setTxnAmt(new BigDecimal("1000"));
		preauth.setTxnTime(now);

		String data = JSONObject.toJSONString(preauth);
		String str = this.sendRequest(apiUrl, data);
		System.out.println(str);

	}

	public void testPreauthQuery() throws Exception {
		String apiUrl = "http://localhost:8080/yifenqi/service/preauthQuery";
		PreauthDTO preauth = new PreauthDTO();
		preauth.setBatchId(UUID.randomUUID().toString());
		preauth.setBizOrderId("170400012017042616153170382336");
		preauth.setQueryId("857146110166183936");
		preauth.setTxnTime("20170426161417");
		preauth.setTxnType("02");

		String data = JSONObject.toJSONString(preauth);
		String str = this.sendRequest(apiUrl, data);
		System.out.println(str);
	}

	public void testPreauthCompletion() throws Exception {
		String apiUrl = "http://localhost:8080/yifenqi/service/preauthCompletion";
		PreauthDTO preauth = new PreauthDTO();
		preauth.setBatchId(UUID.randomUUID().toString());
		preauth.setQueryId("201704271642422084498");
		preauth.setTxnTime("20170426161417");
		preauth.setTxnAmt(new BigDecimal("1001"));

		String data = JSONObject.toJSONString(preauth);
		String str = this.sendRequest(apiUrl, data);
		System.out.println(str);
	}

	public void testPreauthCancel() throws Exception {
		String apiUrl = "http://localhost:8080/yifenqi/service/preauthCancel";
		PreauthDTO preauth = new PreauthDTO();
		preauth.setBatchId(UUID.randomUUID().toString());
		preauth.setQueryId("857508263377252352");
		preauth.setTxnTime("20170426161417");
		preauth.setTxnAmt(new BigDecimal("1"));

		String data = JSONObject.toJSONString(preauth);
		String str = this.sendRequest(apiUrl, data);
		System.out.println(str);
	}

	public static void main(String[] args) throws Exception {
		TestController testController = new TestController();
		 testController.testPreauth();

//		testController.testPreauthQuery();

//		 testController.testPreauthCompletion();

//		 testController.testPreauthCancel();
	}

	public String sendRequest(String apiUrl, String data) throws Exception {
		String returnStr = null;
		// 处理请求参数
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(apiUrl);
		postMethod.setRequestHeader("Content-Type", "application/json;charset=utf-8");
		System.out.println("请求地址: " + apiUrl);
		System.out.println("请求报文: " + data);
		try {
			// 设置编码
			httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpClient.getParams().setSoTimeout(30 * 1000);
			postMethod.setRequestBody(data);
			int statusCode = httpClient.executeMethod(postMethod);
			// 失败
			if (statusCode != HttpStatus.SC_OK) {
				System.out.println("Method failed: " + postMethod.getStatusLine());
				byte[] responseBody = postMethod.getResponseBody();
				String strResp = new String(responseBody, "UTF-8");
				System.out.println(strResp);
				throw new Exception("http状态码为: " + statusCode);
			} else {
				// 读取内容
				byte[] responseBody = postMethod.getResponseBody();
				String strResp = new String(responseBody, "UTF-8");
				System.out.println("返回报文: " + strResp);
				if (strResp == null || strResp.length() <= 0) {
					return returnStr;
				}
				returnStr = strResp;
			}
		} catch (SocketTimeoutException e) {
			System.out.println("timeout");
		} finally {
			postMethod.releaseConnection();
		}
		return returnStr;
	}
}
