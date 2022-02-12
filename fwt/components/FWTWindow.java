package com.arboreantears.fwt.components;

import com.arboreantears.fwt.Direction;
import com.arboreantears.fwt.FWTController;
import com.arboreantears.fwt.FWTWindowManager;
import com.arboreantears.fwt.Fonts;
import com.arboreantears.fwt.Language;
import com.arboreantears.fwt.UIRenderer;
import com.arboreantears.fwt.XMLDataPacket;
import com.arboreantears.fwt.XMLUIReader;
import com.arboreantears.fwt.events.FWTButtonListener;
import com.arboreantears.fwt.events.FWTInputException;
import com.arboreantears.fwt.events.FWTInputReceiver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** A FWT Window to display components with a title bar. */
public class FWTWindow extends FWTContainer
{

	// VARIABLES
	// ********************************************************************
	// ********************************************************************


	@Override
	public String toString()
		{
			return "Window: "+this.name;
		}


	// Title Bar
	// ---------------------------------

	/** The title language tag of this window. */
	String title;
	/** Returns the title tag of this window. */
	public String getTitle()  { return title; }
	/** Sets the title tag of this window. Set to "" to disable. */
	public void setTitle(String title)  { this.title = title; }


	// Window Status
	// ---------------------------------

	/** 'True' if this window is ready to be closed (disabled) by the window manager.  */
	protected boolean readyToClose;
	/** Returns 'true' if this window is ready to be closed (disabled) by the window manager.  */
	public boolean isReadyToClose()  {return readyToClose;}
	/** Sets if this window is ready to be closed (disabled) by the window manager. */
	public void setReadyToClose(boolean c)  { readyToClose = c; }
	/** Set to close this window when possible. */
	public void close()  	
		{ 
			readyToClose = true; 
			readyToOpen = false; 
		}


	/** 'True' if this window is ready to be disposed and removed by the window manager. */
	protected boolean readyToDispose;
	/** Returns 'true' if this window is ready to be disposed and removed by the window manager. */
	public boolean isReadyToDispose()  { return readyToDispose; }
	/** Sets if this window is ready to be disposed and removed by the window manager. */
	public void setReadyToDispose()  {readyToDispose = true;}


	/** 'True' if this window is ready to be opened (enabled) by the window manager. */
	protected boolean readyToOpen;
	/** Returns 'True' if this window is ready to be opened (enabled) by the window manager. */
	public boolean isReadyToOpen()  { return readyToOpen; 	}
	/** Sets if this window is ready to be opened (enabled) by the window manager. */
	public void setReadyToOpen(boolean o)  { readyToOpen = o; 	}
	/** Set to open this window when possible. */
	public void open() 
		{
			readyToOpen = true;
			readyToClose = false;
		}

	/** 'True' if this window blocks all input to other windows when enabled. */
	protected boolean blocking;
	/** Returns 'True' if this window blocks all input to other windows when enabled. */
	public boolean isBlocking()  { return blocking; 	}
	/** Sets if this window blocks all input to other windows when enabled. */
	public void setBlocking(boolean block)  { blocking = block; }


	/** 'True' if this window expects all non-active windows behind it to be dimmed. */
	protected boolean dimming;
	/** 'True' if this window expects all non-active windows behind it to be dimmed. */
	public boolean isDimming()  { return dimming; 	}
	/** Sets if this window expects all non-active windows behind it to be dimmed. */
	public void setDimming(boolean dim)  { dimming = dim; }




	// Hidden windows receive updates, but do not render or generate input events.
	/** 'True' if this is hidden and not rendered. Will still receive updates but not input events. */
	protected boolean hidden;
	/** Returns 'true' if this window is hidden and if it will render.  Will still receive updates but not input events.*/
	public boolean isHidden()  { return hidden; 	}
	/** Sets if this window is hidden and whether it will render.  Will still receive updates but not input events.*/
	public void setHidden(boolean hide)  { hidden = hide; }






	// Window Elements
	// ---------------------------------

