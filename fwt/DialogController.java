package com.arboreantears.fwt;

import java.util.HashMap;



/** A controller for handling the presentation and control of dialog windows. */
public class DialogController
{

	// VARIABLES
	//*********************************************************************************
	//*********************************************************************************


	// Dialog Responses
	//---------------------
	/** Map of window ID to dialog response. */
	static HashMap<Long,Integer> windowResponses = new HashMap<Long,Integer>();

	/** Returns 'true' if a response has been received from user for the given window ID. */
	public static boolean hasResponse(long windowID) 
		{
			// IF has window ID
			if (windowResponses.containsKey(windowID))
				{
					return (windowResponses.get(windowID).intValue() != -1);
				}
					
			return false;
		}

	/** Returns the integer value of the dialog response for the given window.
	 *   *WARNING* This can only be retrieved once as the value is removed from the set. 
	 *   Returns -1 if no value found in set. */
	public static int retrieveResponse(long windowID)
	{
		// IF has window ID
		if (windowResponses.containsKey(windowID))
			{
				int v = (windowResponses.get(windowID).intValue());
				windowResponses.remove(windowID);
				return v;
			}
				
		return -1;
	}

	
	
	
	// Preset Dialog Selection Values
	//---------------------

	/** A 'NO' response (0).*/
	public static final int NO = 0;
	/** A 'YES' response (1).*/
	public static final int YES = 1;
	/** An 'OK' response (100).*/
	public static final int OK = 100;
	/** A 'CANCEL' response (500).*/
	public static final int CANCEL = 500;
	/** A 'CLOSE' response (1000).*/
	public static final int CLOSE = 1000;






	//*********************************************************************************
	// METHODS
	//*********************************************************************************





	/** BLOCKING  
	 * <br> *LOGIC THREAD ONLY* 
	 * <br> Waits for a response from the user for the dialog option from the given window ID. 
	 * <br> Logic halts until a result is submitted. */
	public static void waitforUserOption(long windowID)
		{
			// Wait for GUI
			while(!hasResponse(windowID))
				{
					//TODO: Pause logic engine?
					//GameEngine.pause(100);
				}

		}



	/** Submits the selected dialog response for the given dialog window ID. */
	public static void submitResponse(long windowID, int response)
		{
			windowResponses.put(windowID, response);
		}


	
	





	

















}
