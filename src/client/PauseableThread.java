package client;

public interface PauseableThread {

	//Pauses this thread
	void pauseThread();
	
	//Resumes this thread
	void resumeThread();
	
	//Returns true if thread is paused, else false
	boolean isPaused();
	
}
