/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.common.tuple;


import com.taotao.cloud.common.utils.secure.RSAUtil;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * rsa 的 key pair 封装
 *
  * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class KeyPair {

	private final java.security.KeyPair keyPair;

	public KeyPair(java.security.KeyPair keyPair) {
		this.keyPair = keyPair;
	}

	public PublicKey getPublic() {
		return keyPair.getPublic();
	}

	public PrivateKey getPrivate() {
		return keyPair.getPrivate();
	}

	public byte[] getPublicBytes() {
		return this.getPublic().getEncoded();
	}

	public byte[] getPrivateBytes() {
		return this.getPrivate().getEncoded();
	}

	public String getPublicBase64() {
		return RSAUtil.getKeyString(this.getPublic());
	}

	public String getPrivateBase64() {
		return RSAUtil.getKeyString(this.getPrivate());
	}

	@Override
	public String toString() {
		return "PublicKey=" + this.getPublicBase64() + '\n' + "PrivateKey="
			+ this.getPrivateBase64();
	}
}
