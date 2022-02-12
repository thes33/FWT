package com.arboreantears.fwt.components;

import java.util.StringTokenizer;

import com.arboreantears.fwt.FWTColors;
import com.arboreantears.fwt.FWTController;
import com.arboreantears.fwt.FWTUIFrameBufferController;
import com.arboreantears.fwt.FWTWindowManager;
import com.arboreantears.fwt.UIRenderer;
import com.arboreantears.fwt.XMLDataPacket;
import com.arboreantears.fwt.events.FWTInputException;
import com.arboreantears.fwt.events.FWTInputReceiver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;



/** An FWTScrollPanel which allows multiple FWTComponents to be added to the panel in a predefined dimension
 *    and lists the components with a scrollable interface. Like FWTListScrollable but can add any components. */
public class FWTScrollPanel extends FWTContainer
{

	//  VARIABLES
	//********************************************************************
	//********************************************************************

	@Override
	public String toString()
		{
			return "ScrollPanel: "+this.name;
		}



	// Virtual Dimensions
	//-------------------------

	/** Virtual dimensions of this scroll-panel. X/Y indicates virtual position, W/H indicates virtual maximums.*/
	Rectangle virtualDimensions;
	/** Returns the virtual dimensions of this scroll-panel. X/Y indicates virtual position, W/H indicates virtual maximums. */
	public Rectangle getVirtualDimensions() {return virtualDimensions;}
	/** Sets the virtual dimensions of this scroll-panel. X/Y indicates virtual position, W/H indicates virtual maximums. */
	public void setVirtualDimensions(Rectangle virtDims) 
		{
			// Can't show an area smaller than the scrollable itself
			if (virtDims.width < dims.width)
				virtDims.width = dims.width;
			if (virtDims.height < dims.height)
				virtDims.height = dims.height;

			virtualDimensions = virtDims;
			needsSpacingUpdate = true;
			needsScrollUpdate = true;
		}

	/** Scrolls the scroll panel by the given amount (in pixels), positive is down/right, negative is up/left. */
	public void scroll(int amount)
		{
			// VERTICAL
			if (orientation)
				{
					int newY = (int)virtualDimensions.y - amount;
					if (newY < 0) newY = 0;
					if (newY > (virtualDimensions.height - dims.height))
						newY = (int) (virtualDimensions.height - dims.height);
					virtualDimensions.y = newY;		
				}
			// HORIZONTAL
			else
				{
					int newX = (int)virtualDimensions.x + amount;
					if (newX < 0) newX = 0;
					if (newX > (virtualDimensions.width - dims.width))
						newX = (int) (virtualDimensions.width - dims.width);
					virtualDimensions.x = newX;		
				}		

			// Update scroll position immediately
			updateScrollPosition();
			this.redraw();
		}




	// Scroll Bars
	//-------------------------

	/** 'True' if this panel is vertically oriented (false is horizontal). */
	boolean orientation;
	/** 'True' if this panel is vertically oriented  (false is horizontal). */
	public boolean isVerticallyOriented() {return orientation;}
	/** Set if this panel can be scrolled vertically (false is horizontal). */
	public void setVerticalOrientation(boolean vert){orientation = vert; this.redraw();}


	/** Size of the bar in pixels. */
	int barSize;
	/** Sets the size of the bar in pixels. */
	public void setBarSize(int size) {barSize = size;}
	/** Returns the size of the bar in pixels. */
	public int getBarSize() {return barSize;}


	/** Component for the vertical bar. */
	FWTComponent scrollBar;

	/** 'True' if this scroll-panel should show its scroll bar when required. */
	boolean showScrollBar;
	/** Set if this scroll-panel should show its scroll bar when required. */
	public void setShowScrollBar(boolean s){showScrollBar = s;}


	/** 'True' if this scroll-panel needs to show scroll bar. */
	boolean needsShowScrollBar;


	/** 'True' if this scroll panel is showing its scroll bar. */
	public boolean isShowingScrollBar() {return showScrollBar && needsShowScrollBar;}

	/** Button for the center scrolling bar. */
	FWTButton barButton;

	/** 'True' if the scroll components need a position update. */
	boolean needsScrollUpdate;




	// Component Spacing
	//-------------------------

	/** 'True' if component spacing needs to be updated on the next render cycle. */
	boolean needsSpacingUpdate;
	/** Sets component spacing to be updated on the next render cycle. */
	public void updateComponentSpacing() {needsSpacingUpdate = true;}

	/** Distance between components. */
	int gapDistance;
	/** Returns the spacing between components (in pixels). */
	public int getGapDistance() {return gapDistance;}
	/** Sets the spacing between components (in pixels) [Default: 10]. */
	public void setGapDistance(int gapDistance) {this.gapDistance = gapDistance;}


