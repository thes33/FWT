package com.arboreantears.fwt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.arboreantears.fwt.components.FWTComponent;
import com.arboreantears.fwt.components.FWTWindow;
import com.arboreantears.fwt.components.UIToolTipBar;
import com.arboreantears.fwt.events.FWTDragNDropEvent;
import com.arboreantears.fwt.events.FWTInputEvent;
import com.arboreantears.fwt.events.FWTInputEvent.FWTEventType;
import com.arboreantears.fwt.events.FWTKeyEvent;
import com.arboreantears.fwt.events.FWTScrollEvent;
import com.arboreantears.fwt.events.FWTTouchEvent;
import com.arboreantears.fwt.events.FWTTouchUpEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/** The FWT Window Manager for tracking and controlling UI windows. */
public class FWTWindowManager implements Runnable
{

	// VARIABLES
	// ********************************************************************
	// ********************************************************************

	/** Enables verbose output for debugging the FWT. */
	public static boolean DEBUG_MODE = false;

	/** Enables verbose output for rendering calls for debugging the FWT. 
	 * <br> VERY RESOURCE INTENSIVE! */
	public static boolean RENDER_DEBUG_MODE = false;


	/** The unique ID tag for this manager. */
	long id;
	/** Returns the unique ID tag for this manager. */
	public long getID() { return id; }
	@Override
	public  int hashCode()  {return Long.valueOf(id).hashCode();}
	@Override
	public boolean equals(Object obj)
		{
			if (obj instanceof FWTWindowManager)
				if (((FWTWindowManager)obj).getID() == this.id)
					return true;
			return false;
		}
	

	/** The unique String name for this window manager. */
	protected String name;
	/** Returns the unique String name for this window manager. */
	public String getName() {return name;}
	/** Sets the unique String name for this window manager. */
	public void setName(String nm) {name = nm;}

	
	
	/** Screen dimming color. */
	public final static Color DIM_GRAY = new Color(0f, 0f, 0f, 0.4f);
	




	// Windows
	// ---------------------

	/** The list of windows being managed by this manager. */
	ArrayList<FWTWindow> windows = new ArrayList<FWTWindow>();


	/** The list of windows ready to be removed by the window manager. */
	ArrayList<FWTWindow> disposalList = new ArrayList<FWTWindow>();


	/** Returns the window managed by this manager with the given ID. Returns null if none found. */
	public synchronized FWTWindow getWindowByID(long id)
		{
			for (FWTWindow win : windows)
				{
					if (win.getID() == id) return win;
				}
			return null;
		}

	/** Returns the window managed by this manager with the given String name. Returns null if none found.  */
	public synchronized FWTWindow getWindowByName(String name)
		{
			for (FWTWindow win : windows)
				{
					if (win.getName().equals(name)) return win;
				}
			return null;
		}

	/** Adds the given window to this window manager. Will not add an existing window.*/
	public synchronized void addWindow(FWTWindow window)
		{
			if (window != null && !windows.contains(window))
				{
					window.setLayer(windows.size());
					window.setWindowManager(this);
					if (window instanceof IGlobalKeyReceiver)
						registerGlobalKeyReceiver((IGlobalKeyReceiver)window);
					window.onAdded();
					windows.add(window);
				}
		}

	/** Removes the given window from this window manager if it exists. */
	public synchronized void removeWindow(FWTWindow window)
		{
			if (windows.contains(window))
				{
					window.onRemoved();
					if (window instanceof IGlobalKeyReceiver)
						removeGlobalKeyReceiver((IGlobalKeyReceiver)window);
					int layer = window.getLayer();
					window.setWindowManager(null);
					windows.remove(window);
					window.closing();
					window.dispose();
					for (FWTWindow win2 : windows)
						{
							// IF has layer higher than removed window
							if (win2.getLayer() > layer)
								// Decrement layer
								win2.setLayer(win2.getLayer() - 1);
						}
				}
		}

	/** Removes all windows from this window manager. */
	public synchronized void clearWindows()
		{
			Iterator<FWTWindow> itrW = windows.iterator();
			while (itrW.hasNext())
				{
					FWTWindow win = itrW.next();
					win.onRemoved();
					win.closing();
					win.setWindowManager(null);
					itrW.remove();
					win.dispose();
				}

		}

