package com.arboreantears.fwt;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** Interface for connecting to the FWT Controller. */
public abstract class FWTControlInterface
	{


		//********************************************************************
		//  LOGGING IMPLEMENTATION
		//********************************************************************


		/** Log events to the console and to a text file. */
		public abstract void log(String message); 

		/** Log error events to the console and to a text file. */
		public abstract void error(String message); 



		//********************************************************************
		//  GRAPHICS IMPLEMENTATION
		//********************************************************************


		/** Returns ID of the primary graphics thread (OpenGL loop). */
		public abstract long getOpenGLThreadID();

		/** Returns time since last frame render (delta) [seconds]. */
		public abstract float getDelta();

		/** Returns the current screen width. */
		public abstract int getGraphicsWidth();
		/** Returns the current screen height. */
		public abstract int getGraphicsHeight();

		/** Returns the main sprite batch for rendering. */
		public abstract SpriteBatch getSpriteBatch();

		/** Returns the main shape renderer batch for drawing. */
		public abstract ShapeRenderer getShapeRenderer();

		/** Enables blending via the OpenGL context.  Should only be called from the RENDER loop (e.g. requires OpenGL context). */
		public abstract void enableBlending();

		/** Disables blending via the OpenGL context.  Should only be called from the RENDER loop (e.g. requires OpenGL context). */
		public abstract void disableBlending();

		/** Returns the UI Texture Atlas. */
		public abstract TextureAtlas getUITextureAtlas();

		/** Returns the directory path for loading UI XML specification files. */
		public abstract String getUIFilePath();

		/** Returns the directory path for loading UI XML specification file overrides (e.g. for modding). */
		public abstract String getOverrideUIFilePath();


		/** Returns the tool-tip delay (in seconds). */
		public abstract float getToolTipDelay();

		
		/** Returns 'true' if the key to immediately show the tool-tip is active.*/
		public abstract boolean isShowTooltipKeyDown();


		
		

		//********************************************************************
		// DEFAULT PRESENTATION IMPLEMENTATION
		//********************************************************************

		

		/** Returns the default UI component properties filename. */
		public abstract String getDefaultComponentProperties();




		//********************************************************************
		//  BITMAP FONTS IMPLEMENTATION
		//********************************************************************

		/** Returns the bitmap font file listing. */ 
		public abstract ArrayList<String> getFontListing();



		/** Returns the bitmap font file directory. */
		public abstract String getFontFilePath();


		/** Returns the directory path for loading language files. 
		 * <p> Must contain additional directories for each language. */
		public abstract String getLanguageFilePath();

		/** Returns the directory path for loading override language files. 
		 * <p> Must contain additional directories for each language. */
		public abstract String getOverrideLanguageFilePath();





	}
