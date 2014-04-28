package cntg.imusm.commonutils.commoninterface;

public class MyInterface {

	/** OnCompleteTaskListener */
	public interface ITaskListener {
		/**
		 * onPreExecute
		 * 
		 * @Description: do something in UI thread
		 */
		public void onPreExecuteTask(String id);

		/**
		 * getData
		 * 
		 * @Description: progress in background
		 */
		public void onProcessDataInBackground(String id);

		/**
		 * onComplete
		 * 
		 * @Description: progress in UI thread
		 * */
		public void onCompleteTask(String id);

	}
	
	public interface IDownloadServiceListener{
		public void onServiceUpdate(Object... param);
	}
}