	/** This window can be extended in width. */
	boolean extensibleWidth;
	/** True if this window can be extended in width. */
	public boolean hasExtensibleWidth()  {return extensibleWidth;}
	/** Sets if this window can be extended in width. */
	public void setExtensibleWidth(boolean extensibleWidth)  	{this.extensibleWidth = extensibleWidth;}


	/** This window can be extended in height. */
	boolean extensibleHeight;
	/** 'True' if this window can be extended in height. */
	public boolean hasExtensibleHeight()  {return extensibleHeight;}
	/** Sets if this window can be extended in height. */
	public void setExtensibleHeight(boolean extensibleHeight)  	{this.extensibleHeight = extensibleHeight;}


	/** Minimum window width. */
	int minWidth;
	/** Returns the minimum window width. */
	public int getMinWidth()  	{return minWidth;}
	/** Sets the minimum window width. */
	public void setMinWidth(int minWidth) {this.minWidth = minWidth;}

	/** Minimum window height. */
	int minHeight;
	/** Returns the minimum window height. */
	public int getMinHeight()  	{return minHeight;}
	/** Sets the minimum window height. */
	public void setMinHeight(int minHeight)  {this.minHeight = minHeight;}

	/** Window layer to determine draw order by the manager. */
	int layer;
	/** Returns the window layer to determine draw order by the manager. */
	public int getLayer() {return layer;}
	/** Sets the window layer to determine draw order by the manager. */
	public void setLayer(int layer) {this.layer = layer;	}





	// Border Bar
	// ---------------------------------

	/** This window is has a thick (10 pixel) border bar enabled. */
	boolean hasBorderBar;
	/** Returns 'true' if this window has a thick (10 pixel) border bar. */
	public boolean hasBorderBar()  { return hasBorderBar; 	}
	/** Sets if this window has a thick (10 pixel) border bar. */
	public void setBorderBar(boolean borderBar)  	{ hasBorderBar = borderBar; 	}

	/** The passive (non-active) border color. */
	Color passiveBorderColor;
	/** Returns this drawable component's passive (non-active) border color. */
	public Color getPassiveBorderColor()  	{ return passiveBorderColor; 	}
	/** Sets this drawable component's passive (non-active) border color. */
	public void setPassiveBorderColor(Color bcolor)  { passiveBorderColor = bcolor; 	}



	// Close Button
	// ---------------------------------

	/** This window is able to be manually closed. */
	boolean closeable;
	/** Returns 'true' if this window is able to be manually closed. */
	public boolean isCloseable()  { return closeable; 	}
	/** Sets if this window is able to be manually closed. Adds rendered close button.  */
	public void setCloseable(boolean closeable) 
		{
			// IF now closeable
			if (closeable)
				{
					// IF has no button
					if (closeButton == null)
						{
							createCloseButton();
							this.addComponent(closeButton);
						}
					this.closeable = true;
				}
			else // ELSE remove closeable button
				{
					closeable = false;
					if (closeButton != null) 
						{closeButton.disable(); this.removeComponent(closeButton);}
					closeButton = null;
				}
		}

	/** Close button for this window. */
	protected FWTButton closeButton;



	// Window Dragging / Moving
	// ---------------------------------

	/** 'True' if this window is being moved/dragged. */
	boolean dragging;
	/** Starting X point for dragging. */
	int draggedX;
	/** Starting Y point for dragging. */
	int draggedY;

	/** 'True' if this window is allowed to be moved at all. */
	boolean isMoveAllowed = true;
	/** Returns if this window is allowed to be moved at all. */
	public boolean isMoveAllowed()  { return isMoveAllowed; 	}
	/** Sets if this window is allowed to be moved at all. */
	public void setMoveAllowed(boolean moveAllowed) 
		{ isMoveAllowed = moveAllowed; }

	/** 'True' if this window can be moved or is currently locked. */
	boolean isMovable;
	/** Returns if this window can be moved or is currently locked. */
	public boolean isMovable() 
		{ return isMovable; 	}
	/** Sets if this window can be moved or is currently locked. */
	public void setMovable(boolean moveable) 
		{ isMovable = moveable; 	}

	/** The side of the screen this window is locked to. Determines if it  stays on side when resizing.  */
	Direction lockedSide;
	/** Changes the position of the window to the given coordinates. */
	public void changePosition(float x, float y)  { dims.x = x; dims.y = y; }










