package com.pzad.concurrency;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import android.os.Handler;
import android.os.Message;

public abstract class PzThread<Result> {
	
	private static final int POST_RESULT = 0;
	private static final int POST_PROGRESS = 1;
	
	private InnerHandler handler = new InnerHandler();
	
	private Thread currentThread;
	
	public PzThread(){
		currentThread = new Thread(){
			@Override
			public void run(){
				postResult(PzThread.this.run());
			}
		};
		handler.getLooper();
	}
	
	public void executeOnExecutor(Executor exec){
		onPreExecute();
		if(exec instanceof ExecutorService){
		}
		exec.execute(currentThread);
	}
	
	private void postResult(Result result){
		Message message = handler.obtainMessage(POST_RESULT, new PzThreadResult<Result>(this, result, 0F));
		message.sendToTarget();
	}
	
	public void sendProgress(float progress){
		Message message = handler.obtainMessage(POST_PROGRESS, new PzThreadResult<Result>(this, null, progress));
		message.sendToTarget();
	}
	
	public abstract Result run();
	
	public void onPreExecute(){};
	public void onAborted(){};
	
	public abstract void onFinish(Result result);
	
	public void onProgress(float progress){};
	
	private static class InnerHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			PzThreadResult<?> result = (PzThreadResult<?>) msg.obj;
			switch(msg.what){
			case POST_RESULT:
				result.task.onFinish(result.data);
				break;
			case POST_PROGRESS:
				result.task.onProgress(result.progress);
				break;
			}
		}
	}
	
	private static class PzThreadResult<Result>{
		final PzThread task;
		final Result data;
		final float progress;
		
		public PzThreadResult(PzThread task, Result data, final float progress){
			this.task = task;
			this.data = data;
			this.progress = progress;
		}
	}
}
