package com.arboreantears.fwt.events;

import com.arboreantears.fwt.components.FWTComponent;

/** A receiver for input for a drawable component. Any input methods in use are expected to be overridden.
 * <br> Additional receivers can be chained to this one.  Newest receivers get priority for input events. */
public class FWTInputReceiver
{
	//  VARIABLES
	//********************************************************************
	//********************************************************************

	/** An input receiver to cascade input events. */
	FWTInputReceiver chainedReceiver = null;
	/** Returns the input receiver this receiver cascades input events to.  Null if none. */
	public FWTInputReceiver getChainedInputReceiver() {return chainedReceiver;}
	/** Returns 'true' if this receiver has a chained input receiver.*/
	public boolean hasChainedInputReceiver() {return chainedReceiver != null;}


	/** Adds the given input receiver to the end of this input chain. */
	public void addChainedInputReceiver(FWTInputReceiver receiver) 
		{
			if (chainedReceiver != null)
				chainedReceiver.addChainedInputReceiver(receiver);
			else
				{
					chainedReceiver = receiver;
				}
		}


	/** Clears and removes this chain of input receivers. */
	public void clearChainedInputReceivers() 
		{
			if (chainedReceiver != null)
				{
					chainedReceiver.setComponent(null);
					chainedReceiver.clearChainedInputReceivers();
				}
			chainedReceiver = null;
		}

	/** Reference to the component this input receiver is assigned to. */
	FWTComponent component;
	/** Returns the component this input receiver is assigned to. */
	public FWTComponent getComponent() {return component;}
	/** Sets the component this input receiver is assigned to. */
	public void setComponent(FWTComponent comp) {component = comp;}





	//  CONSTRUCTOR
	//********************************************************************
	//********************************************************************


	/** Creates a new Input Receiver to capture mouse and keyboard events. */
	public FWTInputReceiver()
		{
			
		}




	//  MOUSE INPUT
	//********************************************************************
	//********************************************************************


	// TOUCH DOWN
	/** Attempts a touchDown on the input receiver on the end of the chain. */
	public boolean tryTouchDown(int mX, int mY, int pointer, int button) throws FWTInputException 
		{
			if (chainedReceiver != null)
				return (chainedReceiver.tryTouchDown(mX, mY, pointer, button) | this.touchDown(mX, mY, pointer, button));
			return this.touchDown(mX, mY, pointer, button);
		}
	/** Touch down on this drawable. Returns 'true' if this drawable managed the touch event.*/
	public boolean touchDown(int mX, int mY, int pointer, int button) throws FWTInputException {return false;}


	// TOUCH UP
	/** Attempts a touchUp on the input receiver on the end of the chain. */
	public boolean tryTouchUp(int mX, int mY, int pointer, int button, FWTComponent touchDownComponent) throws FWTInputException 
		{
			if (chainedReceiver != null)
				return (chainedReceiver.tryTouchUp(mX, mY, pointer, button, touchDownComponent) | 
						this.touchUp(mX, mY, pointer, button, touchDownComponent));
			return this.touchUp(mX, mY, pointer, button, touchDownComponent);
		}
	/** Touch up on this drawable. Returns 'true' if this drawable managed the touch event.*/
	public boolean touchUp(int mX, int mY, int pointer, int button, FWTComponent touchDownComponent) throws FWTInputException {return false;}


	// MOUSE MOVED
	/** Attempts a mouseMoved on the input receiver on the end of the chain. */
	public boolean tryMouseMoved(int mX, int mY) throws FWTInputException 
		{
			if (chainedReceiver != null)
				return (chainedReceiver.tryMouseMoved(mX, mY) | this.mouseMoved(mX, mY));
			return this.mouseMoved(mX, mY);
		}
	/** Mouse moved on this drawable. Returns 'true' if this drawable managed the moved event.*/
	public boolean mouseMoved(int mX, int mY) throws FWTInputException {return false;}


	// TOUCH DRAGGED
	/** Attempts a touchDragged on the input receiver on the end of the chain. */
	public boolean tryTouchDragged(int mX, int mY, int pointer) throws FWTInputException 
		{
			if (chainedReceiver != null)
				return (chainedReceiver.tryTouchDragged(mX, mY, pointer) | this.touchDragged(mX, mY, pointer));
			return this.touchDragged(mX, mY, pointer);
		}
	/** Touch dragged on this drawable. Returns 'true' if this drawable managed the dragged event.*/
	public boolean touchDragged(int mX, int mY, int pointer) throws FWTInputException {return false;}
	

