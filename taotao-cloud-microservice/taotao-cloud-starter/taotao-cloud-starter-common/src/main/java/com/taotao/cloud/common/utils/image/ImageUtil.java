/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.common.utils.image;


import com.taotao.cloud.common.utils.exception.ExceptionUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

/**
 * image 工具
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class ImageUtil {

	/**
	 * 读取图片
	 *
	 * @param input 图片文件
	 * @return BufferedImage
	 */
	public static BufferedImage read(File input) {
		try {
			return ImageIO.read(input);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 读取图片
	 *
	 * @param input 图片文件流
	 * @return BufferedImage
	 */
	public static BufferedImage read(InputStream input) {
		try {
			return ImageIO.read(input);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 读取图片，http 或者 file 地址
	 *
	 * @param url 图片链接地址
	 * @return BufferedImage
	 */
	public static BufferedImage read(String url) {
		return StringUtil.isHttpUrl(url) ? readUrl(url) : read(new File(url));
	}

	/**
	 * 读取图片
	 *
	 * @param url 图片链接地址
	 * @return BufferedImage
	 */
	private static BufferedImage readUrl(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 读取图片
	 *
	 * @param url 图片链接地址
	 * @return BufferedImage
	 */
	public static BufferedImage read(URL url) {
		try {
			return ImageIO.read(url);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 写出图片
	 *
	 * @param im         RenderedImage to be written.
	 * @param formatName a String containing the informal name of the format.
	 * @param output     an ImageOutputStream to be written to.
	 * @return false if no appropriate writer is found.
	 */
	public static boolean write(RenderedImage im,
		String formatName,
		ImageOutputStream output) {
		try {
			return ImageIO.write(im, formatName, output);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 写出图片
	 *
	 * @param im         RenderedImage to be written.
	 * @param formatName a String containing the informal name of the format.
	 * @param output     an ImageOutputStream to be written to.
	 * @return false if no appropriate writer is found.
	 */
	public static boolean write(RenderedImage im,
		String formatName,
		File output) {
		try {
			return ImageIO.write(im, formatName, output);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 写出图片
	 *
	 * @param im         RenderedImage to be written.
	 * @param formatName a String containing the informal name of the format.
	 * @param output     an ImageOutputStream to be written to.
	 * @return false if no appropriate writer is found.
	 */
	public static boolean write(RenderedImage im,
		String formatName,
		OutputStream output) {
		try {
			return ImageIO.write(im, formatName, output);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 写出图片为 byte 数组
	 *
	 * @param im         RenderedImage to be written.
	 * @param formatName a String containing the informal name of the format.
	 * @return false if no appropriate writer is found.
	 */
	public static byte[] writeAsBytes(RenderedImage im, String formatName) {
		try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			if (ImageIO.write(im, formatName, output)) {
				return output.toByteArray();
			}
			throw new IllegalArgumentException(
				"ImageWriter formatName " + formatName + " writer is null.");
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 写出图片为 InputStream
	 *
	 * @param im         RenderedImage to be written.
	 * @param formatName a String containing the informal name of the format.
	 * @return false if no appropriate writer is found.
	 */
	public static ByteArrayInputStream writeAsStream(RenderedImage im, String formatName) {
		return new ByteArrayInputStream(writeAsBytes(im, formatName));
	}

}
