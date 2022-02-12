package com.arboreantears.endlessworlds.desktop;

import java.util.ArrayList;
import java.util.Date;

import com.arboreantears.endlessworlds.game.GameLog;
import com.arboreantears.endlessworlds.game.Graphics;
import com.arboreantears.endlessworlds.game.controllers.FWTControl;
import com.arboreantears.endlessworlds.game.io.Directories;
import com.arboreantears.endlessworlds.game.io.GameOptions;
import com.arboreantears.endlessworlds.game.io.ImageManager;
import com.arboreantears.endlessworlds.game.library.Language;
import com.arboreantears.endlessworlds.game.library.Ruleset;
import com.arboreantears.endlessworlds.game.test.TestFWTXML;
import com.arboreantears.endlessworlds.game.test.TestingModule;
import com.arboreantears.endlessworlds.gfx.fwt.addon.EWFWTCustomFactory;
import com.arboreantears.endlessworlds.gfx.renderers.FrameBufferController;
import com.arboreantears.fwt.FWTController;
import com.arboreantears.fwt.FWTWindowManager;
import com.arboreantears.fwt.Fonts;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

/** A testing framework that allows testing of lower-level processes that do not need FWT, but require libGDX libraries and Ruleset. */ 
public class TestFramework extends ApplicationAdapter implements InputProcessor	
{


	FWTWindowManager windowManager;


	ArrayList<TestingModule> modules = new ArrayList<TestingModule>();

	Camera camera;

	Viewport viewport;


	//******************************************************************************************************************
	// RUN TESTS
	//******************************************************************************************************************


	/** Loads the testing modules. */
	public void loadModules()
		{

			// Place testing applications here.
			//===================================================


			//modules.add(new TestCombat());

			//modules.add(new TestEntities());

			//modules.add(new TestCharacterRendering());

			//modules.add(new TestXMLReadingWriting());

			//modules.add(new TestWorldGeneration());
			
			modules.add(new TestFWTXML());



		}









	//******************************************************************************************************************
	// MAIN METHODS CALL
	//******************************************************************************************************************


	public static void main(String[] args) 
		{
			new LwjglApplication(new TestFramework(args));

		}




	//******************************************************************************************************************
	// CONSTRUCTOR
	//******************************************************************************************************************

	/** Command-line arguments. */
	public static String[] commandArgs = null;


	/** A constructor that passes along console arguments. */
	public TestFramework(String[] args)
		{
			commandArgs = args;		
		}




	//******************************************************************************************************************
	// CREATION AND INITIALIZATION
	//******************************************************************************************************************


	/** Create the test environment. */
	public void create()
		{
			// Initialize Logging
			GameOptions.FILE_LOGGING = GameLog.logInitialize("testconsole.log",true);

			//Game log header
			GameLog.log("*************************************************");
			GameLog.log(" TestFramework");
			GameLog.log("*************************************************");
			GameLog.log(new Date(TimeUtils.millis()).toString());
			GameLog.log("*************************************************");

			// Use 'native' ruleset
			String ruleset = "native";

			// Create Directories
			Directories.updateCoreDirectories();
			Directories.updateRulesetDirectories(ruleset);

			// Initialize Ruleset
			//*************************************
			try{
				Ruleset.initialize(ruleset);
				Language.setLanguage("en");

				// Load main language set
				Language.initializeMainLanguageSet();
				Language.initializeRulesetLanguageSet();
			}
			catch(Exception ex)
				{
					// Error message variables
					GameLog.log("Error in XML source files.");
					GameLog.log(ex.getMessage());
					return;
				}
			//*************************************

			// Prepare Graphics Thread (current Thread)
			Graphics.initialize();

			// Ensure proper data folder setup
			Directories.updateCoreDirectories();
			Directories.updateRulesetDirectories(GameOptions.FILES_Last_Ruleset);

			// Initialize Images and Buffers
			ImageManager.initialize();
			FrameBufferController.initialize();

			// Initialize FWT
			FWTController.initialize(new FWTControl(), new EWFWTCustomFactory());
			FWTController.log("FWTController: Initialized.");			

			// Initialize Fonts
			Fonts.initialize("Standard","Title");

			// Initialize Window manager
			windowManager = FWTController.createWindowManager("TestFramework");


			// Prepare input processing
			Gdx.input.setInputProcessor(this);	

			// Set up camera and viewport (Move to Graphics)
			//camera = new OrthographicCamera();
			//viewport = new ExtendViewport(1024,768,1600,900,camera);

		}





	//******************************************************************************************************************
	// RENDERING
	//******************************************************************************************************************

	/** 'True' if modules should load. */
	private boolean loadModules = true;

	/** 'True' if the one-shot tests should be run. */
	private boolean runOneShotTests = true;

	SpriteBatch spriteBatch;

