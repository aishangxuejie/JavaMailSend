package com.aishangxuejie.run;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

import com.aishangxuejie.javamail.CheckErrorService2;

/**
 * 多线程实现并行执行多个计时器任务
 * 
 * timer 同样继承了runable接口
 * @author CuiGM
 * @date 2018年7月12日 下午4:10:14
 * @param name
 * @param pathName
 */
public class DoMailMain {
	/**
	 * 关于此处子线程的wait()方法，为什么子线程t1.join()中调用了wait()方法，却不是子线程t1等待，并且处于阻塞状态呢？
	 * 
	 * 查了下源码关于wait()的注释：
	 * Causes the current thread to wait until either another thread invokes the
	 * 使当前线程等待，直到另一个线程调用
	 * This method should only be called by a thread that is the owner of this object's monitor
	 * 此方法只应由该对象监视器的所有者线程调用
	 * 
	 * 所以，wait()的作用是让“当前线程”等待，而这里的“当前线程”是指当前在CPU上运行的线程。
	 * 虽然t1.join()中调用了wait()方法，但是它是通过“主线程”去调用的；所以，阻塞的是主线程，而不是“子线程”！
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.println("--------------启动邮件提醒服务---------------");
		//java8 lambda 表达式方式
		Thread t3 = new Thread(() -> {
			Timer timer3 = new Timer();
			timer3.schedule(new TimerTask() {

				@Override
				public void run() {
					try {
						CheckErrorService2.checkError();
					} catch (InterruptedException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, CheckErrorService2.PERIOD1);

		});
		
		Thread t4 = new Thread(() -> {
			Timer timer4 = new Timer();
			timer4.schedule(new TimerTask() {

				@Override
				public void run() {
					try {
						CheckErrorService2.checkError();
					} catch (InterruptedException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, CheckErrorService2.PERIOD1);

		});
		t3.start();
		t3.join();
		t4.start();
	}

}