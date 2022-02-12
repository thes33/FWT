package com.arboreantears.fwt.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import com.arboreantears.fwt.FWTController;
import com.arboreantears.fwt.FWTImageManager;
import com.arboreantears.fwt.FWTUIFrameBufferController;
import com.arboreantears.fwt.FWTWindowManager;
import com.arboreantears.fwt.XMLDataPacket;
import com.arboreantears.fwt.events.FWTInputException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;




/** A FWT Container object to hold and render FWT components. */
public class FWTContainer extends FWTComponent
{

	//  VARIABLES
	//********************************************************************
	//********************************************************************


	@Override
	public String toString()
		{
			return "Container: "+this.name;
		}



	// FWT Components
	//---------------------------

	/** The list of FWTComponents being managed by this manager. */
	ArrayList<FWTComponent> components;
	/** Returns the list of FWTComponents being managed by this manager. */
	public ArrayList<FWTComponent> getComponents() {return components;}
	/** Returns 'true' if this container has component children. */
	public boolean hasComponents() {return components.size() > 0;}


	/** Adds the given component to this container. */
	public synchronized void addComponent(FWTComponent component)
		{ 
			if (component != null && component.getID() != this.getID())
				{
					component.setWindowManager(this.getWindowManager());
					component.setParent(this);
					if (this.enabled)
						component.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
					else component.setResize();
					component.onAdded();
					components.add(component);
				}
		}
	/** Returns the FWTComponent contained in this Container.  Returns null if none found. */
	public FWTComponent getComponentByID(long id)
		{
			for (FWTComponent comp : components)	
				{
					if (comp.getID() == id)
						return comp;
				}
			return null;
		}
	/** Removes the given FWTComponent contained in this Container. Returns 'true' if a component was sucessfully removed. */
	public synchronized boolean removeComponent(FWTComponent comp)
		{
			if (comp != null)
				{
					Iterator<FWTComponent> itrC = components.iterator();
					while (itrC.hasNext())
						{
							FWTComponent comp2 = itrC.next();
							if (comp2.getID() == comp.getID())
								{
									comp2.onRemoved();
									comp2.setParent(null);
									comp2.setWindowManager(null);
									itrC.remove();
									return true;
								}
						}
				}
			return false;
		}
	/** Removes all components in this container. */
	public synchronized void clearComponents()
		{
			Iterator<FWTComponent> itrC = components.iterator();
			while (itrC.hasNext())
				{
					FWTComponent comp = itrC.next();
					comp.onRemoved();
					comp.setParent(null);
					comp.setWindowManager(null);
					itrC.remove();
					comp.dispose();
					comp = null;
				}
		}

	@Override
	public void setWindowManager(FWTWindowManager manager)
		{
			this.manager = manager;
			for (FWTComponent comp : components)	
				{
					comp.setWindowManager(manager);
				}
		}

	/** Returns 'true' if the given component exists somewhere in this container's hierarchy. */
	public boolean existsInHierarchy(FWTComponent comp)
		{
			for (FWTComponent icomp : components)	
				{
					if (icomp.getID() == comp.getID())
						return true;
					else if (icomp instanceof FWTContainer)
						if (((FWTContainer) icomp).existsInHierarchy(comp))
							return true;
				}
			return false;
		}



	/** Returns an FWTComponent based on the given name if it exists anywhere in the hierarchy. */
	public FWTComponent findComponent(String name)
		{
			FWTComponent comp = null; 
			// FOR each component
			for (FWTComponent icomp : components)	
				{
					if (icomp.getName().equals(name))
						return icomp;
					else if (icomp instanceof FWTContainer)
						{
							comp = ((FWTContainer) icomp).findComponent(name);
							if (comp != null) return comp;
						}
				}
			return comp;
		}

	
	
	

	// Focus
	//---------------------------

