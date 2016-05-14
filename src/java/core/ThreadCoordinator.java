/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import static core.WorkerUtils.generateListOfTranscodeTasks;
import core.exception.InvalidPreviousThreadTaskException;
import core.worker.ConcatTask;
import core.worker.SplitTask;
import core.worker.TaskStatus;
import core.worker.TranscodeTask;
import entity.Video;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * One Coordinator per Video Transcode demand
 * @author Mokok
 */
public class ThreadCoordinator {
	
	private List<TranscodeTask> listOfTranscodeTask;
	private ThreadManager manager;
	private Video originalVideo;
	
	/**
	 * THIS IS THE WAY TO ADD A VIDEO TO THE GLOBAL PROCESS OF TRANSCODING
	 * @param thread
	 * @param tm 
	 */
	public void videoSubmitProcessStep1(ThreadTask thread, ThreadManager tm) {
		manager = tm;
		originalVideo = thread.getTask().getVideo();
		thread.addListener(new ThreadTaskEndListener(this));
		manager.execute(thread);
	}
	
	void videoSubmitProcessStep2(ThreadTask previousThreadTask) throws InvalidPreviousThreadTaskException, FileNotFoundException{
		if(! (previousThreadTask.getTask() instanceof SplitTask)){
			throw new InvalidPreviousThreadTaskException("Previous task of videoSubmitProcessStep2 must be a SplitTask");
		}
		
		listOfTranscodeTask = generateListOfTranscodeTasks(previousThreadTask.getTask().getVideo());
		/* 
		for(TranscodeTask tTask : list){
			ThreadTask thread = ThreadTask.createNewThreadTask(tTask);
			thread.addListener(new ThreadTaskEndListener(this));
			manager.execute(thread);
		}
		*/
		listOfTranscodeTask.stream().map((tTask) -> ThreadTask.createNewThreadTask(tTask)).map((thread) -> {
			thread.addListener(new ThreadTaskEndListener(this));
			return thread;
		}).forEach((thread) -> {
			manager.execute(thread);
		});
	}

	void videoSubmitProcessStep3(ThreadTask previousThreadTask) throws InvalidPreviousThreadTaskException {
		if(! (previousThreadTask.getTask() instanceof TranscodeTask)){
			throw new InvalidPreviousThreadTaskException("Previous task of videoSubmitProcessStep3 must be a TranscodeTask");
		}
		if(areAllTranscodeTaskFinished()){
			ConcatTask concatTask = new ConcatTask(originalVideo);
			ThreadTask concatThread = ThreadTask.createNewThreadTask(concatTask);
			concatThread.addListener(new ThreadTaskEndListener(this));
			manager.execute(concatThread);
		}
	}
	
	private boolean areAllTranscodeTaskFinished(){
		/*
		for(TranscodeTask task : listOfTranscodeTask){
			if(task.getStatus() != TaskStatus.DONE){
				return false;
			}
		}
		*/
		if (!listOfTranscodeTask.stream().noneMatch((task) -> (task.getStatus() != TaskStatus.DONE))) {
			return false;
		}
		return true;
	}
}
