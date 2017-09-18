package com.voodoo.service.scheduler.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public interface JobScheduler {
	
	
	LinkedHashMap<Integer, Integer> scheduling();
	
	void jsonParsing(String json);
}
