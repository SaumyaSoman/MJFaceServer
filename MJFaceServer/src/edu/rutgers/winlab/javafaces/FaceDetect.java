package edu.rutgers.winlab.javafaces;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import jjil.algorithm.RgbAvgGray;
import jjil.core.Rect;
import jjil.core.RgbImage;
import jjil.j2se.RgbImageJ2se;

import edu.rutgers.winlab.jjil.Gray8DetectHaarMultiScale;

public class FaceDetect {
	
	public void run(String fileName)  {
		try {
			BufferedImage bi = ImageIO.read(new File(fileName));
			findfaces(bi, 1, 40); 
		} catch (Throwable e) {
			throw new IllegalStateException(e);
		}
	}

	private void findfaces(BufferedImage bi, int minScale, int maxScale) {
		try{
			InputStream is  = new FileInputStream("E:\\workspace\\MJFaceServer\\src\\jjilexample\\haar\\frontaldefault.txt");
			Gray8DetectHaarMultiScale detectHaar = new Gray8DetectHaarMultiScale(is, minScale, maxScale);
			RgbImage im = RgbImageJ2se.toRgbImage(bi);
			RgbAvgGray toGray = new RgbAvgGray();
			toGray.push(im);
			List<Rect> results = detectHaar.pushAndReturn(toGray.getFront(),bi);
			System.out.println("Found "+results.size()+" faces");
		} catch (Throwable e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static void main(String[] args){
		FaceDetect f=new FaceDetect();
        f.run("C:\\Users\\Saumya\\Pictures\\pics\\gsa.jpg");
	}
}
