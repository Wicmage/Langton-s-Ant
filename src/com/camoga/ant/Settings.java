package com.camoga.ant;

public class Settings {
	
	//GUI
	
	static int canvasSize = 4; // size of canvas (and output image) in chunks (e.g. scale = 16, cSIZE = 64 => size = 1024x1024)
	static boolean followAnt = true;
	static boolean smoothFollow = false; //TODO
	static boolean renderVoid = false; // draws black where no chunk has been generated
	static int itpf = 33333334; // iterations between frames
	
	//LEVEL
	
	/**
	 * 	Map is stored in chunks
	 *  Size of chunks = 2^cPOW
	 */
	static final int cPOW = 7;
	static final int cSIZE = 1<<cPOW;
	static final int cSIZEm = cSIZE-1;
	
	//FIND HIGHWAYS
	
	static boolean ignoreSavedRules = true; // If true skips all rules that have already been tested
	public static final String file = "ruleperiods.langton";
	static int chunkCheck = 90; // Check if the ant forms a highway when the ant goes further than this chunk from the origin
	static float repeatcheck = 40; // Number of times the period has to repeat to confirm that it's correct (e.g. You're more certain that 10101010101010101010 has a period of 2 than 1010)
	static boolean detectHighways = true; //Detects if the ant follows a periodic pattern
	static long maxiterations = (long) 1.2e8; // After this many iterations, program moves to next rule
	static boolean autosave = false;
	
	static int highwaySizew = 400000;
	static int highwaySizeh = 800;
	
	
	//OUTPUT IMAGES
	static boolean toot = false;
	static boolean savepic = false; // saves pic if ant forms a highway
	static int saveImageW = canvasSize*cSIZE;
	static int saveImageH = canvasSize*cSIZE;
//	static int saveImageW = 100000;
//	static int saveImageH = 1200;
	
	//OTHER
	static boolean deleteOldChunks = false; // only enable when you know old chunks are not going to be visited again
}