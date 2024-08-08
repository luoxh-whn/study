package Test;

/**  
 * @(#) TestFrame.java  
 * @author James  
 */ 
 
import javax.swing.*;  
import java.awt.event.*;  
 
public class TestFrame extends JFrame {  
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -2853471479497684992L;
	private int counter = 0;  
 
    public TestFrame() {  
        /* 使用匿名类添加一个窗口监听器 */ 
        addWindowListener(new WindowAdapter() {  
            public void windowClosing(WindowEvent e) {  
                System.out.println(  
                    "Exit when Closed event");  
                //退出应用程序  
                System.exit(0);  
           }  
 
            public void windowActivated(WindowEvent e) {  
                // 改变窗口标题  
                setTitle("Test Frame " + counter++);  
            }  
        });  
 
        // 设置窗口为固定大小  
        setResizable(true);  
       setSize(100, 100);  
    }  
 
    @SuppressWarnings("deprecation")
	public static void main(String[] args) {  
        TestFrame tf = new TestFrame();  
        tf.show();  
    }  
 
} 
