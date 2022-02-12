package com.arboreantears.fwt.events;

import com.arboreantears.fwt.components.FWTComponent;

public class FWTDragNDropEvent extends FWTTouchEvent
	{
		public FWTComponent draggedComponent;
		
		
		
		/** Creates a new DragNDropEvent indicating the dragged component. */
		public FWTDragNDropEvent(FWTComponent target, FWTEventType type, int mX, int mY, int pointer, int button, FWTComponent draggedComponent)
		{
			super(target,type,mX,mY,pointer,button);
			this.draggedComponent = draggedComponent;
			
		}

	}
