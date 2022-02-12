package com.arboreantears.fwt;

import com.arboreantears.fwt.components.FWTButton;
import com.arboreantears.fwt.components.FWTComponent;
import com.arboreantears.fwt.components.FWTContainer;
import com.arboreantears.fwt.components.FWTIcon;
import com.arboreantears.fwt.components.FWTLabel;
import com.arboreantears.fwt.components.FWTListScrollable;
import com.arboreantears.fwt.components.FWTProgressBar;
import com.arboreantears.fwt.components.FWTScrollPanel;
import com.arboreantears.fwt.components.FWTScrollable;
import com.arboreantears.fwt.components.FWTSlider;
import com.arboreantears.fwt.components.FWTTextButton;
import com.arboreantears.fwt.components.FWTTextField;
import com.arboreantears.fwt.components.FWTToggleButton;
import com.arboreantears.fwt.components.FWTWindow;

/** Factory class for the creation of FWT components based on XML specifications. */
public class FWTComponentFactory
{


	/** Custom component factory. If null, this feature will not be used. */
	static IFWTComponentFactory customFactory = null;
	/** Set the custom component factory. If null, this feature will not be used. */
	public static void setCustomFactory(IFWTComponentFactory custFactory) {customFactory = custFactory;}





	/** Creates a new FWTComponent based on the given type.
	 * <br> Custom types are passed to the custom factory. */
	public static FWTComponent create(XMLDataPacket data, String type)
		{
			FWTComponent comp = null;

			// SWITCH by type
			switch(type)
			{

				// CONTAINERS
				//--------------------------------------------
				// FWTWindow
			case "window":
				comp = new FWTWindow(data); break;

				// FWTScrollable
			case "scrollable":
				comp = new FWTScrollable(data); break;

				// FWTScrollPanel
			case "scrollpanel":
				comp = new FWTScrollPanel(data); break;

				// FWTListScrollable
				//TODO: Apply E,T. Perhaps with reflection?
			case "listscrollable":
				comp = new FWTListScrollable(data); break;

				// FWTContainer
			case "container":
				comp = new FWTContainer(data); break;


				// COMPONENTS
				//--------------------------------------------

				// FWTComponent
			case "component":
				comp = new FWTComponent(data); break;


				// FWTButton
			case "button":
				comp = new FWTButton(data); break;
				
				// FWTIcon
			case "icon":
				comp = new FWTIcon(data); break;

				// FWTLabel
			case "label":
				comp = new FWTLabel(data); break;

				// FWTProgressBar
			case "progressbar":
				comp = new FWTProgressBar(data); break;

				// FWTSlider
			case "slider":
				comp = new FWTSlider(data); break;
				
				// FWTTextButton
			case "textbutton":
				comp = new FWTTextButton(data); break;
				
				// FWTTextField
			case "textfield":
				comp = new FWTTextField(data); break;
				
				// FWTToggleButton
			case "togglebutton":
				comp = new FWTToggleButton(data); break;

				
				
			default: break;
			}

			// Check custom factory
			if (customFactory != null && comp == null)
				comp = customFactory.create(data, type);


			// IF component not created.
			if (comp == null)
				FWTController.error("FWTComponentFactory: Could not create component of type '"+type+"'.");


			return comp;
		}


}
