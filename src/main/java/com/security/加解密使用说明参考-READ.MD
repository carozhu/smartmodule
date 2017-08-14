# rsa加密算法说明参考
* RSA加密 https://blog.cnbluebox.com/blog/2014/03/19/rsajia-mi/

## 参考:
* RSA AES DES 加解密 https://github.com/zhengxiaopeng/Rocko-Android-Demos
* Java中使用OpenSSL生成的RSA公私钥进行数据加解密 http://blog.csdn.net/chaijunkun/article/details/7275632
* java读取openssl生成的private key文件生成密钥的问题 http://shuany.iteye.com/blog/730910
* Bouncy Castle http://bouncycastle.org/checksums.html

## 绝对正确
* 私钥加密  公钥解密 更多请参考
* 参考:http://www.cnblogs.com/whoislcj/p/5470095.html

## 数据加解密在线
* 在线加解密 http://tool.chacuo.net/

## useage:
```java
 * Copyright 2015 Rocko (http://rocko.xyz) <rocko.zxp@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.

package xyz.rocko.security;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

import kotlin.io.NoSuchFileException;
import xyz.rocko.security.config.SecurityConfig;
import xyz.rocko.security.databinding.MainActivityBinding;
import xyz.rocko.security.security.AESCipherStrategy;
import xyz.rocko.security.security.CipherStrategy;
import xyz.rocko.security.security.DESCipherStrategy;
import xyz.rocko.security.security.RSACipherStrategy;

public class MainActivity extends AppCompatActivity {

	private MainActivityBinding mBinding;

	RSACipherStrategy rsaCipherStrategy = new RSACipherStrategy();
	CipherStrategy aesCipherStrategy = new AESCipherStrategy(SecurityConfig.KEY);
	CipherStrategy desCipherStrategy = new DESCipherStrategy(SecurityConfig.KEY);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
		setSupportActionBar(mBinding.toolbar);

	}

	public void onClick(View v) {
		switch (v.getId()) { // Note: 加解密最好不要放在主线程，demo 为了简单展示
			case R.id.encrypt:
				encrypt();
				break;
			case R.id.decrypt:
				decrypt();
				break;
		}

	}

	/**
	 * 加密
	 */
	private void encrypt() {
		String sourceContent = mBinding.sourceContent.getText().toString().trim();
		// rsa 公钥加密
		rsaCipherStrategy.initPublicKey(SecurityConfig.RSA_PUCLIC_KEY);
		//or
		/*try {
			InputStream inPublic = getResources().getAssets().open("rsa_public_key.pem");
			rsaCipherStrategy.initPublicKey(inPublic);
		}catch (Exception e){

		}*/

		String rsaEncrypt = rsaCipherStrategy.encrypt(sourceContent);
		// aes
		String aesEncrypt = aesCipherStrategy.encrypt(sourceContent);
		//des
		String desEncrypt = desCipherStrategy.encrypt(sourceContent);


		mBinding.encryptRsa.setText(rsaEncrypt);
		mBinding.encryptAes.setText(aesEncrypt);
		mBinding.encryptDes.setText(desEncrypt);
	}

	/**
	 * 解密
	 */
	private void decrypt() {
		String rsaEncrypt = mBinding.encryptRsa.getText().toString().trim();
		String aesEncrypt = mBinding.encryptAes.getText().toString().trim();
		String desEncrypt = mBinding.encryptDes.getText().toString().trim();
		// rsa 私钥解密
		rsaCipherStrategy.initPrivateKey(SecurityConfig.RSA_PRIVATE_KEY);
		String rsaDecrypt = rsaCipherStrategy.decrypt(rsaEncrypt);
		// aes
		String aesDecrypt = aesCipherStrategy.decrypt(aesEncrypt);
		// des
		String desDecrypt = desCipherStrategy.decrypt(desEncrypt);


		mBinding.decryptRsa.setText(rsaDecrypt);
		mBinding.decryptAes.setText(aesDecrypt);
		mBinding.decryptDes.setText(desDecrypt);
	}
}
```
## 通过openssl生成rsa密匙对
```
1：先来生成私钥
#:openssl genrsa -out rsa_private_key.pem 1024
Generating RSA private key, 1024 bit long modulus
.......................++++++
..++++++
e is 65537 (0x10001)

这条命令让openssl随机生成了一份私钥，加密长度是1024位。加密长度是指理论上最大允许”被加密的信息“长度的限制，也就是明文的长度限制。
随着这个参数的增大（比方说2048），允许的明文长度也会增加，但同时也会造成计算复杂度的极速增长。一般推荐的长度就是1024位（128字节）。
我们来看一下私钥的内容：
# cat rsa_private_key.pem
-----BEGIN RSA PRIVATE KEY-----
MIICWwIBAAKBgQChDzcjw/rWgFwnxunbKp7/4e8w/UmXx2jk6qEEn69t6N2R1i/L
mcyDT1xr/T2AHGOiXNQ5V8W4iCaaeNawi7aJaRhtVx1uOH/2U378fscEESEG8XDq
ll0GCfB1/TjKI2aitVSzXOtRs8kYgGU78f7VmDNgXIlk3gdhnzh+uoEQywIDAQAB
AoGAaeKk76CSsp7k90mwyWP18GhLZru+vEhfT9BpV67cGLg1owFbntFYQSPVsTFm
U2lWn5HD/IcV+EGaj4fOLXdM43Kt4wyznoABSZCKKxs6uRciu8nQaFNUy4xVeOfX
PHU2TE7vi4LDkw9df1fya+DScSLnaDAUN3OHB5jqGL+Ls5ECQQDUfuxXN3uqGYKk
znrKj0j6pY27HRfROMeHgxbjnnApCQ71SzjqAM77R3wIlKfh935OIV0aQC4jQRB4
iHYSLl9lAkEAwgh4jxxXeIAufMsgjOi3qpJqGvumKX0W96McpCwV3Fsew7W1/msi
suTkJp5BBvjFvFwfMAHYlJdP7W+nEBWkbwJAYbz/eB5NAzA4pxVR5VmCd8cuKaJ4
EgPLwsjI/mkhrb484xZ2VyuICIwYwNmfXpA3yDgQWsKqdgy3Rrl9lV8/AQJAcjLi
IfigUr++nJxA8C4Xy0CZSoBJ76k710wdE1MPGr5WgQF1t+P+bCPjVAdYZm4Mkyv0
/yBXBD16QVixjvnt6QJABli6Zx9GYRWnu6AKpDAHd8QjWOnnNfNLQHue4WepEvkm
CysG+IBs2GgsXNtrzLWJLFx7VHmpqNTTC8yNmX1KFw==
-----END RSA PRIVATE KEY-----

内容都是标准的ASCII字符,密钥文件最终将数据通过Base64编码进行存储。可以看到上述密钥文件内容每一行的长度都很规律。
这是由于RFC2045中规定：The encoded output stream must be represented in lines of no more than 76 characters each。
也就是说Base64编码的数据每行最多不超过76字符，对于超长数据需要按行分割

2: 根据私钥生成公钥
# openssl rsa -in rsa_private_key.pem -out rsa_public_key.pem -pubout
回车
writing RSA key

显示key
# cat rsa_public_ley.pem
-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChDzcjw/rWgFwnxunbKp7/4e8w
/UmXx2jk6qEEn69t6N2R1i/LmcyDT1xr/T2AHGOiXNQ5V8W4iCaaeNawi7aJaRht
Vx1uOH/2U378fscEESEG8XDqll0GCfB1/TjKI2aitVSzXOtRs8kYgGU78f7VmDNg
XIlk3gdhnzh+uoEQywIDAQAB
-----END PUBLIC KEY-----

这时候的私钥还不能直接被使用，需要进行PKCS#8编码：
# openssl pkcs8 -topk8 -in rsa_private_key.pem -out pkcs8_rsa_private_key.pem -nocrypt
命令中指明了输入私钥文件为rsa_private_key.pem，输出私钥文件为pkcs8_rsa_private_key.pem，不采用任何二次加密（-nocrypt）
再来看一下，编码后的私钥文件是不是和之前的私钥文件不同了：
# cat pkcs8_rsa_private_key.pem
-----BEGIN PRIVATE KEY-----
MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKEPNyPD+taAXCfG
6dsqnv/h7zD9SZfHaOTqoQSfr23o3ZHWL8uZzINPXGv9PYAcY6Jc1DlXxbiIJpp4
1rCLtolpGG1XHW44f/ZTfvx+xwQRIQbxcOqWXQYJ8HX9OMojZqK1VLNc61GzyRiA
ZTvx/tWYM2BciWTeB2GfOH66gRDLAgMBAAECgYBp4qTvoJKynuT3SbDJY/XwaEtm
u768SF9P0GlXrtwYuDWjAVue0VhBI9WxMWZTaVafkcP8hxX4QZqPh84td0zjcq3j
DLOegAFJkIorGzq5FyK7ydBoU1TLjFV459c8dTZMTu+LgsOTD11/V/Jr4NJxIudo
MBQ3c4cHmOoYv4uzkQJBANR+7Fc3e6oZgqTOesqPSPqljbsdF9E4x4eDFuOecCkJ
DvVLOOoAzvtHfAiUp+H3fk4hXRpALiNBEHiIdhIuX2UCQQDCCHiPHFd4gC58yyCM
6Leqkmoa+6YpfRb3oxykLBXcWx7DtbX+ayKy5OQmnkEG+MW8XB8wAdiUl0/tb6cQ
FaRvAkBhvP94Hk0DMDinFVHlWYJ3xy4pongSA8vCyMj+aSGtvjzjFnZXK4gIjBjA
2Z9ekDfIOBBawqp2DLdGuX2VXz8BAkByMuIh+KBSv76cnEDwLhfLQJlKgEnvqTvX
TB0TUw8avlaBAXW34/5sI+NUB1hmbgyTK/T/IFcEPXpBWLGO+e3pAkAGWLpnH0Zh
Fae7oAqkMAd3xCNY6ec180tAe57hZ6kS+SYLKwb4gGzYaCxc22vMtYksXHtUeamo
1NMLzI2ZfUoX
-----END PRIVATE KEY-----

至此，可用的密钥对已经生成好了，私钥使用pkcs8_rsa_private_key.pem，公钥采用rsa_public_key.pem
```
## 注意事项
```
#:openssl genrsa -out rsa_private_key.pem 1024
该命令生成的 rsa_private_key.pem 默认是PKCS#1格式，未经过PKCS#8编码的私钥文件。
JAVA支持私钥文件编码是PKCS#1格式，但需要额外处理，只不过多写两行代码而已：

RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure((ASN1Sequence) ASN1Sequence.fromByteArray(priKeyData));
RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
KeyFactory keyFactory= KeyFactory.getInstance("RSA");
PrivateKey priKey= keyFactory.generatePrivate(rsaPrivKeySpec);

首先将PKCS#1的私钥文件读取出来（注意去掉减号开头的注释内容），然后使用Base64解码读出的字符串，便得到priKeyData，也就是第一行代码中的参数。最后一行得到了私钥。接下来的用法就没什么区别了。
参考文献：https://community.oracle.com/thread/1529240?start=0&tstart=0