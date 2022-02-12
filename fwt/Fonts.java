package com.arboreantears.fwt;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;



/** Font Library for all game fonts and custom colors. */
public class Fonts
	{
		
		// FONTS
		//=====================================================================
		
		// Font Format:  "name_size.fnt"
		
		
		public static HashMap<String,BitmapFont> fonts;
		
		/** FWT standard font filename. */
		public static String standardFont;
		/** FWT title font filename. */
		public static String titleFont;
		
		
		/** Returns the Bitmap font with the given name and size. */
		public static BitmapFont getFont(String name, int size)
		{
			return fonts.get(name+"_"+Integer.toString(size));
		}
				
		
		/** Returns the standard FWT font of the given size. */
		public static BitmapFont getFont(int size)
			{return getFont(standardFont,size);}

		/** Returns the title FWT font of the given size. */
		public static BitmapFont getTitleFont(int size)
			{return getFont(titleFont,size);}
				
		
		
		
		
		

		// INITIALIZATION
		//=====================================================================
		
		
		/** Initialize bitmap fonts to memory. */
		public static void initialize(String standard_Font, String title_Font)
		{
			fonts = new HashMap<String,BitmapFont>();
			ArrayList<String> fontNames = FWTController.getFontListing();
			int counter = 0;
			boolean hasStandard = false;
			boolean hasTitle = false;
			
			// FOR each font in listing, add to map
			for (String fontName : fontNames)
				{
					String fName = fontName.substring(0, fontName.lastIndexOf("_"));	
					if (!hasStandard) hasStandard = (fName.equals(standard_Font));
					if (!hasTitle) hasTitle = (fName.equals(title_Font));
					
					BitmapFont font = loadFont(fontName);
					if (font != null)
						{fonts.put(fontName, font); counter++;}
				}
			FWTController.log("Fonts: "+counter+" bitmap fonts initialized.");
			if (hasStandard) standardFont = standard_Font; else FWTController.error("Fonts: Could not find standard font: "+standardFont);
			if (hasTitle) titleFont = title_Font; else FWTController.error("Fonts: Could not find title font: "+titleFont);						
		}
		
		
		
		public static void dispose()
			{
				for (BitmapFont font : fonts.values())
					font.dispose();
				fonts.clear();
				FWTController.log("Fonts: Fonts disposed.");
			}
		
		
		


		//*************************************************************************************************
		// FONT I/O
		//*************************************************************************************************



		/** Loads a bitmap font of the name from file and returns the BitmapFont. */
		public static BitmapFont loadFont(String name)
			{
				FileHandle fontFile = Gdx.files.local(FWTController.getFontFilePath()+"/"+name+".fnt");
				if (fontFile.exists())
					return new BitmapFont(fontFile,false);
				else
					return null;
			}
		

		
		
	}