	// CONSTRUCTOR
	// ********************************************************************
	// ********************************************************************


	/** Create a new Window with the given XML data packet. */
	public FWTWindow(XMLDataPacket data)
		{
			super(data);
			createInputReceiver();
		}

	/** Create a new Window with the given XML target. */
	public FWTWindow(String parent, String object)
		{
			super(parent, object);
			createInputReceiver();
		}



	//  XML DATA
	//********************************************************************
	//********************************************************************

	@Override
	public void refreshData()
		{
			if (closeButton != null) 
				{closeButton.disable(); this.removeComponent(closeButton);}
			super.refreshData();
		}
	

	@Override
	protected void setDefaults()
		{
			super.setDefaults();
			title = "";
			readyToOpen = true;
			extensibleWidth = true;
			extensibleHeight = true;
			minWidth = -1;
			minHeight = -1;
			layer = 0;
			hasBorderBar = true;
			passiveBorderColor = Color.GRAY;
			closeable = false;
			dragging = false;
			draggedX = 0;
			draggedY = 0;
			isMoveAllowed = true;
			isMovable = true;
			lockedSide = Direction.UP;

			// Windows have no drag resistance
			setDragResistance(0);

		}


	@Override
	public void applyDataParameters(XMLDataPacket data)
		{
			super.applyDataParameters(data);

			if (data != null)
				{

					// DATA
					// ----------------------------------
					// Title
					if (data.get("title") != null) 
						{
							this.title = Language.get(data.get("title"));
						}
					// Closable
					if (data.get("closeable") != null)
						{
							this.setCloseable(data.getBoolean("closeable"));
						}
					// Extensible Width
					if (data.get("extendwidth") != null)
						{
							this.extensibleWidth = data.getBoolean("extendwidth");
						}
					// Extensible Height
					if (data.get("extendheight") != null)
						{
							this.extensibleHeight = data.getBoolean("extendheight");
						}
					// Border Bar
					if (data.get("borderbar") != null)
						{
							this.hasBorderBar = data.getBoolean("borderbar");
						}
					// Passive Border Color
					if (data.get("passivebordercolor") != null)
						{
							this.passiveBorderColor = data.getColor("passivebordercolor");
						} else this.passiveBorderColor = borderColor.cpy();
					// Movable
					if (data.get("movable") != null)
						{
							this.isMovable = data.getBoolean("movable");
						}

					// Locked Side (Relative)
					String relPos = data.get("position");
					if (relPos.equals("topleft"))
						{
							this.lockedSide = Direction.WEST;
						}
					else if (relPos.equals("topright"))
						{
							this.lockedSide = Direction.EAST;
						}
					else if (relPos.equals("bottomleft"))
						{
							this.lockedSide = Direction.WEST;
						}
					else if (relPos.equals("bottomright"))
						{
							this.lockedSide = Direction.EAST;
						}
					else if (relPos.equals("topcenter"))
						{
							this.lockedSide = Direction.NORTH;
						}
					else if (relPos.equals("bottomcenter"))
						{
							this.lockedSide = Direction.SOUTH;
						}
					else if (relPos.equals("leftcenter"))
						{
							this.lockedSide = Direction.WEST;
						}
					else if (relPos.equals("rightcenter"))
						{
							this.lockedSide = Direction.EAST;
						}

					// Locked Side (Explicit)
					if (data.get("locked") != null)
						{
							String lock = data.get("locked");
							if (lock.equals("north"))
								this.lockedSide = Direction.NORTH;
							else if (lock.equals("south"))
								this.lockedSide = Direction.SOUTH;
							else if (lock.equals("east"))
								this.lockedSide = Direction.EAST;
							else if (lock.equals("west")) this.lockedSide = Direction.WEST;
						}

					// Minimum Width
					if (data.get("minwidth") != null) 
						{
							this.minWidth = data.getInt("minwidth");
						}
					// Minimum Height
					if (data.get("minheight") != null)
						{
							this.minHeight = data.getInt("minheight");
						}
				}
		}







