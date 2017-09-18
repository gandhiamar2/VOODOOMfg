package com.voodoo.service.scheduler.domain;

public class Printer {

	private boolean statusFree= true; //whether printer is currently idle
	private boolean statusMaterialChange= false; // whether printer is currently material changing
	private int id;
	private int loadedMaterial; //current loaded material
	private String status; // status of printer
	private int timeMaterialChangeStandard; // default material change time from json
	private int timeTasked = 0; // time the printer is tasked with (print time of assigned job)
	private int timeMaterialChangeTasked = 0; // time pending when material change occuring
	private int taskedJob; // the job printer is currently printing or just finished
	private Job pendingTaskedJob; // job to be assigned to printer beacuse printer is changing material
	
	public int getTimeTasked() {
		return timeTasked;
	}
	
	public void setStatus(String status) {
		if(status=="printing"){
			this.statusFree= false;
		}
		this.status = status;
	}

	public String getStatus() {
		if(this.statusFree)
			return "idle";
		else
			return "printing";
	}
	
	//if printer is free and job has same material then job will be started else printer will be material change mode and job is put on hold state at printer
	public String startJob(Job job){
		if(this.statusFree)
		{
			if(this.loadedMaterial==job.getRequiredMaterials()){
				this.statusFree = false;
				this.taskedJob = job.getId();
				this.timeTasked = job.getPrintTime();
				return "started";
			}
			else{
				this.timeMaterialChangeTasked = this.timeMaterialChangeStandard;
				this.statusMaterialChange = true;
				this.pendingTaskedJob = job;
				return "changingMaterial";
			}
		}
		else
		{
			return "alreadyTasked";
		}
	}
	
	//will set new time tasked with printer(decrease) time till it is 0 and printer becomes idle
	public void setTimeTasked(int timeTasked) {
		if(timeTasked==0){
			this.statusFree = true;
		}
		this.timeTasked = timeTasked;
	}
	
	//will set new time material changing with printer(decrease) time till it is 0 and then starts the pending job
	public void setTimeMaterialChangeTasked(int timeMaterialChangeTasked) {
		if(timeMaterialChangeTasked==0){
			this.statusMaterialChange = false;
			this.loadedMaterial = this.pendingTaskedJob.getRequiredMaterials();
			this.startJob(this.pendingTaskedJob);
		}
		this.timeMaterialChangeTasked = timeMaterialChangeTasked;
	}

	
	public int getLoadedMaterial() {
		return loadedMaterial;
	}
	
	public int getTimeMaterialChangeTasked() {
		return timeMaterialChangeTasked;
	}

	public boolean isStatusMaterialChange() {
		return statusMaterialChange;
	}

	public void setTimeMaterialChangeStandard(int timeMaterialChangeStandard) {
		this.timeMaterialChangeStandard = timeMaterialChangeStandard;
	}

	public boolean isStatusFree() {
		return statusFree;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setLoadedMaterial(int loaded_material) {
		this.loadedMaterial = loaded_material;
	}
	public int getTaskedJob() {
		return taskedJob;
	}
}
