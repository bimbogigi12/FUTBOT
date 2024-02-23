package it.shadowlab.botFut;

import java.awt.AWTException;
import java.awt.Robot;

public class Main {

	
	
	public static void main(String[] args) {
			try {
				Robot robot = new Robot();
				
				Bot bot = new Bot(robot);
				bot.start();
			} catch (AWTException e) {
				e.printStackTrace();
			}
	}
}
