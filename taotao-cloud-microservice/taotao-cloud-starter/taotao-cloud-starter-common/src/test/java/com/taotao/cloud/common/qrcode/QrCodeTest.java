package com.taotao.cloud.common.qrcode;

import com.taotao.cloud.common.utils.common.QrCodeUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import org.testcontainers.shaded.com.google.common.base.Charsets;

/**
 */
class QrCodeTest {

	@Test
	void test1() {
		String text = "恭喜发财";
		BufferedImage bufferedImage = QrCodeUtils.form(text).toImage();
		String read = QrCodeUtils.read(bufferedImage);
		Assertions.assertEquals(text, read);
	}

	public static void main(String[] args) {
		// 生成 二维码
		QrCodeUtils.form("牛年大吉")
			.size(512) // 默认 512，可以不设置
			.backGroundColor(Color.WHITE) // 默认白色，可以不设置
			.foreGroundColor(Color.BLACK) // 默认黑色，可以不设置
			.encode(Charsets.UTF_8) // 默认 UTF_8，可以不设置
			.imageFormat("png") // 默认 png，可以不设置
			.deleteMargin(true) // 删除白边，默认为 true，可以不设置
			.logo("/Users/lcm/Desktop/tt.png") // 设置二维码 logo
			.toFile("/Users/lcm/Desktop/xxx1.png"); // 写出，同类方法有 toImage、toStream、toBytes

		// 二维码读取
		String text = QrCodeUtils.read("/Users/lcm/Desktop/xxx1.png");
		System.out.println(text);
	}

}