	/** Maximum size of components in the extendable direction. 0 indicates no limit (matches panel size).*/
	int maxSize;
	/** Returns the maximum size of components in the extendable direction. 0 indicates no limit (matches panel size). */
	public int getMaxComponentSize() {return maxSize;}
	/** Sets the maximum size of components in the extendable direction. 0 indicates no limit (matches panel size). */
	public void setMaxComponentSize(int maxSize) {this.maxSize = maxSize;}




	// Key Scrolling
	//----------------------------------

	/** 'True' if this scrollable can be scrolled with keyboard input. */
	boolean keyScrolling;
	/** Returns 'True' if this scrollable can be scrolled with keyboard input. */
	public boolean isKeyScrolling() {return keyScrolling;}
	/**Sets if this scrollable can be scrolled with keyboard input. */
	public void setKeyScrolling(boolean ks) {keyScrolling = ks;}


	// Wheel Scrolling
	//----------------------------------

	/** 'True' if this scrollable can be scrolled with mouse wheel input. */
	boolean wheelScrolling;
	/** Returns 'True' if this scrollable can be scrolled with mouse wheel input. */
	public boolean isWheelScrolling() {return wheelScrolling;}
	/**Sets if this scrollable can be scrolled with mouse wheel input. */
	public void setWheelScrolling(boolean ws) {wheelScrolling = ws;}









	//  CONSTRUCTOR
	//********************************************************************
	//********************************************************************

	
	/** Create a new FWTScrollPanel which allows any FWTComponent to be added to the list and the
	 *   display panel can extend indefinitely in one direction with scroll bars.*/
	public FWTScrollPanel(XMLDataPacket data)
		{
			super(data);
			createInputReceiver();
		}

	/** Create a new FWTScrollPanel which allows any FWTComponent to be added to the list and the
	 *   display panel can extend indefinitely in one direction with scroll bars.*/
	public FWTScrollPanel(String parent, String object)
		{
			super(parent,object);
			createInputReceiver();
		}



	//  XML DATA
	//********************************************************************
	//********************************************************************

	
	@Override
	protected void setDefaults()
		{
			super.setDefaults();
			virtualDimensions = new Rectangle(0,0,100,100);
			orientation = true;
			barSize = 20;
			showScrollBar = true;
			needsScrollUpdate = true;
			needsSpacingUpdate = true;
			gapDistance = 10;
			maxSize = 0;
			wheelScrolling = true;		
			
		}
	

