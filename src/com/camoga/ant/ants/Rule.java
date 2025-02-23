package com.camoga.ant.ants;

import java.util.Random;

public class Rule extends AbstractRule {
	
	public void createRule(long rule) {
		this.rule = rule;
		this.size = 64-Long.numberOfLeadingZeros(rule);
		colors = new int[size];
		turn = new int[size];
		Random r = new Random();
		for(int i = 0; rule != 0; i++) {
			turn[i] = (rule&1) == 1 ? 1:3;
			rule >>>= 1;
			colors[i] = r.nextInt(0x1000000);
		}
		colors[0] = 0xff101010;
		generateString();
	}
	
	private String generateString() {
		ruleString = "";
		for(int i = 0; i < turn.length; i++) {
			ruleString += turn[i] == 1 ? "R":"L";
		}
		return ruleString;
	}
}