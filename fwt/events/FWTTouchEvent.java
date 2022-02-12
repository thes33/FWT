package com.arboreantears.fwt.events;

import com.arboreantears.fwt.components.FWTComponent;

/** An FWTTouchEvent for a touch/mouse button pressed or released. */
public class FWTTouchEvent extends FWTInputEvent
{

	/** Mouse X position relative to the parent component. */
	public int mX = -1;
	/** Mouse Y position relative to the parent component. */
	public int mY = -1;
	/** Pointer used for the touch event. */
	public int pointer = -1;
	/** Button that triggered the touch event. */
	public int button = -1;






	/** Creates a new FWTTouchEvent with the given parameters. */
	public FWTTouchEvent(FWTComponent target, FWTEventType type, int mX, int mY, int pointer, int button)
		{
			super(target,type);
			this.mX = mX;
			this.mY = mY;
			this.pointer = pointer;
			this.button = button;

		}




}