	/** Render the test environment. */
	public void render()
		{
			spriteBatch = Graphics.getSpriteBatch();
			//camera.update();
			//spriteBatch.setProjectionMatrix(camera.combined);



			// Clear to Background Color
			Gdx.gl.glClearColor(Color.BLACK.r,Color.BLACK.g,Color.BLACK.b,1f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


			// Run any tests (once)
			if (loadModules)
				{
					loadModules(); 
					
					// Set window manager links to modules
					for (TestingModule module : modules)
						module.setWindowManager(windowManager);
					
					loadModules = false; 
				}

			// Render any windows
			if (windowManager != null)
				windowManager.render();

			// One-shot Tests
			for (TestingModule module : modules)
				{
					if (runOneShotTests)
						module.oneShotTests(commandArgs);
				}
			runOneShotTests = false;

			// Update and Render any modules
			for (TestingModule module : modules)
				{
					module.update(Gdx.graphics.getDeltaTime());
					module.render(spriteBatch);
				}


			// ESCAPE -> Exit testing framework
			if (Gdx.input.isKeyPressed(Keys.ESCAPE)) 
				Gdx.app.exit();
		}




	//******************************************************************************************************************
	// RESIZE
	//******************************************************************************************************************


	public void resize(int width, int height)
		{
			//viewport.update(width, height);
			//camera.update();

			windowManager.resize(width, height);
			for (TestingModule module : modules)
				{
					module.resize(width,height);
				}
		}







	//******************************************************************************************************************
	// DISPOSE
	//******************************************************************************************************************

	@Override
	public void dispose()
		{
			for (TestingModule module : modules)
				{
					module.dispose();
				}


		}






	// INPUT
	//########################################################################################################
	//########################################################################################################
	//########################################################################################################


	//********************************************************************
	//  MOUSE MOVED
	//********************************************************************

	@Override
	public boolean mouseMoved(int mX, int mY)
		{
			// Swap Y coordinates
			mY = Gdx.graphics.getHeight()-mY;

			if (GameOptions.GRAPHICS_Show_Windows && windowManager.mouseMoved(mX, mY))
				; 
			for (TestingModule module : modules)
				{
					module.mouseMoved(mX, mY);
				}


			return false;
		}







	//********************************************************************
	//  MOUSE BUTTON DOWN
	//********************************************************************

	@Override
	public boolean touchDown(int mX, int mY, int pointer, int button)
		{
			// Swap Y coordinates
			mY = Gdx.graphics.getHeight()-mY;

			if (GameOptions.GRAPHICS_Show_Windows && windowManager.touchDown(mX, mY,pointer,button))
				;
			for (TestingModule module : modules)
				{
					module.touchDown(mX, mY,pointer,button);
				}

			return false;
		}





	//********************************************************************
	//  MOUSE BUTTON UP
	//********************************************************************

	@Override
	public boolean touchUp(int mX, int mY, int pointer, int button)
		{
			// Swap Y coordinates
			mY = Gdx.graphics.getHeight()-mY;

			if (GameOptions.GRAPHICS_Show_Windows && windowManager.touchUp(mX, mY,pointer,button))
				;
			for (TestingModule module : modules)
				{
					module.touchUp(mX, mY,pointer,button);
				}

			return false;
		}







	//********************************************************************
	//  MOUSE DRAGGED 
	//********************************************************************

	@Override
	public boolean touchDragged(int mX, int mY, int pointer)
		{
			// Swap Y coordinates
			mY = Gdx.graphics.getHeight()-mY;

			if (GameOptions.GRAPHICS_Show_Windows && windowManager.touchDragged(mX, mY,pointer))
				;
			for (TestingModule module : modules)
				{
					module.touchDragged(mX, mY,pointer);
				}

			return false;
		}




	//********************************************************************
	//  MOUSE-WHEEL SCROLLING
	//********************************************************************


	@Override
	public boolean scrolled(float amountX, float amountY)
		{

			if (GameOptions.GRAPHICS_Show_Windows && windowManager.scrolled(amountX,amountY))
				;

			for (TestingModule module : modules)
				{
					module.scrolled(amountX,amountY);
				}

			return false;
		}




	//********************************************************************
	//  KEYBOARD INPUT
	//********************************************************************

	@Override
	public boolean keyDown(int keycode)
		{		
			if (GameOptions.GRAPHICS_Show_Windows && windowManager.keyDown(keycode))
				;
			for (TestingModule module : modules)
				{
					module.keyDown(keycode);
				}

			return false;
		}


	@Override
	public boolean keyUp(int keycode)
		{
			if (GameOptions.GRAPHICS_Show_Windows && windowManager.keyUp(keycode))
				;
			for (TestingModule module : modules)
				{
					module.keyUp(keycode);
				}

			return false;
		}


	@Override
	public boolean keyTyped(char keychar)
		{
			if (GameOptions.GRAPHICS_Show_Windows && windowManager.keyTyped(keychar))
				;
			for (TestingModule module : modules)
				{
					module.keyTyped(keychar);
				}

			return false;
		}








}
