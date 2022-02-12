package com.arboreantears.fwt;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


/** Loads and manages images for the graphics system and handles graphical textures. */
public class FWTImageManager
{


	//********************************************************************
	//  VARIABLES
	//********************************************************************

	/** True if the ImageManager has been properly initialized. */
	static boolean valid = false;
	/** True if the ImageManager has been properly initialized. */
	public static boolean isValid() {return valid;} 
	
	

	// TEXTURE ATLAS
	//============================================

	/** Texture Atlas containing all custom ruleset UIs images. */
	private static TextureAtlas uiAtlas;
	/** Returns the texture Atlas containing all UIs images. */
	public static TextureAtlas getUIAtlas() {return uiAtlas;}

	
	



	// CACHED NINEPATCHES
	//============================================

	/** Map of cached nine-patches. */
	static HashMap<Long,NinePatch> cachedNinePatches = new HashMap<Long, NinePatch>();

	/** Returns a nine-patch with the given component ID, texture name, width, and height. */
	public static NinePatch getNinePatch(long id, String textureID, int width, int height)
	{
		NinePatch ninePatch = cachedNinePatches.get(id);

		// IF does not exist OR not correct size, recreate patch
		if (ninePatch == null || ninePatch.getTotalWidth() != width || ninePatch.getTotalHeight() != height)
			{
				ninePatch = uiAtlas.createPatch(textureID);
				cachedNinePatches.put(id, ninePatch);
			}
		// ELSE return cached patch
		return ninePatch;

	}
	/** Removes a nine-patch associated with the given component ID. */
	public static void removeNinePatch(long id)
	{
		cachedNinePatches.remove(id);
	}





	// CACHED BORDER NINEPATCHES
	//============================================

	/** Map of cached border nine-patches. */
	static HashMap<Long,NinePatch> cachedBorderNinePatches = new HashMap<Long, NinePatch>();

	/** Returns a nine-patch with the given component ID, texture name, width, and height. */
	public static NinePatch getBorderNinePatch(long id, String textureID, int width, int height)
	{
		NinePatch ninePatch = cachedBorderNinePatches.get(id);

		// IF does not exist OR not correct size, recreate patch
		if (ninePatch == null || ninePatch.getTotalWidth() != width || ninePatch.getTotalHeight() != height)
			{
				ninePatch = uiAtlas.createPatch(textureID);
				cachedBorderNinePatches.put(id, ninePatch);
			}
		// ELSE return cached patch
		return ninePatch;

	}
	/** Removes a nine-patch associated with the given component ID. */
	public static void removeBorderNinePatch(long id)
	{
		cachedBorderNinePatches.remove(id);
	}











	//********************************************************************
	//  CONSTRUCTOR
	//********************************************************************


	/** Initializes the ImageManager and underlying frame buffers. Sets core image atlas. */
	public static void initialize()
	{
		valid = false;	
		
		loadTextures();

		valid = true;
	}







	//********************************************************************
	//  TEXTURE LOADING (FROM PACKED FILES)
	//********************************************************************


	/** Loads packaged image files and creates associated UI texture atlas. [Must be called from Graphics Engine loop.] */
	public static void loadTextures()
	{

		// Remove any previous bindings
		if (uiAtlas != null)
			{uiAtlas.dispose(); uiAtlas = null;}

		// UI Texture Atlas
		uiAtlas = FWTController.getUITextureAtlas();

		FWTController.log("FWTImageManager: UI textures initialized.");
	}




	/** Disposes of all texture atlas. [Must be called from Graphics Engine loop.] */
	public static void dispose()
	{
		if (uiAtlas != null)
			{uiAtlas.dispose(); uiAtlas = null;}
		
		cachedNinePatches.clear();
	}









}


