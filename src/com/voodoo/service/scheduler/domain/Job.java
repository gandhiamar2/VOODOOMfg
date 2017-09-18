package com.voodoo.service.scheduler.domain;

public class Job {

	private int id;
	private int printTime;
	private int requiredMaterials;
	private boolean available = true;
	
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPrintTime() {
		return printTime;
	}
	public void setPrintTime(int printTime) {
		this.printTime = printTime;
	}
	public int getRequiredMaterials() {
		return requiredMaterials;
	}
	public void setRequiredMaterials(int requiredMaterials) {
		this.requiredMaterials = requiredMaterials;
	}
	
}