	/** Currently focused component to receive keyboard input. */
	FWTComponent keyFocused;
	/** Sets currently focused component to receive keyboard input. */
	public void setKeyFocus(FWTComponent comp) {keyFocused = comp;}
	/** Returns currently focused component to receive keyboard input. */
	public FWTComponent getKeyFocus() {return keyFocused;}

	/** Currently focused component to receive mouse input.  Mouse is 'over' this object. */
	FWTComponent mouseFocused;








	//  CONSTRUCTOR
	//********************************************************************
	//********************************************************************

	/** Create a new FWTContainer. */
	public FWTContainer(XMLDataPacket data)
		{
			super(data);
		}
	

	/** Create a new FWTContainer. */
	public FWTContainer(String parent, String object)
		{
			super(parent,object);
		}
	
	
	

	//  XML DATA
	//********************************************************************
	//********************************************************************
	
	@Override
	protected void setDefaults()
		{
			super.setDefaults();
			components = new ArrayList<FWTComponent>();
		}
	
	@Override
	public void refreshData()
		{
			super.refreshData();

			if (components != null)
				for (FWTComponent comp : components)
					{comp.refreshData();}
	}





	//  RESIZE
	//********************************************************************
	//********************************************************************

	@Override
	public void setResize() 
		{			
			needsResize = true;
			for (FWTComponent comp : components)
				{comp.setResize();}
		}


	/** Resizes this container and its components based on the given screen dimensions. */
	public void resize(int width, int height)
		{
			super.resize(width, height);

			for (FWTComponent comp : components)
				{
					// Update parent dimensions
					comp.getData().put("parentwidth",Integer.toString((int)dims.width));
					comp.getData().put("parentheight",Integer.toString((int)dims.height));

					// Resize components
					comp.resize(width,height);
				}
		}






	//  RENDER
	//********************************************************************
	//********************************************************************

