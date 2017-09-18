package com.voodoo.service.scheduler.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.voodoo.service.scheduler.domain.Job;
import com.voodoo.service.scheduler.domain.Printer;


//unit test for the printer to be idle when the printing time is set to 0
public class Test3 {

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
		
		assertEquals("printing", printer.getStatus());
		assertEquals(3, printer.getTimeTasked());
		printer.setTimeTasked(0);
		assertTrue(printer.isStatusFree());
		assertEquals("idle", printer.getStatus());
	}

}
