package com.arboreantears.fwt;

import com.arboreantears.fwt.components.FWTComponent;

/** An interface for the creation of custom FWT components. */
public interface IFWTComponentFactory
	{
		
		
		public abstract FWTComponent create(XMLDataPacket data, String type);

	}
