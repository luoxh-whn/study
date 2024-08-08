package test;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @author Administrator
 *
 */
public class JnaTest {

	// 继承Library，用于加载库文件
	public interface Clibrary extends Library {
		// 加载hello.dll链接库
		Clibrary INSTANTCE = (Clibrary) Native.loadLibrary("hello", Clibrary.class);

		// 此方法为链接库中的方法
		void test();
	}

	public static void main(String[] args) {
		// 调用
		Clibrary.INSTANTCE.test();
	}
}