	// RESIZE
	// ********************************************************************
	// ********************************************************************

	/** Moves this window based on the new given screen dimensions. */
	public void resize(int width, int height) 
		{
			super.resize(width, height);

			// Ensure within screen limits
			if (dims.x + dims.width > width) dims.x = width - dims.width;
			if (dims.y + dims.height > height) dims.y = height - dims.height;

			// Stay locked to the side of the screen
			switch (lockedSide)
			{
			case SOUTH: dims.y = 0; break;
			case NORTH: dims.y = height - dims.height; break;
			case WEST: dims.x = 0; break;
			case EAST: dims.x = width - dims.width; 	break;
			default: break;
			}
		}






	// DRAW
	// ********************************************************************
	// ********************************************************************

	@Override
	public void draw(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) 
		{
			// Draw Container
			super.draw(spriteBatch, shapeRenderer);

			// Draw CLOSE button
			if (closeable && closeButton != null)
				{
					closeButton.needsRedrawn = true;
					closeButton.render();

				}

			// Draw Border Bar
			// *************************************************************************
			if (hasBorderBar)
				{
					spriteBatch.begin();
					if (this.getWindowManager().getActiveWindow() != null
							&& this.getWindowManager().getActiveWindow().getID() == this.getID())
						UIRenderer.drawThickBorder(this.getID(), spriteBatch, borderColor, 0, 0,
								dims.width, dims.height);
					else
						UIRenderer.drawThickBorder(this.getID(), spriteBatch, passiveBorderColor,
								0, 0, dims.width, dims.height);
					spriteBatch.end();
				}

			// Draw Title
			// *************************************************************************
			if (title != null && !title.equals(""))
				{
					spriteBatch.begin();
					try {
					Fonts.getFont(22).setColor(Color.BLACK);
					Fonts.getFont(22).draw(spriteBatch, title, 15, dims.height - 15);
					} catch (Exception ex)
						{if (FWTWindowManager.RENDER_DEBUG_MODE) FWTController.error("FWTWindow: Unable to draw title.");}
					
					// Underline
					UIRenderer.drawHorizontalLine(spriteBatch, Color.BLACK, 15, dims.width - 50,
							dims.height - 35, 1);
					spriteBatch.end();
				}

		}






	// OPENING
	// ********************************************************************
	// ********************************************************************

	/** This method is performed when the window is enabled and opened. */
	public void opening() 
		{
			// Expected to be overridden by more complex behavior.
		}






	// CLOSING
	// ********************************************************************
	// ********************************************************************

	/** This method is performed when the window is disabled and closed. */
	public void closing() 
		{
			// Expected to be overridden by more complex behavior.
		}








	// INPUT
	// ********************************************************************
	// ********************************************************************

	/** This window has gained focus by the window manager. */
	public void gainFocus() throws FWTInputException 
		{
			if (enabled && inputReceiver != null) inputReceiver.tryGainFocus();
		}

	/** This component has lost focus by the window manager. */
	public void lostFocus() throws FWTInputException 
		{
			if (enabled && inputReceiver != null) inputReceiver.tryLostFocus();
		}

	/**
	 * Create an input receiver for this window object, in case a component
	 * does not capture the input.
	 */
	private void createInputReceiver() 
		{
			this.setInputReceiver(new FWTInputReceiver()
				{
					// MOUSE INPUT
					// =====================================

					@Override
					public boolean touchDown(int mX, int mY, int pointer, int button)
							throws FWTInputException {
								draggedX = mX;
								draggedY = mY;

								// Control + Click locks the window
								if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)
										|| Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT))
									isMovable = !isMovable;

								return true;
					}