	/** Updates all windows in this window manager. */
	public synchronized void updateWindows()
		{
			Iterator<FWTWindow> itrW = windows.iterator();
			while (itrW.hasNext())
				{
					FWTWindow win = itrW.next();
					win.redraw();
				}

		}

	/** The currently active window. Receives keyboard and scroll input. */
	FWTWindow activeWindow;
	/** Returns the currently active window. Receives keyboard and scroll input. */
	public FWTWindow getActiveWindow() {return activeWindow;}

	/** The previously active window, can be used to return focus. */
	FWTWindow lastActiveWindow;
	/** Sets the last active window as the current active window (if any). */
	public void setLastActiveWindow()
		{
			if (lastActiveWindow != null)
				{
					setActiveWindow(lastActiveWindow);
				}
			lastActiveWindow = null;
		}


	/** Sets the active window and adjusts the window layers. */
	public synchronized void setActiveWindow(FWTWindow win)
		{
			// Is same as active window?
			boolean sameWin = activeWindow != null && activeWindow.equals(win);

			// IF has active window and not same, it loses focus
			try{if (activeWindow != null && !sameWin) activeWindow.lostFocus();}
			catch(Exception ex){FWTController.error("Error on Lost Focus: "+ex.getMessage());}

			// Set new active window
			FWTWindow lastWin = activeWindow;
			activeWindow = win;

			// IF has new active window AND not the same as previous
			if (activeWindow != null && !sameWin)
				{
					// Set last window
					lastActiveWindow = lastWin;
					// Gain Focus
					try{activeWindow.gainFocus();}catch(Exception ex)
						{FWTController.error("Error on Gain Focus: "+ex.getMessage());}
					// Set blocking
					if (activeWindow.isBlocking())
						blocking = true;
					// Adjust layers
					for (FWTWindow win2 : windows)
						{
							// IF has layer lower than new current window
							if (win2.getLayer() < activeWindow.getLayer())
								// Increment layer
								win2.setLayer(win2.getLayer() + 1);
						}
					activeWindow.setLayer(0);
				}
		}



	// Tool-tip Bar
	// ---------------------

	/** This window manager's instance of a tool-tip component. */
	UIToolTipBar tooltipBar = new UIToolTipBar();
	/** Returns this gamestate's tool-tip component. */
	public UIToolTipBar getTooltip() {return tooltipBar;}




	// Input Control
	// ---------------------


	/** The window that the cursor is currently over. */
	FWTWindow mouseWindow;

	/** The current component being dragged and receiving drag events. */
	FWTComponent dragComponent;
	/** Sets the current component being dragged and receiving drag events. */
	public void setDraggedComponent(FWTComponent dragComp) {dragComponent = dragComp;}

	boolean isDragging = false;

	/** Returns 'true' if the mouse is being dragged. */
	public boolean isDragging() {return isDragging;}

	/** The current component the mouse has gone down on, to ensure only it can receive the touchUp event. */
	FWTComponent mouseDownComponent;
	/** Sets the current component the mouse has gone down on, to ensure only it can receive the touchUp event. */
	public void setMouseDownComponent(FWTComponent mouseComp) {mouseDownComponent = mouseComp;}


	/** Mouse position (X) where mouse button was pressed. */
	int mouseDownX = -1;
	/** Mouse position (Y) where mouse button was pressed. */
	int mouseDownY = -1;



	/** 'True' if input is blocked by the current active window. */
	boolean blocking = false;
	/** 'True' if input is blocked by the current active window. */
	public boolean isBlocking() {return blocking;}


	/** 'True' if everything behind the current active window is dimmed. */
	public boolean isDimming() 
		{
			if (activeWindow != null) return activeWindow.isDimming();
			else return false;
		}










	// CONSTRUCTOR
	// ********************************************************************
	// ********************************************************************
	

	/** Creates a new empty window manager and starts the FWTInputThread. 
	 * DO NOT USE. Create a new window manager through FWTController.createWindowManager(). */
	protected FWTWindowManager(String name)
	{
		this();
		this.name = name;
	}

