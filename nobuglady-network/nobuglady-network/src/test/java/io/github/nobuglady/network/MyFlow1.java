package io.github.nobuglady.network;

import io.github.nobuglady.network.fw.FlowRunner;
import io.github.nobuglady.network.fw.annotation.Node;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MyFlow1 extends FlowRunner {
	@Node(label="a")
	public void process_a() throws InterruptedException {
		System.out.println("processing a");
		Thread.sleep(100);
		log.info("processing a");
	}

	@Node(label="f")
	public void process_f() throws InterruptedException {
		Thread.sleep(80);
		System.out.println("processing f");
		log.info("processing f");
	}
	
	@Node(label="b")
	public void process_b() throws InterruptedException {
		System.out.println("processing b");
		Thread.sleep(400);
		//throw new RuntimeException("test");
		log.info("processing b");
	}
	
	

	@Node(label="d")
	public void process_d() throws InterruptedException {
		Thread.sleep(80);
		System.out.println("processing d");
		log.info("processing d");
		throw new RuntimeException("test");
	}
	@Node(label="c")
	public void process_c() throws InterruptedException {
		Thread.sleep(80);
		System.out.println("processing c");
		log.info("processing c");
	}
	
	@Node(label="e")
	public void process_e() throws InterruptedException {
		Thread.sleep(80);
		System.out.println("processing e");
		log.info("processing e");
	}
	
	
	
}
