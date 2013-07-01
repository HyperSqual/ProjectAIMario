package level2;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

import dk.itu.mario.MarioInterface.Constraints;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.engine.DataRecorder;
import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.engine.sprites.Enemy;
import Architect.*;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

public class ArchLevel extends Level{
	//Store information about the level
	public   int DIFFICULTY_sander;
	public   int ENEMIES = 0; //the number of enemies the level contains
	public   int BLOCKS_EMPTY = 0; // the number of empty blocks
	public   int BLOCKS_COINS = 0; // the number of coin blocks
	public   int BLOCKS_POWER = 0; // the number of power blocks
	public   int COINS = 0; //These are the coins in boxes that Mario collect
	
	public double[] playValues;
	
	private static Random levelSeedRandom = new Random();
	public static long lastSeed;

	Random random;

	private static final int ODDS_STRAIGHT = 0;
	private static final int ODDS_HILL_STRAIGHT = 1;
	private static final int ODDS_TUBES = 2;
	private static final int ODDS_JUMP = 3;
	private static final int ODDS_CANNONS = 4;


	private static int MAX_COINS = 5;
	private static int MAX_TURTLES = 10;
	private static int GAP_SIZE = 4;
	private static int NO_GAPS = 6;

	private int[] odds = new int[5];

	private int totalOdds;

	private int difficulty;
	private int type;
	private int gaps;


	public ArchLevel(int width, int height)
	{
		super(width, height);
	}


	public ArchLevel(int width, int height, long seed, int difficulty, int type,ARCH_MESSAGE m)
	{
		this(width, height);
		creat(seed, difficulty, type,m);
	}

