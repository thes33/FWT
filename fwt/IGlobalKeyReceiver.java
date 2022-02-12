package com.arboreantears.fwt;

import com.arboreantears.fwt.events.FWTInputException;

/** An interface for windows that are global key receivers.
 * <br>Must register themselves with the FWTWindowManager to receive global key-press events.
 */
public interface IGlobalKeyReceiver 
	{
		
		/** Returns the unique ID of the global key receiver. */
		public long getID();
		

		/** Global key down event receiver. Returns true iff processed the event (blocking). */
		public boolean globalKeyDown(int keycode) throws FWTInputException; 
		

		/** Global key up event receiver. Returns true iff processed the event (blocking). */
		public boolean globalKeyUp(int keycode) throws FWTInputException;
		
		
		/** Global key typed event receiver. Returns true iff processed the event (blocking). */
		public boolean globalKeyTyped(char keychar) throws FWTInputException;
		
	}
