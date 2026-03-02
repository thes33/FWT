package com.arboreantears.fwt;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;



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

 

	/** Returns the standard FWT font of the given size. */
	public static BitmapFont getFont(int size)
		{return getFont(standardFont,size);}

	/** Returns the title FWT font of the given size. */
	public static BitmapFont getTitleFont(int size)
		{return getFont(titleFont,size);}


	/** Returns the Bitmap font with the given name and size. */
	public static BitmapFont getFont(String fontName, int size)
		{
			// IF exists, return font
			if (fonts.containsKey(fontName+"_"+Integer.toString(size)))
				return fonts.get(fontName+"_"+Integer.toString(size));
			else // ELSE generate font
				{
					BitmapFont font = generateBitmapFont(fontName, size);
					if (font != null)
						{
							fonts.put(fontName+"_"+Integer.toString(size), font);
							return font;
						}
					FWTController.error("Fonts: Cannot generate missing font: "+fontName+" : "+size+" pnt.");
					return null;
				}
		}






	// INITIALIZATION
	//=====================================================================


	/** Initialize bitmap fonts to memory. */
	public static void initialize(String standard_Font, String title_Font)
		{
			standardFont = standard_Font;
			titleFont = title_Font;

			fonts = new HashMap<String,BitmapFont>();
			ArrayList<String> fontNames = FWTController.getTrueTypeFontListing();

			// FOR each font, generate from TrueType Font
			int counter = 0;
			for (String fontName : fontNames)
				for (int size = 12; size < 43; size = size+2)
					{
						BitmapFont font = generateBitmapFont(fontName, size);
						if (font != null)
							{fonts.put(fontName+"_"+Integer.toString(size), font); counter++;}
					}
			FWTController.log("Fonts: "+counter+" bitmap fonts generated.");
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
	public static BitmapFont loadBitmapFont(String name)
		{
			FileHandle fontFile = Gdx.files.local(FWTController.getFontFilePath()+"/"+name+".fnt");
			if (fontFile.exists())
				return new BitmapFont(fontFile,false);
			else
				return null;
		}


	/** Generate a new BitmapFont using the given TrueType font name with the given size. */
	public static BitmapFont generateBitmapFont(String fontName, int size)
		{
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local(FWTController.getFontFilePath()+"/"+fontName+".ttf"));
			FreeTypeFontParameter parameter = new FreeTypeFontParameter();
			parameter.size = size;
			BitmapFont font = generator.generateFont(parameter); 
			generator.dispose(); 
			return font;
		}




}
