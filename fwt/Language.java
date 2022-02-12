package com.arboreantears.fwt;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;




/** Language library for handling text and strings.*/
public class Language
{

	// VARIABLES
	//********************************************************************
	//********************************************************************

	/** 'True' if a valid language map is loaded. */
	static boolean valid = false;
	/** Returns 'True' if a valid language map is loaded. */
	public static boolean isValid() {return valid;}

	
	
	/** The current language set.*/
	static String currentLangauge = "en";
	/** Sets the current language set.*/
	public static void setLanguage(String lang) {currentLangauge = lang;}
	/** Gets the current language set.*/
	public static String getLanguage(){return currentLangauge;}

	

	/** HashMap containing the language data for the main games states. */
	static HashMap<String,String> languageMap = null;	
	public final static void setLanguageMap(HashMap<String,String> langmap) {languageMap = langmap;}
	public final static HashMap<String,String> getLanguageMap() {return languageMap;}



	

	//  METHODS
	//********************************************************************
	//********************************************************************
	

	/** Initialize the main language files.*/
	public static void initializeLanguageSet() throws Exception
		{
			// Reset validity
			valid = false;

			// Read in language files
			languageMap = loadLanguageFiles();
			valid = true;
		}

	
	
	

	/** Returns the localized language string with the given tag, if does not exist, returns "".*/
	public static String get(String tag)
		{
			String str = "";
			if (languageMap != null)
				str =  languageMap.get(tag);
			if (str != null)
				return str;
			return "";
		}


	

	//*************************************************************************************************
	// LANGUAGE FILE I/O
	//*************************************************************************************************



	/**  Loads all the main game state language files in the language directory of the current language
	 * and returns the HashMap<String,String> database.*/
	public static HashMap<String,String> loadLanguageFiles() throws Exception
		{
			// Current core language directory
			FileHandle langDir = Gdx.files.local(FWTController.getLanguageFilePath()+"/"+Language.getLanguage()+"/");
			HashMap<String,String> languageMap = new HashMap<String,String>();


			// IF language directory exists
			if (langDir.exists())
				{
					// Prepare Files Array
					FileHandle[] Files = langDir.list();

					// FOR each files
					for (int f=0;f<Files.length;f++)
						{
							// IF file is XML file
							if (Files[f].extension().equalsIgnoreCase("xml"))
								{
									HashMap<String,String> lang = null;
									String file = Gdx.files.local(langDir.path()+"/"+Files[f].name()).path();

									// Read XML file
									try{
										XMLLanguageReader xread = new XMLLanguageReader();
										lang = xread.read(file);
									}catch(Exception e) {e.printStackTrace(); throw e;}

									// Add language definitions to map
									if (lang != null)
										try{
											languageMap.putAll(lang);
										}catch(Exception ex) {throw new Exception("Cannot read '"+Language.getLanguage()+"' language file: '"+file+"'. "+ex.getMessage());}

								} // END if XML file
						} // END for each file

				} // END if language directory exists
			return languageMap;
		}





	/**  Loads all the override language files for overrides and returns the HashMap<String,String> database.*/
	public static HashMap<String,String> loadOverrideLanguageFiles() throws Exception
		{
			// Current language directory in current ruleset
			FileHandle langDir = Gdx.files.local(FWTController.getOverrideLanguageFilePath()+"/"+Language.getLanguage());
			HashMap<String,String> overridesMap = new HashMap<String,String>();
			
			
			// IF language directory exists
			if (langDir.exists())
				{
					// Prepare Files Array
					FileHandle[] Files = langDir.list();

					// FOR each files
					for (int f=0;f<Files.length;f++)
						{
							// IF file is XML file
							if (Files[f].extension().equalsIgnoreCase("xml"))
								{
									HashMap<String,String> lang = null;
									FileHandle file = Gdx.files.local(langDir.path()+"/"+Files[f].name());

									// Read XML file
									try{
										XMLLanguageReader xread = new XMLLanguageReader();
										lang = xread.read(file.path());
									}catch(Exception e) {e.printStackTrace(); throw e;}

									// Add language definitions to overrides map
									if (lang != null)
										try{
											overridesMap.putAll(lang);
										}catch(Exception ex) {throw new Exception("Cannot read ruleset '"+Language.getLanguage()+"' language file: '"+file.name()+"'. "+ex.getMessage());}

								} // END if XML file
						} // END for each file

				} // END if language directory exists
			
			return overridesMap;
		}
	
	
	
	

















}
