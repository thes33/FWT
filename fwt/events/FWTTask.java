package com.arboreantears.fwt.events;




/** A FWT Task to be processed in the OpenGL context thread. 
 * <br> Required for opening new windows and other graphical objects. */
public class FWTTask
	{
		
		/** The method of activity for this FWTTask. */
		public void process()
		{
			// Expected to be overridden during instantiation
		}

	}
