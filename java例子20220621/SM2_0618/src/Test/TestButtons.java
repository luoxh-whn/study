package Test;
/*                                                                                                        
* TestButtons.java                                                                                        
* @author Fancy                                                                                           
*/                                                                                                        
                                                                                                          
import java.awt.event.ActionEvent;                                                                         
import java.awt.event.ActionListener;                                                                      
import java.awt.event.ItemEvent;                                                                           
import java.awt.event.ItemListener;                                                                        
                                                                                                          
import javax.swing.ButtonGroup;                                                                           
import javax.swing.JButton;                                                                               
import javax.swing.JCheckBox;                                                                             
import javax.swing.JFrame;                                                                                
import javax.swing.JLabel;                                                                                
import javax.swing.JRadioButton;                                                                          
import javax.swing.JToggleButton;                                                                         

 public final class TestButtons {                                       
                                                                        
    public static void main(String[] args) {                            
        TestButtons tb = new TestButtons();                             
        tb.show();                                                      
    }                                                                   
                                                                        
    JFrame frame = new JFrame("测试   按钮");                          
                                                                        
    JButton jButton = new JButton("按钮"); // 按钮                     
                                                                        
    JToggleButton toggle = new JToggleButton("切换  按钮"); // 切换按钮  
                                                                        
    JCheckBox checkBox = new JCheckBox("复选 按钮"); // 复选按钮            
                                                                        
    JRadioButton radio1 = new JRadioButton("单选 按钮1"); // 单选按钮   
                                                                        
    JRadioButton radio2 = new JRadioButton("单选 按钮2");           
                                                                        
    JRadioButton radio3 = new JRadioButton("单选 按钮 3");           
                                                                        
    JLabel label = new JLabel("不是按钮, 是静态。"); // 不是按钮，是静态
                                                                        
    public TestButtons() {                                              
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           
        frame.getContentPane().setLayout(new java.awt.FlowLayout());    
                                                                        
        // 为一般按钮添加动作监听器                                                 
        jButton.addActionListener(new ActionListener() {                
            public void actionPerformed(ActionEvent ae) {               
                label.setText("你点击了按钮");                   
            }                                                           
        });                                                             
                                                                        
        // 为切换按钮添加动作监听器                                                 
        toggle.addActionListener(new ActionListener() {                 
            public void actionPerformed(ActionEvent ae) {               
                JToggleButton toggle = (JToggleButton) ae.getSource();  
                if (toggle.isSelected()) {                              
                    label.setText("你选择了切换妞妞");        
                } else {                                                
                    label.setText("你离开了切换按钮");      
                }                                                       
            }                                                           
        });                                                             
                                                                        
        // 为复选按钮添加条目监听器                                                 
        checkBox.addItemListener(new ItemListener() {                   
            public void itemStateChanged(ItemEvent e) {                 
                JCheckBox cb = (JCheckBox) e.getSource();               
                label.setText("复选框的状态 " + cb.isSelected());
            }                                                           
        });                                                             
                                                                        
        // 用一个按钮组对象包容一组单选按钮                                             
        ButtonGroup group = new ButtonGroup();                          
        // 生成一个新的动作监听器对象，备用                                             
        ActionListener al = new ActionListener() {                      
            public void actionPerformed(ActionEvent ae) {               
                JRadioButton radio = (JRadioButton) ae.getSource();     
                if (radio == radio1) {                                  
                    label.setText("你选择了单选 1");       
                } else if (radio == radio2) {                           
                    label.setText("你选择了单选2");       
                } else {                                                
                    label.setText("你选择了单选3");       
                }                                                       
            }                                                           
        };                                                              
        // 为各单选按钮添加动作监听器                                                
        radio1.addActionListener(al);                                   
        radio2.addActionListener(al);                                   
        radio3.addActionListener(al);                                   
        // 将单选按钮添加到按钮组中                                                 
        group.add(radio1);                                              
        group.add(radio2);                                              
        group.add(radio3);                                              
                                                                        
        frame.getContentPane().add(jButton);                            
        frame.getContentPane().add(toggle);                             
        frame.getContentPane().add(checkBox);                           
         frame.getContentPane().add(radio1);                            
         frame.getContentPane().add(radio2);                            
         frame.getContentPane().add(radio3);                            
         frame.getContentPane().add(label);                             
                                                                        
         frame.setSize(200, 250);                                       
     }                                                                  
                                                                        
     public void show() {                                               
         frame.setVisible(true);                                        
     }                                                                  
                                                                        
 }                                                                      
                                                                        
                                                                        
                                                                         