	public ArchLevel(int width, int height, long seed, int difficulty, int type,ARCH_MESSAGE m, DataRecorder recorder, double [][] valueList)
	{
		this(width, height);
		creat2(seed, difficulty, type,m, recorder, valueList);
	}

	
	public double[] arrayMeans(double[][] array)//small method to return array of means of collumns in [][]array
	{	
		double[] means = new double[array[0].length];
		for (int i = 0; i < array.length;i++)
		{
			for(int j = 0; j <array[i].length;j++ )
			{
				means[j] += array[i][j];
			}
		}
		for (int i = 0; i< means.length;i++)
		{
			means[i] = means[i]/array.length;
		}
		return means;
	}
	public void creat2(long seed, int difficulty, int type, ARCH_MESSAGE m, DataRecorder recorder, double [][] valueList)
	{
		//System.out.println("");
		//System.out.println("-ArcLevel.creat() called...");                  
		this.type = type;       
		//this.difficulty = m.DIFFICULTY * 3 + 1;
		this.difficulty = difficulty; //use function parameter value instead of m.DIFFICULTY
		System.out.println("-procedurally generating a level segment with difficulty: " + difficulty);
		DIFFICULTY_sander = difficulty;
		//System.out.println("-also, using m.DIFFICULTY: " + m.DIFFICULTY);
		//System.out.println("-resulting in this.DIFFICULTY: " + this.difficulty);

		MAX_COINS = m.MAX_COINS * 10;
		//GAP_SIZE = m.GAP_SIZE;
		// MAX_TURTLES = m.MAX_TURTLES;

		double [] means = arrayMeans(valueList);//{10,10,6,3,25,9};//means for normal distribution
		System.out.println(Arrays.toString(means));
		
		System.out.println("test stuff:");
//		means[ODDS_STRAIGHT] += 1-recorder.tr();
//		means[ODDS_HILL_STRAIGHT]+= 1-recorder.tr();
//		means[ODDS_TUBES] += recorder.getKills(SpriteTemplate.CHOMP_FLOWER)-recorder.getDeaths(SpriteTemplate.CHOMP_FLOWER);
//		means[ODDS_JUMP] += recorder.J()-recorder.dg();
//		means[ODDS_CANNONS] += recorder.getKills(SpriteTemplate.CANNON_BALL)-recorder.getDeaths(SpriteTemplate.CANNON_BALL);//todo get kill ratio instead of kills

//		double [][]cov = 		{	{means[0],0,0,0,0},
//									{0,means[1],0,0,0},
//									{0,0,means[2],0,0},
//									{0,0,0,means[3],0},
//									{0,0,0,0,means[4]}};
		//System.out.printf("%d,%d",valueList.length,valueList[0].length);
		RealMatrix matrix = new Array2DRowRealMatrix(valueList);

		Covariance covM = new Covariance(matrix,true);
		double[][] covdouble = covM.getCovarianceMatrix().getData();
		covdouble[0][0] += 3;
		covdouble[1][1] += 3;
		covdouble[2][2] += 3;
		covdouble[3][3] += 3;
		covdouble[4][4] += 3;
		//System.out.println(Arrays.deepToString(cov2.getCovarianceMatrix().getData()));
		//System.out.println(Arrays.deepToString(covdouble));//matrix.getData()));
		
		MultivariateNormalDistribution MND = new MultivariateNormalDistribution(means,covdouble);//cov2.getCovarianceMatrix().getData());

		double[] odds_sample = MND.sample();
		//System.out.println(Arrays.toString(odds_sample));
		odds[ODDS_STRAIGHT] = (int) odds_sample[ODDS_STRAIGHT];
		odds[ODDS_HILL_STRAIGHT] = (int) odds_sample[ODDS_HILL_STRAIGHT];
		odds[ODDS_TUBES] =(int) odds_sample[ODDS_TUBES];
		odds[ODDS_JUMP] = (int) odds_sample[ODDS_JUMP];
		odds[ODDS_CANNONS] =  (int) odds_sample[ODDS_CANNONS];

		//		        odds[ODDS_STRAIGHT] = 20;
		//		        odds[ODDS_HILL_STRAIGHT] = 10;
		//		        odds[ODDS_TUBES] = 2 + 1 * this.difficulty;
		//		        odds[ODDS_JUMP] = 1*this.difficulty;
		//		        odds[ODDS_CANNONS] = -10 + 5 * this.difficulty;

		if (type != LevelInterface.TYPE_OVERGROUND)
		{
			odds[ODDS_HILL_STRAIGHT] = 0;
		}

		for (int i = 0; i < odds.length; i++)
		{
			//failsafe (no negative odds)
			if (odds[i] < 0){
				odds[i] = 0;
			}

			totalOdds += odds[i];
			odds[i] = totalOdds - odds[i];
		}

		lastSeed = seed;
		random = new Random(seed);

		//create the start location
		int length = 0;
		length += buildStraight(0, width, true);

		//create all of the medium sections
		while (length < width - 10)// 64)
		{
			length += buildZone(length, width - length);
		}

		//set the end piece
		int floor = height - 1 - random.nextInt(4);

		xExit = length + 8;
		yExit = floor;

		// fills the end piece
		for (int x = length; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				if (y >= floor)
				{
					setBlock(x, y, GROUND);
				}
			}
		}

		if (type == LevelInterface.TYPE_CASTLE || type == LevelInterface.TYPE_UNDERGROUND)
		{
			int ceiling = 0;
			int run = 0;
			for (int x = 0; x < width; x++)
			{
				if (run-- <= 0 && x > 4)
				{
					ceiling = random.nextInt(4);
					run = random.nextInt(4) + 4;
				}
				for (int y = 0; y < height; y++)
				{
					if ((x > 4 && y <= ceiling) || x < 1)
					{
						setBlock(x, y, GROUND);
					}
				}
			}
		}

