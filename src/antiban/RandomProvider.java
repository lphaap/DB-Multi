package antiban;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Set;

public class RandomProvider {
	 
	 public static int randomInt(int limit) {
		 int re = 0;
		 try {
			 
			re = SecureRandom.getInstanceStrong().nextInt(limit);
			
		} catch (NoSuchAlgorithmException e) {e.printStackTrace();}
		 
		 return re;
	 }
	 
	 public static int randomInt(int min, int max) {
		 int re = 0;
		 try {
			 
			re = SecureRandom.getInstanceStrong().nextInt(max-min) + min;
			
		} catch (NoSuchAlgorithmException e) {e.printStackTrace();}
		 
		 return re;
	 }
	 
	 public static int randomIntSplit(int split) {
		 int re = 0;
		 re = randomInt(split) + randomInt(split);
			
		 return re;
	 }
	 
	 /*
	  * Sleep from minTime -> maxTime
	  */
	 public static  void sleep(int minTime, int maxTime) {
			try {
				Thread.sleep(randomInt(maxTime-minTime)+minTime);
			} catch (InterruptedException e) {e.printStackTrace();}
	 }
	 
	 /*
	  * Sleep from 0 -> maxTime
	  */
	 public static  void sleep(int maxTime) {
			try {
				Thread.sleep(randomInt(maxTime));
			} catch (InterruptedException e) {e.printStackTrace();}
	 }
	 
	 /*
	  * Sleep from 0 -> splitx2
	  */
	 public static  void randomSplitSleep(int split) {
			try {
				Thread.sleep(randomInt(split) + randomInt(split));
			} catch (InterruptedException e) {e.printStackTrace();}
	 }
	 
	 /*
	  * 50/50 chance for false/true
	  */
	 public static boolean fiftyfifty() {
		 int re = 0;
		 re = randomInt(2);
		 if(re == 0) {
			 return true;
		 }
		 else {
			return false;
		 }
	 }
}
