/**
 * Copyright (c)  TMA Mobile Solutions, TMA Solutions company. All Rights Reserved.
 * This software is the confidential and proprietary information of TMA Solutions, 
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with TMA Solutions.
 *
 *
 * TMA SOLUTIONS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, 
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TMA SOLUTIONS SHALL NOT BE LIABLE FOR ANY DAMAGES 
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES
 */
package cntg.imusm.commonutils.task;

import java.util.ArrayList;

import cntg.imusm.commonutils.commoninterface.MyInterface.ITaskListener;
import cntg.imusm.commonutils.debug.MyDebugLog;

/**
 * 
 * @author vtthach
 * @CreatedDate Apr 12, 2013
 * @Description: control only 1 task running in same time to enhance performance
 */
public class MyTaskManager {
	private static final String TAG = MyTaskManager.class.getName();
	ArrayList<MyTask> curTaskList;
	final int max = 4;
	private static MyTaskManager instance = new MyTaskManager();
	public static MyTaskManager getInstance() {
		return instance;
	}

	/**
	 * 
	 * @Description: start task to do in background
	 * @param listener: in type of OnMyTaskListener
	 * @param isCancelable: define that task can cancel by this TaskManager
	 */
	public void start(ITaskListener listener, String id, boolean isCancelable) {

		if (listener == null) {
			MyDebugLog.d(TAG, TAG+"-start error: listener == null");
			return;
		}

		MyDebugLog.d(TAG, "TaskManager.start: " + listener.getClass().toString());

		if (curTaskList == null) {
			curTaskList = new ArrayList<MyTask>();
		}
		// Create new task
		MyTask task = new MyTask(listener, id, isCancelable);
		// Stop current task if any
		if (curTaskList.size() >= max) {
			int indexRemove = curTaskList.size()-1;
			while (indexRemove >= 0) {
				MyTask task0 = curTaskList.get(indexRemove);
				if (task0 != null && (task0.isCancelable||!task0.isRunning)) {
					if(!task0.isCancelled())
						task0.cancel(true);
					MyDebugLog.d(TAG, "remove: " + task0.taskID);
					curTaskList.remove(indexRemove);
					task0 = null;
					break;
				} else {
					indexRemove--;
				}
			}
		}

		// Run task
		task.execute();

		// Save task
		if(curTaskList!=null){
			curTaskList.add(task);
			MyDebugLog.d(TAG, "curTasks.size1: " + curTaskList.size());
			MyDebugLog.d(TAG, "curTasks.taskId1: " + task.taskID);
		}
		task = null;
		MyDebugLog.d(TAG, "curTasks.size2: " + curTaskList.size());
		for(MyTask t: curTaskList){
			MyDebugLog.d(TAG, "curTasks.size2: " + t.taskID);
		}
	}

	public synchronized void stopOtherTask(String curID){
		MyDebugLog.i(TAG, "stopOtherTask ");
		if(curTaskList!=null){
			int indexRemove = curTaskList.size()-1;
			MyDebugLog.d(TAG, "curTasks.size 1: " + curTaskList.size());
			while (indexRemove >= 0) {
				MyTask task0 = curTaskList.get(indexRemove);
				if (task0 != null && !task0.taskID.equalsIgnoreCase(curID) && (task0.isCancelable||!task0.isRunning)) {
					if(!task0.isCancelled())
						task0.cancel(true);
					MyDebugLog.d(TAG, "remove: " + task0.taskID);
					curTaskList.remove(indexRemove);
					task0 = null;
				}
				indexRemove--;
			}
			MyDebugLog.d(TAG, "curTasks.size 2: " + curTaskList.size());
		}
	}
	/**
	 * stop
	 * 
	 * @Description: stop task running if any
	 */
	public void stop() {
		if(curTaskList!=null){
			for (MyTask i : curTaskList) {
				if (i != null && !i.isCancelled()&&i.isCancelable) {
					i.cancel(true);
					i = null;
				}
			}
			curTaskList.clear();
		}
	}
}