	// DRAGGED RELEASE
	/** Attempts a dragRelease on the input receiver on the end of the chain. */
	public boolean tryDragRelease(int mX, int mY, int pointer) throws FWTInputException 
		{
			if (chainedReceiver != null)
				return (chainedReceiver.tryDragRelease(mX, mY, pointer) | this.dragRelease(mX, mY, pointer));
			return this.dragRelease(mX, mY, pointer);
		}
	/** Drag release on this drawable (i.e. no longer being dragged). Returns 'true' if this drawable managed the release event.*/
	public boolean dragRelease(int mX, int mY, int pointer) throws FWTInputException {return false;}


	// SCROLLED
	/** Attempts a scrolled on the input receiver on the end of the chain. */
	public boolean tryScrolled(float amountX,float amountY) throws FWTInputException 
		{
			if (chainedReceiver != null)
				return (chainedReceiver.tryScrolled(amountX,amountY) |  this.scrolled(amountX,amountY));
			return this.scrolled(amountX,amountY);
		}
	/** Mouse scrolled on this drawable. Returns 'true' if this drawable managed the scrolled event.*/
	public boolean scrolled(float amountX,float amountY) throws FWTInputException {return false;}


	// ON ENTER
	/** Attempts an onEnter on the input receiver on the end of the chain. */
	public void tryOnEnter() throws FWTInputException 
		{
			if (chainedReceiver != null)
				chainedReceiver.tryOnEnter();
			this.onEnter();
		}
	/** Mouse entered the dimensions of this drawable.*/
	public void onEnter() throws FWTInputException { }


	// ON EXIT
	/** Attempts an onExit on the input receiver on the end of the chain. */
	public void tryOnExit() throws FWTInputException 
		{
			if (chainedReceiver != null)
				chainedReceiver.tryOnExit();
			this.onExit();
		}
	/** Mouse exited the dimensions of this drawable.*/
	public void onExit() throws FWTInputException { } 


	// DRAG 'N' DROP
	/** Attempts a dragNDrop on the input receiver on the end of the chain. */
	public boolean tryDragNDrop(int mX, int mY, int pointer, int button, FWTComponent dragComponent) throws FWTInputException 
		{
			if (chainedReceiver != null)
				return (chainedReceiver.tryDragNDrop(mX, mY, pointer, button, dragComponent) 
						|  this.dragNDrop(mX, mY, pointer, button, dragComponent));
			return this.dragNDrop(mX, mY, pointer, button, dragComponent);
		}
	/** Component dropped on this component. Returns 'true' if this drawable managed the drop event.*/
	public boolean dragNDrop(int mX, int mY, int pointer, int button, FWTComponent dragComponent) throws FWTInputException {return false;}






	//  KEYBOARD INPUT
	//********************************************************************
	//********************************************************************


	// KEY DOWN
	/** Attempts a keyDown on the input receiver on the end of the chain. */
	public boolean tryKeyDown(int keycode) throws FWTInputException 
		{
			if (chainedReceiver != null)
				return (chainedReceiver.tryKeyDown(keycode) | this.keyDown(keycode));
			return this.keyDown(keycode);
		}
	/** Key down on this drawable. Returns 'true' if this drawable managed the key event.*/
	public boolean keyDown(int keycode) throws FWTInputException {return false;}


	// KEY UP
	/** Attempts a keyUp on the input receiver on the end of the chain. */
	public boolean tryKeyUp(int keycode) throws FWTInputException 
		{
			if (chainedReceiver != null)
				return (chainedReceiver.tryKeyUp(keycode) | this.keyUp(keycode));
			return this.keyUp(keycode);
		}
	/** Key up on this drawable. Returns 'true' if this drawable managed the key event.*/
	public boolean keyUp(int keycode) throws FWTInputException {return false;}


	// KEY TYPED
	/** Attempts a keyTyped on the input receiver on the end of the chain. */
	public boolean tryKeyTyped(char keychar) throws FWTInputException 
		{
			if (chainedReceiver != null)
				return (chainedReceiver.tryKeyTyped(keychar) | this.keyTyped(keychar));
			return this.keyTyped(keychar);
		}
	/** Key typed on this drawable. Returns 'true' if this drawable managed the key event.*/
	public boolean keyTyped(char keychar) throws FWTInputException {return false;}









	//  WINDOW FOCUS
	//********************************************************************
	//********************************************************************



	// ON FOCUS
	/** Attempts a gainFocus() on the input receiver on the end of the chain. */
	public void tryGainFocus() throws FWTInputException 
		{
			if (chainedReceiver != null)
				chainedReceiver.tryGainFocus();
			this.gainFocus();
		}
	/** This drawable (window) has been given focus by the window manager.*/
	public void gainFocus() throws FWTInputException { }


	// OFF FOCUS
	/** Attempts an lostFocus() on the input receiver on the end of the chain. */
	public void tryLostFocus() throws FWTInputException 
		{
			if (chainedReceiver != null)
				chainedReceiver.tryLostFocus();
			this.lostFocus();
		}
	/** This drawable (window) has lost its focus by the window manager.*/
	public void lostFocus() throws FWTInputException { } 











}