	/** Creates a new empty window manager and starts the FWTInputThread. 
	 * DO NOT USE. Create a new window manager through FWTController.createWindowManager(). */
	protected FWTWindowManager()
		{
			this.name = Long.toString(id);
			startInputThread();
		}




	// RESIZE
	// ********************************************************************
	// ********************************************************************

	/** Resize each window to ensure they are not outside the new viewable area. */
	public synchronized void resize(int width, int height)
		{
			// FOR each window
			for (FWTWindow win : windows)
				win.resize(width, height); 
		}


	/** Refreshes all windows and reloads their configurations. */
	public synchronized void refresh()
		{
			FWTController.log("FWTWindowManager: "+name+": Refreshing XML data.");
			// FOR each window
			for (FWTWindow win : windows)
				win.refreshData(); 
			resize(FWTController.getGraphicsWidth(),FWTController.getGraphicsHeight());
		}



	// RENDER
	// ********************************************************************
	// ********************************************************************

	/** Renders all the enabled windows with the active window in the top layer.  */
	public synchronized void render()
		{
			try{
				// CLOSING WINDOWS
				//-------------------
				// FOR each window
				for (FWTWindow win : windows)
					{
						// IF closing
						if (win.isReadyToClose())
							{
								win.closing();
								win.disable();
								win.setReadyToClose(false);
								if (activeWindow != null && activeWindow.getID()==win.getID())
									{
										activeWindow = null;
										blocking = false;
										setLastActiveWindow();
									}
							}

						// IF disposing
						if (win.isReadyToDispose())
							{
								disposalList.add(win);
								if (activeWindow != null && activeWindow.getID()==win.getID())
									{
										activeWindow = null;
										blocking = false;
										setLastActiveWindow();
									}
							}
					}

				// REMOVING WINDOWS
				//-------------------
				if (disposalList.size() > 0)
					{
						for (FWTWindow win : disposalList)
							// Remove window
							this.removeWindow(win);
						disposalList.clear();
					}

				// OPENING NEW WINDOWS
				//-------------------
				// FOR each window
				for (FWTWindow win : windows)
					{
						// IF opening
						if (win.isReadyToOpen())
							{
								win.enable();
								win.setReadyToOpen(false);
								win.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
								setActiveWindow(win);
								win.opening();
							}
					}

				// DRAWING ENABLED WINDOWS
				//-------------------
				// FOR each layer (starting at the back)
				for (int ii = windows.size() - 1; ii > -1; ii--)
					// FOR each window
					for (FWTWindow win : windows)
						{
							// IF on this layer
							if (win.getLayer() == ii)
								{
									// IF window enabled AND (no active window OR is not active window)
									if (win.isEnabled() && (activeWindow==null || activeWindow.getID()!=win.getID()))
										{
											// Hidden windows get updates, but aren't rendered
											if (win.isHidden())
												win.update();
											else
												// Update and Render window
												{ win.update(); win.render();}
										}
								}
						}


				// DRAWING SCREEN DIMMER
				//-------------------
				if (activeWindow != null && activeWindow.isDimming())
					{
						ShapeRenderer shape = FWTController.getShapeRenderer();
						FWTController.enableBlending();
						shape.begin(ShapeType.Filled);
						shape.setColor(DIM_GRAY);
						shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
						shape.end();
					}

				// DRAWING ACTIVE WINDOW
				//-------------------
				// IF has active window
				if (activeWindow != null && activeWindow.isEnabled())
					// Render window
					activeWindow.render();


				// DRAWING TOOL-TIP BAR
				//-------------------
				tooltipBar.render(FWTController.getSpriteBatch(),FWTController.getDelta());


			}catch(RuntimeException ex) {FWTController.error("FWTManager: Error processing FWTWindow."); FWTController.error(ex.getMessage()); ex.printStackTrace();}

		}




	// DISPOSE
	// ********************************************************************
	// ********************************************************************

	/** Dispose of this window manager and all managed windows. */
	public void dispose()
		{
			stopInputThread();
			activeWindow = null;
			mouseWindow = null;
			clearWindows();
		}











	// MOUSE INPUT
	// ********************************************************************
	// ********************************************************************

