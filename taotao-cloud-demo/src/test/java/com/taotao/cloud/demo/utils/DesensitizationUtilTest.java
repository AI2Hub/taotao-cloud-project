package com.taotao.cloud.demo.utils;

import com.taotao.cloud.common.utils.DesensitizationUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * 脱敏工具-测试
 *
 */
public class DesensitizationUtilTest {

	@Test
	public void test1() {
		String str1 = "12345678";
		String str2 = "1******8";
		String middle1 = DesensitizationUtil.middle(str1);
		Assert.assertEquals(str2, middle1);
		String middle2 = DesensitizationUtil.middle("1");
		String middle3 = DesensitizationUtil.middle("12");
		String middle4 = DesensitizationUtil.middle("123");
		Assert.assertEquals(middle2, "*");
		Assert.assertEquals(middle3, "**");
		Assert.assertEquals(middle4, "1*3");
	}

	@Test
	public void test2() {
		String str1 = "12345678";
		String middle1 = DesensitizationUtil.all(str1);
		Assert.assertEquals("********", middle1);
	}

	@Test
	public void test3() {
		String str1 = "卢某某";
		String middle1 = DesensitizationUtil.chineseName(str1);
		Assert.assertEquals("卢**", middle1);
	}

	@Test
	public void test4() {
		String str1 = "596392912@qq.com";
		String middle1 = DesensitizationUtil.email(str1);
		Assert.assertEquals("5********@qq.com", middle1);
	}

	@Test
	public void test5() {
		String str1 = "18000000000";
		String middle1 = DesensitizationUtil.mobileNo(str1);
		Assert.assertEquals("180****0000", middle1);
	}

	@Test
	public void test6() {
		String str1 = "622260000000000001234";
		String middle1 = DesensitizationUtil.bankCard(str1);
		Assert.assertEquals("622260***********1234", middle1);
	}

	@Test
	public void test7() {
		String str1 = "11111";
		String middle1 = DesensitizationUtil.left(str1);
		Assert.assertEquals("***11", middle1);
	}

	@Test
	public void test8() {
		String str1 = "11111";
		String middle1 = DesensitizationUtil.right(str1);
		Assert.assertEquals("11***", middle1);
	}

	@Test
	public void test9() {
		String str1 = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzYzNlOGI5MTljMjg2OTMzYjIxMWRkNzNlNTgwZmJiNSIsImF1ZCI6Ik1pY2EtRmFzdC1XZWIiLCJpc3MiOiJNaWNhLUZhc3QtQXBpIiwiaWF0IjoxNjE1NDQzODQzLCJzdWIiOiJhZG1pbiIsIm5iZiI6MTYxNTQ0Mzg0MywiZXhwIjoxNjE4MDM1ODQzfQ.XjZV-f0r3FK75VjSdM7lNFf6rU4v_2jxPwD-_-NfyN4";
		String sensitive = DesensitizationUtil.sensitive(str1, 14, 14, 4);
		Assert.assertEquals("eyJhbGciOiJIUz****2jxPwD-_-NfyN4", sensitive);
	}

}
