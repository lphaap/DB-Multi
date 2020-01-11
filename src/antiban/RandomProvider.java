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
}
