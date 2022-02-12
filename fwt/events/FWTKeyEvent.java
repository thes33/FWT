package com.arboreantears.fwt.events;

import com.arboreantears.fwt.components.FWTComponent;

/** A FWTKeyEvent to handle keyboard input. */
public class FWTKeyEvent extends FWTInputEvent
{


	/** Key-code for the key event. */
	public int keycode = -1;




	/** Creates a new FWTTouchEvent with the given parameters. */
	public FWTKeyEvent(FWTComponent target, FWTEventType type, int keycode)
		{
			super(target,type);
			this.keycode = keycode;

		}
}
