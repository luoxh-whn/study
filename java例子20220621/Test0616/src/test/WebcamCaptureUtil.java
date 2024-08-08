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

 // ����ͷ���չ����� : ���ñ�������ͷץȡ��������
 //@author qxl
 // @date 2020/10/12
// 
@Slf4j
@Data
public class WebcamCaptureUtil {

    // ��Ƶ�������
    private VideoCapture vc;
    // ��Ƶ��ʾ JFrame ���ڶ���
    private JFrame windows;
    // ��Ƶ��ʾ����
    private VideoDisplay<MBFImage> vd;
    //��������ͼƬ��ż���
    private LinkedList<BufferedImage> faceImages = new LinkedList<>();

    /**
     * ������ͷ��������,���ݴ��� faceImages
     */
    public void faceCapture() throws VideoCaptureException{
        // ������Ƶ�������
        vc = new VideoCapture(320,240);
       
        //���� JFrame ����,������ʾ��Ƶ
        windows = DisplayUtilities.makeFrame("����ͷ���������...");
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windows.setVisible(true);
        //������Ƶ��ʾ����
        vd = VideoDisplay.createVideoDisplay(vc,windows);
        // ������Ƶ
        vd.addVideoListener(
             //��Ƶ��ʾ�ļ���--���ÿһ֡��ͼƬ
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
     * ��������ͼƬ
     * @param image  Ҫ�����image
     * @param savePath  �����·��
     * @param imageName  ͼƬ����
     */
    public void saveImage(BufferedImage image,String savePath,String imageName) throws IOException {
        File path = new File(savePath);
        if (!path.exists()) {//����ļ������ڣ��򴴽���Ŀ¼
            path.mkdirs();
        }
        File file = new File(savePath + "/" + imageName + ".png");
        ImageIO.write(image,"png",file);
    }


    /**
     * �ر�����ͷ����������
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
            // �ر� jFrame ����
            windows.removeNotify();
        }
    }

    /**
     * ������ͼƬ
     */
    public void clearFaceImages(){
        faceImages.clear();
    }


    public static void main(String[] args) {
        WebcamCaptureUtil webcamCaptureUtil = new WebcamCaptureUtil();
        try {
            //��ʼ��������
            webcamCaptureUtil.faceCapture();
            //�ȴ���������
            LinkedList<BufferedImage> faceImages = webcamCaptureUtil.getFaceImages();
            String filePath = "D:\\" + "/picture/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            //�����ȡ10��ͼƬ֮��,�����ȶԳɹ�
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
            //�ر�����ͷ
            webcamCaptureUtil.closeWebcam();
            //��ӡ����Ԫ�ظ���
            System.out.println("δ���ǰ"+faceImages.size());
            webcamCaptureUtil.clearFaceImages();
            System.out.println("��պ�"+ faceImages.size());
        } catch (VideoCaptureException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
