package com.camoga.ant;

import java.util.ArrayList;

public class WorkerManager {

	static ArrayList<Worker> workers = new ArrayList<Worker>();
	static int[] numworkers = new int[3]; // 0: normal, 1: hex, 2: 3D
	
	static int idcount;
	
	//TODO do not start new workers until old workers have stopped
	private static void updateWorkers() {
		int[] count = new int[3];
		for(Worker w : workers) {
			count[w.getType()]++;
		}
		for(Worker w : workers) {
			if(count[0] <= numworkers[0]) break;
			if(w.getType() == 0) {
				w.kill();
				count[0]--;
			}
		}
		for(Worker w : workers) {
			if(count[1] <= numworkers[1]) break;
			if(w.getType() == 1) {
				w.kill();
				count[1]--;
			}
		}
		for(Worker w : workers) {
			if(count[2] <= numworkers[2]) break;
			if(w.getType() == 2) {
				w.kill();
				count[2]--;
			}
		}
		for(int i = count[0]; i < numworkers[0]; i++) {
			workers.add(new Worker(idcount++, 0));
		}
		for(int i = count[1]; i < numworkers[1]; i++) {
			workers.add(new Worker(idcount++, 1));
		}
		for(int i = count[2]; i < numworkers[2]; i++) {
			workers.add(new Worker(idcount++, 2));
		}
	}
	
	public static void remove(Worker worker) {
		workers.remove(worker);
	}
	
	public static void setWorkerType(int type, int num) {
		if(num < 0 || numworkers[0] + numworkers[1] + numworkers[2] + numworkers[type] - num > Runtime.getRuntime().availableProcessors()) throw new RuntimeException();
		numworkers[type] = num;
		updateWorkers();
	}
	
	public static void setWorkers(int normal, int hex, int r3) {
		if(normal < 0 || hex < 0 || r3 < 0 || normal + hex + r3 > Runtime.getRuntime().availableProcessors()) throw new RuntimeException("More workers than available processors ("+Runtime.getRuntime().availableProcessors()+")");
		numworkers[0] = normal;
		numworkers[1] = hex;
		numworkers[2] = r3;
		updateWorkers();
	}
	
	public static void start() {
		for(Worker w : workers) {
			w.start();
		}
	}
	
	public static int size() {
		return numworkers[0]+numworkers[1]+numworkers[2];
	}
	
	public static int size(int type) {
		return numworkers[type];
	}

	public static Worker getWorker(int id) {
		if(id < 0 || id >= workers.size()) return null;
		return workers.get(id);
	}
}