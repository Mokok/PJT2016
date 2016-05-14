/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.exception.InvalidPreviousThreadTaskException;
import core.worker.ConcatTask;
import core.worker.SplitTask;
import core.worker.TaskStatus;
import core.worker.TranscodeTask;
import java.io.FileNotFoundException;
import java.util.EventListener;

/**
 *
 * @author Mokok
 */
public class ThreadTaskEndListener implements EventListener {
	
	private final ThreadCoordinator coordinator;
	
	ThreadTaskEndListener(ThreadCoordinator coord) {
		coordinator = coord;
	}

	void processFinished(ThreadTask thread) throws InvalidPreviousThreadTaskException, FileNotFoundException{
		thread.getTask().setStatus(TaskStatus.DONE);
		if(thread.getTask() instanceof SplitTask){
			coordinator.videoSubmitProcessStep2(thread);
		}else if(thread.getTask() instanceof TranscodeTask){
			coordinator.videoSubmitProcessStep3(thread);
		}else if(thread.getTask() instanceof ConcatTask){
			//mark the submited video task to "done"
		}
	}
}