	@Override
	public void applyDataParameters(XMLDataPacket data)
		{
			super.applyDataParameters(data);

			// Scroll Bar
			XMLDataPacket xml = new XMLDataPacket();
			xml.put("name", this.name+":"+"scrollBar");
			xml.put("position", "1|1");
			xml.put("width", "1");
			xml.put("height", "1");
			
			if (scrollBar != null) scrollBar.dispose();
			scrollBar = new FWTComponent(xml);
			scrollBar.setParent(this);
			//this.addComponent(scrollBar); // Handled separately
			scrollBar.setInputReceiver(new FWTInputReceiver()
				{
					@Override
					public boolean touchDown(int mX, int mY, int pointer, int button) throws FWTInputException
						{
							// VERTICAL
							if (orientation)
								{ 
									int page = (int)(dims.height)/2;
									if (mY-dims.y < barButton.dims.y)
										{scroll(page);}
									if (mY-dims.y > barButton.dims.y+barButton.dims.height)
										{scroll(-(page));}
									return true;
								}

							// HORIZONTAL
							else
								{
									int page = (int)(dims.width)/2;
									if (mX-dims.x < barButton.dims.x)
										{scroll(page);}
									if (mX-dims.x > barButton.dims.x+barButton.dims.width)
										{scroll(-(page));}
									return true;
								}
						}

					@Override
					public boolean touchUp(int mX, int mY, int pointer, int button, FWTComponent touchDownComponent) throws FWTInputException
						{
							return true;
						}

				});

			// Slider Bar Button
			xml = new XMLDataPacket();
			xml.put("name", this.name+":"+"barButton");
			xml.put("position", "1|1");
			xml.put("width", Integer.toString(barSize));
			xml.put("height", Integer.toString(barSize));

			if (barButton != null) barButton.dispose();
			barButton = new FWTButton(xml);
			barButton.setParent(this);
			barButton.setDragResistance(0);
			//this.addComponent(barButton);  // Handled separately
			barButton.addInputReceiver(new FWTInputReceiver()
				{
					@Override
					public boolean touchDragged(int mX, int mY, int pointer) throws FWTInputException
						{ 
							// Vertical
							if (orientation)
								{
									mY = mY - barButton.getParentOffsetY();
									float posRatio = (float)(mY-dims.y-(barButton.dims.height/2)) / (dims.height);
									int newY = (int)(virtualDimensions.height * posRatio);
									if (newY > virtualDimensions.height - dims.height) newY = (int)(virtualDimensions.height - dims.height);
									if (newY < 0) newY = 0;
									virtualDimensions.y = newY;
								}
							else // Horizontal
								{
									mX = mX - barButton.getParentOffsetX();
									float posRatio = (float)(mX-dims.x-(barButton.dims.width/2)) / (dims.width);
									int newX = (int)(virtualDimensions.width * posRatio);
									if (newX > virtualDimensions.width - dims.width) newX = (int)(virtualDimensions.width - dims.width);
									if (newX < 0) newX = 0;
									virtualDimensions.x = newX;
								}

							// Update scroll position immediately
							updateScrollPosition();
							return true; 
						}					
					@Override
					public void onEnter() throws FWTInputException 
						{ 
							scrollBar.mouseOver = true;
						}					
					@Override
					public void onExit() throws FWTInputException 
						{ 
							scrollBar.mouseOver = false;
						}

				});


			// DATA
			//----------------------------------
			try{if (data != null)
				{

					// Virtual Width
					//========================
					if (data.get("virtualwidth") != null)
						{
							this.virtualDimensions.width = data.getInt("virtualwidth");
						}
					else this.virtualDimensions.width = this.getDimensions().width;


					// Virtual Height
					//========================
					if (data.get("virtualheight") != null)
						{
							this.virtualDimensions.height = data.getInt("virtualheight");
						}
					else this.virtualDimensions.height =this.getDimensions().height;



					// Virtual Start Position
					//========================
					if (data.get("virtualstart") != null)
						{
							String vStart = data.get("virtualstart");
							StringTokenizer sT = new StringTokenizer(vStart,"|");
							// X
							String X = sT.nextToken();
							if (X.contains("left"))
								this.virtualDimensions.x = 0;
							else if (X.contains("right"))
								this.virtualDimensions.x = virtualDimensions.width-dims.width;
							else
								this.virtualDimensions.x = Integer.parseInt(X);

							// Y
							String Y = sT.nextToken();
							if (Y.contains("bottom"))
								this.virtualDimensions.y = 0;
							else if (Y.contains("top"))
								this.virtualDimensions.y = virtualDimensions.height-dims.height;
							else
								this.virtualDimensions.y = Integer.parseInt(Y);
						}
					else // Defaults to top and left
						{
							this.virtualDimensions.x = 0;
							this.virtualDimensions.y = virtualDimensions.height-dims.height;
						}



					// Scroll Bars
					//----------------------------------

					// Orientation
					//========================
					if (data.get("orientation") != null)
						{
							if (data.get("orientation").contains("horz"))
								this.orientation = false;
							else this.orientation = true;
						}

					// Maximum Component Size
					//========================
					if (data.get("maxcompsize") != null)
						{
							this.maxSize = data.getInt("maxcompsize");
						}

					// Gap Distance
					//========================
					if (data.get("gapdistance") != null)
						{
							this.gapDistance = data.getInt("gapdistance");
						}

					// Bar Size
					//========================
					if (data.get("barsize") != null)
						{
							this.barSize = data.getInt("barsize");
						}


					// Key Scrolling
					//========================
					if (data.get("keyscrolling") != null)
						{
							this.keyScrolling = data.getBoolean("keyscrolling");
						}

					// Wheel Scrolling
					//========================
					if (data.get("wheelscrolling") != null)
						{
							this.wheelScrolling = data.getBoolean("wheelscrolling");
						}

					// Show Scroll Bar
					//========================
					if (data.get("showscrollbar") != null)
						{
							this.showScrollBar = data.getBoolean("showscrollbar");
						}


					// Center Button
					//----------------------------------

					// BUTTON BORDER TEXTURE
					//========================
					if (data.get("buttonbordertexture") != null)
						{
							barButton.borderTexture = data.get("buttonbordertexture");
							barButton.getData().put("bordertexture",data.get("buttonbordertexture"));
						}

					// BUTTON BORDER COLOR
					//========================
					if (data.get("buttonbordercolor") != null)
						{
							barButton.borderColor = data.getColor("buttonbordercolor");
							barButton.getData().put("bordercolor",data.get("buttonbordercolor"));
						}

					// BUTTON BACKGROUND COLOR
					//========================
					if (data.get("buttonbackgroundcolor") != null)
						{
							barButton.backgroundColor = data.getColor("buttonbackgroundcolor");
							barButton.getData().put("backgroundcolor",data.get("buttonbackgroundcolor"));
						}

					// BUTTON HIGHLIGHT COLOR
					//========================
					if (data.get("buttonhighlightcolor") != null)
						{
							barButton.highlightColor = data.getColor("buttonhighlightcolor");
							barButton.getData().put("highlightcolor",data.get("buttonhighlightcolor"));
						}

					// BUTTON PRESSED COLOR
					//========================
					if (data.get("buttonpressedcolor") != null)
						{
							barButton.pressedColor = data.getColor("buttonpressedcolor");
							barButton.getData().put("pressedcolor",data.get("buttonpressedcolor"));
						}

					// BUTTON BACKGROUND TEXTURE
					//========================
					if (data.get("buttonbackgroundtexture") != null)
						{
							barButton.bgTexture = data.get("buttonbackgroundtexture");
							barButton.getData().put("backgroundtexture",data.get("buttonbackgroundtexture"));
						}

					// BUTTON HIGHLIGHT TEXTURE
					//========================
					if (data.get("buttonhighlighttexture") != null)
						{
							barButton.highlightTexture = data.get("buttonhighlighttexture");
							barButton.getData().put("highlighttexture",data.get("buttonhighlighttexture"));
						}

					// BUTTON PRESSED TEXTURE
					//========================
					if (data.get("buttonpressedtexture") != null)
						{
							barButton.pressedTexture = data.get("buttonpressedtexture");
							barButton.getData().put("pressedtexture",data.get("buttonpressedtexture"));
						}

					// BUTTON SCALE TEXTURE
					//========================
					if (data.get("buttontexturefill") != null)
						{
							barButton.scaleTexture = data.get("buttontexturefill").equals("stretched") ? true : false;
							barButton.getData().put("texturefill",data.get("buttontexturefill"));
						}

					// BUTTON BORDER
					//========================
					if (data.get("buttonborder") != null)
						{
							if (!data.getBoolean("buttonborder"))	
								{
									barButton.setBorderColor(FWTColors.INVISIBLE);
									barButton.setBorderTexture(null);
								}
							barButton.getData().put("border",data.get("buttonborder"));
						}



					// Bars
					//----------------------------------

					// BAR BORDER TEXTURE
					//========================
					if (data.get("barbordertexture") != null)
						{
							scrollBar.borderTexture = data.get("barbordertexture");
							scrollBar.getData().put("bordertexture",data.get("barbordertexture"));
						}

					// BAR BORDER COLOR
					//========================
					if (data.get("barbordercolor") != null)
						{
							scrollBar.borderColor = data.getColor("barbordercolor");
							scrollBar.getData().put("bordercolor",data.get("barbordercolor"));
						}

					// BAR BACKGROUND COLOR
					//========================
					if (data.get("barbackgroundcolor") != null)
						{
							scrollBar.backgroundColor = data.getColor("barbackgroundcolor");
							scrollBar.getData().put("backgroundcolor",data.get("barbackgroundcolor"));
						}

					// BAR HIGHLIGHT COLOR
					//========================
					if (data.get("barhighlightcolor") != null)
						{
							scrollBar.highlightColor = data.getColor("barhighlightcolor");
							scrollBar.getData().put("highlightcolor",data.get("barhighlightcolor"));
						}

					// BAR BACKGROUND TEXTURE
					//========================
					if (data.get("barbackgroundtexture") != null)
						{
							scrollBar.bgTexture = data.get("barbackgroundtexture");
							scrollBar.getData().put("backgroundtexture",data.get("barbackgroundtexture"));
						}

					// BAR HIGHLIGHT TEXTURE
					//========================
					if (data.get("barhighlighttexture") != null)
						{
							scrollBar.highlightTexture = data.get("barhighlighttexture");
							scrollBar.getData().put("highlighttexture",data.get("barhighlighttexture"));
						}

					// BAR SCALE TEXTURE
					//========================
					if (data.get("bartexturefill") != null)
						{
							scrollBar.scaleTexture = data.get("bartexturefill").equals("stretched") ? true : false;
							scrollBar.getData().put("texturefill",data.get("bartexturefill"));
						}

					// BAR BORDER
					//========================
					if (data.get("barborder") != null)
						{
							if (!data.getBoolean("barborder"))	
								{
									scrollBar.setBorderColor(FWTColors.INVISIBLE);
									scrollBar.setBorderTexture(null);
								}
							scrollBar.getData().put("border",data.get("barborder"));
						}
				}
			}catch(Exception ex){
				FWTController.error("Invalid XML Data for UI:ScrollPanel: '"+this.name+"'. Using defaults.");
			}

			// Update components and spacing
			needsSpacingUpdate = true;
			needsScrollUpdate = true;
		}






