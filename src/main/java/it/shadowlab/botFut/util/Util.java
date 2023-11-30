package it.shadowlab.botFut.util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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

	public static String Read(Robot robot, Rectangle rect, boolean saveImage) {
		BufferedImage image = robot.createScreenCapture(rect);

		String stringRead = "";
		try {
			stringRead = tesseract.doOCR(image);

			if (saveImage) {
				Date date = new Date();
				String pattern = "yyyy_MM_dd HH.mm.ss SSSS";
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
		write(robot, text, 300);
	}

	public static void write(Robot robot, String text, long waitmillis) {
		try {
			text.replace("à", "a");

			text.replace("è", "e");
			text.replace("é", "e");

			text.replace("ì", "i");

			text.replace("ò", "o");
			text.replace("ù", "u");

			for (char charT : text.toCharArray()) {
				if (charT == '@') {
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyPress(KeyEvent.VK_NUMPAD6);
					robot.keyPress(KeyEvent.VK_NUMPAD4);
					robot.keyRelease(KeyEvent.VK_ALT);
				} else if (charT == '/') {
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyPress(KeyEvent.VK_NUMPAD4);
					robot.keyPress(KeyEvent.VK_NUMPAD7);
					robot.keyRelease(KeyEvent.VK_ALT);
				} else if (charT == ':') {
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyPress(KeyEvent.VK_NUMPAD5);
					robot.keyPress(KeyEvent.VK_NUMPAD8);
					robot.keyRelease(KeyEvent.VK_ALT);
				} else if (charT == '?') {
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyPress(KeyEvent.VK_NUMPAD6);
					robot.keyPress(KeyEvent.VK_NUMPAD3);
					robot.keyRelease(KeyEvent.VK_ALT);
				} else if (charT == '=') {
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyPress(KeyEvent.VK_NUMPAD6);
					robot.keyPress(KeyEvent.VK_NUMPAD1);
					robot.keyRelease(KeyEvent.VK_ALT);
				} else if (charT == '&') {
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyPress(KeyEvent.VK_NUMPAD3);
					robot.keyPress(KeyEvent.VK_NUMPAD8);
					robot.keyRelease(KeyEvent.VK_ALT);
				} else if (charT == '_') {
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyPress(KeyEvent.VK_NUMPAD9);
					robot.keyPress(KeyEvent.VK_NUMPAD5);
					robot.keyRelease(KeyEvent.VK_ALT);
				} else if (charT == '-') {
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyPress(KeyEvent.VK_NUMPAD4);
					robot.keyPress(KeyEvent.VK_NUMPAD5);
					robot.keyRelease(KeyEvent.VK_ALT);
				} else if (charT == 'G') {
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(java.awt.event.KeyEvent.getExtendedKeyCodeForChar(charT));
					robot.keyRelease(KeyEvent.VK_SHIFT);
				} else {

					robot.keyPress(java.awt.event.KeyEvent.getExtendedKeyCodeForChar(charT));
				}
				try {
					Thread.sleep(waitmillis);
				} catch (InterruptedException e) {
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void waitAction(long millis) {
		try {
			Thread.sleep(millis + (int) Math.random() + 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static int convertToInt(String read) {
		read = read.replaceAll("Z", "7");
		read = read.replaceAll("n", "11");
		read = read.replaceAll(" ", "");
		return Integer.valueOf(read.replace(",", "").replace(".", ""));
	}

	public static String normalizePlauerName(String playerName) {
		playerName = playerName.replace("(ST)", "");
		playerName = playerName.replace("(GK)", "");
		playerName = playerName.replace("(CDM)", "");
		playerName = playerName.replace("(CB)", "");
		playerName = playerName.replace("(LW)", "");
		playerName = playerName.replace("(RW)", "");
		playerName = playerName.replace("(CAM)", "");
		playerName = playerName.replace("(CM)", "");
		playerName = playerName.replace("(RB)", "");
		playerName = playerName.replace("(LB)", "");
		playerName = playerName.replace("(CB)", "");
		playerName = playerName.replace("(RM)", "");
		playerName = playerName.replace("(CF)", "");
		playerName = playerName.replace("(RWB)", "");
		playerName = playerName.replace("(LWB)", "");
		playerName = playerName.replace("(6K)", "");
		playerName = playerName.replace("(LM)", "");
		playerName = playerName.replace("ğ", "g");
		playerName = playerName.replace("Ç", "c");
		playerName = playerName.replace("á", "a");
		playerName = playerName.replace("Ø", "o");
		playerName = playerName.replace("ü", "u");
		playerName = playerName.replace("ó", "o");
		playerName = playerName.replace("ã", "a");
		playerName = playerName.replace("ñ", "n");
		playerName = playerName.replace("-", " ");
		playerName = playerName.replace("_", " ");
		playerName = playerName.replace("~", "");
		playerName = playerName.replace("\"", "");
		playerName = playerName.replace(")", "");
		playerName = playerName.replace("+", "");
		playerName = playerName.replaceAll("è", "e");
		playerName = playerName.replaceAll("é", "e");
		playerName = playerName.replaceAll("ò", "o");
		playerName = playerName.replaceAll("ì", "i");
		playerName = playerName.replaceAll("à", "a");
		playerName = playerName.replaceAll("ù", "u");
		playerName = playerName.replace(")", "");
		playerName = playerName.replace("|", "");
		playerName = playerName.replace("*", "");
		playerName = playerName.replace(".", "");
		playerName = playerName.replace("'", "");
		playerName = playerName.replace("‘", "");
		playerName = playerName.replace("{", "");
		playerName = playerName.replace("> ", "");
		playerName = playerName.replaceAll("_", "");

		return playerName.trim();
	}
	
}
