package com.voodoo.service.scheduler.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.voodoo.service.scheduler.domain.Job;
import com.voodoo.service.scheduler.domain.Printer;

//unit test for the printer to start printing when materialchanging is done
public class Test5 {

	Printer printer = new Printer();
	Job job = new Job();
	String result;
	
	@Test
	public void test() {
		
		printer.setLoadedMaterial(1);
		printer.setId(1);
		job.setId(2);
		job.setPrintTime(3);
		job.setRequiredMaterials(2);
		
		result = printer.startJob(job);
		assertEquals("changingMaterial", result);
		assertTrue(printer.isStatusMaterialChange());
		assertEquals("idle", printer.getStatus());
		printer.setTimeMaterialChangeTasked(0);
		assertTrue(!printer.isStatusMaterialChange());
		assertEquals("printing", printer.getStatus());
	}

}
