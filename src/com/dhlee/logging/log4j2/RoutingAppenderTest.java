package com.dhlee.logging.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class RoutingAppenderTest {

	Logger log = LogManager.getLogger(this.getClass().getCanonicalName());
	public RoutingAppenderTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		RoutingAppenderTest test = new RoutingAppenderTest();
		test.log("groupId1", "thread1");
		test.log("groupId1", "thread2");
	}
	
	public void log(String groupId, String threadId) {
		ThreadContext.put("groupId", groupId);
	    ThreadContext.put("threadId", threadId);
	    log.info("Parsing file " + threadId + " from " + groupId);
	    log.info("Uploading data from " + threadId + " from " + groupId);
	    log.info("Generating invoices from " + threadId + " from " + groupId);
	    log.info("Processing completed **** " + threadId + " from " + groupId);
	    ThreadContext.remove("groupId");
	    ThreadContext.remove("threadId");
	}
}
