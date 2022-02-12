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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;



/** An FWTScrollable container (panel) in which the dimensions of the panel 
 * are theoretically infinite within a finite viewing area.
 * Scroll bars provide access to non-viewable areas. 
 * Non-viewed areas are still drawn to the back buffer, scrolling allows a window into the back-buffer. */
public class FWTScrollable extends FWTContainer
{

	//  VARIABLES
	//********************************************************************
	//********************************************************************

	@Override
	public String toString()
		{
			return "Scrollable: "+this.name;
		}


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
			needsScrollUpdate = true;
		}

	/** Scrolls along the virtual Y dimensions. */
	public void scrollVertical(int amount)
		{
			int newY = (int)virtualDimensions.y - amount;
			if (newY < 0) newY = 0;
			if (newY > (virtualDimensions.height - dims.height))
				newY = (int) (virtualDimensions.height - dims.height);
			virtualDimensions.y = newY;	
			updateVerticalBar();
			this.redraw();
		}

	/** Sets the virtual Y dimension. */
	public void setVerticalScroll(float virtY)
		{
			if (virtY < 0) virtY = 0;
			if (virtY > virtualDimensions.height - dims.height)
				virtY = virtualDimensions.height - dims.height;
			virtualDimensions.y = virtY;

			updateVerticalBar();
			redraw();
		}


	/** Scrolls along the virtual X dimensions. */
	public void scrollHorizontal(int amount)
		{
			int newX = (int)virtualDimensions.x + amount;
			if (newX < 0) newX = 0;
			if (newX > (virtualDimensions.width - dims.width))
				newX = (int) (virtualDimensions.width - dims.width);
			virtualDimensions.x = newX;		

			updateHorizontalBar();
			redraw();
		}
	/** Sets the virtual X dimension. */
	public void setHorizontalScroll(float virtX)
		{
			if (virtX < 0) virtX = 0;
			if (virtX > virtualDimensions.width - dims.width)
				virtX = virtualDimensions.width - dims.width;
			virtualDimensions.x = virtX;

			updateHorizontalBar();
			redraw();
		}



	/** 'True' if this scroll-panel can be scrolled vertically. */
	boolean scrollVertical;
	/** 'True' if this scroll-panel can be scrolled vertically. */
	public boolean isVerticalScrolling() {return scrollVertical;}
	/** Set if this scroll-panel can be scrolled vertically. */
	public void setVerticalScrolling(boolean vert)
		{
			scrollVertical = vert;
			if (scrollVertical)
				{ scrollVBar.enable(); barVButton.enable();}
			else
				{ scrollVBar.disable(); barVButton.disable();}
		}

	/** 'True' if this scroll-panel can be scrolled horizontally. */
	boolean scrollHorizontal;
	/** 'True' if this scroll-panel can be scrolled horizontally. */
	public boolean isHorizontalScrolling() {return scrollHorizontal;}
	/** Set if this scroll-panel can be scrolled horizontally. */
	public void setHorizontalScrolling(boolean horz)
		{
			scrollHorizontal = horz;
			if (scrollHorizontal)
				{ scrollHBar.enable(); barHButton.enable();}
			else
				{ scrollHBar.disable(); barHButton.disable();}
		}


	/** Size of the bar in pixels. */
	int barSize;
	/** Sets the size of the bar in pixels. */
	public void setBarSize(int size) {barSize = size;}
	/** Returns the size of the bar in pixels. */
	public int getBarSize() {return barSize;}



	/** 'True' if the scroll components need a position update. */
	boolean needsScrollUpdate;

	// Vertical Bar
	//----------------------------------

	/** Component for the vertical bar. */
	FWTComponent scrollVBar;

	/** 'True' if this scroll-panel should show its vertical bar. */
	boolean showVerticalBar;
	/** 'True' if this scroll-panel should show its vertical bar. */
	public boolean isShowingVerticalBar() {return showVerticalBar;}
	/** Set if this scroll-panel should show its vertical bar. */
	public void setShowVerticalBar(boolean vert){showVerticalBar = vert;}

	/** Button for the center vertical scrolling bar. */
	FWTButton barVButton;



	// Horizontal Bar
	//----------------------------------

	/** Component for the horizontal bar. */
	FWTComponent scrollHBar;

	/** 'True' if this scroll-panel should show its horizontal bar. */
	boolean showHorizontalBar;
	/** 'True' if this scroll-panel should show its horizontal bar. */
	public boolean isShowingHorizontalBar() {return showHorizontalBar;}
	/** Set if this scroll-panel should show its horizontal bar. */
	public void setShowHorizontalBar(boolean vert){showHorizontalBar = vert;}

	/** Button for the center horizontal scrolling bar. */
	FWTButton barHButton;





	// Key Scrolling
	//----------------------------------

	/** 'True' if this scrollable can be scrolled with keyboard input. */
	boolean keyScrolling;
	/** Returns 'True' if this scrollable can be scrolled with keyboard input. */
	public boolean isKeyScrolling() {return keyScrolling;}
	/**Sets if this scrollable can be scrolled with keyboard input. */
	public void setKeyScrolling(boolean ks) {keyScrolling = ks;}





	@Override
	public void setWindowManager(FWTWindowManager manager)
		{
			super.setWindowManager(manager);

			barVButton.setWindowManager(manager);
			scrollVBar.setWindowManager(manager);
			scrollHBar.setWindowManager(manager);
			barHButton.setWindowManager(manager);
		}














	//  CONSTRUCTOR
	//********************************************************************
	//********************************************************************

	/** Create a new FWTScrollable. */
	public FWTScrollable(XMLDataPacket data)
		{
			super(data);
			createInputReceiver();
		}

	/** Create a new FWTScrollable. */
	public FWTScrollable(String parent, String object)
		{
			super(parent, object);
			createInputReceiver();
		}



	//  XML DATA
	//********************************************************************
	//********************************************************************

	@Override
	protected void setDefaults()
		{
			super.setDefaults();
			barSize = 20;
			needsScrollUpdate = true;			
		}


	@Override
	public void applyDataParameters(XMLDataPacket data)
		{
			super.applyDataParameters(data);

			// Set default values to a matched-size panel.
			virtualDimensions = new Rectangle(0,0,100,100);
			virtualDimensions.width = dims.width;
			virtualDimensions.height = dims.height;

			initializeScrollBars();

			try{if (data != null)
				{
					// DATA
					//----------------------------------
					// Vertical Scrolling
					//========================
					if (data.get("verticalscroll") != null)
						{
							this.scrollVertical = data.getBoolean("verticalscroll");
						}

					// Horizontal Scrolling
					//========================
					if (data.get("horizontalscroll") != null)
						{
							this.scrollHorizontal = data.getBoolean("horizontalscroll");
						}

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



					// Scroll Bars
					//----------------------------------


					// Vertical Scroll Bar
					//========================
					if (data.get("showverticalscroll") != null)
						{
							this.showVerticalBar = data.getBoolean("showverticalscroll");
						}

					// Horizontal Scroll Bar
					//========================
					if (data.get("showhorizontalscroll") != null)
						{
							this.showHorizontalBar = data.getBoolean("showhorizontalscroll");
						}

					// BAR SIZE
					//========================
					if (data.get("barsize") != null)
						{
							this.barSize = data.getInt("barsize");
						}

					// KEY SCROLLING
					//========================
					if (data.get("keyscrolling") != null)
						{
							this.keyScrolling = data.getBoolean("keyscrolling");
						}



					// Center Buttons
					//----------------------------------

					// BUTTON BORDER TEXTURE
					//========================
					if (data.get("buttonbordertexture") != null)
						{
							barVButton.borderTexture = data.get("buttonbordertexture");
							barHButton.borderTexture = data.get("buttonbordertexture");
						}

					// BUTTON BORDER COLOR
					//========================
					if (data.get("buttonbordercolor") != null)
						{
							barVButton.borderColor = data.getColor("buttonbordercolor");
							barHButton.borderColor = data.getColor("buttonbordercolor");
						}

					// BUTTON BACKGROUND COLOR
					//========================
					if (data.get("buttonbackgroundcolor") != null)
						{
							barVButton.backgroundColor = data.getColor("buttonbackgroundcolor");
							barHButton.backgroundColor = data.getColor("buttonbackgroundcolor");
						}

					// BUTTON HIGHLIGHT COLOR
					//========================
					if (data.get("buttonhighlightcolor") != null)
						{
							barVButton.highlightColor = data.getColor("buttonhighlightcolor");
							barHButton.highlightColor = data.getColor("buttonhighlightcolor");
						}

					// BUTTON PRESSED COLOR
					//========================
					if (data.get("buttonpressedcolor") != null)
						{
							barVButton.pressedColor = data.getColor("buttonpressedcolor");
							barHButton.pressedColor = data.getColor("buttonpressedcolor");
						}

					// BUTTON BACKGROUND TEXTURE
					//========================
					if (data.get("buttonbackgroundtexture") != null)
						{
							barVButton.bgTexture = data.get("buttonbackgroundtexture");
							barHButton.bgTexture = data.get("buttonbackgroundtexture");
						}

					// BUTTON HIGHLIGHT TEXTURE
					//========================
					if (data.get("buttonhighlighttexture") != null)
						{
							barVButton.highlightTexture = data.get("buttonhighlighttexture");
							barHButton.highlightTexture = data.get("buttonhighlighttexture");
						}

					// BUTTON PRESSED TEXTURE
					//========================
					if (data.get("buttonpressedtexture") != null)
						{
							barVButton.pressedTexture = data.get("buttonpressedtexture");
							barHButton.pressedTexture = data.get("buttonpressedtexture");
						}

					// BUTTON SCALE TEXTURE
					//========================
					if (data.get("buttontexturefill") != null)
						{
							barVButton.scaleTexture = data.get("buttontexturefill").equals("stretched") ? true : false;
							barHButton.scaleTexture = data.get("buttontexturefill").equals("stretched") ? true : false;
						}


					// BUTTON BORDER
					//========================
					if (data.get("buttonborder") != null)
						{
							if (!data.getBoolean("buttonborder"))	
								{
									barVButton.setBorderColor(FWTColors.INVISIBLE);
									barHButton.setBorderColor(FWTColors.INVISIBLE);
									barVButton.setBorderTexture(null);
									barHButton.setBorderTexture(null);
								}
						}



					// Bars
					//----------------------------------

					// BAR BORDER TEXTURE
					//========================
					if (data.get("barbordertexture") != null)
						{
							scrollHBar.borderTexture = data.get("barbordertexture");
							scrollVBar.borderTexture = data.get("barbordertexture");
						}

					// BAR BORDER COLOR
					//========================
					if (data.get("barbordercolor") != null)
						{
							scrollHBar.borderColor = data.getColor("barbordercolor");
							scrollVBar.borderColor = data.getColor("barbordercolor");
						}

					// BAR BACKGROUND COLOR
					//========================
					if (data.get("barbackgroundcolor") != null)
						{
							scrollHBar.backgroundColor = data.getColor("barbackgroundcolor");
							scrollVBar.backgroundColor = data.getColor("barbackgroundcolor");
						}

					// BAR HIGHLIGHT COLOR
					//========================
					if (data.get("barhighlightcolor") != null)
						{
							scrollHBar.highlightColor = data.getColor("barhighlightcolor");
							scrollVBar.highlightColor = data.getColor("barhighlightcolor");
						}

					// BAR BACKGROUND TEXTURE
					//========================
					if (data.get("barbackgroundtexture") != null)
						{
							scrollHBar.bgTexture = data.get("barbackgroundtexture");
							scrollVBar.bgTexture = data.get("barbackgroundtexture");
						}

					// BAR HIGHLIGHT TEXTURE
					//========================
					if (data.get("barhighlighttexture") != null)
						{
							scrollHBar.highlightTexture = data.get("barhighlighttexture");
							scrollVBar.highlightTexture = data.get("barhighlighttexture");
						}

					// BAR SCALE TEXTURE
					//========================
					if (data.get("bartexturefill") != null)
						{
							scrollHBar.scaleTexture = data.get("bartexturefill").equals("stretched") ? true : false;
							scrollVBar.scaleTexture = data.get("bartexturefill").equals("stretched") ? true : false;
						}

					// BAR BORDER
					//========================
					if (data.get("barborder") != null)
						{
							if (!data.getBoolean("barborder"))	
								{
									scrollVBar.setBorderColor(FWTColors.INVISIBLE);
									scrollHBar.setBorderColor(FWTColors.INVISIBLE);
									scrollVBar.setBorderTexture(null);
									scrollHBar.setBorderTexture(null);
								}
						}
				}
			}catch(Exception ex){
				FWTController.error("Invalid XML Data for UI:Scrollable: '"+this.name+"'. Using defaults.");
			}	
		}






	//  RESIZE
	//********************************************************************
	//********************************************************************

	/** Resizes this scroll-panel and its components based on the given screen dimensions. */
	public void resize(int width, int height)
		{
			super.resize(width, height);

			// Check virtual dimensions
			if (virtualDimensions.width < dims.width)
				virtualDimensions.width = dims.width;
			if (virtualDimensions.height < dims.height)
				virtualDimensions.height = dims.height;

			// No buffer changes outside of OpenGL loop
			if (Thread.currentThread().getId() == FWTController.getOpenGLThreadID())
				{
					FWTUIFrameBufferController.resizeUIBuffer(this, (int)virtualDimensions.width, (int)virtualDimensions.height);
					needsResize = false;
				}
			else
				needsResize = true;

			resizeScrollComponents();
			if (showHorizontalBar) updateHorizontalBar();
			if (showVerticalBar) updateVerticalBar();
		}





	//  RESIZE SCROLL COMPONENTS
	//********************************************************************
	//**************************************

	/** Resizes the scroll bar components. */
	public void resizeScrollComponents()
		{
			// VERTICAL BAR
			//------------------------
			if (dims.height <= virtualDimensions.height)
				{
					setVerticalScrolling(true);
					float ratio = dims.height/virtualDimensions.height;
					int barHeight = (int) (ratio * dims.height);
					if (barHeight < barSize) barHeight = barSize;
					int barWidth = barSize;
					int startPos = (int)(dims.height - barHeight);
					barVButton.setDimensions(new Rectangle(dims.x+(dims.width-barWidth),dims.y+startPos,barWidth,barHeight));
					barVButton.pushDimensionsToData();
					barVButton.setResize();
					barVButton.redraw();

					scrollVBar.setDimensions(new Rectangle(dims.x+(dims.width-barWidth),dims.y,barWidth,dims.height));
					scrollVBar.pushDimensionsToData();
					scrollVBar.setResize();
					scrollVBar.redraw();
				}
			else setVerticalScrolling(false);

			// HORIZONTAL BAR
			//------------------------
			if (dims.width <= virtualDimensions.width)
				{
					setHorizontalScrolling(true);
					float ratio = dims.width/virtualDimensions.width;
					int barWidth = (int) (ratio * dims.width);
					if (barWidth < barSize) barWidth = barSize;
					int barHeight = barSize;
					barHButton.setDimensions(new Rectangle(dims.x,dims.y,barWidth,barHeight));
					barHButton.pushDimensionsToData();
					barHButton.setResize();
					barHButton.redraw();

					scrollHBar.setDimensions(new Rectangle(dims.x,dims.y,dims.width,barHeight));
					scrollHBar.pushDimensionsToData();
					scrollHBar.setResize();
					scrollHBar.redraw();
				}
			else setHorizontalScrolling(false);


			this.redraw();
		}









	//  UPDATE SCROLL BAR POSITIONS
	//********************************************************************
	//********************************************************************


	/** Updates the position of the vertical bar. */
	public void updateVerticalBar()
		{			
			float viewHeight = (dims.height - barVButton.dims.height);
			float posRatio = virtualDimensions.y / (virtualDimensions.height - dims.height);
			int barY = (int) (posRatio * viewHeight);
			barVButton.getDimensions().y = dims.y + barY;
			barVButton.pushDimensionsToData();
			barVButton.redraw();
			this.redraw();
		}


	/** Updates the position of the horizontal bar. */
	public void updateHorizontalBar()
		{
			float viewWidth = (dims.width - barHButton.dims.width);
			float posRatio = virtualDimensions.x / (virtualDimensions.width - dims.width);
			int barX = (int) (posRatio * viewWidth);
			barHButton.getDimensions().x = dims.x + barX;
			barHButton.pushDimensionsToData();
			barHButton.redraw();
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
					FWTController.error("Error updating scrollable: "+this.getName());
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
				spriteBatch.draw(fb.getColorBufferTexture(), dims.x,dims.y,dims.width,dims.height,
						(int)virtualDimensions.x,(int)virtualDimensions.y,(int)dims.width,(int)dims.height,
						false, true); 
				spriteBatch.end();

				if (showHorizontalBar)
					{
						scrollHBar.render();
						barHButton.render();
					}
				if (showVerticalBar)
					{
						scrollVBar.render();
						barVButton.render();
					}
			}catch (Exception ex)
				{
					FWTController.error("Error rendering scrollable: "+this.getName());
					FWTController.error(ex.getMessage());
				}
		}



	//  COMPONENTS NEED REDRAWN
	//********************************************************************
	//********************************************************************


	@Override
	public boolean componentsNeedRedrawn()
		{
			if (showHorizontalBar)
				{
					if (scrollHBar.needsRedrawn)
						return true;
					if (barHButton.needsRedrawn)
						return true;
				}
			if (showVerticalBar)
				{
					if (scrollVBar.needsRedrawn)
						return true;
					if (barVButton.needsRedrawn)
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

			// Border drawn after components.
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




	//  DISPOSE
	//********************************************************************
	//********************************************************************

	@Override
	public void dispose()
		{
			super.dispose();

			scrollVBar.dispose();
			barVButton.dispose();
			scrollHBar.dispose();
			barHButton.dispose();
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
					if (mouseFocused.equals(barHButton) && mouseFocused.touchDown(mX, mY, pointer, button))
						{return true;}
					else if (mouseFocused.equals(barVButton) && mouseFocused.touchDown(mX, mY, pointer, button))
						{return true;}
					else if (mouseFocused.equals(scrollHBar) && mouseFocused.touchDown(mX, mY, pointer, button))
						{return true;}
					else if (mouseFocused.equals(scrollVBar) && mouseFocused.touchDown(mX, mY, pointer, button))
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

			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Touch DOWN: Scrollable: "+this.name);

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
					if (mouseFocused.equals(barHButton) && mouseFocused.touchUp(mX, mY, pointer, button,touchDownComponent))
						return true;
					else if (mouseFocused.equals(barVButton) && mouseFocused.touchUp(mX, mY, pointer, button,touchDownComponent))
						return true;
					else if (mouseFocused.equals(scrollHBar) && mouseFocused.touchUp(mX, mY, pointer, button,touchDownComponent))
						return true;
					else if (mouseFocused.equals(scrollVBar) && mouseFocused.touchUp(mX, mY, pointer, button,touchDownComponent))
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

			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Touch UP: Scrollable: "+this.name);

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

			if (barVButton.contains(mX, mY)) 
				{
					// IF no previous focus component
					if (mouseFocused == null)
						{
							// Enter next focus object
							mouseFocused = barVButton;
							barVButton.onEnter();
						}
					// ELSE IF not the same object
					else if (barVButton.getID() != mouseFocused.getID())
						{
							// Leave last focus object
							mouseFocused.onExit();
							// Enter next focus object
							mouseFocused = barVButton;
							barVButton.onEnter();
						}
					return barVButton.mouseMoved(mX, mY);
				}
			if (barHButton.contains(mX, mY)) 
				{
					// IF no previous focus component
					if (mouseFocused == null)
						{
							// Enter next focus object
							mouseFocused = barHButton;
							barHButton.onEnter();
						}
					// ELSE IF not the same object
					else if (barHButton.getID() != mouseFocused.getID())
						{
							// Leave last focus object
							mouseFocused.onExit();
							// Enter next focus object
							mouseFocused = barHButton;
							barHButton.onEnter();
						}
					return barHButton.mouseMoved(mX, mY);
				}
			if (scrollVBar.contains(mX, mY)) 
				{
					// IF no previous focus component
					if (mouseFocused == null)
						{
							// Enter next focus object
							mouseFocused = scrollVBar;
							scrollVBar.onEnter();
						}
					// ELSE IF not the same object
					else if (scrollVBar.getID() != mouseFocused.getID())
						{
							// Leave last focus object
							mouseFocused.onExit();
							// Enter next focus object
							mouseFocused = scrollVBar;
							scrollVBar.onEnter();
						}
					return scrollVBar.mouseMoved(mX, mY);
				}
			if (scrollHBar.contains(mX, mY)) 
				{
					// IF no previous focus component
					if (mouseFocused == null)
						{
							// Enter next focus object
							mouseFocused = scrollHBar;
							scrollHBar.onEnter();
						}
					// ELSE IF not the same object
					else if (scrollHBar.getID() != mouseFocused.getID())
						{
							// Leave last focus object
							mouseFocused.onExit();
							// Enter next focus object
							mouseFocused = scrollHBar;
							scrollHBar.onEnter();
						}
					return scrollHBar.mouseMoved(mX, mY);
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

			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Drag 'n Drop: Scrollable: "+this.name);

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

			if (barVButton.contains(mX, mY)) 
				return barVButton.touchDragged(mX, mY, pointer);
			if (barHButton.contains(mX, mY)) 
				return barHButton.touchDragged(mX, mY, pointer);
			if (scrollVBar.contains(mX, mY)) 
				return scrollVBar.touchDragged(mX, mY, pointer);
			if (scrollHBar.contains(mX, mY)) 
				return scrollHBar.touchDragged(mX, mY, pointer);

			// Convert to local virtual coordinates
			lmX = lmX + (int)virtualDimensions.x;
			lmY = lmY + (int)virtualDimensions.y;

			if (FWTWindowManager.DEBUG_MODE) FWTController.log("Touch DRAGGED: Scrollable: "+this.name);

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
							int page = (int)(dims.height);
							scrollVertical(((int)amountY)*page/2);
							return true;
						}



					//  KEYBOARD INPUT
					//********************************************************************
					//********************************************************************


					// @Override
					public boolean keyDown(int keycode) throws FWTInputException 
						{
							if (keyScrolling)
								{
									if (showVerticalBar)
										{
											if (keycode == Keys.DOWN)
												{scrollVertical(30); return true;}
											if (keycode == Keys.UP)
												{scrollVertical(-30); return true;}
										}
									if (showHorizontalBar)
										{
											if (keycode == Keys.LEFT)
												{scrollHorizontal(-30); return true;}
											if (keycode == Keys.RIGHT)
												{scrollHorizontal(30); return true;}
										}
								}
							return false;
						}

				});
		}








	/** Creates and initializes the scroll bar buttons. */
	protected void initializeScrollBars()
		{


			// Horizontal Bar
			//----------------------------------
			XMLDataPacket xml = new XMLDataPacket();
			xml.put("name", this.name+":"+"scrollHBar");
			xml.put("position", "1|1");
			xml.put("width", "1");
			xml.put("height", "1");

			if (scrollHBar != null) scrollHBar.dispose();

			scrollHBar = new FWTComponent(xml);
			scrollHBar.setParent(this);
			scrollHBar.setInputReceiver(new FWTInputReceiver(){
				@Override
				public boolean touchDown(int mX, int mY, int pointer, int button) throws FWTInputException
					{
						int page = (int)(dims.width)/2;
						if (mX-dims.x < barHButton.dims.x)
							{scrollHorizontal(page);}
						if (mX-dims.x > barHButton.dims.x+barHButton.dims.width)
							{scrollHorizontal(-(page));}
						return true;
					}

				@Override
				public boolean touchUp(int mX, int mY, int pointer, int button, FWTComponent touchDownComponent) throws FWTInputException
					{
						return true;
					}
			});
			if (!showHorizontalBar)
				scrollHBar.disable();
			//----------------------------------


			// Horizontal Center Button
			//----------------------------------
			xml = new XMLDataPacket();
			xml.put("name", this.name+":"+"barHButton");
			xml.put("position", "1|1");
			xml.put("width", Integer.toString(barSize));
			xml.put("height", Integer.toString(barSize));

			if (barHButton != null) barHButton.dispose();
			barHButton = new FWTButton(xml);
			barHButton.setParent(this);
			barHButton.setDragResistance(0);
			barHButton.addInputReceiver(new FWTInputReceiver(){
				@Override
				public boolean touchDragged(int mX, int mY, int pointer) throws FWTInputException
					{
						mX = mX - barHButton.getParentOffsetX();
						float posRatio = (float)(mX-dims.x-(barHButton.dims.width/2)) / (dims.width);
						int newX = (int)(virtualDimensions.width * posRatio);
						if (newX > virtualDimensions.width - dims.width) newX = (int)(virtualDimensions.width - dims.width);
						if (newX < 0) newX = 0;
						virtualDimensions.x = newX;
						updateHorizontalBar();
						return true; 
					}				
				@Override
				public void onEnter() throws FWTInputException  {scrollHBar.mouseOver = true;}					
				@Override
				public void onExit() throws FWTInputException  {scrollHBar.mouseOver = false;}
			});
			if (!showHorizontalBar)
				barHButton.disable();
			//----------------------------------



			// Vertical Bar
			//----------------------------------
			xml = new XMLDataPacket();
			xml.put("name", this.name+":"+"scrollVBar");
			xml.put("position", "1|1");
			xml.put("width", "1");
			xml.put("height", "1");

			if (scrollVBar != null) scrollVBar.dispose();
			scrollVBar = new FWTComponent(xml);
			scrollVBar.setParent(this);
			scrollVBar.setInputReceiver(new FWTInputReceiver(){
				@Override
				public boolean touchDown(int mX, int mY, int pointer, int button) throws FWTInputException
					{
						int page = (int)(dims.height)/2;
						if (mY-dims.y < barVButton.dims.y)
							{scrollVertical(page);}
						if (mY-dims.y > barVButton.dims.y+barVButton.dims.height)
							{scrollVertical(-(page));}
						return true;
					}
			});
			if (!showVerticalBar)
				scrollVBar.disable();
			//----------------------------------


			// Vertical Center Button
			//----------------------------------
			xml = new XMLDataPacket();
			xml.put("name", this.name+":"+"barVButton");
			xml.put("position", "1|1");
			xml.put("width", Integer.toString(barSize));
			xml.put("height", Integer.toString(barSize));

			if (barVButton != null) barVButton.dispose();
			barVButton = new FWTButton(xml);
			barVButton.setParent(this);
			barVButton.setDragResistance(0);
			barVButton.addInputReceiver(new FWTInputReceiver(){
				@Override
				public boolean touchDragged(int mX, int mY, int pointer) throws FWTInputException
					{
						mY = mY - barVButton.getParentOffsetY();
						float posRatio = (float)(mY-dims.y-(barVButton.dims.height/2)) / (dims.height);
						int newY = (int)(virtualDimensions.height * posRatio);
						if (newY > virtualDimensions.height - dims.height) newY = (int)(virtualDimensions.height - dims.height);
						if (newY < 0) newY = 0;
						virtualDimensions.y = newY;
						updateVerticalBar();
						return true; 
					}
			});
			if (!showVerticalBar)
				barVButton.disable();
			//----------------------------------
			resizeScrollComponents();
		}











}
