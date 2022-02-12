package com.arboreantears.fwt.events;

import com.arboreantears.fwt.components.FWTComponent;

/** An FWTTouchEvent for a touch/mouse button released with the object that was pressed down. */
public class FWTTouchUpEvent extends FWTTouchEvent
{

	public FWTComponent touchDownComponent;
	
	
	
	/** Creates a new TouchUpEvent indicating the component that received the touch-down event. */
	public FWTTouchUpEvent(FWTComponent target, FWTEventType type, int mX, int mY, int pointer, int button, FWTComponent touchDownComp)
	{
		super(target,type,mX,mY,pointer,button);
		this.touchDownComponent = touchDownComp;
		
	}





}
