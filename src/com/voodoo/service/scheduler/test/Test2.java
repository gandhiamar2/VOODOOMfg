package com.voodoo.service.scheduler.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.voodoo.service.scheduler.domain.Job;
import com.voodoo.service.scheduler.domain.Printer;

//unit test for the printer to not to assign new job when printing
public class Test2 {

	Printer printer = new Printer();
	Job job = new Job();
	String result;
	
	@Test
	public void test() {
		
		printer.setLoadedMaterial(1);
		printer.setId(1);
		job.setId(2);
		job.setPrintTime(3);
		job.setRequiredMaterials(1);
		result = printer.startJob(job);
		
		assertEquals("started", result);
		assertEquals("printing", printer.getStatus());
		result = printer.startJob(job);
		assertEquals("alreadyTasked", result);
	}

}
