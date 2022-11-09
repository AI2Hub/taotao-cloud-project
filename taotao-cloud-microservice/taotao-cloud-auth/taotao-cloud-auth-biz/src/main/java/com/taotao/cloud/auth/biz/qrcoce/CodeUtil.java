package com.taotao.cloud.auth.biz.qrcoce;

/**
 * 二维码工具类
 */
public interface CodeUtil {
	/**
	 * 获取过期二维码存储信息
	 *
	 * @return 二维码值对象
	 */
	static CodeData getExpireCodeInfo() {
		return new CodeData(CodeStatusEnum.EXPIRE, "二维码已更新");
	}

	/**
	 * 获取未使用二维码存储信息
	 *
	 * @return 二维码值对象
	 */
	static CodeData getUnusedCodeInfo() {
		return new CodeData(CodeStatusEnum.UNUSED, "二维码等待扫描");
	}

	/**
	 * 获取已扫码二维码存储信息
	 */
	static CodeData getConfirmingCodeInfo() {
		return new CodeData(CodeStatusEnum.CONFIRMING, "二维码扫描成功，等待确认");
	}

	/**
	 * 获取已扫码确认二维码存储信息
	 *
	 * @return 二维码值对象
	 */
	static CodeData getConfirmedCodeInfo(String token) {
		return new CodeData(CodeStatusEnum.CONFIRMED, "二维码已确认", token);
	}
}
