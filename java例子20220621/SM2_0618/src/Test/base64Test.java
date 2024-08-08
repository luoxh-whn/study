package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
//import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class base64Test {
	
	@Test
	public void jdk8() {
		// 编码
		String encodedStr = java.util.Base64.getEncoder().encodeToString("123".getBytes());
		Assertions.assertEquals(encodedStr, "MTIz1");

		// 解码
		byte[] decodeBytes = java.util.Base64.getDecoder().decode("MTIz");
		Assertions.assertEquals(new String(decodeBytes), "123");
	}

	@Test
	public void jdk8_1() {
		// 编码
		String encodedStr = java.util.Base64.getEncoder().encodeToString("123".getBytes());
		Assertions.assertEquals(encodedStr, "MTIz");

		// 解码
		byte[] decodeBytes = java.util.Base64.getDecoder().decode("MTIz");
		Assertions.assertEquals(new String(decodeBytes), "123");
	}
}
