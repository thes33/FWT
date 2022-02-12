package com.arboreantears.fwt.events;

import com.arboreantears.fwt.components.FWTComponent;

/** A FWTScrollEvent to handle mouse-wheel input. */
public class FWTScrollEvent extends FWTInputEvent
{


	/** Amount of vertical movement for the scroll wheel. */
	public float amountY = 0f;

	/** Amount of horizontal movement for the scroll wheel. */
	public float amountX = 0f;






	/** Creates a new FWTScrollEvent with the given parameters. */
	public FWTScrollEvent(FWTComponent target, FWTEventType type, float amountX, float amountY)
		{
			super(target,type);
			this.amountX = amountX;
			this.amountY = amountY;

		}



}