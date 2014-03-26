package com.pzad.concurrency;

import java.util.concurrent.Executor;

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
		exec.execute(currentThread);
	}
	
	private void postResult(Result result){
		Message message = handler.obtainMessage(POST_RESULT, new PzThreadResult<Result>(this, result));
		message.sendToTarget();
	}
	
	public abstract Result run();
	
	public abstract void onFinish(Result result);
	
	private static class InnerHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			PzThreadResult<?> result = (PzThreadResult<?>) msg.obj;
			switch(msg.what){
			case POST_RESULT:
				result.task.onFinish(result.data);
				break;
			case POST_PROGRESS:
				break;
			}
		}
	}
	
	private static class PzThreadResult<Result>{
		final PzThread task;
		final Result data;
		
		public PzThreadResult(PzThread task, Result data){
			this.task = task;
			this.data = data;
		}
	}
}
