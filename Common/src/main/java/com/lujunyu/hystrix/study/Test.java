package com.lujunyu.hystrix.study;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class Test {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CommandHelloWorld command = new CommandHelloWorld("command1");
		CommandHelloWorld1 command1 = new CommandHelloWorld1("command1");
		
		//同步
//		System.out.println(command.execute());
//		System.out.println(command1.toObservable().toBlocking().toFuture().get());
		
		//异步
//		Future<String> fs = command.queue();
//		System.out.println(fs.get());
		
		
	}
	private static class CommandHelloWorld extends HystrixCommand<String> {
		private final String name;

		protected CommandHelloWorld(String name) {
			super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
			this.name = name;
		}

		@Override
		protected String run() throws Exception {
			return "hello " + name;
		}

	}
	
	private static class CommandHelloWorld1 extends HystrixObservableCommand<String>{
		private final String name;
		protected CommandHelloWorld1(String name) {
			super(HystrixCommandGroupKey.Factory.asKey("aa"));
			this.name = name;
		}

		@Override
		protected Observable<String> construct() {
			return Observable.create(new OnSubscribe<String>() {

				@Override
				public void call(Subscriber<? super String> t) {
					try{
						if(t.isUnsubscribed()){
							t.onNext("Hello");
							t.onNext(name+"!");
							t.onCompleted();
						}
					}catch(Exception e){
						t.onError(e);
					}
				}
			}).subscribeOn(Schedulers.io());
		}
	}
}