	//  WINDOW MANAGER
	//********************************************************************
	//********************************************************************


	@Override
	public void setWindowManager(FWTWindowManager manager)
		{
			super.setWindowManager(manager);

			barButton.setWindowManager(manager);
			scrollBar.setWindowManager(manager);
		}



	//  COMPONENT HANDLING
	//********************************************************************
	//********************************************************************

	@Override
	public synchronized void addComponent(FWTComponent component)
		{
			super.addComponent(component);
			updateComponentSpacing();		
		}

	@Override
	public synchronized boolean removeComponent(FWTComponent component)
		{
			boolean rtn = super.removeComponent(component);
			updateComponentSpacing();		
			return rtn;
		}

	@Override
	public synchronized void clearComponents()
		{
			super.clearComponents();
			updateComponentSpacing();
		}







	//  RESIZE
	//********************************************************************
	//********************************************************************

	@Override
	public void resize(int width, int height)
		{		
			super.resize(width, height);

			// No buffer changes outside of OpenGL loop
			if (Thread.currentThread().getId() == FWTController.getOpenGLThreadID())
				{
					// Ensures that buffer resizes are using virtual dimensions
					FWTUIFrameBufferController.resizeUIBuffer(this, (int)virtualDimensions.width, (int)virtualDimensions.height);
					needsResize = false;
				}
			else
				needsResize = true;

			// Update component spacing
			updateCompSpacing();
			updateScrollComponents();
			// Resize scroll components
			updateScrollPosition();
		}




