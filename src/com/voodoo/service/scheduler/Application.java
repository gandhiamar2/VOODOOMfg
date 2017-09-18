package com.voodoo.service.scheduler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import com.voodoo.service.scheduler.dao.JobScheduler;
import com.voodoo.service.scheduler.service.SchedulerService;

public class Application {
	
	
	public static void main(String[] args) {
		SchedulerService schedulerService = new SchedulerService();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("factory-input.json");
		//Json pasring call
		schedulerService.jsonParsing(convertStreamToString(is));
		
		//polling
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int elapsedTime = 0;
			LinkedHashMap<Integer, Integer> schedule;
			
			  @Override
			  public void run() {
				  //Printer job scheduling call
					schedule= schedulerService.scheduling();
					if(schedule.size()==0)
						timer.cancel();
					else{

						System.out.print("time elapsed: "+elapsedTime+" ");
						for (int i :schedule.keySet()) {
							System.out.print("("+schedule.get(i)+","+i +") ");
						}
						System.out.println("");
						elapsedTime++;
					}
			  }
			}, 1000,1000);
	}
	
	//referenced from stackoverflow
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}