	/** Touch down on the available windows. Returns 'true' if a window accepted the event. */
	public boolean touchDown(int mX, int mY, int pointer, int button)
		{
			mouseDownX = mX;
			mouseDownY = mY;
			try
				{
					// FOR each layer (starting at the front)	
					for (int ii = 0; ii < windows.size(); ii++)
						for (FWTWindow win : windows)
							if (win.getLayer() == ii)
								if (win.isEnabled() && !win.isHidden() && win.contains(mX, mY))
									{
										// IF has NO active window OR (is NOT blocking OR is same as blocking window)
										if (activeWindow == null || (!blocking || activeWindow.getID()==win.getID()))
											{
												// Sets as active window and adjust layers
												setActiveWindow(win);
												addInputEvent(new FWTTouchEvent(win,FWTEventType.TOUCH_DOWN,mX,mY,pointer,button));
												return true;
											}
									}
					if (!blocking)
						setActiveWindow(null);
				}
			catch (Exception ex)
				{
					FWTController.error("TouchDown: " + ex.getMessage());
				}
			return false;
		}





	/** Touch up on the same touchDown component. Performs a drag'n'drop if dragging over a new component. 
	 * <br> Returns 'true' if a component accepted the mouse event. */
	public boolean touchUp(int mX, int mY, int pointer, int button)
		{
			isDragging = false;
			mouseDownX = -1;
			mouseDownY = -1;
			try
				{
					// DRAG 'N" DROP
					//-----------------------------------------
					// IF dragging a component
					if (dragComponent != null)
						{
							FWTComponent draggedComp = dragComponent;
							dragComponent = null;

							// FOR each layer (starting at the front)	
							for (int ii = 0; ii < windows.size(); ii++)
								for (FWTWindow win : windows)
									if (win.getLayer() == ii)
										if (win.isEnabled() && !win.isHidden() && win.contains(mX, mY))
											{
												// IF has NO active window OR (is NOT blocking OR is same as blocking window)
												if (activeWindow == null || (!blocking || activeWindow.getID()==win.getID()))
													{
														// Sets as active window and adjust layers
														setActiveWindow(win);
														// Send drag'n'drop to window
														addInputEvent(new FWTDragNDropEvent(win,FWTEventType.DRAG_N_DROP,mX,mY,pointer,button, draggedComp));

														// Send drag release event to the dragged component
														addInputEvent(new FWTTouchEvent(draggedComp,FWTEventType.DRAG_RELEASE,mX,mY,pointer,button));
														return true;
													}
											}

							// Send drag release event to the dragged component
							addInputEvent(new FWTTouchEvent(draggedComp,FWTEventType.DRAG_RELEASE,mX,mY,pointer,button));
						}
					dragComponent = null;


					// TOUCH UP
					//-----------------------------------------
					// Ensure has a touch-down component
					if (mouseDownComponent != null)
						{
							FWTComponent mouseComp = mouseDownComponent;
							mouseDownComponent = null;
							if (mouseComp.isEnabled())
								{
									// Add touch-up event to parent window while indicating the touch-down receiver
									if (mouseComp.getParentWindow() != null)
										addInputEvent(new FWTTouchUpEvent(mouseComp.getParentWindow(),FWTEventType.TOUCH_UP,mX,mY,pointer,button,mouseComp));
									else // ELSE is window
										addInputEvent(new FWTTouchUpEvent(mouseComp,FWTEventType.TOUCH_UP,mX,mY,pointer,button,mouseComp));
									return true;
								}
						}
				}
			catch (Exception ex)
				{
					FWTController.error("TouchUp: " + ex.getMessage());
				}
			return false;
		}

