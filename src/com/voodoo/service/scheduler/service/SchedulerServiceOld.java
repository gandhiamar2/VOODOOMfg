package com.voodoo.service.scheduler.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import com.voodoo.service.scheduler.dao.JobScheduler;
import com.voodoo.service.scheduler.domain.Job;
import com.voodoo.service.scheduler.domain.Printer;
import org.json.*;

//initial not used in application
public class SchedulerServiceOld {
	
	ArrayList<Printer> printers = new ArrayList<>();
	ArrayList<Job> jobs = new ArrayList<>();
	int jobsIndex = 0;
	TreeMap<Integer,Integer> jobsScheduled; //treemap is used for auto sorting based on job ids
	
	//json parsing into jobs and printers
	public void jsonParsing(String json) {
		JSONObject object = new JSONObject(json);
		JSONArray arr1 = object.getJSONArray("printers");
		for (int i = 0; i < arr1.length(); i++){
			JSONObject obj = arr1.getJSONObject(i);
			Printer printer = new Printer();
			printer.setTimeMaterialChangeStandard(object.getInt("MATERIAL_CHANGE_TIME"));
			printer.setId(obj.getInt("id"));
			printer.setLoadedMaterial(obj.getInt("loaded_material"));
			printers.add(printer);
		}
		JSONArray arr2 = object.getJSONArray("jobs");
		for (int i = 0; i < arr2.length(); i++){
			JSONObject obj = arr2.getJSONObject(i);
			Job job = new Job();
			job.setId(obj.getInt("id"));
			job.setPrintTime(obj.getInt("print_time"));
			job.setRequiredMaterials(obj.getInt("required_material"));
			jobs.add(job);
		}	
	}
	
	//rescheduling printers
	public TreeMap<Integer, Integer> scheduling() {

		jobsScheduled = new TreeMap<>(); //TreeMap will sort by keys (job numbers) there by resulted in sorted by starttime of jobs
		for(int i=0;i<printers.size();i++){
			Printer printer = printers.get(i);
			printer.setTimeTasked((printer.getTimeTasked())-1);
			
			if(printer.isStatusMaterialChange()){ 
				printer.setTimeMaterialChangeTasked(printer.getTimeMaterialChangeTasked()-1);
			}
			
			if(printer.isStatusFree() && !printer.isStatusMaterialChange()){ //sequential assignment of jobs to printers even with material change enabled
				if(jobsIndex<jobs.size()){
					while(jobs.get(jobsIndex).getPrintTime()<=0){
						jobsIndex++;
					}
					printer.startJob(jobs.get(jobsIndex));
					jobsIndex++;
				}
			}
			//no printers will be ever free if it's free then that means no more jobs to be assigned
			if(!printer.isStatusFree() && !printer.isStatusMaterialChange())
				jobsScheduled.put(printer.getTaskedJob(),printer.getId());
		}
		
		return jobsScheduled;
	}
}