	//  UPDATE
	//********************************************************************
	//********************************************************************

	@Override
	public void update()
		{
			super.update();

			if (needsSpacingUpdate)
				{
					needsSpacingUpdate = false; 
					updateCompSpacing();
					updateScrollComponents();
				}

			if (needsScrollUpdate)
				{
					needsScrollUpdate = false; 
					updateScrollPosition();
				}
		}







	//  UPDATE COMPONENT SPACING
	//********************************************************************
	//*******************************************************************

	/** Updates the spacing of the current components. */
	private void updateCompSpacing()
		{
			if (FWTWindowManager.DEBUG_MODE) FWTController.log("ScrollPanel: "+this.name+": Component Spacing Updated");

			// IF Vertical scrolling
			if (this.orientation)
				{
					// Calculate widths
					int scrollWidth = ((int)dims.width) - barSize;
					if (!showScrollBar) scrollWidth = ((int)dims.width);
					int compX = gapDistance;
					int compWidth = scrollWidth - gapDistance - gapDistance;
					if (maxSize > 0 && compWidth > maxSize) 
						{
							// Center components in available space
							compX = gapDistance + (compWidth-maxSize)/2;
							compWidth = maxSize;
						}

					// FOR each component, set relative Y
					int currentY = gapDistance;
					for (FWTComponent comp : this.getComponents())
						{
							comp.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
							currentY = currentY + (int)comp.getDimensions().height;
							comp.getDimensions().width = compWidth;
							comp.getData().put("width", Integer.toString(compWidth));
							comp.getDimensions().x = compX;
							comp.getDimensions().y = currentY;
							currentY = currentY + gapDistance;
						}

					// Calculate total required height
					Rectangle virtDims = new Rectangle();
					virtDims.height = currentY;
					virtDims.width = this.getDimensions().width;
					if (currentY < this.getDimensions().height)
						virtDims.height = this.getDimensions().height;
					virtDims.x = 0;
					virtDims.y = virtDims.height - this.getDimensions().height;
					// Set new virtual dimensions
					if (virtDims.width < dims.width)
						virtDims.width = dims.width;
					if (virtDims.height < dims.height)
						virtDims.height = dims.height;
					virtualDimensions = virtDims;

					// Recycle through each component to update absolute Y
					for (FWTComponent comp : this.getComponents())
						{
							comp.getDimensions().y = this.virtualDimensions.height - comp.getDimensions().y;
							comp.getData().put("position", (int)comp.getDimensions().x+"|"+(int)comp.getDimensions().y);
							comp.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
							comp.redraw();
						}

					// Scroll bar
					if (virtualDimensions.height > dims.height)
						this.needsShowScrollBar = true;
					else needsShowScrollBar = false;
				}


			else // ELSE horizontal scrolling
				{
					// Calculate heights
					int scrollHeight = ((int)dims.height) - barSize;
					if (!showScrollBar) scrollHeight = ((int)dims.height);
					int compY = barSize + gapDistance;
					if (!showScrollBar) compY = gapDistance;
					int compHeight = scrollHeight - gapDistance - gapDistance;
					if (maxSize > 0 && compHeight > maxSize) 
						{
							// Center components in available space
							compY = gapDistance + (compHeight-maxSize)/2;
							compHeight = maxSize;
						}

					// FOR each component, set absolute X
					int currentX = gapDistance;
					for (FWTComponent comp : this.getComponents())
						{
							comp.getDimensions().height = compHeight;
							comp.getData().put("height", Integer.toString(compHeight));
							comp.getDimensions().x = currentX;
							comp.getDimensions().y = compY;
							currentX = currentX + gapDistance;
							comp.getData().put("position", (int)comp.getDimensions().x+"|"+(int)comp.getDimensions().y);
							comp.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
							comp.redraw();
							currentX = currentX + (int)comp.getDimensions().width;
						}
					// Calculate total required width
					Rectangle virtDims = new Rectangle();
					virtDims.width = currentX;
					virtDims.height = this.getDimensions().height;
					virtDims.x = 0;
					virtDims.y = 0;

					// Can't show an area smaller than the scrollable itself
					if (virtDims.width < dims.width)
						virtDims.width = dims.width;
					if (virtDims.height < dims.height)
						virtDims.height = dims.height;
					virtualDimensions = virtDims;


					// Scroll bar
					if (virtualDimensions.width > dims.width)
						this.needsShowScrollBar = true;
					else needsShowScrollBar = false;
				}

			needsScrollUpdate = true;
			this.redraw();
			this.setResize();
		}





	//  UPDATE SCROLL COMPONENTS
	//********************************************************************
	//********************************************************************