	/** Mouse moved on the available windows. Returns 'true' if a window accepted the mouse event. */
	public boolean mouseMoved(int mX, int mY)
		{
			try
				{
					// Hide Tool-tip bar
					getTooltip().hide();

					// FOR each layer (starting at the front)
					for (int ii = 0; ii < windows.size(); ii++)
						// FOR each window
						for (FWTWindow win : windows)
							{
								// IF on this layer AND is enabled AND
								// contains mouse coordinates
								if (win.getLayer() == ii && win.isEnabled() && !win.isHidden() && win.contains(mX, mY))
									{
										// IF has NO active window OR (is NOT blocking OR is same as blocking window)
										if (activeWindow == null || (!blocking || activeWindow.getID()==win.getID()))
											{
												// IF has no previous window
												if (mouseWindow == null)
													{
														// Enter window
														mouseWindow = win;
														addInputEvent(new FWTInputEvent(win,FWTEventType.ON_ENTER));
													}
												// IF not the same window as
												// previous
												else if (mouseWindow.getID() != win.getID())
													{
														// Leave last focus window
														addInputEvent(new FWTInputEvent(mouseWindow,FWTEventType.ON_EXIT));

														// Enter next focus window
														mouseWindow = win;
														addInputEvent(new FWTInputEvent(win,FWTEventType.ON_ENTER));
													}
												addInputEvent(new FWTTouchEvent(win,FWTEventType.MOUSE_MOVED,mX,mY,-1,-1));
												return true;
											}
									}
							}

					// IF no window caught movement
					if (mouseWindow != null)
						{
							addInputEvent(new FWTInputEvent(mouseWindow,FWTEventType.ON_EXIT));
							mouseWindow = null;
						}

				}
			catch (Exception ex)
				{
					FWTController.error("MouseMoved: " + ex.getMessage());
				}
			return false;
		}





	/**  Touch dragged on the available windows. Returns 'true' if a window accepted the touch event.  */
	public boolean touchDragged(int mX, int mY, int pointer)
		{
			isDragging = true;
			try
				{
					// Hide Tool-tip bar
					getTooltip().hide();

					// IF touched down on a component
					if (mouseDownComponent != null)
						{
							// Set as the drag component
							dragComponent = mouseDownComponent;
							mouseDownComponent = null;
						}

					// IF no dragged component OR not moved minimum distance, treat as moved event.
					if (dragComponent == null || 
							(dragComponent.hasDragResistance() && getAbsoluteDistance(mouseDownX, mouseDownY, mX, mY) < dragComponent.getDragResistance()))
						{
							mouseMoved(mX,mY);
						}
					else
						// ELSE has drag component
						{
							addInputEvent(new FWTTouchEvent(dragComponent,FWTEventType.TOUCH_DRAGGED,mX,mY,pointer,-1));
							// Also provide mouse move event
							mouseMoved(mX,mY);
							return true;
						}

				}
			catch (Exception ex)
				{
					FWTController.error("TouchDragged: " + ex.getMessage());
				}
			return false;
		}





	/**  Mouse scroll on the active window. Returns 'true' if a window accepted the scroll event. */
	public boolean scrolled(float amountX, float amountY)
		{
			try{
				for (FWTWindow win : windows)
					if (activeWindow != null && activeWindow.getID() == win.getID() && activeWindow.isEnabled())
						{
							addInputEvent(new FWTScrollEvent(win,FWTEventType.SCROLLED,amountX,amountY));
							return true;
						}
			}catch (Exception ex)
				{
					FWTController.error("Scrolled: " + ex.getMessage());
				}
			return false;
		}









	// KEYBOARD INPUT
	// ********************************************************************
	// ********************************************************************


	/**	Key down on the active window. Returns 'true' if a window accepted the key event. */
	public boolean keyDown(int keycode)
		{
			try
				{
					if (!blocking)
						for (IGlobalKeyReceiver gKeyReceive : globalKeyReceivers)
							if (gKeyReceive != null && gKeyReceive.globalKeyDown(keycode)) return true;

					if (activeWindow != null && activeWindow.isEnabled())
						{
							addInputEvent(new FWTKeyEvent(activeWindow,FWTEventType.KEY_DOWN,keycode));
							return true;
						}
				}
			catch (Exception ex)
				{
					FWTController.error("KeyDown: " + ex.getMessage());
				}
			return false;
		}



