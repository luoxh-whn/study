package test;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @author Administrator
 *
 */
public class JnaTest {

	// �̳�Library�����ڼ��ؿ��ļ�
	public interface Clibrary extends Library {
		// ����hello.dll���ӿ�
		Clibrary INSTANTCE = (Clibrary) Native.loadLibrary("hello", Clibrary.class);

		// �˷���Ϊ���ӿ��еķ���
		void test();
	}

	public static void main(String[] args) {
		// ����
		Clibrary.INSTANTCE.test();
	}
}
