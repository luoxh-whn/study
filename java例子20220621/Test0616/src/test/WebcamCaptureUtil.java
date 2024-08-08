package test;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.openimaj.image.*;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

 // 摄像头拍照工具类 : 调用本机摄像头抓取人脸拍照
 //@author qxl
 // @date 2020/10/12
// 
@Slf4j
@Data
public class WebcamCaptureUtil {

    // 视频捕获对象
    private VideoCapture vc;
    // 视频显示 JFrame 窗口对象
    private JFrame windows;
    // 视频显示对象
    private VideoDisplay<MBFImage> vd;
    //捕获人脸图片存放集合
    private LinkedList<BufferedImage> faceImages = new LinkedList<>();

    /**
     * 打开摄像头捕获人脸,数据存入 faceImages
     */
    public void faceCapture() throws VideoCaptureException{
        // 创建视频捕获对象
        vc = new VideoCapture(320,240);
       
        //创建 JFrame 窗口,用于显示视频
        windows = DisplayUtilities.makeFrame("摄像头人脸检测中...");
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windows.setVisible(true);
        //创建视频显示对象
        vd = VideoDisplay.createVideoDisplay(vc,windows);
        // 监听视频
        vd.addVideoListener(
             //视频显示的监听--针对每一帧的图片
            new VideoDisplayListener<MBFImage>(){
                public void beforeUpdate(MBFImage frame){
                    FaceDetector<DetectedFace,FImage> fd = new HaarCascadeDetector(40);
                    List<DetectedFace> faces = fd.detectFaces(Transforms.calculateIntensity(frame));
                    for(DetectedFace face : faces ) {
                        frame.drawShape(face.getBounds(), RGBColour.RED);
                        BufferedImage image = ImageUtilities.createBufferedImageForDisplay(face.getFacePatch());
                        faceImages.addLast(image);
                    }
                }

                public void afterUpdate(VideoDisplay<MBFImage> display){
                }
        });
    }

    /**
     * 保存人脸图片
     * @param image  要保存的image
     * @param savePath  保存的路径
     * @param imageName  图片名称
     */
    public void saveImage(BufferedImage image,String savePath,String imageName) throws IOException {
        File path = new File(savePath);
        if (!path.exists()) {//如果文件不存在，则创建该目录
            path.mkdirs();
        }
        File file = new File(savePath + "/" + imageName + ".png");
        ImageIO.write(image,"png",file);
    }


    /**
     * 关闭摄像头及人脸捕获
     */
    public void closeWebcam(){
        if(vc != null){
            vc.stopCapture();
            vc.close();
        }
        if(vd != null){
            vd.close();
        }
        if(windows != null){
            // 关闭 jFrame 窗口
            windows.removeNotify();
        }
    }

    /**
     * 清理缓存图片
     */
    public void clearFaceImages(){
        faceImages.clear();
    }


    public static void main(String[] args) {
        WebcamCaptureUtil webcamCaptureUtil = new WebcamCaptureUtil();
        try {
            //开始人脸捕获
            webcamCaptureUtil.faceCapture();
            //等待捕获人脸
            LinkedList<BufferedImage> faceImages = webcamCaptureUtil.getFaceImages();
            String filePath = "D:\\" + "/picture/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            //假设获取10张图片之后,人脸比对成功
            int count = 0;
            while (count<10){
              // if (CollectionUtil.isNotEmpty(faceImages) ){
            	if(faceImages.size()>0){
                   String time = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
                   BufferedImage image = faceImages.pollFirst();
                   if (image != null){
                       webcamCaptureUtil.saveImage(image,filePath,time);
                   }
                   Thread.sleep(1000L);
                   count++;
               }
            }
            //关闭摄像头
            webcamCaptureUtil.closeWebcam();
            //打印集合元素个数
            System.out.println("未清空前"+faceImages.size());
            webcamCaptureUtil.clearFaceImages();
            System.out.println("清空后"+ faceImages.size());
        } catch (VideoCaptureException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