	/** Key up on the active window. Returns 'true' if a window accepted the key event. */
	public boolean keyUp(int keycode)
		{
			try
				{
					if (!blocking)
						for (IGlobalKeyReceiver gKeyReceive : globalKeyReceivers)
							if (gKeyReceive != null && gKeyReceive.globalKeyUp(keycode)) return true;

					if (activeWindow != null && activeWindow.isEnabled())
						{
							addInputEvent(new FWTKeyEvent(activeWindow,FWTEventType.KEY_UP,keycode));
							return true;
						}
				}
			catch (Exception ex)
				{
					FWTController.error("KeyUp: " + ex.getMessage());
				}
			return false;
		}




	/** Key typed on the active window. Returns 'true' if a window accepted the key event.  */
	public boolean keyTyped(char keychar)
		{
			try
				{
					if (!blocking)
						for (IGlobalKeyReceiver gKeyReceive : globalKeyReceivers)
							if (gKeyReceive != null && gKeyReceive.globalKeyTyped(keychar)) return true;

					if (activeWindow != null && activeWindow.isEnabled())
						{
							addInputEvent(new FWTKeyEvent(activeWindow,FWTEventType.KEY_TYPED,keychar));
							return true;
						}
				}
			catch (Exception ex)
				{
					FWTController.error("KeyTyped: " + ex.getMessage());
				}
			return false;
		}





	// GLOBAL KEY RECEIVERS
	// ********************************************************************
	// ********************************************************************

	/** List of global key receivers for processing global key events. Works on a first-come-first-serve basis. */
	ArrayList<IGlobalKeyReceiver> globalKeyReceivers = new ArrayList<IGlobalKeyReceiver>();


	/** Registers the given global key receiver to receive global key events. */
	public void registerGlobalKeyReceiver(IGlobalKeyReceiver gkeyReceive) {globalKeyReceivers.add(gkeyReceive);}


	/** Removes the given global key receiver to stop global key events. */
	public void removeGlobalKeyReceiver(IGlobalKeyReceiver gkeyReceive) {globalKeyReceivers.remove(gkeyReceive);}
















	// INPUT THREAD
	// ********************************************************************
	// ********************************************************************

	/** The FWTInputThread. */
	Thread inputThread;

	/** 'True' if the FWTInputThread should stop running. */
	private boolean stopInputThread = false;

	private BlockingQueue<FWTInputEvent> inputEvents;
	/** Adds the given input event to the queue for processing by the FWTInputThread. */
	public void addInputEvent(FWTInputEvent event){inputEvents.add(event);}


	/** Starts the FWTInputThread. */
	public void startInputThread()
		{
			if (inputThread != null)
				return;
			inputThread = new Thread(this,"FWTInputThread");
			inputThread.start();
		}

	/** Stops the FWTInputThread. */
	public void stopInputThread()
		{
			stopInputThread = true;
			inputThread = null;
		}


	@Override
	public void run()
		{
			// Create Queue
			inputEvents = new LinkedBlockingQueue<FWTInputEvent>();

			// WHILE not stopping
			while(!stopInputThread)
				{
					// IF events in queue
					if (!inputEvents.isEmpty())
						{
							// Process event
							FWTInputEvent currentEvent = inputEvents.poll();
							this.processInputEvent(currentEvent);
						}
					// Yield Thread
					Thread.yield();
				}

			// Clear Queue
			inputEvents.clear();
			inputEvents = null;
		}





	// PROCESS INPUT EVENTS
	// ********************************************************************
	// ********************************************************************