	/** Updates the scroll components (bar and button) when the virtual area is resized. */
	private void updateScrollComponents()
		{
			if (FWTWindowManager.DEBUG_MODE) FWTController.log("ScrollPanel: "+this.name+": Scroll Components Resized");

			// IF VERTICAL
			if (orientation)
				{	
					float ratio = dims.height/virtualDimensions.height;
					int barHeight = (int) (ratio * dims.height);
					if (barHeight < barSize) barHeight = barSize;
					int barWidth = barSize;
					int startPos = (int)(dims.height - barHeight);
					barButton.setDimensions(new Rectangle(dims.x+(dims.width-barWidth),dims.y+startPos,barWidth,barHeight));
					barButton.pushDimensionsToData();
					barButton.redraw();

					scrollBar.setDimensions(new Rectangle(dims.x+(dims.width-barWidth),dims.y,barWidth,dims.height));
					scrollBar.pushDimensionsToData();
					scrollBar.redraw();
				}
			else // HORIZONTAL
				{
					float ratio = dims.width/virtualDimensions.width;
					int barWidth = (int) (ratio * dims.width);
					if (barWidth < barSize) barWidth = barSize;
					int barHeight = barSize;
					barButton.setDimensions(new Rectangle(dims.x,dims.y,barWidth,barHeight));
					barButton.pushDimensionsToData();
					barButton.redraw();

					scrollBar.setDimensions(new Rectangle(dims.x,dims.y,dims.width,barHeight));
					scrollBar.pushDimensionsToData();
					scrollBar.redraw();
				}
			this.redraw();
		}



	/** Updates the position of the scroll bar based on the current virtual X/Y coordinates. */
	private void updateScrollPosition()
		{
			//if (FWTWindowManager.DEBUG_MODE) FWTController.log("ScrollPanel: "+this.name+": Scroll Position Updated");

			// Update Bar Position
			//------------------------------------
			// IF VERTICAL
			if (orientation)
				{				
					float viewHeight = (dims.height - barButton.dims.height);
					float posRatio = virtualDimensions.y / (virtualDimensions.height - dims.height);
					int barY = (int) (posRatio * viewHeight);
					barButton.getDimensions().y = dims.y + barY;
					barButton.pushDimensionsToData();
					barButton.redraw();
				}
			else // HORIZONTAL
				{
					float viewWidth = (dims.width - barButton.dims.width);
					float posRatio = virtualDimensions.x / (virtualDimensions.width - dims.width);
					int barX = (int) (posRatio * viewWidth);
					barButton.getDimensions().x = dims.x + barX;
					barButton.pushDimensionsToData();
					barButton.redraw();
				}
			this.redraw();
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
					FWTController.error("Error updating scrollpanel: "+this.getName());
					FWTController.error(ex.getMessage());
				}

			try {
				// Resize if required
				if (needsResize)
					{FWTUIFrameBufferController.resizeUIBuffer(this, (int)virtualDimensions.width,(int)virtualDimensions.height); needsResize = false;}

				// Redraw back buffer if required
				if (needsRedrawn || isAnimating || componentsNeedRedrawn() || componentsAnimating())
					{redrawBuffer(); needsRedrawn = false;}

				SpriteBatch spriteBatch = FWTController.getSpriteBatch();

				// Draw back buffer
				FrameBuffer fb = FWTUIFrameBufferController.getUIBuffer(this);
				spriteBatch.begin();
				spriteBatch.setBlendFunction(-1, -1);
				Gdx.gl20.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE);
				spriteBatch.draw(fb.getColorBufferTexture(), dims.x,dims.y,dims.width,dims.height,
						(int)virtualDimensions.x,(int)virtualDimensions.y,(int)dims.width,(int)dims.height,
						false, true); 
				spriteBatch.end();
				spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