					@Override
					public boolean touchDragged(int mX, int mY, int pointer)
							throws FWTInputException {
								// IF allowed to move and movable
								if (isMoveAllowed && isMovable)
									{
										int dragDistX = (mX - draggedX);
										int dragDistY = (mY - draggedY);

										// IF NOT locked, Can lock anywhere
										if (lockedSide == Direction.UP)
											{
												float newX = dims.x + dragDistX;
												float newY = dims.y + dragDistY;
												if (newX < 0)
													{
														newX = 0;
														lockedSide = Direction.WEST;
													}
												if (newY < 0)
													{
														newY = 0;
														lockedSide = Direction.SOUTH;
													}
												if (newX + dims.width > Gdx.graphics.getWidth())
													{
														newX = Gdx.graphics.getWidth() - dims.width;
														lockedSide = Direction.EAST;
													}
												if (newY + dims.height > Gdx.graphics.getHeight())
													{
														newY = Gdx.graphics.getHeight() - dims.height;
														lockedSide = Direction.NORTH;
													}
												changePosition(newX, newY);
											}
										// ELSE IF locked on top/bottom
										else if (lockedSide == Direction.NORTH
												|| lockedSide == Direction.SOUTH)
											{
												// IF pulled off Y axis, set free
												if (Math.abs(dragDistY) > 40) lockedSide = Direction.UP;

												// Move on X dimension
												float newX = dims.x + dragDistX;
												if (dims.x < 0)
													{
														newX = 0;
													}
												if (newX + dims.width > Gdx.graphics.getWidth())
													{
														newX = Gdx.graphics.getWidth() - dims.width;
													}
												changePosition(newX, dims.y);
											}
										// ELSE IF locked on left/right
										else if (lockedSide == Direction.WEST
												|| lockedSide == Direction.EAST)
											{
												// IF pulled off X axis, set free
												if (Math.abs(dragDistX) > 40) lockedSide = Direction.UP;

												// Move on Y dimension
												float newY = dims.y + dragDistY;
												if (newY < 0)
													{
														newY = 0;
													}
												if (newY + dims.height > Gdx.graphics.getHeight())
													{
														newY = Gdx.graphics.getHeight() - dims.height;
													}
												changePosition(dims.x, newY);
											}
									}
								return true;
					}





				});
		}






	// CLOSE BUTTON
	// ********************************************************************
	// ********************************************************************

	/** Creates a default close button for this window. */
	public void createCloseButton() 
		{
			XMLDataPacket data = null;

			// Try to read button data
			if (XMLUIReader.hasUISpecs(this.getData().get("parentfile"), "closeButton"))
				data = XMLUIReader.getUISpecs(this.getData().get("parentfile"), "closeButton");
			
			// IF not specified, use UI defaults
			if (data == null && XMLUIReader.hasUISpecs("UI_Defaults", "closeButton"))
				data = XMLUIReader.getUISpecs("UI_Defaults", "closeButton");

			// IF no existing data, use static defaults
			if (data == null)
				{data = new XMLDataPacket();}
			// Force window-based name
			data.put("name", this.name + ":closeButton");

			// Replace missing fields
			if (!data.has("width")) data.put("width", "20");
			if (!data.has("height")) data.put("height", "20");
			if (!data.has("parentwidth")) data.put("parentwidth", Integer.toString((int) dims.width));
			if (!data.has("parentheight")) data.put("parentheight", Integer.toString((int) dims.height));
			if (!data.has("position")) data.put("position", "width-32|height-32");
			if (!data.has("backgroundcolor")) data.put("backgroundcolor", "0.65|0.65|0.65|1");
			if (!data.has("highlightcolor")) data.put("highlightcolor", "0.80|0.80|0.80|1");
			if (!data.has("pressedcolor")) data.put("pressedcolor", "0.50|0.50|0.50|1");
			if (!data.has("tooltip")) data.put("tooltip", "gui_close");
			if (!data.has("backgroundtexture")) data.put("backgroundtexture", "button.9");
			if (!data.has("highlighttexture")) data.put("highlighttexture", "buttonh.9");
			if (!data.has("pressedtexture")) data.put("pressedtexture", "buttonp.9");
			if (!data.has("icontexture")) data.put("icontexture", "closebtn");
			if (!data.has("bordercolor")) data.put("bordercolor", "0|0|0|1");

			if (closeButton != null) closeButton.dispose();
			closeButton = new FWTButton(data);
			closeButton.setButtonListener(new FWTButtonListener()
				{
					@Override
					public void buttonPressed(int button) {
						close();
					}
				});
			closeButton.enable();
		}





























}
