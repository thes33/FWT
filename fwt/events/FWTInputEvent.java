package com.arboreantears.fwt.events;

import com.arboreantears.fwt.components.FWTComponent;

/** Top-level for the FWT input event hierarchy.  Covers:  ON_ENTER, ON_EXIT, WINDOW_GAIN_FOCUS, WINDOW_LOST_FOCUS. */ 
public class FWTInputEvent
{
	/** The FWTEventType for this event. */
	public FWTEventType type = FWTEventType.TOUCH_DOWN;

	/** The FWTComponent that is the target of this event. */
	public FWTComponent target = null;
	
	
	/** Creates a new FWTInputEvent with the given type. */
	public FWTInputEvent(FWTComponent target, FWTEventType type)
	{
		this.target = target;
		this.type = type;	
	}



	/** The type for a FWTInputEvent. */
	public static enum FWTEventType
	{
		TOUCH_DOWN,
		TOUCH_UP,
		MOUSE_MOVED,
		TOUCH_DRAGGED,
		DRAG_RELEASE,

		DRAG_N_DROP,
		SCROLLED,

		ON_ENTER,
		ON_EXIT,

		WINDOW_GAIN_FOCUS,
		WINDOW_LOST_FOCUS,

		KEY_DOWN,
		KEY_UP,
		KEY_TYPED;

	}


}