				// Scroll Bars
				if (isShowingScrollBar())
					{
						scrollBar.render();
						barButton.render();
					}
			}catch (Exception ex)
				{
					FWTController.error("Error rendering scroll-panel: "+this.getName());
					FWTController.error(ex.getMessage());
				}
		}




	//  COMPONENTS NEED REDRAWN
	//********************************************************************
	//********************************************************************


	@Override
	public boolean componentsNeedRedrawn()
		{
			if (isShowingScrollBar())
				{
					if (scrollBar.needsRedrawn)
						return true;
					if (barButton.needsRedrawn)
						return true;
				}

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



	//  DRAW
	//********************************************************************
	//********************************************************************


	@Override
	public void draw(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
		{

			// IF mouse not over
			if (!mouseOver)
				{

					// IF has background
					if (bgTexture == null || bgTexture.isEmpty() || !UIRenderer.hasUIImage(bgTexture))
						{
							// Draw fill color
							if (backgroundColor.a > 0f)
								{
									spriteBatch.begin();
									UIRenderer.drawRectangle(spriteBatch,backgroundColor,0,0,virtualDimensions.width,virtualDimensions.height);
									spriteBatch.end();
								}
						}

					// ELSE has a texture image
					else
						{
							spriteBatch.begin();
							UIRenderer.drawUIImage(spriteBatch, id, bgTexture, 0, 0,(int)virtualDimensions.width,(int)virtualDimensions.height, scaleTexture, !scaleTexture);
							spriteBatch.end();
						}

				}
			else // ELSE mouse is over (highlighted)
				{
					// IF has no highlighted background
					if (highlightTexture == null || highlightTexture.isEmpty() || !UIRenderer.hasUIImage(highlightTexture))
						{
							// IF has no background texture
							if (bgTexture == null || bgTexture.isEmpty() || !UIRenderer.hasUIImage(bgTexture))
								{
									// Draw fill color
									if (highlightColor.a > 0f)
										{
											spriteBatch.begin();
											UIRenderer.drawRectangle(spriteBatch,highlightColor,0,0,virtualDimensions.width,virtualDimensions.height);
											spriteBatch.end();
										}
								}

							// ELSE has a texture image
							else
								{
									spriteBatch.begin();
									UIRenderer.drawUIImage(spriteBatch, id, bgTexture, 0, 0,(int)virtualDimensions.width,(int)virtualDimensions.height, scaleTexture, !scaleTexture);
									spriteBatch.end();
								}
						}

					// ELSE has a highlighted texture image
					else
						{
							spriteBatch.begin();
							UIRenderer.drawUIImage(spriteBatch, id, highlightTexture, 0, 0,(int)virtualDimensions.width,(int)virtualDimensions.height, scaleTexture, !scaleTexture);
							spriteBatch.end();
						}
				}

			// Draw border after components
		}





	@Override
	public void redrawComponents()
		{
			// Enter This Component's Buffer
			FWTUIFrameBufferController.pushDrawingContext(FWTUIFrameBufferController.getUIBuffer(this));

			// Draw Components
			//***********************************
			// Render each component to this frame buffer
			drawComponents();

			// Draw panel's border over the components.
			SpriteBatch spriteBatch = FWTController.getSpriteBatch();
			ShapeRenderer shapeRenderer = FWTController.getShapeRenderer();
			drawBorder(spriteBatch, shapeRenderer);

			// Leave Frame Buffer
			FWTUIFrameBufferController.popDrawingContext();
		}



	public void drawBorder(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
		{
			// Draw border
			// IF has border
			if (borderTexture == null || borderTexture.isEmpty() || !UIRenderer.hasUIImage(borderTexture))
				{
					// Highlighted?
					if (!mouseOver)
						{
							// Draw border color
							if (borderColor.a > 0f)
								{
									spriteBatch.begin();
									UIRenderer.drawThinBorder(this.getID()+1L,spriteBatch,borderColor,virtualDimensions.x,virtualDimensions.y,dims.width,dims.height);
									spriteBatch.end();
								}
						}
					else
						{
							// Draw highlighted border color
							if (highlightborderColor.a > 0f)
								{
									spriteBatch.begin();
									UIRenderer.drawThinBorder(this.getID()+1L,spriteBatch,highlightborderColor,virtualDimensions.x,virtualDimensions.y,dims.width,dims.height);
									spriteBatch.end();
								}

						}
				}
			// ELSE has a texture image
			else
				{
					spriteBatch.begin();
					UIRenderer.drawUIImage(spriteBatch, id, borderTexture, (int)virtualDimensions.x,(int)virtualDimensions.y,(int)dims.width,(int)dims.height, scaleTexture, !scaleTexture);
					spriteBatch.end();
				}
		}






	//********************************************************************
	//  INPUT
	//********************************************************************

	@Override
	public boolean touchDown(int mX, int mY, int pointer, int button) throws FWTInputException
		{
			// Reset key focus
			keyFocused = null;

			// Convert to local coordinates
			int lmX = (mX - (int)dims.x);
			int lmY = (mY - (int)dims.y);

			if (mouseFocused != null)
				{
					if (mouseFocused.equals(barButton) && mouseFocused.touchDown(mX, mY, pointer, button))
						{return true;}
					else if (mouseFocused.equals(scrollBar) && mouseFocused.touchDown(mX, mY, pointer, button))
						{return true;}
				}

			// Convert to local virtual coordinates
			lmX = lmX + (int)virtualDimensions.x;
			lmY = lmY + (int)virtualDimensions.y;

			if (mouseFocused != null)
				{
					keyFocused = mouseFocused;
					if (mouseFocused.touchDown(lmX, lmY, pointer, button))
						return true;
				}

			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Touch DOWN: ScrollPanel: "+this.name);

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
			int lmX = (mX - (int)dims.x);
			int lmY = (mY - (int)dims.y);

			if (mouseFocused != null)
				{
					if (mouseFocused.equals(barButton) && mouseFocused.touchUp(mX, mY, pointer, button,touchDownComponent))
						return true;
					else if (mouseFocused.equals(scrollBar) && mouseFocused.touchUp(mX, mY, pointer, button,touchDownComponent))
						return true;
				}

			// Convert to local virtual coordinates
			lmX = lmX + (int)virtualDimensions.x;
			lmY = lmY + (int)virtualDimensions.y;

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

			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Touch UP: ScrollPanel: "+this.name);

			if (inputReceiver != null)
				return inputReceiver.tryTouchUp(lmX, lmY, pointer, button, touchDownComponent);
			return false;
		}

	@Override
	public boolean mouseMoved(int mX, int mY) throws FWTInputException
		{
			// Convert to local coordinates
			int lmX = (mX - (int)dims.x);
			int lmY = (mY - (int)dims.y);

			if (isShowingScrollBar())
				{
					if (barButton.contains(mX, mY)) 
						{
							// IF no previous focus component
							if (mouseFocused == null)
								{
									// Enter next focus object
									mouseFocused = barButton;
									barButton.onEnter();
								}
							// ELSE IF not the same object
							else if (barButton.getID() != mouseFocused.getID())
								{
									// Leave last focus object
									mouseFocused.onExit();
									// Enter next focus object
									mouseFocused = barButton;
									barButton.onEnter();
								}
							return barButton.mouseMoved(mX, mY);
						}
					if (scrollBar.contains(mX, mY)) 
						{
							// IF no previous focus component
							if (mouseFocused == null)
								{
									// Enter next focus object
									mouseFocused = scrollBar;
									scrollBar.onEnter();
								}
							// ELSE IF not the same object
							else if (scrollBar.getID() != mouseFocused.getID())
								{
									// Leave last focus object
									mouseFocused.onExit();
									// Enter next focus object
									mouseFocused = scrollBar;
									scrollBar.onEnter();
								}
							return scrollBar.mouseMoved(mX, mY);
						}
				}

			// Convert to local virtual coordinates
			lmX = lmX + (int)virtualDimensions.x;
			lmY = lmY + (int)virtualDimensions.y;

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
			// Convert to local virtual coordinates
			int lmX = (mX - (int)dims.x) + (int)virtualDimensions.x;
			int lmY = (mY - (int)dims.y) + (int)virtualDimensions.y;

			if (mouseFocused != null)
				if (mouseFocused.dragNDrop(lmX, lmY, pointer, button, dragComponent))
					return true;

			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Drag 'n Drop: ScrollPanel: "+this.name);

			if (inputReceiver != null)
				return inputReceiver.tryDragNDrop(lmX, lmY, pointer, button, dragComponent);
			return false;
		}


	@Override
	public boolean touchDragged(int mX, int mY, int pointer)  throws FWTInputException
		{
			// Convert to local coordinates
			int lmX = (mX - (int)dims.x);
			int lmY = (mY - (int)dims.y);


			if (isShowingScrollBar())
				{
					if (barButton.contains(mX, mY)) 
						return barButton.touchDragged(mX, mY, pointer);
					if (scrollBar.contains(mX, mY)) 
						return scrollBar.touchDragged(mX, mY, pointer);
				}

			// Convert to local virtual coordinates
			lmX = lmX + (int)virtualDimensions.x;
			lmY = lmY + (int)virtualDimensions.y;

			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Touch DRAGGED: ScrollPanel: "+this.name);

			if (inputReceiver != null)
				return inputReceiver.tryTouchDragged(lmX, lmY, pointer);
			return false;
		}






	//  INPUT RECEIVER
	//********************************************************************
	//********************************************************************

	/** Create an input receiver for this window object, in case a component does not capture the input. */
	private void createInputReceiver()
		{
			this.setInputReceiver(new FWTInputReceiver()
				{
					//  MOUSE INPUT
					//********************************************************************
					//********************************************************************

					@Override
					public boolean scrolled(float amountX, float amountY) throws FWTInputException 
						{
							if (wheelScrolling)
								{
									// IF VERTICAL
									if (orientation)
										{
											int page = (int)(dims.height);
											scroll(((int)amountY)*page/2);
										}
									else // HORIZONTAL
										{
											int page = (int)(dims.width);
											scroll(((int)amountY)*page/2);
										}
									return true;
								}
							return false;
						}


					//  KEYBOARD INPUT
					//********************************************************************
					//********************************************************************


					// @Override
					public boolean keyDown(int keycode) throws FWTInputException 
						{
							if (keyScrolling)
								{
									if (orientation && keycode == Keys.UP)
										{scroll(-30); return true;}
									if (orientation && keycode == Keys.DOWN)
										{scroll(30); return true;}
									if (!orientation && keycode == Keys.LEFT)
										{scroll(-30); return true;}
									if (!orientation && keycode == Keys.RIGHT)
										{scroll(30); return true;}
								}
							return false;
						}
				});
		}










	//  DISPOSE
	//********************************************************************
	//********************************************************************

	@Override
	public void dispose()
		{
			super.dispose();

			scrollBar.dispose();
			barButton.dispose();
		}










}
