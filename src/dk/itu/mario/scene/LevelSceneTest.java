package dk.itu.mario.scene;

import java.awt.GraphicsConfiguration;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;

import Architect.*;

import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.trees.RandomForest;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.classifiers.Evaluation;

import weka.estimators.KernelEstimator;

import level2.ArchLevel;
import level2.BgLevelGenerator;
import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.engine.sonar.FixedSoundSource;
import dk.itu.mario.engine.sprites.CoinAnim;
import dk.itu.mario.engine.sprites.FireFlower;
import dk.itu.mario.engine.sprites.Mario;
import dk.itu.mario.engine.sprites.Mushroom;
import dk.itu.mario.engine.sprites.Particle;
import dk.itu.mario.engine.sprites.Sprite;
import dk.itu.mario.engine.util.FileHandler;

import dk.itu.mario.engine.Art;
import dk.itu.mario.engine.BgRenderer;
import dk.itu.mario.engine.DataRecorder;
import dk.itu.mario.engine.LevelRenderer;
import dk.itu.mario.engine.MarioComponent;
import level2.*;
import level2.generator.CustomizedLevelGenerator;
import dk.itu.mario.engine.Play;
import dk.itu.mario.res.ResourcesManager;



	public class LevelSceneTest extends LevelScene{

			ArrayList<Double> switchPoints;
			private double thresshold; //how large the distance from point to mario should be before switching
			private int point = -1;
			private int []checkPoints;
			private boolean isCustom;
			
			public boolean recording = false;
			public boolean l2 = true;
			public boolean l3 = false;
			public ArchLevel level2;
			public ArchLevel level2_reset;
                        public ArchLevel level3;
                        public ArchLevel level3_reset;
		        public boolean gameover = false;
                        
                        //General variables
                        public boolean verbose = false;
                        
                        //Variables for Random Forest classification
                        public RandomForest RF = new RandomForest();
                        public Instances RF_trainingInstances;
                        public Instances RF_testInstances;
                                               
                        //Create KernelEstimators for all possible actions (difficulty levels 1, 4, and 7) and all 45 features
                        double kernel_estimator_precision = 0.1;
                        //Non-appropriate player experience (i.e. 0)
                        public KernelEstimator KE0_1 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_2 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_3 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_4 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_5 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_6 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_7 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_8 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_9 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_10 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_11 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_12 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_13 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_14 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_15 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_16 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_17 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_18 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_19 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_20 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_21 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_22 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_23 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_24 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_25 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_26 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_27 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_28 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_29 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_30 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_31 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_32 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_33 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_34 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_35 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_36 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_37 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_38 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_39 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_40 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_41 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_42 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_43 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_44 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE0_45 = new KernelEstimator(kernel_estimator_precision);                        
                        //Appropriate player experience (i.e. 1)
                        public KernelEstimator KE1_1 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_2 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_3 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_4 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_5 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_6 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_7 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_8 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_9 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_10 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_11 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_12 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_13 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_14 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_15 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_16 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_17 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_18 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_19 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_20 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_21 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_22 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_23 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_24 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_25 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_26 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_27 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_28 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_29 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_30 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_31 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_32 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_33 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_34 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_35 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_36 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_37 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_38 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_39 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_40 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_41 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_42 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_43 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_44 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KE1_45 = new KernelEstimator(kernel_estimator_precision);
                        //Kernel filled with all observations
                        public KernelEstimator KEall_1 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_2 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_3 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_4 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_5 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_6 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_7 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_8 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_9 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_10 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_11 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_12 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_13 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_14 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_15 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_16 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_17 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_18 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_19 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_20 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_21 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_22 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_23 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_24 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_25 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_26 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_27 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_28 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_29 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_30 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_31 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_32 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_33 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_34 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_35 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_36 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_37 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_38 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_39 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_40 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_41 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_42 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_43 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_44 = new KernelEstimator(kernel_estimator_precision);
                        public KernelEstimator KEall_45 = new KernelEstimator(kernel_estimator_precision);
                        

		    
		        Architect arch;

			public LevelSceneTest(GraphicsConfiguration graphicsConfiguration,
					MarioComponent renderer, long seed, int levelDifficulty, int type, boolean isCustom){
				super(graphicsConfiguration,renderer,seed,levelDifficulty,type);
				this.isCustom = isCustom;
			}

			public void init() {
		        try
		        {
		            Level.loadBehaviors(new DataInputStream(ResourcesManager.class.getResourceAsStream("res/tiles.dat")));
		        }
		        catch (IOException e)
		        {
		            e.printStackTrace();
		            System.exit(0);
		        }

                        System.out.println("");
                        System.out.println("----------------------------------------");
                        System.out.println("---------- Initialising game -----------");
                        System.out.println("----------------------------------------");
                        
		        //Init state
                        arch = new Architect();
		        arch.updateMessage();
		        m = new ARCH_MESSAGE();
                        
                        //Track planned difficuly levels for each level segment
                        currentLevelSegment = 0;
                        nextSegmentAlreadyGenerated = false;

                        //Load training instances from ARFF file
                        boolean verbose = false;
                        loadTrainingInstances(verbose);
                        
                        //Fill kernels with training data
                        //fillKernels();
                                               
                        //Init player model with average rewards based on historic data (i.e., from training instances)
                        System.out.println("");
                        System.out.println("Initialising player model with training instances...");                       
                        //Loop through training instances
                        for (int i=0; i < RF_trainingInstances.numInstances(); i++) {
                            //Calculate reward for selected instance, add reward to appropriate player models, and update display of average accumulate reward
                            Instance trainingInstance = selectTrainingInstance(i);
                            boolean doBernoulliRewards = false;
                            boolean isTrainingInstance = true;
                            verbose = false;
                            updateReward(trainingInstance, doBernoulliRewards, isTrainingInstance, verbose); //update reward in playerModelDiff1,4,7
                            updatePlayerModel();
                            //displayReceivedRewards();
                        }
                        displayReceivedRewards();
                        
                        //Set difficulty of first level segment(s?)
                        //levelDifficulty = 1;
                        levelDifficulty = setAction(); //set action using Softmax
                        m.DIFFICULTY = levelDifficulty;
                        //m.DIFFICULTY = arch.message.DIFFICULTY*3+1; //arch.message.DIFFICULTY is initialised with 1 in Architect\state
                        //m.DIFFICULTY = setAction(); //set action using Softmax
                        //m.state = getDifficulty(); //function return m.DIFFICULTY
                        m.state = levelDifficulty;
                        //m.state[1] = randomNumber(0,3); //initial appropriateness measurement is random at the moment
                        System.out.println("");
                        System.out.println("Initialising game...");
                        //System.out.println("-levelDifficulty: " + levelDifficulty);
                        //System.out.println("-arch.message.DIFFICULTY: " + arch.message.DIFFICULTY);
                        //System.out.println("-m.DIFFICULTY: " + m.DIFFICULTY);
                        //System.out.println("-m.state: " + m.state);
                        System.out.println("-initialising two level segments with difficulty: " + levelDifficulty );
                        
                        //TEST - Set next action using Softmax
                        //setAction(); //sets m.bestAction
                                               
                        //TEST - Get probability of appropriateness
                        //String observation_str = "32, 32, 24, 0, 0, 28, 2, 19, 5, 23, 30, 1, 5, 5, 5, 1, 1, 1, 0, 0, 4, 23, 0, 0, 2, 16.0, 2.0, 1.0, 0.0, 1.0, 0.0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1";
                        //getProbsAppropriateness(observation_str, true);

                        //TEST - Update reward in player models
                        //System.out.println("-reward function has been initialised"); //is now initialised to 0 in setPlayerModel()
                        //String observation_str = "32, 32, 24, 0, 0, 28, 2, 19, 5, 23, 30, 1, 5, 5, 5, 1, 1, 1, 0, 0, 4, 23, 0, 0, 2, 16.0, 2.0, 1.0, 0.0, 1.0, 0.0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1";
                        //updateReward(observation_str);                       

                        //Generate base level?
		        if(level==null)
		        	/*if(isCustom){
		        		CustomizedLevelGenerator clg = new CustomizedLevelGenerator();
		        		GamePlay gp = new GamePlay();
		        		gp = gp.read("player.txt");
		        		currentLevel = (Level)clg.generateLevel(gp);
		        		
		        		//You can use the following commands if you want to benefit from
		        		//	the interface containing detailed information
		        		String detailedInfo = FileHandler.readFile("DetailedInfo.txt");
		                
		              }
			        	else*/
                                        //levelDifficulty = 1;
                                        //plannedDifficultyLevels.add(levelDifficulty); //as this level segment seems not to be used, only add difficulties of actually created segments
                                        currentLevel = new RandomLevel(200, 15, levelSeed, levelDifficulty, levelType); //it's my impression this level segment is not directly used, perhaps overwritten elsewhere?
		        try {
					 level = currentLevel.clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}                        
                        
                        //And continue
                        m.states=arch.states.clone();
		        //level is always overground
		        //Art.startMusic(1);

		        paused = false;
		        Sprite.spriteContext = this;
		        sprites.clear();
		        
		        Random randomGenerator = new Random();
		        int randomInt = randomGenerator.nextInt(100);
		        //level = LevelGenerator.createLevel(320, 15, levelSeed+randomInt, levelDifficulty, levelType);
		        //randomInt = randomGenerator.nextInt(100);
		        //level2 = new CustomizedLevel(100, 15, levelSeed+randomInt , levelDifficulty , levelType,arch.message);
                        
                        //levelDifficulty = m.DIFFICULTY; - this is good, but let's use the same function everywhere to get the difficulty, so:
                        //levelDifficulty = getDifficulty();
                        //levelDifficulty = 1;
                        //plannedDifficultyLevels.add(levelDifficulty);
                        level2 = new ArchLevel(100, 15, levelSeed+randomInt, levelDifficulty, levelType, arch.message);
                        plannedDifficultyLevels.add(level2.DIFFICULTY_sander);
                        
		        //level2 = new ArchLevel(100, 15, levelSeed+randomInt , levelDifficulty , levelType,arch.message);
		        //level2 = new RandomLevel(100, 15, levelSeed+randomInt , levelDifficulty , levelType);
		        
		        randomInt = randomGenerator.nextInt(100);
		       // level3 = new CustomizedLevel(100, 15, levelSeed+randomInt , levelDifficulty,levelType,arch.message);
		        //level3 = new RandomLevel(100, 15, levelSeed+randomInt , levelDifficulty , levelType);
                        //plannedDifficultyLevels.add(levelDifficulty);
		        level3 = new ArchLevel(100, 15, levelSeed+randomInt, levelDifficulty, levelType, arch.message);
                        plannedDifficultyLevels.add(level3.DIFFICULTY_sander);

		        fixborders();
		        conjoin();
                        		        
	    		try {
					level2_reset = level2.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	try {
					level3_reset = level3.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		       		        
		        layer = new LevelRenderer(level, graphicsConfiguration, 320, 240);
		        for (int i = 0; i < 2; i++)
		        {
		            int scrollSpeed = 4 >> i;
		            int w = ((level.getWidth() * 16) - 320) / scrollSpeed + 320;
		            int h = ((level.getHeight() * 16) - 240) / scrollSpeed + 240;
		            Level bgLevel = BgLevelGenerator.createLevel(w / 32 + 1, h / 32 + 1, i == 0, levelType);
		            bgLayer[i] = new BgRenderer(bgLevel, graphicsConfiguration, 320, 240, scrollSpeed);
		        }

		        double oldX = 0;
		        if(mario!=null)
		        	oldX = mario.x;

		        mario = new Mario(this);
		        sprites.add(mario);
		        startTime = 1;

		        timeLeft = 200*15;

		        tick = 0;
		        
		        /*
		         * SETS UP ALL OF THE CHECKPOINTS TO CHECK FOR SWITCHING
		         */
		        switchPoints = new ArrayList<Double>();

		        //first pick a random starting waypoint from among ten positions
		    	int squareSize = 16; //size of one square in pixels
		        int sections = 10;

		    	double startX = 32; //mario start position
		    	double endX = level.getxExit()*squareSize; //position of the end on the level
		    	//if(!isCustom && recorder==null)
		    	
		    	recorder = new DataRecorder(this,level2,keys);
		    	//System.out.println("\n enemies LEFT : " + recorder.level.COINS); //SANDER disable
		    	//System.out.println("\n enemies LEFT : " + recorder.level.BLOCKS_COINS);
		    	//System.out.println("\n enemies LEFT : " + recorder.level.BLOCKS_POWER);
		        gameStarted = false;
			}                     
                        
                        public double getProbsAppropriateness_ObservationStr(String observation_str, boolean verbose){
                            //Return probability of appropriateness
                            //NOTE OLD - for use with Kernels, not RandomForest classifiers
                            double prob_obs_given_appr_0 = getObsevationProbability(observation_str, 0); //appr level 0
                            double prob_obs_given_appr_1 = getObsevationProbability(observation_str, 1); //appr level 1
                            double prior_appropriateness = 0.5;
                            double prob_obs = getObsevationProbability(observation_str, 99); //99 is identifier for KEall kernels with all observations
                            //double prob_appr_0_given_obs = (prob_obs_given_appr_0 * prior_appropriateness) / prob_obs;
                            //double prob_appr_1_given_obs = (prob_obs_given_appr_1 * prior_appropriateness) / prob_obs;
                            double prob_appr_0_given_obs = ( prob_obs_given_appr_0 * prior_appropriateness ) / ( prob_obs_given_appr_0 * prior_appropriateness + prob_obs_given_appr_1  * prior_appropriateness);
                            double prob_appr_1_given_obs = ( prob_obs_given_appr_1 * prior_appropriateness ) / ( prob_obs_given_appr_1 * prior_appropriateness + prob_obs_given_appr_0  * prior_appropriateness);

                            //Verbose output
                            if (verbose) {
                                System.out.println("");
                                System.out.println("Calculating observation probabilities");
                                System.out.println("-for string: " + observation_str);
                                //String observation_str = "32, 32, 24, 0, 0, 28, 2, 19, 5, 23, 30, 1, 5, 5, 5, 1, 1, 1, 0, 0, 4, 23, 0, 0, 2, 16.0, 2.0, 1.0, 0.0, 1.0, 0.0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1";
                                System.out.println("-probability of observation given appropriateness level 0: " + prob_obs_given_appr_0 );
                                System.out.println("-probability of observation given appropriateness level 1: " + prob_obs_given_appr_1 );                      
                                System.out.println("-prior probability of appropriateness: " + prior_appropriateness );
                                System.out.println("-probability of observation: " + prob_obs );
                                System.out.println("-probability of appropriateness level 0 given observation: " + prob_appr_0_given_obs );
                                System.out.println("-probability of appropriateness level 1 given observation: " + prob_appr_1_given_obs );
                            }
                            
                            //Return probability of appropriateness
                            return prob_appr_1_given_obs;
                        }
                        
                        public void loadTrainingInstances(boolean verbose) {
                            try {
                                //Load training instances into data
                                System.out.println("");
                                System.out.println("Loading training instances into RandomForest classifier...");
                                BufferedReader reader = new BufferedReader(
                                    new FileReader("C:\\Users\\software\\Documents\\NetBeansProjects\\MarioPOMDP\\MarioPOMDP-traininginstances.arff"));
                                Instances data = new Instances(reader);
                                reader.close();
                                // setting class attribute
                                data.setClassIndex(data.numAttributes() - 2);            //2nd to last attribute is used for classification (last is timestamp)
                                
                                //Filter out timestamp string data
                                String[] options = new String[2];
                                options[0] = "-R";                                       // "range"
                                options[1] = "48";                                       // last timestamp attribute
                                Remove remove = new Remove();                            // new instance of filter
                                remove.setOptions(options);                              // set options
                                remove.setInputFormat(data);                             // inform filter about dataset **AFTER** setting options
                                RF_trainingInstances = Filter.useFilter(data, remove);   // apply filter
                                                               
                                //Build RandomForest classifier
                                String[] options_RF = new String[1];
                                options_RF[0] = "-D";          // debug output
                                //RandomForest RF = new RandomForest(); //declared as public
                                //RF.setOptions(options_RF);
                                RF.buildClassifier(RF_trainingInstances);
                                
                                if (verbose) {
                                    //Get classification of example data
                                    //System.out.println(RF.toString());
                                    Evaluation eTest = new Evaluation(data);
                                    //eTest.evaluateModel(RF, RF_trainingInstances);
                                    //eTest.crossValidateModel(RF, data, 10, new Random());
                                    int folds = 10;
                                    Random rand = new Random(0);  // using seed = 0 (should be 1?)
                                    eTest.crossValidateModel(RF, RF_trainingInstances, folds, rand);

                                    // Print the result à la Weka explorer:
                                    String strSummary = eTest.toSummaryString();
                                    System.out.println(strSummary);
                                    //System.out.println(eTest.toClassDetailsString());

                                    // Get the confusion matrix
                                    //double[][] cmMatrix = eTest.confusionMatrix();                                
                                    //System.out.println(eTest.toMatrixString());
                                }
                                
                                System.out.println("-done loading " + RF_trainingInstances.numInstances() + " training instance(s)");
                            }
                            catch (Exception e) {
                                //Error reading file
                                System.out.println("ERROR!!! - In function loadTrainingInstances()...");
                                System.out.println("-" + e);
                            }                            
                        }

                        public void loadTestInstances(boolean verbose) {
                            try {
                                //Load test instances into data
                                System.out.println("");
                                System.out.println("Loading test instances...");
                                BufferedReader reader = new BufferedReader(
                                    new FileReader("C:\\Users\\software\\Documents\\NetBeansProjects\\MarioPOMDP\\MarioPOMDP-testinstances.arff"));
                                Instances data = new Instances(reader);
                                reader.close();
                                // setting class attribute
                                data.setClassIndex(data.numAttributes() - 2);        //2nd to last attribute is used for classification (last is timestamp)
                                
                                //Filter out string data
                                String[] options = new String[2];
                                options[0] = "-R";                                   // "range"
                                options[1] = "48";                                   // last timestamp attribute
                                Remove remove = new Remove();                        // new instance of filter
                                remove.setOptions(options);                          // set options
                                        remove.setInputFormat(data);                 // inform filter about dataset **AFTER** setting options
                                RF_testInstances = Filter.useFilter(data, remove);   // apply filter                                

                                System.out.println("-done loading " + RF_testInstances.numInstances() + " test instance(s)");
                            }
                            catch (Exception e) {
                                //Error reading file
                                System.out.println("ERROR!!! - In function loadTestInstances()...");
                                System.out.println("-" + e);
                            }                            
                        }
                        
                        public Instance selectTestInstance() {
                            //Select last instance from loaded set of Test Instances

                            //Create test instance
                            //Instance testInstance = new Instance(newDataTest.firstInstance());
                            //Instance testInstance = new Instance(newDataTest.instance(0));
                            Instance testInstance = new Instance(RF_testInstances.lastInstance());
                            //System.out.println("-selecting last instance in test set RF_testInstances, done");

                            // Specify that the instance belong to the training set 
                            // in order to inherit from the set description                                
                            testInstance.setDataset(RF_trainingInstances);
                            System.out.println("-selected last instance in test set: " + testInstance.toString() );

                            return testInstance;
                        }

                        public Instance selectTrainingInstance(int index) {
                            //Select last instance from loaded set of Test Instances

                            //Create test instance
                            //Instance trainingInstance = new Instance(RF_trainingInstances.firstInstance());
                            //Instance trainingInstance = new Instance(RF_trainingInstances.lastInstance());
                            Instance trainingInstance = new Instance(RF_trainingInstances.instance(index));                           

                            // Specify that the instance belong to the training set 
                            // in order to inherit from the set description                                
                            trainingInstance.setDataset(RF_trainingInstances);
                            System.out.println("-processing instance # " + index + " in training set: " + trainingInstance.toString() );

                            return trainingInstance;
                        }
                        
                        public double classifyInstance(Instance testInstance, boolean verbose) {
                            try {
                                //Classify one particular instance from loaded set of Test Instances
                               
                                //Create test instance
                                //Instance testInstance = new Instance(newDataTest.firstInstance());
                                //Instance testInstance = new Instance(newDataTest.instance(0));
                                //Instance testInstance = new Instance(RF_testInstances.lastInstance());
                                //System.out.println("-selecting last instance in test set RF_testInstances, done");
                                
                                // Specify that the instance belong to the training set 
                                // in order to inherit from the set description                                
                                //testInstance.setDataset(RF_trainingInstances);
                                
                                // Get the likelihood of each classes 
                                // fDistribution[0] is the probability of being positive
                                // fDistribution[1] is the probability of being negative 
                                double[] fDistribution = RF.distributionForInstance(testInstance);
                                
                                if (verbose) {
                                    System.out.println("");
                                    System.out.println("Classifying selected test instance...");                               
                                    System.out.println("-probability of instance being appropriate     (1): " + fDistribution[1]);
                                    System.out.println("-probability of instance being non-appropriate (0): " + fDistribution[0]);
                                    System.out.println("-returning appropriateness probability of: " + fDistribution[1]);
                                }
                                
                                return fDistribution[1];
                            }
                            catch (Exception e) {
                                //Error reading file
                                System.out.println("ERROR!!! - In function classifyInstance()...");
                                System.out.println("-" + e);
                                return 0.0; //dummy value
                            }                            
                        }
                        
                        public void fillKernels() {
                                try {
                                    //Fill KernelEstimators
                                    System.out.println("");
                                    System.out.println("Filling kernels with training data...");
                                    int linesProcessed = 0;
                                    //System.out.println("-KE0_1 before filling: " + KE0_1.toString());
                                    BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\software\\Documents\\NetBeansProjects\\MarioPOMDP\\POMDPmetrics-training-data.arff"));
                                    String str;
                                    //str = in.readLine();
                                    while ((str = in.readLine()) != null)  {
                                          if ( (str.startsWith("@") == false) && (str.isEmpty() == false) ) {
                                                //Fill KernelEstimators depending on appropriateness level (so, for each appropriateness level, there will be kernels of the 45 observational features)
                                                linesProcessed++;
                                                String[] ar = str.split(",");
                                                //System.out.println("---------------");
                                                //System.out.println(str);
                                                /*
                                                System.out.println("---------------");
                                                System.out.println(str);
                                                System.out.println( Double.parseDouble(ar[0]) );
                                                System.out.println( Double.parseDouble(ar[1]) );
                                                System.out.println( Double.parseDouble(ar[2]) );
                                                System.out.println( Double.parseDouble(ar[3]) );
                                                System.out.println( Double.parseDouble(ar[4]) );
                                                System.out.println( Double.parseDouble(ar[5]) );
                                                System.out.println( Double.parseDouble(ar[6]) );
                                                System.out.println( Double.parseDouble(ar[7]) );
                                                System.out.println( Double.parseDouble(ar[8]) );
                                                System.out.println( Double.parseDouble(ar[9]) );
                                                System.out.println( Double.parseDouble(ar[10]) );
                                                System.out.println( Double.parseDouble(ar[11]) );
                                                System.out.println( Double.parseDouble(ar[12]) );
                                                System.out.println( Double.parseDouble(ar[13]) );
                                                System.out.println( Double.parseDouble(ar[14]) );
                                                System.out.println( Double.parseDouble(ar[15]) );
                                                System.out.println( Double.parseDouble(ar[16]) );
                                                System.out.println( Double.parseDouble(ar[17]) );
                                                System.out.println( Double.parseDouble(ar[18]) );
                                                System.out.println( Double.parseDouble(ar[19]) );
                                                System.out.println( Double.parseDouble(ar[20]) );
                                                System.out.println( Double.parseDouble(ar[21]) );
                                                System.out.println( Double.parseDouble(ar[22]) );
                                                System.out.println( Double.parseDouble(ar[23]) );
                                                System.out.println( Double.parseDouble(ar[24]) );
                                                System.out.println( Double.parseDouble(ar[25]) );
                                                System.out.println( Double.parseDouble(ar[26]) );
                                                System.out.println( Double.parseDouble(ar[27]) );
                                                System.out.println( Double.parseDouble(ar[28]) );
                                                System.out.println( Double.parseDouble(ar[29]) );
                                                System.out.println( Double.parseDouble(ar[30]) );
                                                System.out.println( Double.parseDouble(ar[31]) );
                                                System.out.println( Double.parseDouble(ar[32]) );
                                                System.out.println( Double.parseDouble(ar[33]) );
                                                System.out.println( Double.parseDouble(ar[34]) );
                                                System.out.println( Double.parseDouble(ar[35]) );
                                                System.out.println( Double.parseDouble(ar[36]) );
                                                System.out.println( Double.parseDouble(ar[37]) );
                                                System.out.println( Double.parseDouble(ar[38]) );
                                                System.out.println( Double.parseDouble(ar[39]) );
                                                System.out.println( Double.parseDouble(ar[40]) );
                                                System.out.println( Double.parseDouble(ar[41]) );
                                                System.out.println( Double.parseDouble(ar[42]) );
                                                System.out.println( Double.parseDouble(ar[43]) );
                                                System.out.println( Double.parseDouble(ar[44]) );
                                                */
                                                //int diff = (int)Double.parseDouble(ar[45]); //actual difficulty level of this observation //and yes i know the casting is silly here
                                                int appr = (int)Double.parseDouble(ar[46]); //appropriateness of the level segment to the player
                                                //System.out.println(appr);
                                                double weight = 1.0;
                                                switch (appr) {
                                                    case 0: 
                                                        //Add to KE0 kernels
                                                        KE0_1.addValue(Double.parseDouble(ar[0]), weight);
                                                        KE0_2.addValue(Double.parseDouble(ar[1]), weight);
                                                        KE0_3.addValue(Double.parseDouble(ar[2]), weight);
                                                        KE0_4.addValue(Double.parseDouble(ar[3]), weight);
                                                        KE0_5.addValue(Double.parseDouble(ar[4]), weight);
                                                        KE0_6.addValue(Double.parseDouble(ar[5]), weight);
                                                        KE0_7.addValue(Double.parseDouble(ar[6]), weight);
                                                        KE0_8.addValue(Double.parseDouble(ar[7]), weight);
                                                        KE0_9.addValue(Double.parseDouble(ar[8]), weight);
                                                        KE0_10.addValue(Double.parseDouble(ar[9]), weight);
                                                        KE0_11.addValue(Double.parseDouble(ar[10]), weight);
                                                        KE0_12.addValue(Double.parseDouble(ar[11]), weight);
                                                        KE0_13.addValue(Double.parseDouble(ar[12]), weight);
                                                        KE0_14.addValue(Double.parseDouble(ar[13]), weight);
                                                        KE0_15.addValue(Double.parseDouble(ar[14]), weight);
                                                        KE0_16.addValue(Double.parseDouble(ar[15]), weight);
                                                        KE0_17.addValue(Double.parseDouble(ar[16]), weight);
                                                        KE0_18.addValue(Double.parseDouble(ar[17]), weight);
                                                        KE0_19.addValue(Double.parseDouble(ar[18]), weight);
                                                        KE0_20.addValue(Double.parseDouble(ar[19]), weight);
                                                        KE0_21.addValue(Double.parseDouble(ar[20]), weight);
                                                        KE0_22.addValue(Double.parseDouble(ar[21]), weight);
                                                        KE0_23.addValue(Double.parseDouble(ar[22]), weight);
                                                        KE0_24.addValue(Double.parseDouble(ar[23]), weight);
                                                        KE0_25.addValue(Double.parseDouble(ar[24]), weight);
                                                        KE0_26.addValue(Double.parseDouble(ar[25]), weight);
                                                        KE0_27.addValue(Double.parseDouble(ar[26]), weight);
                                                        KE0_28.addValue(Double.parseDouble(ar[27]), weight);
                                                        KE0_29.addValue(Double.parseDouble(ar[28]), weight);
                                                        KE0_30.addValue(Double.parseDouble(ar[29]), weight);
                                                        KE0_31.addValue(Double.parseDouble(ar[30]), weight);
                                                        KE0_32.addValue(Double.parseDouble(ar[31]), weight);
                                                        KE0_33.addValue(Double.parseDouble(ar[32]), weight);
                                                        KE0_34.addValue(Double.parseDouble(ar[33]), weight);
                                                        KE0_35.addValue(Double.parseDouble(ar[34]), weight);
                                                        KE0_36.addValue(Double.parseDouble(ar[35]), weight);
                                                        KE0_37.addValue(Double.parseDouble(ar[36]), weight);
                                                        KE0_38.addValue(Double.parseDouble(ar[37]), weight);
                                                        KE0_39.addValue(Double.parseDouble(ar[38]), weight);
                                                        KE0_40.addValue(Double.parseDouble(ar[39]), weight);
                                                        KE0_41.addValue(Double.parseDouble(ar[40]), weight);
                                                        KE0_42.addValue(Double.parseDouble(ar[41]), weight);
                                                        KE0_43.addValue(Double.parseDouble(ar[42]), weight);
                                                        KE0_44.addValue(Double.parseDouble(ar[43]), weight);
                                                        KE0_45.addValue(Double.parseDouble(ar[44]), weight);
                                                        //Also: Add to KEall kernels
                                                        KEall_1.addValue(Double.parseDouble(ar[0]), weight);
                                                        KEall_2.addValue(Double.parseDouble(ar[1]), weight);
                                                        KEall_3.addValue(Double.parseDouble(ar[2]), weight);
                                                        KEall_4.addValue(Double.parseDouble(ar[3]), weight);
                                                        KEall_5.addValue(Double.parseDouble(ar[4]), weight);
                                                        KEall_6.addValue(Double.parseDouble(ar[5]), weight);
                                                        KEall_7.addValue(Double.parseDouble(ar[6]), weight);
                                                        KEall_8.addValue(Double.parseDouble(ar[7]), weight);
                                                        KEall_9.addValue(Double.parseDouble(ar[8]), weight);
                                                        KEall_10.addValue(Double.parseDouble(ar[9]), weight);
                                                        KEall_11.addValue(Double.parseDouble(ar[10]), weight);
                                                        KEall_12.addValue(Double.parseDouble(ar[11]), weight);
                                                        KEall_13.addValue(Double.parseDouble(ar[12]), weight);
                                                        KEall_14.addValue(Double.parseDouble(ar[13]), weight);
                                                        KEall_15.addValue(Double.parseDouble(ar[14]), weight);
                                                        KEall_16.addValue(Double.parseDouble(ar[15]), weight);
                                                        KEall_17.addValue(Double.parseDouble(ar[16]), weight);
                                                        KEall_18.addValue(Double.parseDouble(ar[17]), weight);
                                                        KEall_19.addValue(Double.parseDouble(ar[18]), weight);
                                                        KEall_20.addValue(Double.parseDouble(ar[19]), weight);
                                                        KEall_21.addValue(Double.parseDouble(ar[20]), weight);
                                                        KEall_22.addValue(Double.parseDouble(ar[21]), weight);
                                                        KEall_23.addValue(Double.parseDouble(ar[22]), weight);
                                                        KEall_24.addValue(Double.parseDouble(ar[23]), weight);
                                                        KEall_25.addValue(Double.parseDouble(ar[24]), weight);
                                                        KEall_26.addValue(Double.parseDouble(ar[25]), weight);
                                                        KEall_27.addValue(Double.parseDouble(ar[26]), weight);
                                                        KEall_28.addValue(Double.parseDouble(ar[27]), weight);
                                                        KEall_29.addValue(Double.parseDouble(ar[28]), weight);
                                                        KEall_30.addValue(Double.parseDouble(ar[29]), weight);
                                                        KEall_31.addValue(Double.parseDouble(ar[30]), weight);
                                                        KEall_32.addValue(Double.parseDouble(ar[31]), weight);
                                                        KEall_33.addValue(Double.parseDouble(ar[32]), weight);
                                                        KEall_34.addValue(Double.parseDouble(ar[33]), weight);
                                                        KEall_35.addValue(Double.parseDouble(ar[34]), weight);
                                                        KEall_36.addValue(Double.parseDouble(ar[35]), weight);
                                                        KEall_37.addValue(Double.parseDouble(ar[36]), weight);
                                                        KEall_38.addValue(Double.parseDouble(ar[37]), weight);
                                                        KEall_39.addValue(Double.parseDouble(ar[38]), weight);
                                                        KEall_40.addValue(Double.parseDouble(ar[39]), weight);
                                                        KEall_41.addValue(Double.parseDouble(ar[40]), weight);
                                                        KEall_42.addValue(Double.parseDouble(ar[41]), weight);
                                                        KEall_43.addValue(Double.parseDouble(ar[42]), weight);
                                                        KEall_44.addValue(Double.parseDouble(ar[43]), weight);
                                                        KEall_45.addValue(Double.parseDouble(ar[44]), weight);                                                        
                                                        break;
                                                    case 1: 
                                                        //Add to KE1 kernels
                                                        KE1_1.addValue(Double.parseDouble(ar[0]), weight);
                                                        KE1_2.addValue(Double.parseDouble(ar[1]), weight);
                                                        KE1_3.addValue(Double.parseDouble(ar[2]), weight);
                                                        KE1_4.addValue(Double.parseDouble(ar[3]), weight);
                                                        KE1_5.addValue(Double.parseDouble(ar[4]), weight);
                                                        KE1_6.addValue(Double.parseDouble(ar[5]), weight);
                                                        KE1_7.addValue(Double.parseDouble(ar[6]), weight);
                                                        KE1_8.addValue(Double.parseDouble(ar[7]), weight);
                                                        KE1_9.addValue(Double.parseDouble(ar[8]), weight);
                                                        KE1_10.addValue(Double.parseDouble(ar[9]), weight);
                                                        KE1_11.addValue(Double.parseDouble(ar[10]), weight);
                                                        KE1_12.addValue(Double.parseDouble(ar[11]), weight);
                                                        KE1_13.addValue(Double.parseDouble(ar[12]), weight);
                                                        KE1_14.addValue(Double.parseDouble(ar[13]), weight);
                                                        KE1_15.addValue(Double.parseDouble(ar[14]), weight);
                                                        KE1_16.addValue(Double.parseDouble(ar[15]), weight);
                                                        KE1_17.addValue(Double.parseDouble(ar[16]), weight);
                                                        KE1_18.addValue(Double.parseDouble(ar[17]), weight);
                                                        KE1_19.addValue(Double.parseDouble(ar[18]), weight);
                                                        KE1_20.addValue(Double.parseDouble(ar[19]), weight);
                                                        KE1_21.addValue(Double.parseDouble(ar[20]), weight);
                                                        KE1_22.addValue(Double.parseDouble(ar[21]), weight);
                                                        KE1_23.addValue(Double.parseDouble(ar[22]), weight);
                                                        KE1_24.addValue(Double.parseDouble(ar[23]), weight);
                                                        KE1_25.addValue(Double.parseDouble(ar[24]), weight);
                                                        KE1_26.addValue(Double.parseDouble(ar[25]), weight);
                                                        KE1_27.addValue(Double.parseDouble(ar[26]), weight);
                                                        KE1_28.addValue(Double.parseDouble(ar[27]), weight);
                                                        KE1_29.addValue(Double.parseDouble(ar[28]), weight);
                                                        KE1_30.addValue(Double.parseDouble(ar[29]), weight);
                                                        KE1_31.addValue(Double.parseDouble(ar[30]), weight);
                                                        KE1_32.addValue(Double.parseDouble(ar[31]), weight);
                                                        KE1_33.addValue(Double.parseDouble(ar[32]), weight);
                                                        KE1_34.addValue(Double.parseDouble(ar[33]), weight);
                                                        KE1_35.addValue(Double.parseDouble(ar[34]), weight);
                                                        KE1_36.addValue(Double.parseDouble(ar[35]), weight);
                                                        KE1_37.addValue(Double.parseDouble(ar[36]), weight);
                                                        KE1_38.addValue(Double.parseDouble(ar[37]), weight);
                                                        KE1_39.addValue(Double.parseDouble(ar[38]), weight);
                                                        KE1_40.addValue(Double.parseDouble(ar[39]), weight);
                                                        KE1_41.addValue(Double.parseDouble(ar[40]), weight);
                                                        KE1_42.addValue(Double.parseDouble(ar[41]), weight);
                                                        KE1_43.addValue(Double.parseDouble(ar[42]), weight);
                                                        KE1_44.addValue(Double.parseDouble(ar[43]), weight);
                                                        KE1_45.addValue(Double.parseDouble(ar[44]), weight);
                                                        //Also: Add to KEall kernels
                                                        KEall_1.addValue(Double.parseDouble(ar[0]), weight);
                                                        KEall_2.addValue(Double.parseDouble(ar[1]), weight);
                                                        KEall_3.addValue(Double.parseDouble(ar[2]), weight);
                                                        KEall_4.addValue(Double.parseDouble(ar[3]), weight);
                                                        KEall_5.addValue(Double.parseDouble(ar[4]), weight);
                                                        KEall_6.addValue(Double.parseDouble(ar[5]), weight);
                                                        KEall_7.addValue(Double.parseDouble(ar[6]), weight);
                                                        KEall_8.addValue(Double.parseDouble(ar[7]), weight);
                                                        KEall_9.addValue(Double.parseDouble(ar[8]), weight);
                                                        KEall_10.addValue(Double.parseDouble(ar[9]), weight);
                                                        KEall_11.addValue(Double.parseDouble(ar[10]), weight);
                                                        KEall_12.addValue(Double.parseDouble(ar[11]), weight);
                                                        KEall_13.addValue(Double.parseDouble(ar[12]), weight);
                                                        KEall_14.addValue(Double.parseDouble(ar[13]), weight);
                                                        KEall_15.addValue(Double.parseDouble(ar[14]), weight);
                                                        KEall_16.addValue(Double.parseDouble(ar[15]), weight);
                                                        KEall_17.addValue(Double.parseDouble(ar[16]), weight);
                                                        KEall_18.addValue(Double.parseDouble(ar[17]), weight);
                                                        KEall_19.addValue(Double.parseDouble(ar[18]), weight);
                                                        KEall_20.addValue(Double.parseDouble(ar[19]), weight);
                                                        KEall_21.addValue(Double.parseDouble(ar[20]), weight);
                                                        KEall_22.addValue(Double.parseDouble(ar[21]), weight);
                                                        KEall_23.addValue(Double.parseDouble(ar[22]), weight);
                                                        KEall_24.addValue(Double.parseDouble(ar[23]), weight);
                                                        KEall_25.addValue(Double.parseDouble(ar[24]), weight);
                                                        KEall_26.addValue(Double.parseDouble(ar[25]), weight);
                                                        KEall_27.addValue(Double.parseDouble(ar[26]), weight);
                                                        KEall_28.addValue(Double.parseDouble(ar[27]), weight);
                                                        KEall_29.addValue(Double.parseDouble(ar[28]), weight);
                                                        KEall_30.addValue(Double.parseDouble(ar[29]), weight);
                                                        KEall_31.addValue(Double.parseDouble(ar[30]), weight);
                                                        KEall_32.addValue(Double.parseDouble(ar[31]), weight);
                                                        KEall_33.addValue(Double.parseDouble(ar[32]), weight);
                                                        KEall_34.addValue(Double.parseDouble(ar[33]), weight);
                                                        KEall_35.addValue(Double.parseDouble(ar[34]), weight);
                                                        KEall_36.addValue(Double.parseDouble(ar[35]), weight);
                                                        KEall_37.addValue(Double.parseDouble(ar[36]), weight);
                                                        KEall_38.addValue(Double.parseDouble(ar[37]), weight);
                                                        KEall_39.addValue(Double.parseDouble(ar[38]), weight);
                                                        KEall_40.addValue(Double.parseDouble(ar[39]), weight);
                                                        KEall_41.addValue(Double.parseDouble(ar[40]), weight);
                                                        KEall_42.addValue(Double.parseDouble(ar[41]), weight);
                                                        KEall_43.addValue(Double.parseDouble(ar[42]), weight);
                                                        KEall_44.addValue(Double.parseDouble(ar[43]), weight);
                                                        KEall_45.addValue(Double.parseDouble(ar[44]), weight);                                                        
                                                        break;
                                                    default: 
                                                        System.out.println("-ERROR in fillKernels() - switch statement input with an unknown appropriateness level");
                                                        break;
                                                }                                                
                                          }
                                    }
                                    in.close();
                                    //System.out.println("-KE0_1 after filling: " + KE0_1.toString());
                                    System.out.println("-processing training data from " + linesProcessed + " previously observed level segments");
                                    System.out.println("-done");
                                } catch (IOException e) {
                                    //Error reading file
                                    System.out.println("-ERROR - File Read Error");
                                }                            
                        }
                        
                        public double getObsevationProbability(String observation_str, int appr_level)
                        {
                            //Return probability of this observation resulting from
                            //Call WEKA functions
                            double prob_appr = 9.99; //dummy initialise for debugging
                            try {
                                //Now that all kernels are filled, get probabilities of a certain observation resulting from difficulty 1, 4, and 7
                                //System.out.println("KE0_1.getProbability: " + KE0_1.getProbability(0.20));
                                //System.out.println("KE1_1.getProbability: " + KE0_1.getProbability(0.20));
                                //String observation_str = "32, 32, 24, 0, 0, 28, 2, 19, 5, 23, 30, 1, 5, 5, 5, 1, 1, 1, 0, 0, 4, 23, 0, 0, 2, 16.0, 2.0, 1.0, 0.0, 1.0, 0.0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1";
                                String[] ar = observation_str.split(",");
                                /*
                                System.out.println( Double.parseDouble(ar[0]) );
                                System.out.println( Double.parseDouble(ar[1]) );
                                System.out.println( Double.parseDouble(ar[2]) );
                                System.out.println( Double.parseDouble(ar[3]) );
                                System.out.println( Double.parseDouble(ar[4]) );
                                System.out.println( Double.parseDouble(ar[5]) );
                                System.out.println( Double.parseDouble(ar[6]) );
                                System.out.println( Double.parseDouble(ar[7]) );
                                System.out.println( Double.parseDouble(ar[8]) );
                                System.out.println( Double.parseDouble(ar[9]) );
                                System.out.println( Double.parseDouble(ar[10]) );
                                System.out.println( Double.parseDouble(ar[11]) );
                                System.out.println( Double.parseDouble(ar[12]) );
                                System.out.println( Double.parseDouble(ar[13]) );
                                System.out.println( Double.parseDouble(ar[14]) );
                                System.out.println( Double.parseDouble(ar[15]) );
                                System.out.println( Double.parseDouble(ar[16]) );
                                System.out.println( Double.parseDouble(ar[17]) );
                                System.out.println( Double.parseDouble(ar[18]) );
                                System.out.println( Double.parseDouble(ar[19]) );
                                System.out.println( Double.parseDouble(ar[20]) );
                                System.out.println( Double.parseDouble(ar[21]) );
                                System.out.println( Double.parseDouble(ar[22]) );
                                System.out.println( Double.parseDouble(ar[23]) );
                                System.out.println( Double.parseDouble(ar[24]) );
                                System.out.println( Double.parseDouble(ar[25]) );
                                System.out.println( Double.parseDouble(ar[26]) );
                                System.out.println( Double.parseDouble(ar[27]) );
                                System.out.println( Double.parseDouble(ar[28]) );
                                System.out.println( Double.parseDouble(ar[29]) );
                                System.out.println( Double.parseDouble(ar[30]) );
                                System.out.println( Double.parseDouble(ar[31]) );
                                System.out.println( Double.parseDouble(ar[32]) );
                                System.out.println( Double.parseDouble(ar[33]) );
                                System.out.println( Double.parseDouble(ar[34]) );
                                System.out.println( Double.parseDouble(ar[35]) );
                                System.out.println( Double.parseDouble(ar[36]) );
                                System.out.println( Double.parseDouble(ar[37]) );
                                System.out.println( Double.parseDouble(ar[38]) );
                                System.out.println( Double.parseDouble(ar[39]) );
                                System.out.println( Double.parseDouble(ar[40]) );
                                System.out.println( Double.parseDouble(ar[41]) );
                                System.out.println( Double.parseDouble(ar[42]) );
                                System.out.println( Double.parseDouble(ar[43]) );
                                System.out.println( Double.parseDouble(ar[44]) );
                                */                                
                                //System.out.println("--KE0_1.getProbability: " + KE0_1.getProbability(Double.parseDouble(ar[0])));
                                //System.out.println("--KE1_1.getProbability: " + KE1_1.getProbability(Double.parseDouble(ar[0])));
                                switch (appr_level) {
                                    case 0: 
                                        prob_appr = 
                                            KE0_1.getProbability(Double.parseDouble(ar[0])) * 
                                            KE0_2.getProbability(Double.parseDouble(ar[1])) *
                                            KE0_3.getProbability(Double.parseDouble(ar[2])) *
                                            KE0_4.getProbability(Double.parseDouble(ar[3])) *
                                            KE0_5.getProbability(Double.parseDouble(ar[4])) *
                                            KE0_6.getProbability(Double.parseDouble(ar[5])) *
                                            KE0_7.getProbability(Double.parseDouble(ar[6])) *
                                            KE0_8.getProbability(Double.parseDouble(ar[7])) *
                                            KE0_9.getProbability(Double.parseDouble(ar[8])) *
                                            KE0_10.getProbability(Double.parseDouble(ar[9])) * 
                                            KE0_11.getProbability(Double.parseDouble(ar[10])) * 
                                            KE0_12.getProbability(Double.parseDouble(ar[11])) * 
                                            KE0_13.getProbability(Double.parseDouble(ar[12])) * 
                                            KE0_14.getProbability(Double.parseDouble(ar[13])) *
                                            KE0_15.getProbability(Double.parseDouble(ar[14])) *
                                            KE0_16.getProbability(Double.parseDouble(ar[15])) *
                                            KE0_17.getProbability(Double.parseDouble(ar[16])) *
                                            KE0_18.getProbability(Double.parseDouble(ar[17])) *
                                            KE0_19.getProbability(Double.parseDouble(ar[18])) *
                                            KE0_20.getProbability(Double.parseDouble(ar[19])) *
                                            KE0_21.getProbability(Double.parseDouble(ar[20])) *
                                            KE0_22.getProbability(Double.parseDouble(ar[21])) *
                                            KE0_23.getProbability(Double.parseDouble(ar[22])) *
                                            KE0_24.getProbability(Double.parseDouble(ar[23])) *
                                            KE0_25.getProbability(Double.parseDouble(ar[24])) *
                                            KE0_26.getProbability(Double.parseDouble(ar[25])) *
                                            KE0_27.getProbability(Double.parseDouble(ar[26])) *
                                            KE0_28.getProbability(Double.parseDouble(ar[27])) *
                                            KE0_29.getProbability(Double.parseDouble(ar[28])) *
                                            KE0_30.getProbability(Double.parseDouble(ar[29])) *
                                            KE0_31.getProbability(Double.parseDouble(ar[30])) *
                                            KE0_32.getProbability(Double.parseDouble(ar[31])) *
                                            KE0_33.getProbability(Double.parseDouble(ar[32])) *
                                            KE0_34.getProbability(Double.parseDouble(ar[33])) *
                                            KE0_35.getProbability(Double.parseDouble(ar[34])) *
                                            KE0_36.getProbability(Double.parseDouble(ar[35])) *
                                            KE0_37.getProbability(Double.parseDouble(ar[36])) *
                                            KE0_38.getProbability(Double.parseDouble(ar[37])) *
                                            KE0_39.getProbability(Double.parseDouble(ar[38])) *
                                            KE0_40.getProbability(Double.parseDouble(ar[39])) *
                                            KE0_41.getProbability(Double.parseDouble(ar[40])) *
                                            KE0_42.getProbability(Double.parseDouble(ar[41])) *
                                            KE0_43.getProbability(Double.parseDouble(ar[42])) *
                                            KE0_44.getProbability(Double.parseDouble(ar[43])) *
                                            KE0_45.getProbability(Double.parseDouble(ar[44]));
                                        break;
                                    case 1: 
                                        prob_appr = 
                                            KE1_1.getProbability(Double.parseDouble(ar[0])) * 
                                            KE1_2.getProbability(Double.parseDouble(ar[1])) *
                                            KE1_3.getProbability(Double.parseDouble(ar[2])) *
                                            KE1_4.getProbability(Double.parseDouble(ar[3])) *
                                            KE1_5.getProbability(Double.parseDouble(ar[4])) *
                                            KE1_6.getProbability(Double.parseDouble(ar[5])) *
                                            KE1_7.getProbability(Double.parseDouble(ar[6])) *
                                            KE1_8.getProbability(Double.parseDouble(ar[7])) *
                                            KE1_9.getProbability(Double.parseDouble(ar[8])) *
                                            KE1_10.getProbability(Double.parseDouble(ar[9])) * 
                                            KE1_11.getProbability(Double.parseDouble(ar[10])) * 
                                            KE1_12.getProbability(Double.parseDouble(ar[11])) * 
                                            KE1_13.getProbability(Double.parseDouble(ar[12])) * 
                                            KE1_14.getProbability(Double.parseDouble(ar[13])) *
                                            KE1_15.getProbability(Double.parseDouble(ar[14])) *
                                            KE1_16.getProbability(Double.parseDouble(ar[15])) *
                                            KE1_17.getProbability(Double.parseDouble(ar[16])) *
                                            KE1_18.getProbability(Double.parseDouble(ar[17])) *
                                            KE1_19.getProbability(Double.parseDouble(ar[18])) *
                                            KE1_20.getProbability(Double.parseDouble(ar[19])) *
                                            KE1_21.getProbability(Double.parseDouble(ar[20])) *
                                            KE1_22.getProbability(Double.parseDouble(ar[21])) *
                                            KE1_23.getProbability(Double.parseDouble(ar[22])) *
                                            KE1_24.getProbability(Double.parseDouble(ar[23])) *
                                            KE1_25.getProbability(Double.parseDouble(ar[24])) *
                                            KE1_26.getProbability(Double.parseDouble(ar[25])) *
                                            KE1_27.getProbability(Double.parseDouble(ar[26])) *
                                            KE1_28.getProbability(Double.parseDouble(ar[27])) *
                                            KE1_29.getProbability(Double.parseDouble(ar[28])) *
                                            KE1_30.getProbability(Double.parseDouble(ar[29])) *
                                            KE1_31.getProbability(Double.parseDouble(ar[30])) *
                                            KE1_32.getProbability(Double.parseDouble(ar[31])) *
                                            KE1_33.getProbability(Double.parseDouble(ar[32])) *
                                            KE1_34.getProbability(Double.parseDouble(ar[33])) *
                                            KE1_35.getProbability(Double.parseDouble(ar[34])) *
                                            KE1_36.getProbability(Double.parseDouble(ar[35])) *
                                            KE1_37.getProbability(Double.parseDouble(ar[36])) *
                                            KE1_38.getProbability(Double.parseDouble(ar[37])) *
                                            KE1_39.getProbability(Double.parseDouble(ar[38])) *
                                            KE1_40.getProbability(Double.parseDouble(ar[39])) *
                                            KE1_41.getProbability(Double.parseDouble(ar[40])) *
                                            KE1_42.getProbability(Double.parseDouble(ar[41])) *
                                            KE1_43.getProbability(Double.parseDouble(ar[42])) *
                                            KE1_44.getProbability(Double.parseDouble(ar[43])) *
                                            KE1_45.getProbability(Double.parseDouble(ar[44]));
                                        break;
                                    case 99: 
                                        prob_appr = 
                                            KEall_1.getProbability(Double.parseDouble(ar[0])) * 
                                            KEall_2.getProbability(Double.parseDouble(ar[1])) *
                                            KEall_3.getProbability(Double.parseDouble(ar[2])) *
                                            KEall_4.getProbability(Double.parseDouble(ar[3])) *
                                            KEall_5.getProbability(Double.parseDouble(ar[4])) *
                                            KEall_6.getProbability(Double.parseDouble(ar[5])) *
                                            KEall_7.getProbability(Double.parseDouble(ar[6])) *
                                            KEall_8.getProbability(Double.parseDouble(ar[7])) *
                                            KEall_9.getProbability(Double.parseDouble(ar[8])) *
                                            KEall_10.getProbability(Double.parseDouble(ar[9])) * 
                                            KEall_11.getProbability(Double.parseDouble(ar[10])) * 
                                            KEall_12.getProbability(Double.parseDouble(ar[11])) * 
                                            KEall_13.getProbability(Double.parseDouble(ar[12])) * 
                                            KEall_14.getProbability(Double.parseDouble(ar[13])) *
                                            KEall_15.getProbability(Double.parseDouble(ar[14])) *
                                            KEall_16.getProbability(Double.parseDouble(ar[15])) *
                                            KEall_17.getProbability(Double.parseDouble(ar[16])) *
                                            KEall_18.getProbability(Double.parseDouble(ar[17])) *
                                            KEall_19.getProbability(Double.parseDouble(ar[18])) *
                                            KEall_20.getProbability(Double.parseDouble(ar[19])) *
                                            KEall_21.getProbability(Double.parseDouble(ar[20])) *
                                            KEall_22.getProbability(Double.parseDouble(ar[21])) *
                                            KEall_23.getProbability(Double.parseDouble(ar[22])) *
                                            KEall_24.getProbability(Double.parseDouble(ar[23])) *
                                            KEall_25.getProbability(Double.parseDouble(ar[24])) *
                                            KEall_26.getProbability(Double.parseDouble(ar[25])) *
                                            KEall_27.getProbability(Double.parseDouble(ar[26])) *
                                            KEall_28.getProbability(Double.parseDouble(ar[27])) *
                                            KEall_29.getProbability(Double.parseDouble(ar[28])) *
                                            KEall_30.getProbability(Double.parseDouble(ar[29])) *
                                            KEall_31.getProbability(Double.parseDouble(ar[30])) *
                                            KEall_32.getProbability(Double.parseDouble(ar[31])) *
                                            KEall_33.getProbability(Double.parseDouble(ar[32])) *
                                            KEall_34.getProbability(Double.parseDouble(ar[33])) *
                                            KEall_35.getProbability(Double.parseDouble(ar[34])) *
                                            KEall_36.getProbability(Double.parseDouble(ar[35])) *
                                            KEall_37.getProbability(Double.parseDouble(ar[36])) *
                                            KEall_38.getProbability(Double.parseDouble(ar[37])) *
                                            KEall_39.getProbability(Double.parseDouble(ar[38])) *
                                            KEall_40.getProbability(Double.parseDouble(ar[39])) *
                                            KEall_41.getProbability(Double.parseDouble(ar[40])) *
                                            KEall_42.getProbability(Double.parseDouble(ar[41])) *
                                            KEall_43.getProbability(Double.parseDouble(ar[42])) *
                                            KEall_44.getProbability(Double.parseDouble(ar[43])) *
                                            KEall_45.getProbability(Double.parseDouble(ar[44]));
                                        break;
                                    default: 
                                        System.out.println("-ERROR in getObservationalProbability() - switch statement input with an unknown appropriateness level");
                                        break;
                                }
                            } catch (Exception e) {
                                //Catch possible WEKA errors
                                System.out.println("-ERROR in calling WEKA in function getObsevationProbability()");
                                e.printStackTrace();
                            }   

                            //Return probability
                            //System.out.println("Probability of observation resulting from experience appropriates " + appr_level + ": " + prob_appr);
                            return prob_appr;
                        }
                        
                        public int getDifficulty()
                        {
                            //return m.DIFFICULTY; //this is outdated - lets return the actual values as also displaye on the screen
                            //    public int currentLevelSegment;
                            //    public ArrayList plannedDifficultyLevels = new ArrayList(0);                       
                            return (int) plannedDifficultyLevels.get(currentLevelSegment);
                        }

                        public int getAppropriateness()
                        {
                            return randomNumber(0,3);
                        }
			
			public void updateReward(Instance selectedInstance, boolean doBernoulliRewards, boolean isTrainingInstance, boolean verbose)
                        {                          
                            //Update reward in the vector playerModel[]
                            if (verbose) System.out.println("");
                            if (verbose) System.out.println("updateReward called()");

                            //Determine difficulty level associated to this instance
                            int difficultyLevel;
                            if (isTrainingInstance) {
                                difficultyLevel = Integer.parseInt(selectedInstance.toString(45)); //in this attribute the difficulty level is stored
                            } 
                            else {
                                difficultyLevel = getDifficulty();  //now: m.DIFFICULTY. perhaps it should be m.state ?
                            }
                            if (verbose) System.out.println("-calculating reward for previous level segment with difficulty level: " + difficultyLevel );
                            //double probsAppro = getProbsAppropriateness_ObservationStr(observation_str, false);
                            double probsAppro = classifyInstance(selectedInstance, false);                           
                            if (verbose) System.out.println("-difficulty of level segment was deemed appropriate with a probability of: " + probsAppro );
                            
                            //Determine rewards according to Bernoulli scheme / proportional reward
                            double reward = 0.0;
                            if (doBernoulliRewards) {
                                if (verbose) System.out.println("-returning reward of 1 with probablity of " + probsAppro + ", else reward of 0 (Bernoulli rewards)");
                                boolean returnBernoulliReward;
                                if ( Math.random() <= probsAppro ) returnBernoulliReward = true;
                                    else returnBernoulliReward = false;

                                if (verbose) System.out.println("-boolean returnBernoulliReward: " + returnBernoulliReward);
                                if (returnBernoulliReward) reward = 1.0;
                                    else reward = 0.0;                                
                            }
                            else {
                                if (verbose) System.out.println("-returning reward " + probsAppro + " (regular non-Bernoulli rewards)");
                                reward = probsAppro;
                            }

                            if (verbose) System.out.println("-adding reward of " + reward + " to arraylist playerModelDiff" + difficultyLevel);
                            switch (difficultyLevel) {
                                case 1: playerModelDiff1.add(reward); break;
                                case 4: playerModelDiff4.add(reward); break;
                                case 7: playerModelDiff7.add(reward); break;
                                default: System.out.println("-ERROR! Cannot add reward to concerning playerModelDiff1,4,7 due to incorrect input of difficultyLevel"); break;
                            }
                            //Note, updating the display of average rewards is performed by updatePlayerModel()
                            //int index = getPlayerModelIndex(difficultyLevel);
                            //System.out.println("-updating playerModel[" + index + "] with reward: " + reward);
                            //playerModel[index] += reward;
                            if (verbose) System.out.println("-done");
                                                       
                            //OLD
                            //Increase reward proportionally to appropriateness of current difficulty level to the specific player
                            //As determed by probabilities in player model
                            /*
                            System.out.println("");
                            System.out.println("updateReward called()");
                            double reward = getPlayerModelElement(m.DIFFICULTY);
                            System.out.println("-increasing reward by: " + reward);
                            m.REWARD += reward;
                            System.out.println("-new reward is now: " + m.REWARD);
                            */
                                                        
                            //OLD OLD
                            /*
                            if (m.state == 1) { //SANDER UPDATE - NOT CORRECT AT THE MOMENT
                                //Appropriate difficulty - Increase reward
                                int rangeMin = 0;
                                int rangeMax = 1;
                                Random r = new Random();
                                double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
                                System.out.println("Increasing reward by: " + randomValue);
                                m.REWARD += randomValue;
                                System.out.println("New cummulative reward: " + m.REWARD);
                            }
                            else {
                                //No appropriate difficulty - Do not increase reward for this level block
                                m.REWARD += 0;                              
                            }
                            */
                        }
			
			public void newchunk()
                        {
				if(!gameover)
				{
					arch.UpdateState();
					//arch.updateMessage();
					
			    	Random randomGenerator = new Random();
			        int randomInt = randomGenerator.nextInt(100);
			    	//level2 = new CustomizedLevel(100, 15, levelSeed + randomInt, levelDifficulty,levelType, arch.message);
			    	// level2 = new RandomLevel(100, 15, levelSeed+randomInt , levelDifficulty , levelType);
                                System.out.println("-newchunck called");
			        level2 = new ArchLevel(100, 15, levelSeed+randomInt, levelDifficulty , levelType,arch.message,recorder);
                                nextSegmentAlreadyGenerated = true;
                                System.out.println("-setting nextSegmentAlreadyGenerated to: " + nextSegmentAlreadyGenerated);
			    	
			        try {
						level2_reset = level3_reset.clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
			    	try {
						level3_reset = level2.clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
				}
				else
				{
					level.xExit = 105;
				}
				 
                        }
			
			
			private int convertTime(int time){
				return (int)Math.floor((time+15-1)/15);
			}
		    
                        public void swap()
                        {
		    	
		    	int k = 0;
		        //The background info should change aswell                       
                        
		    	if (mario.x > level2.width*16)
		    	{
		    		if(!l3)
		    		{
		    			//System.out.println("\n\n\n Shells stomped: " + arch.newstats.n_s);
		    			//System.out.println("\n ENEMIES  TOTAL: " + recorder.level.ENEMIES);
		    			recorder.endTime();
		    			//arch.TranslateStats(recorder.fillGamePlayMetrics());
		    			
		    			/*//save qvalue table to file:
		    			String dataFile = "value_policy_tables";

		    			double[] prices = { 19.99, 9.99, 15.99, 3.99, 4.99 };
		    			int[] units = { 12, 8, 13, 29, 50 };
		    			String[] descs = {
		    			    "Java T-shirt",
		    			    "Java Mug",
		    			    "Duke Juggling Dolls",
		    			    "Java Pin",
		    			    "Java Key Chain"
		    			};
		    			
		    			out = new DataOutputStream(new BufferedOutputStream(
		    		              new FileOutputStream(dataFile)));
		    			
		    			for (int i = 0; i < prices.length; i ++) {
		    			    out.writeDouble(prices[i]);
		    			    out.writeInt(units[i]);
		    			    out.writeUTF(descs[i]);
		    			}*/

                                        //Swapping level segment
                                        System.out.println("");
                                        System.out.println("----------------------------------------");
                                        System.out.println("-------- Swapping level segment --------");
                                        System.out.println("----------------------------------------");
                                                                                
                                        //Write to log + get observation string for calculating appropriateness
                                        //String observation_str = "32, 32, 24, 0, 0, 28, 2, 19, 5, 23, 30, 1, 5, 5, 5, 1, 1, 1, 0, 0, 4, 23, 0, 0, 2, 16.0, 2.0, 1.0, 0.0, 1.0, 0.0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1";
                                        GamePlay temp_gpm = new GamePlay();
                                        temp_gpm = recorder.fillGamePlayMetrics(m, getDifficulty(), verbose); //write metrics at swapping to new level segment
                                        //String observation_str = temp_gpm.POMDPmetrics; //OLD - for use with Kernels, not RandomForest classifiers
                                        //recorder.fillGamePlayMetrics(m); //write metrics at swapping to new level segment
                                       
                                        //Get probability of appropriateness
                                        //getProbsAppropriateness_ObservationStr(observation_str, true); //OLD - for use with Kernels, not RandomForest classifiers

                                        //Load test instances and select last instance for classification
                                        loadTestInstances(false);
                                        Instance testInstance = selectTestInstance();
                                        
                                        //Calculate reward for selected instance, add reward to appropriate player models, and update display of average accumulate reward
                                        boolean doBernoulliRewards = false;
                                        boolean isTrainingInstance = false;
                                        boolean verbose = true;
                                        updateReward(testInstance, doBernoulliRewards, isTrainingInstance, verbose); //update reward in playerModelDiff1,4,7
                                        updatePlayerModel();
                                        displayReceivedRewards();
                                        
                                        //Determine next action
                                        //System.out.println("");
                                        //System.out.println("Trying to switch to difficulty level... ");
                                                                               
                                        //Switch to new state
                                        //System.out.println("Switching state: best action is action " + m.bestAction + " with appropriateness probability " + m.bestActionProb);                                    
                                        //m.DIFFICULTY = arch.message.DIFFICULTY*3 + 1;
                                        //m.state[0] = getDifficulty();
                                        //m.state[1] = randomNumber(0,3);
                                        //m.DIFFICULTY = m.bestAction;
                                        //m.state = m.DIFFICULTY;
                                        //levelDifficulty = 7;
                                        levelDifficulty = setAction(); //set next action using softMax
                                        m.DIFFICULTY = levelDifficulty;
                                        m.state = getDifficulty();
                                        //System.out.println("-m.bestAction: " + m.bestAction);
                                        //System.out.println("-m.DIFFICULTY: " + m.DIFFICULTY);
                                        //System.out.println("-m.state: " + m.state);

                                        //Update in which level segment the player currently is
                                        currentLevelSegment++;                                        
                                        
                                        System.out.println("");
                                        System.out.println("Preparing swapping to new game segment...");
                                        //System.out.println("-levelDifficulty: " + levelDifficulty);
                                        //System.out.println("-arch.message.DIFFICULTY: " + arch.message.DIFFICULTY);
                                        //System.out.println("-m.DIFFICULTY: " + m.DIFFICULTY);
                                        //System.out.println("-m.state: " + m.state);
                                        System.out.println("-preparing to switch to difficulty: " + levelDifficulty );
                                        nextSegmentAlreadyGenerated = false;
                                        System.out.println("-setting nextSegmentAlreadyGenerated to: " + nextSegmentAlreadyGenerated );
                                        
                                        //Update planned difficulty for the upcoming level segment
                                        plannedDifficultyLevels.add(levelDifficulty); //more efficient code as if statement has become redundant
                                        //if (currentLevelSegment > 1) {
                                        //    plannedDifficultyLevels.add(null);
                                        //}
                                        //plannedDifficultyLevels.set(plannedDifficultyLevels.size()-1, levelDifficulty);
                                                                                                                        
                                        //m.state[1] = 1; //IMPORTANT! This is the expected Appr state, as we selected an action to lead here - may update during ongoing gameplay
                                        m.states = arch.states.clone();
					m.Reward = arch.Reward;
						
		    			//System.out.println("\n coinsss : " + recorder.kf());
		    			recorder.levelScene.resetTime();
		    			
			    		// here u are in level 3
			    		recorder.reset();
			    		recorder.startTime();
			    		recorder.level = level3;
			    		
			    		l3 = true;
			    		l2 = false;
		    		}
	
		    	}
		    	else
		    	{
		    		// here u are in level 2
                                //System.out.println("HERE YOU ARE IN LEVEL 2");
		    		
		    		if(!l2)
		    		{
		    			
		    			l3 = false;
		    			// here u are in level 3		    		
                                        //System.out.println("HERE YOU ARE IN LEVEL 3");
			    		recorder.level = level2;
			    		
			    		
			    		l2 = true;
		    		}		
		    	}
		    	
		    	if(mario.x > (level2.width*16 + level3.width*16/2))
		    	{
                                //if(swap_done==0)
		    		//{
		    			 for (int i = 0; i < level.width; i++)
		    		        {
		    		        	if(i < level2.width)
		    		        	{
		    			            level2.map[i] = level.map[i];
		    			           // level2.data[i] = level.data[i];
		    			            level2.spriteTemplates[i] = level.spriteTemplates[i];
		    			            
		    		        	}
		    		        	else
		    		        	{
		    		        		level3.map[k] = level.map[i];
		    			           // level3.data[k] = level.data[i];
		    			            level3.spriteTemplates[k] = level.spriteTemplates[i];
		    			            k++;
		    		        	}
		    		        	
		    		        }
		    			 
		    			 newchunk();
		    			 fixborders();
			    			k = 0;
			    			
			    			
				    		for (int i = 0; i <level.width; i++)
				            {
				            	if(i < level3.width)
				            	{
				    	            level.map[i] = level3.map[i];
				    	           // level.data[i] = level3.data[i];
				    	            
				    	            level.spriteTemplates[i] = level3.spriteTemplates[i];
				    	            
				            	}
				            	else
				            	{
				            		level.map[i] = level2.map[k];
				    	           // level.data[i] = level2.data[k];
				    	            level.spriteTemplates[i] = level2.spriteTemplates[k];
				    	            k++;
				            	}
				            	
				            }
				    		for(int i = 0 ; i < sprites.size() ; i++)
				    		{
				    			//if(sprites.get(i).x < level2.width)sprites.get(i).release();
				    			sprites.get(i).x = sprites.get(i).x - level2.width*16;
				    		}
				    		//sprites.get(i).x = sprites.get(i).x - level2.width*16;
				    		//swap_done = 1;
				    		
				    		
				    		// ############################ STAT SECTION ########################
				            // total coins for level 3
				    		/*PrintStream out = System.out;
				        	if(currentL == 2)
				        		{
				        		currentL = 3;
				        		totalCoinsPerLevel += level2.totalCoins;
				        		out.println("coinblock 1  : " + level2.coinblocks );
				        		stats.destroyableBlocks = level2.destroyableBlocks;
				        		stats.coinblocks = level2.coinblocks;
				        		stats.totalEnemies = level2.enemies;
				        		}
				        	else
				        		{
				        		currentL = 2;
				        		totalCoinsPerLevel += level3.totalCoins;
				        		out.println("coinblock 2  : " + level3.coinblocks );
				        		stats.destroyableBlocks = level3.destroyableBlocks;
				        		stats.totalEnemies = level3.enemies;
				        		}
				    		
				        	
				        	//out.println("part  1 coins : " + level2.totalCoins );
				        	//out.println("part  2 coins : " + level3.totalCoins );
				        	//out.println("TotalCoins untill now : " + totalCoinsPerLevel );
				        	//out.println("current part : " + currentL );
				        	*/
				        	// ############################ STAT SECTION ########################
				    		
			    	}
		    	
		    		
		    	 }
		   // }
		    
		    public void save()
		    {
                        try {
					level2_reset = level2.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		                     
		    	try {
					level3_reset = level3.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }

                    public void conjoin()
		    {
		    	 //fixborders();
		         //INSERTED THIS CODE
		         //to conjoin two levels into one
		         int width = level2.width + level3.width;
		         int height = level2.height;
		         Level level4 = new Level(width, height);
		         level4.map = new byte[width][height];
		        // level4.data = new byte[width][height];
		         level4.xExit = width - 5;
		         int k = 0;
		         for (int i = 0; i < width; i++)
		         {
		         	if(i < level2.width)
		         	{
		 	            level4.map[i] = level2.map[i].clone();
		 	            //level4.data[i] = level2.data[i];
		 	            level4.spriteTemplates[i] = level2.spriteTemplates[i];
		 	            
		         	}
		         	else
		         	{
		         		level4.map[i] = level3.map[k].clone();
		 	            //level4.data[i] = level3.data[k];
		 	            level4.spriteTemplates[i] = level3.spriteTemplates[k];
		 	            k++;
		         	}
		         	
		         }
		         level = level4;
		    }
		    
		    
		    public void fixborders()
		    {    	    	
		    	for( int i = 0 ; i<15 ; i++)
		    	{
		    		level2.map[0][i] = (byte)(0);
		    		level2.map[level2.width-1][i] = (byte)(0);
		    		
		    		//if(level2.map[level2.width-1][i] == )
		    		if (level2.map[level2.width-2][i] == (byte)(-127))
		    		{
		    			level2.map[level2.width-2][i] = (byte)(-126);
		    		}
		    		
		    		if (level2.map[level2.width-2][i] == (byte)(-111))
		    		{
		    			level2.map[level2.width-2][i] = (byte)(-110);
		    		}
		    		
		    		if (level2.map[1][i] == (byte)(-127))
		    		{
		    			//change to corner
		    			level2.map[1][i] = (byte)(-128);
		    			
		    		}
		    		if(level2.map[1][i] == (byte)(-111))
		    		{
		    			level2.map[1][i] = (byte)(-112);
		    		}
		    				    		
		    		level3.map[0][i] = (byte)(0);
		    		level3.map[level3.width-1][i] = (byte)(0);
		    		
		    		//if(level2.map[level2.width-1][i] == )
		    		if (level3.map[level3.width-2][i] == (byte)(-127))
		    		{
		    			level3.map[level3.width-2][i] = (byte)(-126);
		    		}
		    		
		    		if (level3.map[level3.width-2][i] == (byte)(-111))
		    		{
		    			level3.map[level3.width-2][i] = (byte)(-110);
		    		}
		    		
		    		if (level3.map[1][i] == (byte)(-127))
		    		{
		    			//change to corner
		    			level3.map[1][i] = (byte)(-128);
		    			
		    		}
		    		if(level3.map[1][i] == (byte)(-111))
		    		{
		    			level3.map[1][i] = (byte)(-112);
		    		}
		    	}
		    }

			public void tick(){
				swap();
				super.tick();

				if(recorder != null && !gameStarted){
					recorder.startLittleRecord();
					recorder.startTime();
					gameStarted = true;
				}
				if(recorder != null)
				recorder.tickRecord();
			}

			public void winActions(){
				if (recorder != null)
                                    recorder.fillGamePlayMetrics(m, getDifficulty(), verbose); //write metrics at winning the game (currently never reached in infinite setup)
				
				marioComponent.win();
			}

			public void deathActions(){
                                //Reset general mario stuff
                                if(Mario.lives <=0){ //has no more lives
					if(recorder != null) 
                                            recorder.fillGamePlayMetrics(m, getDifficulty(), verbose); //write metrics at game over
					marioComponent.lose();
				}
				else // mario still has lives to play :)--> have a new beginning
				{
					//mario.x = 5; 
					//mario.y = 5;
                                       	if(recorder != null) 
                                            recorder.fillGamePlayMetrics(m, getDifficulty(), verbose); //write metrics at regular death with still lives left
                                        Mario.lives--;
					reset();
				}
			}

			public void bump(int x, int y, boolean canBreakBricks){
		        byte block = level.getBlock(x, y);

		        if ((Level.TILE_BEHAVIORS[block & 0xff] & Level.BIT_BUMPABLE) > 0)
		        {
		            bumpInto(x, y - 1);
		            level.setBlock(x, y, (byte) 4);

		            if (((Level.TILE_BEHAVIORS[block & 0xff]) & Level.BIT_SPECIAL) > 0)
		            {
		                sound.play(Art.samples[Art.SAMPLE_ITEM_SPROUT], new FixedSoundSource(x * 16 + 8, y * 16 + 8), 1, 1, 1);
		                if (!Mario.large)
		                {
		                    addSprite(new Mushroom(this, x * 16 + 8, y * 16 + 8));
		                }
		                else
		                {
		                    addSprite(new FireFlower(this, x * 16 + 8, y * 16 + 8));
		                }

		                if(recorder != null){
		                	recorder.blockPowerDestroyRecord();
		                }
		            }
		            else
		            {
		            	//TODO should only record hidden coins (in boxes)
		            	if(recorder != null){
		            		recorder.blockCoinDestroyRecord();
		            	}

		                Mario.getCoin();
		                sound.play(Art.samples[Art.SAMPLE_GET_COIN], new FixedSoundSource(x * 16 + 8, y * 16 + 8), 1, 1, 1);
		                addSprite(new CoinAnim(x, y));
		            }
		        }

		        if ((Level.TILE_BEHAVIORS[block & 0xff] & Level.BIT_BREAKABLE) > 0)
		        {
		            bumpInto(x, y - 1);
		            if (canBreakBricks)
		            {
		            	if(recorder != null){
		            		recorder.blockEmptyDestroyRecord();
		            	}

		                sound.play(Art.samples[Art.SAMPLE_BREAK_BLOCK], new FixedSoundSource(x * 16 + 8, y * 16 + 8), 1, 1, 1);
		                level.setBlock(x, y, (byte) 0);
		                for (int xx = 0; xx < 2; xx++)
		                    for (int yy = 0; yy < 2; yy++)
		                        addSprite(new Particle(x * 16 + xx * 8 + 4, y * 16 + yy * 8 + 4, (xx * 2 - 1) * 4, (yy * 2 - 1) * 4 - 8));
		            }

		        }
		    }

			 public void bumpInto(int x, int y)
			    {
			        byte block = level.getBlock(x, y);
			        if (((Level.TILE_BEHAVIORS[block & 0xff]) & Level.BIT_PICKUPABLE) > 0)
			        {
			            Mario.getCoin();
			            sound.play(Art.samples[Art.SAMPLE_GET_COIN], new FixedSoundSource(x * 16 + 8, y * 16 + 8), 1, 1, 1);
			            level.setBlock(x, y, (byte) 0);
			            addSprite(new CoinAnim(x, y + 1));


			            //TODO no idea when this happens... maybe remove coin count
			            if(recorder != null)
			            	recorder.recordCoin();
			        }

			        for (Sprite sprite : sprites)
			        {
			            sprite.bumpCheck(x, y);
			        }
			    }

			private int randomNumber(int low, int high){
				return new Random(new Random().nextLong()).nextInt(high-low)+low;
			}

			private int toBlock(float n){
				return (int)(n/16);
			}

			private int toBlock(double n){
				return (int)(n/16);
			}

			private float toReal(int b){
				return b*16;
			}



			public void reset() {
                            System.out.println("");
                            System.out.println("----------------------------------------");
                            System.out.println("------------ Resetting game ------------");
                            System.out.println("----------------------------------------");                               

                            //Always reset POMDP stuff
                            playerModelDiff1.clear();
                            playerModelDiff4.clear();
                            playerModelDiff7.clear();
                            updatePlayerModel();
                            displayReceivedRewards();
                            
                            int temp_diffsegment1;
                            int temp_diffsegment2;
                            if (currentLevelSegment == 0) {
                                System.out.println("-you died in the first segment, resetting to how you just started");
                                temp_diffsegment1 = (int) plannedDifficultyLevels.get(0);
                                temp_diffsegment2 = (int) plannedDifficultyLevels.get(1);
                            }
                            else {
                                System.out.println("-nextSegmentAlreadyGenerated:" + nextSegmentAlreadyGenerated);
                                if (nextSegmentAlreadyGenerated) {
                                    //because the next segment is already generated (and so the previous does not exist anymore),
                                    temp_diffsegment1 = (int) plannedDifficultyLevels.get(currentLevelSegment);
                                    temp_diffsegment2 = (int) plannedDifficultyLevels.get(currentLevelSegment+1);
                                }
                                else {
                                    //because the next segment is not yet generated
                                    temp_diffsegment1 = (int) plannedDifficultyLevels.get(currentLevelSegment-1);
                                    temp_diffsegment2 = (int) plannedDifficultyLevels.get(currentLevelSegment);
                                }
                            }
                            plannedDifficultyLevels.clear();

                            System.out.println("-resetting to: " + temp_diffsegment1 + ", " + temp_diffsegment2);
                            plannedDifficultyLevels.add(temp_diffsegment1);
                            plannedDifficultyLevels.add(temp_diffsegment2);
                            currentLevelSegment = 0;
                            
                            paused = false;
                            Sprite.spriteContext = this;
                            sprites.clear();

                            try {
                                            level2 = level2_reset.clone();
                                    } catch (CloneNotSupportedException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                    }
                            try {
                                            level3 = level3_reset.clone();
                                    } catch (CloneNotSupportedException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                    }

                            conjoin();
                            
                            layer = new LevelRenderer(level, graphicsConfiguration, 320, 240);
                            for (int i = 0; i < 2; i++)
                            {
                                int scrollSpeed = 4 >> i;
                                int w = ((level.getWidth() * 16) - 320) / scrollSpeed + 320;
                                int h = ((level.getHeight() * 16) - 240) / scrollSpeed + 240;
                                Level bgLevel = BgLevelGenerator.createLevel(w / 32 + 1, h / 32 + 1, i == 0, levelType);
                                bgLayer[i] = new BgRenderer(bgLevel, graphicsConfiguration, 320, 240, scrollSpeed);
                            }

                            double oldX = 0;
                            if(mario!=null)
                                    oldX = mario.x;

                            mario = new Mario(this);
                            sprites.add(mario);
                            startTime = 1;

                            timeLeft = 200*15;

                            tick = 0;

                            /*
                             * SETS UP ALL OF THE CHECKPOINTS TO CHECK FOR SWITCHING
                             */
                            switchPoints = new ArrayList<Double>();

                            //first pick a random starting waypoint from among ten positions
                            int squareSize = 16; //size of one square in pixels
                            int sections = 10;

                            double startX = 32; //mario start position
                            double endX = level.getxExit()*squareSize; //position of the end on the level
                            //if(!isCustom && recorder==null)

                            recorder = new DataRecorder(this,level2,keys);
                            //System.out.println("\n enemies LEFT : " + recorder.level.COINS); //Sander disable
                            //System.out.println("\n enemies LEFT : " + recorder.level.BLOCKS_COINS);
                            //System.out.println("\n enemies LEFT : " + recorder.level.BLOCKS_POWER);
                            gameStarted = false;
			}

}
