package com.arboreantears.fwt;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;

/** Main controller for the FWT (Flexible Windowing Toolkit). 
 *  <p> Contains access to all external components and static references.
 *  This is where one connects the FWT to their libGDX system. */
public class FWTController
{
	
	

	//********************************************************************
	//  INITIALIZATION
	//********************************************************************

	/** Initialize the FWTController to use the given interface. */
	public static void initialize(FWTControlInterface cInterface, IFWTComponentFactory customFactory)
	{
		// Set Interface
		controlInterface = cInterface;
		

		// Initialize Images and Buffers
		FWTImageManager.initialize();
		FWTUIFrameBufferController.initialize();
		
		// Initialize Custom Component Factory
		FWTComponentFactory.setCustomFactory(customFactory);
		
		// Extra colors
		FWTColors.initialize();
	}
	
	/** Dispose of all FWT resources. */
	public static void dispose()
	{
		for (FWTWindowManager mgr : windowManagers)
			mgr.dispose();
		FWTImageManager.dispose();
		FWTUIFrameBufferController.dispose();
		controlInterface = null;
	}
	
	

	//********************************************************************
	//  WINDOW MANAGERS
	//********************************************************************


	/** The list of window managers being managed by this controller. */
	static ArrayList<FWTWindowManager> windowManagers = new ArrayList<FWTWindowManager>();

	/** Creates a new window manager. */
	public static FWTWindowManager createWindowManager()
	{
		FWTWindowManager manager = new FWTWindowManager();
		windowManagers.add(manager);
		return manager;
	}

	/** Creates a new window manager with the given name. */
	public static FWTWindowManager createWindowManager(String name)
	{
		FWTWindowManager manager = new FWTWindowManager(name);
		windowManagers.add(manager);
		return manager;
	}
	
	/** Removes the given window manager and its resources. */
	public static void removeManager(FWTWindowManager manager)
	{
		windowManagers.remove(manager);
		manager.dispose();
	}
	
	
	
	/** Reload all XML data and refresh all FWT components. */
	public static void refresh()
	{
		FWTController.log("FWTController: Refeshing XML Data");
		// For each window manager
		for (FWTWindowManager mgr : windowManagers)
			mgr.refresh();
			
	}
	
	


	//********************************************************************
	//  CONTROL INFERFACE
	//********************************************************************

	
	/** Current control interface for the FWT system. */
	static FWTControlInterface controlInterface;
	/** Sets the current control interface for the FWT system. */
	public static void setControlInterface(FWTControlInterface cInterface)
		{controlInterface = cInterface;}
	/** Returns the current control interface for the FWT system. */
	public static FWTControlInterface getControlInterface()
		{return controlInterface;}



	//********************************************************************
	//  UI OBJECT ID IMPLEMENTATION
	//********************************************************************


	/** Random seed generator*/
	public static Random RandGen = new Random(TimeUtils.millis());


	/** Generates a unique object long integer ID tag. */
	public static long generateObjectTag()
		{
			long p = ( RandGen.nextLong() );
			return p;
		}




	//********************************************************************
	//  LOGGING IMPLEMENTATION
	//********************************************************************


	/** Log events to the console and to a text file. */
	public static void log(String message)
		{controlInterface.log(message);}

	/** Log error events to the console and to a text file. */
	public static void error(String message)
		{controlInterface.error(message);}



	//********************************************************************
	//  GRAPHICS IMPLEMENTATION
	//********************************************************************


	/** Returns ID of the primary graphics thread (OpenGL loop). */
	public static long getOpenGLThreadID() {return controlInterface.getOpenGLThreadID();};

	/** Returns time since last frame render (delta) [seconds]. */
	public static float getDelta() {return controlInterface.getDelta();}

	/** Returns the current screen width. */
	public static int getGraphicsWidth() 
		{return controlInterface.getGraphicsWidth();}
	/** Returns the current screen height. */
	public static int getGraphicsHeight() 
		{return controlInterface.getGraphicsHeight();}

	/** Returns the main sprite batch for rendering. */
	public static SpriteBatch getSpriteBatch() 
		{return controlInterface.getSpriteBatch();}

	/** Returns the main shape renderer batch for drawing. */
	public static ShapeRenderer getShapeRenderer() 
		{return controlInterface.getShapeRenderer();}

	/** Enables blending via the OpenGL context.  Should only be called from the RENDER loop (e.g. requires OpenGL context). */
	public static void enableBlending()
		{controlInterface.enableBlending();}

	/** Disables blending via the OpenGL context.  Should only be called from the RENDER loop (e.g. requires OpenGL context). */
	public static void disableBlending()
		{controlInterface.disableBlending();}

	/** Returns the UI Texture Atlas. */
	public static TextureAtlas getUITextureAtlas()
		{return controlInterface.getUITextureAtlas();}

	/** Returns the directory path for loading UI XML specification files. */
	public static String getUIFilePath()
		{ return controlInterface.getUIFilePath();}

	/** Returns the directory path for loading UI XML specification file overrides (e.g. for modding). */
	public static String getOverrideUIFilePath()
		{ return controlInterface.getOverrideUIFilePath();}


	/** Returns the tool-tip delay (in seconds). */
	public static float getToolTipDelay()
		{return controlInterface.getToolTipDelay();}


	/** Returns 'true' if the key to immediately show the tool-tip is active.*/
	public static boolean isShowTooltipKeyDown()
		{ return controlInterface.isShowTooltipKeyDown();}



	//********************************************************************
	// DEFAULT VIEW IMPLEMENTATION
	//********************************************************************


	/** Returns the default UI component properties filename. */
	public String getDefaultComponentProperties()
		{return controlInterface.getDefaultComponentProperties();}

	
	


	//********************************************************************
	//  BITMAP FONTS IMPLEMENTATION
	//********************************************************************

	/** Returns the bitmap font file listing. */ 
	public static ArrayList<String> getFontListing()
		{ return controlInterface.getFontListing(); }


	/** Returns the bitmap font file directory. */
	public static String getFontFilePath() 
		{return	controlInterface.getFontFilePath();}


	/** Returns the directory path for loading language files. 
	 * <p> Must contain additional directories for each language. */
	public static String getLanguageFilePath()
		{ return controlInterface.getLanguageFilePath();}

	
	/** Returns the directory path for loading override language files. 
	 * <p> Must contain additional directories for each language. */
	public static String getOverrideLanguageFilePath()
		{ return controlInterface.getOverrideLanguageFilePath();}
























}