	@Override
	public void render()
		{
			try {
				// Update component data
				update();
			}catch (Exception ex)
				{
					FWTController.error("Error updating component: "+this.getName());
					FWTController.error(ex.getMessage());
				}

			try{


				// Resize if required
				if (needsResize)
					{FWTUIFrameBufferController.resizeUIBuffer(this); needsResize = false;}

				// Redraw back buffer if required
				if (needsRedrawn || isAnimating || componentsNeedRedrawn() || componentsAnimating())
					{redrawBuffer(); needsRedrawn = false;}

				SpriteBatch spriteBatch = FWTController.getSpriteBatch();

				// Draw back buffer
				FrameBuffer fb = FWTUIFrameBufferController.getUIBuffer(this);
				spriteBatch.begin();
				spriteBatch.setBlendFunction(-1, -1);
				Gdx.gl20.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE);
				spriteBatch.draw(fb.getColorBufferTexture(), 
						dims.x,dims.y,dims.width,dims.height,
						0,0,fb.getWidth(),fb.getHeight(),
						false, true); 
				spriteBatch.end();
				spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			}catch (Exception ex)
				{
					FWTController.error("Error rendering container: "+this.getName());
					FWTController.error(ex.getMessage());
				}
		}


	/** Returns 'true' if any of this containers components need redrawn. */
	public boolean componentsNeedRedrawn()
		{
			for (FWTComponent comp : components)
				{
					if (comp.enabled)
						{
							if (comp instanceof FWTContainer)
								{
									if (((FWTContainer) comp).componentsNeedRedrawn() || comp.needsRedrawn)
										return true;
								}
							else if (comp.needsRedrawn)
								return true;
						}
				}
			return false;
		}

	/** Returns 'true' if any of this containers components are currently animating. */
	public boolean componentsAnimating()
		{
			for (FWTComponent comp : components)
				{
					if (comp.enabled)
						{
							if (comp instanceof FWTContainer)
								{
									if (((FWTContainer) comp).componentsAnimating() || comp.isAnimating)
										return true;
								}
							else if (comp.isAnimating)
								return true;
						}
				}
			return false;
		}





	//  REDRAW
	//********************************************************************
	//********************************************************************

	@Override
	public void redrawBuffer()
		{
			super.redrawBuffer();
			redrawComponents();
		}

	/** Redraws this container's components. */
	public void redrawComponents()
		{
			// Enter This Component's Buffer
			FWTUIFrameBufferController.pushDrawingContext(FWTUIFrameBufferController.getUIBuffer(this));

			// Draw Components
			//***********************************
			// Render each component to this frame buffer
			drawComponents();

			// Leave Frame Buffer
			FWTUIFrameBufferController.popDrawingContext();
		}




	//  SORT COMPONENTS
	//********************************************************************
	//********************************************************************

	/** Sorts all components based on draw priority. Components with unlisted draw priorities are drawn last. 
	<br> This method should be called after all components have been added to the FWTContainer.  */
	public void sortComponents()
		{
			Collections.sort(components,FWTContainer.getComponentComparator());
		}




	//  DRAW COMPONENTS
	//********************************************************************
	//********************************************************************


	/** Render this containers components. */
	protected void drawComponents()
		{
			for (FWTComponent comp : components)
				if (comp.enabled)
					{
						comp.render();
					}
		}




	//  DISPOSE
	//********************************************************************
	//********************************************************************

	@Override
	public synchronized void dispose()
		{
			keyFocused = null;
			mouseFocused = null;
			enabled = false;
			this.clearComponents();
			FWTUIFrameBufferController.removeUIBuffer(this);
			FWTImageManager.removeBorderNinePatch(this.getID());
		}







	//  MOUSE INPUT
	//********************************************************************
	//********************************************************************

	@Override
	public boolean touchDown(int mX, int mY, int pointer, int button) throws FWTInputException
		{			
			// Reset key focus
			keyFocused = null;

			// Convert to local coordinates
			int lmX = mX - (int)dims.x;
			int lmY = mY - (int)dims.y;

			if (mouseFocused != null)
				{
					keyFocused = mouseFocused;
					if (mouseFocused.touchDown(lmX, lmY, pointer, button))
						return true;
				}

			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Touch DOWN: Container: "+this.name);

			// Set component as mouse-down component if it receives event.
			getWindowManager().setMouseDownComponent(this);

			if (inputReceiver != null)
				return inputReceiver.tryTouchDown(lmX, lmY, pointer, button);
			return false;
		}

	@Override
	public boolean touchUp(int mX, int mY, int pointer, int button, FWTComponent touchDownComponent) throws FWTInputException
		{
			// Convert to local coordinates
			int lmX = mX - (int)dims.x;
			int lmY = mY - (int)dims.y;

			// IF has touch-down receiver AND
			if (mouseFocused != null && touchDownComponent != null && 
					// is the same as the currently-focused object OR
					(mouseFocused.equals(touchDownComponent) || 
							// is a container that contains the touch-down component
							((mouseFocused instanceof FWTContainer) && ((FWTContainer)mouseFocused).existsInHierarchy(touchDownComponent))))
				{

					// Send touch-up event
					if (mouseFocused.touchUp(lmX, lmY, pointer, button, touchDownComponent))
						return true;
				}

			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Touch UP: Container: "+this.name);

			if (inputReceiver != null)
				return inputReceiver.tryTouchUp(lmX, lmY, pointer, button, touchDownComponent);
			return false;
		}

	@Override
	public boolean mouseMoved(int mX, int mY) throws FWTInputException
		{
			// Convert to local coordinates
			int lmX = mX - (int)dims.x;
			int lmY = mY - (int)dims.y;

			// Reversal through components (opposite of draw order)
			for (int cc=components.size()-1; cc>-1; cc--)
				{
					FWTComponent comp = components.get(cc);
					if (comp.enabled && comp.contains(lmX, lmY))
						{
							// IF no previous focus component
							if (mouseFocused == null)
								{
									// Enter next focus object
									mouseFocused = comp;
									comp.onEnter();
								}
							// ELSE IF not the same object
							else if (comp.getID() != mouseFocused.getID())
								{
									// Leave last focus object
									mouseFocused.onExit();
									// Enter next focus object
									mouseFocused = comp;
									comp.onEnter();
								}
							return comp.mouseMoved(lmX, lmY);
						}
				}

			// IF no component caught movement
			if (mouseFocused != null)
				{
					mouseFocused.onExit();
					mouseFocused = null;
				}		


			// Have window process movement
			if (inputReceiver != null)
				return inputReceiver.tryMouseMoved(lmX, lmY);

			return false;
		}


	@Override
	public boolean dragNDrop(int mX, int mY, int pointer, int button, FWTComponent dragComponent) throws FWTInputException
		{
			// Convert to local coordinates
			int lmX = mX - (int)dims.x;
			int lmY = mY - (int)dims.y;

			if (mouseFocused != null)
				if (mouseFocused.dragNDrop(lmX, lmY, pointer, button, dragComponent))
					return true;

			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Drag 'n Drop: Container: "+this.name);

			if (inputReceiver != null)
				return inputReceiver.tryDragNDrop(lmX, lmY, pointer, button, dragComponent);
			return false;
		}


	@Override
	public boolean touchDragged(int mX, int mY, int pointer)  throws FWTInputException
		{
			// Convert to local coordinates
			int lmX = mX - (int)dims.x;
			int lmY = mY - (int)dims.y;

			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Touch DRAGGED: Container: "+this.name);

			if (inputReceiver != null)
				return inputReceiver.tryTouchDragged(lmX, lmY, pointer);
			return false;
		}

	@Override
	public boolean scrolled(float amountX, float amountY) throws FWTInputException
		{
			for (FWTComponent comp : components)
				if (mouseFocused != null && comp.getID() == mouseFocused.getID())
					if (comp.scrolled(amountX, amountY))
						return true;

			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Scrolled: Container: "+this.name);

			if (inputReceiver != null)
				return inputReceiver.tryScrolled(amountX, amountY);
			return false;
		}

	@Override
	public void onExit() throws FWTInputException
		{
			super.onExit();

			// IF focused object
			if (mouseFocused != null)
				{
					mouseFocused.onExit();
					mouseFocused = null;
				}	
		}




	//  KEYBOARD INPUT
	//********************************************************************
	//********************************************************************


	@Override
	public boolean keyDown(int keycode)  throws FWTInputException
		{
			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Key DOWN: "+this.name);
			if (keyFocused != null)
				if (keyFocused.keyDown(keycode))
					return true;
			if (inputReceiver != null)
				return inputReceiver.tryKeyDown(keycode);
			return false;
		}

	@Override
	public boolean keyUp(int keycode) throws FWTInputException
		{
			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Key UP: "+this.name);
			if (keyFocused != null)
				if (keyFocused.keyUp(keycode))
					return true;
			if (inputReceiver != null)
				return inputReceiver.tryKeyUp(keycode);
			return false;
		}

	@Override
	public boolean keyTyped(char keychar) throws FWTInputException
		{
			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Key TYPED: "+this.name);
			if (keyFocused != null)
				if (keyFocused.keyTyped(keychar))
					return true;
			if (inputReceiver != null)
				return inputReceiver.tryKeyTyped(keychar);
			return false;
		}









	// COMPONENT COMPARATOR
	//**************************************************************************************************
	//**************************************************************************************************

	/** Returns a new comparator for sorting components by draw priority. */
	public static Comparator<FWTComponent> getComponentComparator()
		{
			return new ComponentDrawComparator();
		}

	/** A comparator for draw priority on FWTComponents. */
	public static class ComponentDrawComparator implements Comparator<FWTComponent>
		{
			// Comparison
			@Override
			public int compare(FWTComponent c1, FWTComponent c2)
				{
					return c1.getDrawPriority() - c2.getDrawPriority();
				}
		}

















}