	/** Processes the given FWTInputEvent by parsing it through to the proper component triggers. */
	public void processInputEvent(FWTInputEvent event)
		{
			switch (event.type)
			{
			case MOUSE_MOVED:
			{
				FWTTouchEvent tEvent = (FWTTouchEvent) event;
				try{
					tEvent.target.mouseMoved(tEvent.mX,tEvent.mY);
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'mouse-moved' event for component: "+tEvent.target.getName());}
			} break;


			case ON_ENTER:
			{
				FWTInputEvent iEvent = (FWTInputEvent) event;
				try{
					iEvent.target.onEnter();
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'enter' event for component: "+iEvent.target.getName());}
			} break;


			case ON_EXIT:
			{
				FWTInputEvent iEvent = (FWTInputEvent) event;
				try{
					iEvent.target.onExit();
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'exit' event for component: "+iEvent.target.getName());}
			} break;


			case WINDOW_GAIN_FOCUS:
			{
				FWTInputEvent iEvent = (FWTInputEvent) event;
				try{
					((FWTWindow)iEvent.target).gainFocus();
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'gain-focus' event for component: "+iEvent.target.getName());}
			} break;


			case WINDOW_LOST_FOCUS:
			{
				FWTInputEvent iEvent = (FWTInputEvent) event;
				try{
					((FWTWindow)iEvent.target).lostFocus();
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'lost-focus' event for component: "+iEvent.target.getName());}
			} break;


			case SCROLLED:
			{
				FWTScrollEvent sEvent = (FWTScrollEvent) event;
				try{
					sEvent.target.scrolled(sEvent.amountX,sEvent.amountY);
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'scrolled' event for component: "+sEvent.target.getName());}
			} break;


			case TOUCH_UP:
			{
				FWTTouchUpEvent tEvent = (FWTTouchUpEvent) event;
				try{
					tEvent.target.touchUp(tEvent.mX,tEvent.mY,tEvent.pointer,tEvent.button,tEvent.touchDownComponent);
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'touch-up' event for component: "+tEvent.target.getName());}
			} break;


			case TOUCH_DOWN:
			{
				FWTTouchEvent tEvent = (FWTTouchEvent) event;
				try{
					tEvent.target.touchDown(tEvent.mX,tEvent.mY,tEvent.pointer,tEvent.button);
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'touch-down' event for component: "+tEvent.target.getName());}
			} break;


			case DRAG_RELEASE:
			{
				FWTTouchEvent tEvent = (FWTTouchEvent) event;
				try{
					tEvent.target.dragRelease(tEvent.mX,tEvent.mY,tEvent.pointer);
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'drag-release' event for component: "+tEvent.target.getName());}
			} break;


			case TOUCH_DRAGGED:
			{
				FWTTouchEvent tEvent = (FWTTouchEvent) event;
				try{
					tEvent.target.touchDragged(tEvent.mX,tEvent.mY,tEvent.pointer);
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'touch-dragged' event for component: "+tEvent.target.getName());}
			} break;


			case DRAG_N_DROP:
			{
				FWTDragNDropEvent dEvent = (FWTDragNDropEvent) event;
				try{
					dEvent.target.dragNDrop(dEvent.mX,dEvent.mY,dEvent.pointer,dEvent.button, dEvent.draggedComponent);
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'drag-N-drop' event for component: "+dEvent.target.getName());}
			} break;


			case KEY_DOWN:
			{
				FWTKeyEvent kEvent = (FWTKeyEvent) event;
				try{
					kEvent.target.keyDown(kEvent.keycode);
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'key-down' event for component: "+kEvent.target.getName());}
			} break;


			case KEY_UP:
			{
				FWTKeyEvent kEvent = (FWTKeyEvent) event;
				try{
					for (IGlobalKeyReceiver gKeyReceive : globalKeyReceivers) 
						if (gKeyReceive != null && gKeyReceive.globalKeyUp((char)kEvent.keycode)) return;
					kEvent.target.keyUp(kEvent.keycode);
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'key-up' event for component: "+kEvent.target.getName());}
			} break;


			case KEY_TYPED:
			{
				FWTKeyEvent kEvent = (FWTKeyEvent) event;
				try{
					for (IGlobalKeyReceiver gKeyReceive : globalKeyReceivers)
						if (gKeyReceive != null && gKeyReceive.globalKeyTyped((char)kEvent.keycode)) return;
					kEvent.target.keyTyped((char)kEvent.keycode);
				}catch(Exception ex){ex.printStackTrace(); FWTController.error("Error processing 'key-typed' event for component: "+kEvent.target.getName());}
			} break;

			default: break;
			}



		}










	// CALCULATION METHODS
	// ********************************************************************
	// ********************************************************************



	/** Calculates absolute distance between the two given points.*/
	private static int getAbsoluteDistance(int x1, int y1, int x2, int y2)
		{
			int dist = 0;

			int xDist = (x1-x2);
			int yDist = (y1-y2);

			dist = Math.max(Math.abs(xDist), Math.abs(yDist));

			return dist;
		}






















}
