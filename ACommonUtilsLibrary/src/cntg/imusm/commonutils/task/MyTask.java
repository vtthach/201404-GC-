package cntg.imusm.commonutils.task;

import android.os.AsyncTask;
import cntg.imusm.commonutils.commoninterface.MyInterface.ITaskListener;
import cntg.imusm.commonutils.debug.MyDebugLog;

public class MyTask extends AsyncTask<Object, Void, Object> {
	// Tag
	private static final String TAG = MyTask.class.getName();
	// ID_DEFAULT
	private static final String ID_DEFAULT = "CNTG_IMU_ID_DEFAULT";
	// flag IS CANCELABLE
	boolean isCancelable = true;
	// flag IS RUNNING
	boolean isRunning = false;
	// instance of OnMyTaskListener
	ITaskListener mOnMyTaskListener;
	// task ID
	public String taskID = ID_DEFAULT;

	/**
	 * Constructor
	 * @param mOnCompleteTaskListener
	 *            : instance of interface OnMyTaskListener
	 * @param taskName
	 *            : name of task
	 */
	public MyTask(ITaskListener mOnCompleteTaskListener, String taskID) {
		this.mOnMyTaskListener = mOnCompleteTaskListener;
		if (taskID != null)
			this.taskID = taskID;
	}
	
	public MyTask(ITaskListener mOnCompleteTaskListener, String taskID, boolean isCancelable) {
		this.mOnMyTaskListener = mOnCompleteTaskListener;
		if (taskID != null)
			this.taskID = taskID;
		this.isCancelable = isCancelable;
	}

	@Override
	protected void onPreExecute() {
		isRunning = true;
		// TODO Auto-generated method stub
		if (mOnMyTaskListener != null)
			mOnMyTaskListener.onPreExecuteTask(taskID);
	}

	@Override
	protected Object doInBackground(Object... params) {
		MyDebugLog.i(TAG, "doInBackground: " + taskID);
		if (mOnMyTaskListener != null)
			mOnMyTaskListener.onProcessDataInBackground(taskID);
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		if (mOnMyTaskListener != null) {
			mOnMyTaskListener.onCompleteTask(taskID);
		}
		mOnMyTaskListener = null;
		isRunning = false;
	}

	public void setCacleable(boolean isCancelable) {
		this.isCancelable = isCancelable;
	}

}
