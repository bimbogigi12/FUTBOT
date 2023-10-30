package it.shadowlab.botFut.util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Util {
	static Tesseract tesseract = new Tesseract();
	static Logger log = LoggerFactory.getLogger(Util.class);
	
	static {
		tesseract.setLanguage("eng");
		tesseract.setDatapath("C:\\Users\\grass\\git\\tessdata");
	}
	
	public static String Read(Robot robot, Rectangle rect, boolean saveImage){
		BufferedImage image = robot.createScreenCapture(rect);
		
		
		String stringRead = "";
		try {
			stringRead = tesseract.doOCR(image);
			
			if (saveImage) {
				Date date = new Date();
				String pattern = "yyyy_MM_dd HH.mm SSS";
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				ImageIO.write(image, "jpg", new File("d:\\temp\\FUT\\" + sdf.format(date) + ".jpg"));

			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringRead;
		
	}
	
	public static void click(Robot robot, Dimension dim) {
		robot.mouseMove((int) Math.round(dim.getHeight()), (int) Math.round(dim.getWidth()));
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
	public static void write(Robot robot, String text) {

		for (char charT: text.toCharArray()) {
			robot.keyPress(java.awt.event.KeyEvent.getExtendedKeyCodeForChar(charT));
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
			}
		}
		
		
	} 
	
	public static void waitAction(long millis) {
		try {
			Thread.sleep(millis+(int)Math.random()+1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static int convertToInt(String read) {
		return Integer.valueOf(read.replace(",", "").replace(".", ""));
	}
}
