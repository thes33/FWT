package com.arboreantears.fwt.events;

import com.arboreantears.fwt.components.FWTComponent;

/** A listener for button events. */
public class FWTButtonListener
{
	//  VARIABLES
	//********************************************************************
	//********************************************************************

	/** An button listener to cascade input events. */
	FWTButtonListener chainedReceiver = null;
	/** Returns the button listener this receiver cascades button events to.  Null if none. */
	public FWTButtonListener getChainedButtonListener() {return chainedReceiver;}
	/** Returns 'true' if this listener has a chained button receiver.*/
	public boolean hasChainedButtonListener() {return chainedReceiver != null;}


	/** Adds the given button listener to the end of this input chain. */
	public void addChainedButtonListener(FWTButtonListener receiver) 
		{
			receiver.setComponent(component);
			if (chainedReceiver != null)
				chainedReceiver.addChainedButtonListener(receiver);
			else
				{
					chainedReceiver = receiver;
				}
		}


	/** Clears and removes this chain of button listeners. */
	public void clearChainedButtonListeners() 
		{
			if (chainedReceiver != null)
				{
					chainedReceiver.clearChainedButtonListeners();
				}
			chainedReceiver.setComponent(null);
			chainedReceiver = null;
		}

	/** Reference to the component this button listener is assigned to. */
	FWTComponent component;
	/** Returns the component this button listener is assigned to. */
	public FWTComponent getComponent() {return component;}
	/** Sets the component this button listener is assigned to. */
	public void setComponent(FWTComponent comp) {component = comp;}


	//  CONSTRUCTOR
	//********************************************************************
	//********************************************************************

	/** Creates a new blank FWTButtonListener.  Event methods should be overridden. */
	public FWTButtonListener()
		{
		}




	//  BUTTON EVENTS
	//********************************************************************
	//********************************************************************


	/** Attempts a buttonPressed on the button listener on the end of the chain and each listener in the chain. */
	public void tryButtonPressed(int button) throws FWTInputException 
		{
			if (chainedReceiver != null)
				{
					chainedReceiver.tryButtonPressed(button);
				}
			this.buttonPressed(button);
		}
	/** Fires when this button has been pressed, down then up.*/
	public void buttonPressed(int button) {};


	/** Attempts a buttonDown on the button listener on the end of the chain and each listener in the chain. */
	public void tryButtonDown() throws FWTInputException 
		{
			if (chainedReceiver != null)
				{
					chainedReceiver.tryButtonDown();
				}
			this.buttonDown();
		}
	/** Fires when this button has been pressed down.*/
	public void buttonDown() {};


	/** Attempts a buttonUp on the button listener on the end of the chain and each listener in the chain. */
	public void tryButtonUp() throws FWTInputException 
		{
			if (chainedReceiver != null)
				{
					chainedReceiver.tryButtonUp();
				}
			this.buttonUp();
		}
	/** Fires when this button has been released up.*/
	public void buttonUp() {};



















}
