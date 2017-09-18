package com.voodoo.service.scheduler.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;
import com.voodoo.service.scheduler.dao.JobScheduler;
import com.voodoo.service.scheduler.domain.Job;
import com.voodoo.service.scheduler.domain.Printer;
import org.json.*;

public class SchedulerService implements JobScheduler{
	
	ArrayList<Printer> printers = new ArrayList<>();
	ArrayList<Job> jobs = new ArrayList<>();
	int jobsIndex = 0;
	LinkedHashMap<Integer,Integer> jobsScheduled = new LinkedHashMap<>();
	
	//json parsing into jobs and printers
	@Override
	public void jsonParsing(String json) {
		JSONObject object = new JSONObject(json);
		JSONArray arr1 = object.getJSONArray("printers"); // setting id, loaded material, changetime onto printers
		for (int i = 0; i < arr1.length(); i++){
			JSONObject obj = arr1.getJSONObject(i);
			Printer printer = new Printer();
			printer.setTimeMaterialChangeStandard(object.getInt("MATERIAL_CHANGE_TIME"));
			printer.setId(obj.getInt("id"));
			printer.setLoadedMaterial(obj.getInt("loaded_material"));
			printers.add(printer);
		}
		JSONArray arr2 = object.getJSONArray("jobs");  // setting id, required material, print time onto jobs
		for (int i = 0; i < arr2.length(); i++){
			JSONObject obj = arr2.getJSONObject(i);
			Job job = new Job();
			job.setId(obj.getInt("id"));
			job.setPrintTime(obj.getInt("print_time"));
			job.setRequiredMaterials(obj.getInt("required_material"));
			jobs.add(job);
			
		}	
	}
	
	//rescheduling printers will be called by poll (if printer finishes a job in at a particular second new job will be assigned to that in the same second)
	@Override
	public LinkedHashMap<Integer, Integer> scheduling() {
		
		for(int i=0;i<printers.size();i++){
			Printer printer = printers.get(i);
			int selectedIndex=-1;
			//on every call printer total working time reduced by 1 sec till it is 0 and made available for next job
			if(!printer.isStatusFree() && !printer.isStatusMaterialChange())
			printer.setTimeTasked((printer.getTimeTasked())-1); 
			
			//when printer finishes the job the job is removed from the list
			if(printer.getTimeTasked()==0){
				jobsScheduled.remove(printer.getTaskedJob());  
			}
			
			//if material change is active for printer the changing time is reduced by 1 sec
			if(printer.isStatusMaterialChange()){
				printer.setTimeMaterialChangeTasked(printer.getTimeMaterialChangeTasked()-1);  
			}
			
			//if printer is free and not in material change state then open for scheduling
			if(printer.isStatusFree() && !printer.isStatusMaterialChange()){
				if(jobs.size()>0){
					for (int j=0; j< jobs.size();j++) {
						//checking for any jobs that has the same material as printer if found assigns to printer and breaks the loop
						if(jobs.get(j).getRequiredMaterials()==printer.getLoadedMaterial() && jobs.get(j).getPrintTime()>0){
							selectedIndex =j; 
							break;
						}
						//assigning last job if printer color is not matching to any of jobs
						else if(jobs.get(j).getPrintTime()>0){
							selectedIndex =j; 
						}
					}
				
					//starting the job selected by above loop in this printer
				if(selectedIndex!=-1){
					printer.startJob(jobs.get(selectedIndex));
					jobs.remove(selectedIndex);
					}
					//if above loop doesnt select any job then that infer rest of jobs are dummies with 0 print time so clear them all
				else{
					jobs.clear();
					}
				}
			}
			
			//no printers will be ever free if it's free then that means no more jobs to be assigned or in material change time
			if(!printer.isStatusFree() && !printer.isStatusMaterialChange())
				jobsScheduled.put(printer.getTaskedJob(),printer.getId());
		}
		
		return jobsScheduled;
	}
}