		fixWalls();

	}
	public void creat(long seed, int difficulty, int type, ARCH_MESSAGE m)
	{
		//System.out.println("");
		//System.out.println("-ArcLevel.creat() called...");                  
		this.type = type;       
		//this.difficulty = m.DIFFICULTY * 3 + 1;
		this.difficulty = difficulty; //use function parameter value instead of m.DIFFICULTY
		System.out.println("-procedurally generating a level segment with difficulty: " + difficulty);
		DIFFICULTY_sander = difficulty;
		//System.out.println("-also, using m.DIFFICULTY: " + m.DIFFICULTY);
		//System.out.println("-resulting in this.DIFFICULTY: " + this.difficulty);

		/*odds[ODDS_STRAIGHT] = m.ODDS_STRAIGHT;
	        odds[ODDS_HILL_STRAIGHT] = m.ODDS_HILL_STRAIGHT;
	        odds[ODDS_TUBES] = m.ODDS_TUBES;
	        odds[ODDS_JUMP] =  m.ODDS_JUMP;
	        odds[ODDS_CANNONS] = m.ODDS_CANNONS;*/
		MAX_COINS = m.MAX_COINS * 10;
		//GAP_SIZE = m.GAP_SIZE;
		// MAX_TURTLES = m.MAX_TURTLES;
		odds[ODDS_STRAIGHT] = 20;
		odds[ODDS_HILL_STRAIGHT] = 10;
		odds[ODDS_TUBES] = 2 + 1 * this.difficulty;
		odds[ODDS_JUMP] = 1*this.difficulty;
		odds[ODDS_CANNONS] = -10 + 5 * this.difficulty;

		if (type != LevelInterface.TYPE_OVERGROUND)
		{
			odds[ODDS_HILL_STRAIGHT] = 0;
		}

		for (int i = 0; i < odds.length; i++)
		{
			//failsafe (no negative odds)
			if (odds[i] < 0){
				odds[i] = 0;
			}

			totalOdds += odds[i];
			odds[i] = totalOdds - odds[i];
		}

		lastSeed = seed;
		random = new Random(seed);

		//create the start location
		int length = 0;
		length += buildStraight(0, width, true);

		//create all of the medium sections
		while (length < width - 10)// 64)
		{
			length += buildZone(length, width - length);
		}

		//set the end piece
		int floor = height - 1 - random.nextInt(4);

		xExit = length + 8;
		yExit = floor;

		// fills the end piece
		for (int x = length; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				if (y >= floor)
				{
					setBlock(x, y, GROUND);
				}
			}
		}

		if (type == LevelInterface.TYPE_CASTLE || type == LevelInterface.TYPE_UNDERGROUND)
		{
			int ceiling = 0;
			int run = 0;
			for (int x = 0; x < width; x++)
			{
				if (run-- <= 0 && x > 4)
				{
					ceiling = random.nextInt(4);
					run = random.nextInt(4) + 4;
				}
				for (int y = 0; y < height; y++)
				{
					if ((x > 4 && y <= ceiling) || x < 1)
					{
						setBlock(x, y, GROUND);
					}
				}
			}
		}

		fixWalls();

	}

	private int buildZone(int x, int maxLength)
	{
		int t = random.nextInt(totalOdds);
		int type = 0;

		for (int i = 0; i < odds.length; i++)
		{
			if (odds[i] <= t)
			{
				type = i;
			}
		}

		switch (type)
		{
		case ODDS_STRAIGHT:
			return buildStraight(x, maxLength, false);
		case ODDS_HILL_STRAIGHT:
			return buildHillStraight(x, maxLength);
		case ODDS_TUBES:
			return buildTubes(x, maxLength);
		case ODDS_JUMP:
			if (gaps < Constraints.gaps)
				return buildJump(x, maxLength);
			else
				return buildStraight(x, maxLength, false);
		case ODDS_CANNONS:
			return buildCannons(x, maxLength);
		}
		return 0;
	}

	private int buildJump(int xo, int maxLength)
	{	gaps++;
	//jl: jump length
	//js: the number of blocks that are available at either side for free
	int js = random.nextInt(GAP_SIZE) + 2;
	int jl = random.nextInt(GAP_SIZE) + 2;
	int length = js * 2 + jl;

	boolean hasStairs = random.nextInt(3) == 0;

	int floor = height - 1 - random.nextInt(4);
	//run from the start x position, for the whole length
	for (int x = xo; x < xo + length; x++)
	{
		if (x < xo + js || x > xo + length - js - 1)
		{
			//run for all y's since we need to paint blocks upward
			for (int y = 0; y < height; y++)
			{	//paint ground up until the floor
				if (y >= floor)
				{
					setBlock(x, y, GROUND);
				}
				//if it is above ground, start making stairs of rocks
				else if (hasStairs)
				{	//LEFT SIDE
					if (x < xo + js)
					{ //we need to max it out and level because it wont
						//paint ground correctly unless two bricks are side by side
						if (y >= floor - (x - xo) + 1)
						{
							setBlock(x, y, ROCK);
						}
					}
					else
					{ //RIGHT SIDE
						if (y >= floor - ((xo + length) - x) + 2)
						{
							setBlock(x, y, ROCK);
						}
					}
				}
			}
		}
	}

	return length;
	}

	private int buildCannons(int xo, int maxLength)
	{
		int length = random.nextInt(10) + 2;
		if (length > maxLength) length = maxLength;

		int floor = height - 1 - random.nextInt(4);
		int xCannon = xo + 1 + random.nextInt(4);
		for (int x = xo; x < xo + length; x++)
		{
			if (x > xCannon)
			{
				xCannon += 2 + random.nextInt(4);
			}
			if (xCannon == xo + length - 1) xCannon += 10;
			int cannonHeight = floor - random.nextInt(4) - 1;

			for (int y = 0; y < height; y++)
			{
				if (y >= floor)
				{
					setBlock(x, y, GROUND);
				}
				else
				{
					if (x == xCannon && y >= cannonHeight)
					{
						if (y == cannonHeight)
						{
							setBlock(x, y, (byte) (14 + 0 * 16));
						}
						else if (y == cannonHeight + 1)
						{
							setBlock(x, y, (byte) (14 + 1 * 16));
						}
						else
						{
							setBlock(x, y, (byte) (14 + 2 * 16));
						}
					}
				}
			}
		}

		return length;
	}

	private int buildHillStraight(int xo, int maxLength)
	{
		int length = random.nextInt(10) + 10;
		if (length > maxLength) length = maxLength;

		int floor = height - 1 - random.nextInt(4);
		for (int x = xo; x < xo + length; x++)
		{
			for (int y = 0; y < height; y++)
			{
				if (y >= floor)
				{
					setBlock(x, y, GROUND);
				}
			}
		}

		addEnemyLine(xo + 0, xo + length - 0, floor - 1);

		int h = floor;

		boolean keepGoing = true;

		boolean[] occupied = new boolean[length];
		while (keepGoing)
		{
			h = h - 2 - random.nextInt(3);

			if (h <= 0)
			{
				keepGoing = false;
			}
			else
			{
				int l = random.nextInt(5) + 3;
				int xxo = random.nextInt(length - l - 2) + xo + 1;

				if (occupied[xxo - xo] || occupied[xxo - xo + l] || occupied[xxo - xo - 1] || occupied[xxo - xo + l + 1])
				{
					keepGoing = false;
				}
				else
				{
					occupied[xxo - xo] = true;
					occupied[xxo - xo + l] = true;
					addEnemyLine(xxo, xxo + l, h - 1);
					if (random.nextInt(4) == 0)
					{
						decorate(xxo - 1, xxo + l + 1, h);
						keepGoing = false;
					}
					for (int x = xxo; x < xxo + l; x++)
					{
						for (int y = h; y < floor; y++)
						{
							int xx = 5;
							if (x == xxo) xx = 4;
							if (x == xxo + l - 1) xx = 6;
							int yy = 9;
							if (y == h) yy = 8;

							if (getBlock(x, y) == 0)
							{
								setBlock(x, y, (byte) (xx + yy * 16));
							}
							else
							{
								if (getBlock(x, y) == HILL_TOP_LEFT) setBlock(x, y, HILL_TOP_LEFT_IN);
								if (getBlock(x, y) == HILL_TOP_RIGHT) setBlock(x, y, HILL_TOP_RIGHT_IN);
							}
						}
					}
				}
			}
		}

		return length;
	}

	private void addEnemyLine(int x0, int x1, int y)
	{
		for (int x = x0; x < x1; x++)
		{
			if (random.nextInt(35) < difficulty + 1)
			{
				int type = random.nextInt(4);

				if (difficulty < 1)
				{
					type = Enemy.ENEMY_GOOMBA;
				}
				else if (difficulty < 3)
				{
					type = random.nextInt(3);
				}

				setSpriteTemplate(x, y, new SpriteTemplate(type, random.nextInt(35) < difficulty));
				ENEMIES++;
			}
		}
	}

	private int buildTubes(int xo, int maxLength)
	{
		int length = random.nextInt(10) + 5;
		if (length > maxLength) length = maxLength;

		int floor = height - 1 - random.nextInt(4);
		int xTube = xo + 1 + random.nextInt(4);
		int tubeHeight = floor - random.nextInt(2) - 2;
		for (int x = xo; x < xo + length; x++)
		{
			if (x > xTube + 1)
			{
				xTube += 3 + random.nextInt(4);
				tubeHeight = floor - random.nextInt(2) - 2;
			}
			if (xTube >= xo + length - 2) xTube += 10;

			if (x == xTube && random.nextInt(11) < difficulty + 1)
			{
				setSpriteTemplate(x, tubeHeight, new SpriteTemplate(Enemy.ENEMY_FLOWER, false));
				ENEMIES++;
			}

			for (int y = 0; y < height; y++)
			{
				if (y >= floor)
				{
					setBlock(x, y,GROUND);

				}
				else
				{
					if ((x == xTube || x == xTube + 1) && y >= tubeHeight)
					{
						int xPic = 10 + x - xTube;

						if (y == tubeHeight)
						{
							//tube top
							setBlock(x, y, (byte) (xPic + 0 * 16));
						}
						else
						{
							//tube side
							setBlock(x, y, (byte) (xPic + 1 * 16));
						}
					}
				}
			}
		}

		return length;
	}

	private int buildStraight(int xo, int maxLength, boolean safe)
	{
		int length = random.nextInt(10) + 2;

		if (safe)
			length = 10 + random.nextInt(5);

		if (length > maxLength)
			length = maxLength;

		int floor = height - 1 - random.nextInt(4);

		//runs from the specified x position to the length of the segment
		for (int x = xo; x < xo + length; x++)
		{
			for (int y = 0; y < height; y++)
			{
				if (y >= floor)
				{
					setBlock(x, y, GROUND);
				}
			}
		}

		if (!safe)
		{
			if (length > 5)
			{
				decorate(xo, xo + length, floor);
			}
		}

		return length;
	}

	private void decorate(int xStart, int xLength, int floor) {
		//if its at the very top, just return
				if (floor < 1)
					return;
		boolean rocks = true;

		//add an enemy line above the box
		addEnemyLine(xStart + 1, xLength - 1, floor - 1);

		int s = random.nextInt(4);
		int e = random.nextInt(4);


		if (floor - 2 > 0) {
			if ((xLength - 1 - e) - (xStart + 1 + s) > 1) {
				for (int x = xStart + 1 + s; x < xLength - 1 - e; x++) {
					setBlock(x, floor - 2, COIN);
					COINS++;
				}
			}
		}

		s = random.nextInt(4);
		e = random.nextInt(4);

		if (floor - 4 > 0) {
			if ((xLength - 1 - e) - (xStart + 1 + s) > 2) {
				for (int x = xStart + 1 + s; x < xLength - 1 - e; x++) {
					if (rocks) {
						if (x != xStart + 1 && x != xLength - 2 &&
								random.nextInt(2) == 0) {
							if (random.nextInt(2) == 0) {
								setBlock(x, floor - 4, BLOCK_POWERUP);
								BLOCKS_POWER++;
							} else {
								if(COINS < MAX_COINS){
									COINS++;
									setBlock(x, floor - 4, BLOCK_COIN);
									BLOCKS_COINS++;

								}
								else{
									setBlock(x, floor - 4, BLOCK_EMPTY);
									BLOCKS_EMPTY++;
								}
							}
						} else if (random.nextInt(4) == 0) {
							if (random.nextInt(4) == 0) {
								setBlock(x, floor - 4, (byte) (2 + 1 * 16));
							} else {
								setBlock(x, floor - 4, (byte) (1 + 1 * 16));
							}
						} else {
							setBlock(x, floor - 4, BLOCK_EMPTY);
							BLOCKS_EMPTY++;
						}
					}
				}
			}
		}
	}

	private void fixWalls()
	{
		boolean[][] blockMap = new boolean[width + 1][height + 1];

		for (int x = 0; x < width + 1; x++)
		{
			for (int y = 0; y < height + 1; y++)
			{
				int blocks = 0;
				for (int xx = x - 1; xx < x + 1; xx++)
				{
					for (int yy = y - 1; yy < y + 1; yy++)
					{
						if (getBlockCapped(xx, yy) == GROUND){
							blocks++;
						}
					}
				}
				blockMap[x][y] = blocks == 4;
			}
		}
		blockify(this, blockMap, width + 1, height + 1);
	}

	private void blockify(Level level, boolean[][] blocks, int width, int height){
		int to = 0;
		if (type == LevelInterface.TYPE_CASTLE)
		{
			to = 4 * 2;
		}
		else if (type == LevelInterface.TYPE_UNDERGROUND)
		{
			to = 4 * 3;
		}

		boolean[][] b = new boolean[2][2];

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				for (int xx = x; xx <= x + 1; xx++)
				{
					for (int yy = y; yy <= y + 1; yy++)
					{
						int _xx = xx;
						int _yy = yy;
						if (_xx < 0) _xx = 0;
						if (_yy < 0) _yy = 0;
						if (_xx > width - 1) _xx = width - 1;
						if (_yy > height - 1) _yy = height - 1;
						b[xx - x][yy - y] = blocks[_xx][_yy];
					}
				}

				if (b[0][0] == b[1][0] && b[0][1] == b[1][1])
				{
					if (b[0][0] == b[0][1])
					{
						if (b[0][0])
						{
							level.setBlock(x, y, (byte) (1 + 9 * 16 + to));
						}
						else
						{
							// KEEP OLD BLOCK!
						}
					}
					else
					{
						if (b[0][0])
						{
							//down grass top?
									level.setBlock(x, y, (byte) (1 + 10 * 16 + to));
						}
						else
						{
							//up grass top
							level.setBlock(x, y, (byte) (1 + 8 * 16 + to));
						}
					}
				}
				else if (b[0][0] == b[0][1] && b[1][0] == b[1][1])
				{
					if (b[0][0])
					{
						//right grass top
						level.setBlock(x, y, (byte) (2 + 9 * 16 + to));
					}
					else
					{
						//left grass top
						level.setBlock(x, y, (byte) (0 + 9 * 16 + to));
					}
				}
				else if (b[0][0] == b[1][1] && b[0][1] == b[1][0])
				{
					level.setBlock(x, y, (byte) (1 + 9 * 16 + to));
				}
				else if (b[0][0] == b[1][0])
				{
					if (b[0][0])
					{
						if (b[0][1])
						{
							level.setBlock(x, y, (byte) (3 + 10 * 16 + to));
						}
						else
						{
							level.setBlock(x, y, (byte) (3 + 11 * 16 + to));
						}
					}
					else
					{
						if (b[0][1])
						{
							//right up grass top
							level.setBlock(x, y, (byte) (2 + 8 * 16 + to));
						}
						else
						{
							//left up grass top
							level.setBlock(x, y, (byte) (0 + 8 * 16 + to));
						}
					}
				}
				else if (b[0][1] == b[1][1])
				{
					if (b[0][1])
					{
						if (b[0][0])
						{
							//left pocket grass
							level.setBlock(x, y, (byte) (3 + 9 * 16 + to));
						}
						else
						{
							//right pocket grass
							level.setBlock(x, y, (byte) (3 + 8 * 16 + to));
						}
					}
					else
					{
						if (b[0][0])
						{
							level.setBlock(x, y, (byte) (2 + 10 * 16 + to));
						}
						else
						{
							level.setBlock(x, y, (byte) (0 + 10 * 16 + to));
						}
					}
				}
				else
				{
					level.setBlock(x, y, (byte) (0 + 1 * 16 + to));
				}
			}
		}
	}

	public ArchLevel clone() throws CloneNotSupportedException {

		ArchLevel clone=new ArchLevel(width, height);

		clone.xExit = xExit;
		clone.yExit = yExit;
		byte[][] map = getMap();
		SpriteTemplate[][] st = getSpriteTemplate();

		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++) {
				clone.setBlock(i, j, map[i][j]);
				clone.setSpriteTemplate(i, j, st[i][j]);
			}
		clone.BLOCKS_COINS = BLOCKS_COINS;
		clone.BLOCKS_EMPTY = BLOCKS_EMPTY;
		clone.BLOCKS_POWER = BLOCKS_POWER;
		clone.ENEMIES = ENEMIES;
		clone.COINS = COINS;

		return clone;

	}




}
