package com.camoga.ant.ants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map.Entry;

import org.apache.commons.collections4.keyvalue.MultiKey;

import com.camoga.ant.Settings;
import com.camoga.ant.Worker;
import com.camoga.ant.level.Level.Chunk;

public abstract class AbstractAnt {

	protected Worker worker;
	protected AbstractRule rule;
	
	protected Chunk chunk;

	// Space/chunk
	public int dimension;
	public int cPOW;
	public int cSIZE;
	public int cSIZEm;
	public int cSIZE2;
	
	// Ant
	protected int dir;
	protected int state;
	protected int wc,xc,yc,zc;
	protected int w,x,y,z;
	
	// Highway
	protected boolean saveState = false;
	protected byte[] states;
	protected int repeatLength;
	protected long stateindex;
	public long wstart, xstart, ystart, zstart, wend, xend, yend, zend;
	protected long minHighwayPeriod = 0;  // This is the final period length
	protected boolean PERIODFOUND = false;
	
	public AbstractAnt(Worker worker, int dimension) {
		this.worker = worker;
		this.dimension = dimension;
		if(dimension==2) cPOW = 7;
		else if(dimension==3) cPOW = 5;
		else if(dimension==4) cPOW = 4;
		cSIZE = 1<<cPOW;
		cSIZEm = cSIZE-1;
		cSIZE2 = cSIZE<<cPOW;
		worker.getLevel().chunkSize = 1<<(cPOW*dimension);
	}
	
	public abstract int move();
	
	public void init(long rule, long iterations) {
		int stateslen = iterations == -1 ? 200000000:(int) Math.min(Math.max(5000000,iterations/(int)Settings.repeatcheck*2), 200000000);
		if(states == null || states.length != stateslen) states = new byte[stateslen];
		this.rule.createRule(rule);
		worker.getLevel().init();
		w = 0;
		x = 0;
		y = 0;
		z = 0;
		wc = 0;
		xc = 0;
		yc = 0;	
		zc = 0;
		dir = 0;
		state = 0;
		saveState = false;
		repeatLength = 1;
		states[1] = -1;
		stateindex = 0;
		minHighwayPeriod = 0;
		PERIODFOUND = false;
		if(dimension == 2) {
			chunk = worker.getLevel().getChunk(0, 0);
		} else if(dimension == 3) {
			chunk = worker.getLevel().getChunk(0, 0, 0);
		} else if(dimension == 4) {
			chunk = worker.getLevel().getChunk(0, 0, 0, 0);
		} else throw new RuntimeException("Invalid dimension");
	}
	
	public long getPeriod() { return minHighwayPeriod; }
	public long getW() { return w + wc*cSIZE; }
	public long getX() { return x + xc*cSIZE; }
	public long getY() { return y + yc*cSIZE; }
	public long getZ() { return z + zc*cSIZE; }
	public int getWC() { return wc; }
	public int getXC() { return xc; }
	public int getYC() { return yc; }
	public int getZC() { return zc; }
	public AbstractRule getRule() {return rule;}
	
	public boolean findingPeriod() {return saveState;}
	
	public boolean periodFound() {return PERIODFOUND;}
	
	public void setFindingPeriod(boolean b) {
		saveState = b;
		xstart = getX();
		ystart = getY();
		zstart = getZ();
		wstart = getW();
	}
	
	public void saveState(String file) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeLong(rule.getRule());
			oos.writeLong(worker.getIterations());
			oos.writeInt(dir);
			oos.writeInt(state);
			oos.writeInt(x);
			oos.writeInt(y);
			oos.writeInt(xc);
			oos.writeInt(yc);
			oos.writeBoolean(saveState);
			if(saveState) {
				oos.writeLong(stateindex);
				oos.writeInt(repeatLength);
				oos.writeLong(minHighwayPeriod);
				oos.write(states);
			}
			oos.writeByte(cPOW);
			oos.writeInt(worker.getLevel().chunks.size());
			for(Entry<MultiKey<? extends Integer>, Chunk> c : worker.getLevel().chunks.entrySet()) {
				MultiKey<? extends Integer> key = c.getKey();
				oos.writeInt(key.getKey(0));
				oos.writeInt(key.getKey(1));
				oos.write(c.getValue().cells);
			}
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}