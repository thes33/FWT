package com.arboreantears.fwt.components;

import java.util.Map.Entry;
import java.util.StringTokenizer;

import com.arboreantears.fwt.FWTColors;
import com.arboreantears.fwt.FWTController;
import com.arboreantears.fwt.FWTImageManager;
import com.arboreantears.fwt.FWTUIFrameBufferController;
import com.arboreantears.fwt.FWTWindowManager;
import com.arboreantears.fwt.Language;
import com.arboreantears.fwt.UIRenderer;
import com.arboreantears.fwt.XMLDataPacket;
import com.arboreantears.fwt.XMLUIReader;
import com.arboreantears.fwt.events.FWTInputException;
import com.arboreantears.fwt.events.FWTInputReceiver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;


/** Drawable root of all FWT graphical UI objects. Each component is backed by a frame buffer. */
public class FWTComponent
{

	//  VARIABLES
	//********************************************************************
	//********************************************************************

	// Basics
	//---------------------------

	/** The unique ID tag for this instance of the drawable component. */
	protected long id;
	/** Returns the unique ID tag for this instance of the drawable component. */
	public long getID() {return id;}

	@Override
	public  int hashCode()  {return Long.valueOf(id).hashCode();}
	@Override
	public boolean equals(Object obj)
		{
			if (obj instanceof FWTComponent)
				if (((FWTComponent)obj).getID() == this.id)
					return true;
			return false;
		}


	/** The unique String name for this drawable component. */
	protected String name;
	/** Returns the unique String name for this drawable component. */
	public String getName() {return name;}
	/** Sets the unique String name for this drawable component. */
	public void setName(String nm) {name = nm;}


	@Override
	public String toString()
		{
			return "Component: "+this.name;
		}




	// Content Management
	//---------------------------


	/** Reference to this component's window manager (if any). */
	protected FWTWindowManager manager;
	/** Returns this component's window manager (if any). */
	public FWTWindowManager getWindowManager() {return manager;}
	/** Sets this component's window manager (if any). */
	public void setWindowManager(FWTWindowManager manager) {this.manager = manager;}


	/** Reference to this component's parent container (if any). */
	protected FWTContainer parent;
	/** Returns this component's parent container (if any). */
	public FWTContainer getParent() {return parent;}
	/** Sets this component's parent container (set to null if none). */
	public void setParent(FWTContainer parent) {this.parent = parent;}
	/** Returns this component's top-level parent window. */
	public FWTWindow getParentWindow()
		{
			if (parent != null)
				{
					if (parent instanceof FWTWindow)
						return (FWTWindow)parent;
					else
						return parent.getParentWindow();
				}
			return null;
		}
	/** Returns the total offset to X from all parent windows. */ 
	public int getParentOffsetX()
		{
			int offset = 0;
			if (parent != null)
				{
					if (parent instanceof FWTWindow)
						offset = offset + (int)parent.getDimensions().x;
					else
						offset = offset + (int)parent.getDimensions().x + parent.getParentOffsetX();
				}
			return offset;
		}
	/** Returns the total offset to Y from all parent windows. */ 
	public int getParentOffsetY()
		{
			int offset = 0;
			if (parent != null)
				{
					if (parent instanceof FWTWindow)
						offset = offset + (int)parent.getDimensions().y;
					else
						offset = offset + (int)parent.getDimensions().y + parent.getParentOffsetY();
				}
			return offset;
		}


	/** The XML parent file-name for this drawable component (if any). */
	protected String XMLparentName;
	/** Returns the XML parent file-name for this drawable component.  Null if not XML inflated. */
	public String getXMLParentName() {return XMLparentName;}


	/** Tool-tip String ID */
	protected String tooltipID;
	/** Returns the tool-tip String ID. */
	public String getToolTipStringID() {return tooltipID;}
	/** Sets the Tool-tip String ID for Language look-up.  This is shown during mouse-over.  Set to "" to disable.*/
	public void setToolTipStringID(String id){tooltipID = id;}

	/** Tool-tip String */
	protected String tooltipText;
	public String getToolTipText() {return tooltipText;}
	/** Sets the Tool-tip String to override language lookup.  This is shown during mouse-over.  Set to "" to disable.*/
	public void setToolTipText(String text){tooltipText = text;}


	/** Returns the String for tooltip display.  By default this returns the Language data with the
	 * set ToolTipStringID.  But this method can be overridden for a more specific behavior. */
	public String getToolTipString()
		{
			if (tooltipText != null && !tooltipText.isEmpty())
				return tooltipText;
			else if (tooltipID != null && !tooltipID.isEmpty())
				return Language.get(tooltipID);
			return null;
		}




	// Dimensions
	//---------------------------

	/** The current dimensions of this drawable component. */
	protected Rectangle dims;
	/** Returns the current dimensions of this drawable component. */
	public Rectangle getDimensions() {return dims;}
	/** Sets the current dimensions of this drawable component. */
	public void setDimensions(Rectangle dim) {resize(dim);}
	/** Returns 'true' if this drawable component is within the given mouse coordinates. */
	public boolean contains(float mX, float mY) {return dims.contains(mX,mY);}

	/** Pushes the current absolute values of this components dimensions to its XML data. */
	public void pushDimensionsToData()
		{
			this.data.put("position", (int)this.getDimensions().x + "|" + (int)this.getDimensions().y);
			this.data.put("width", (int)this.getDimensions().width+"");
			this.data.put("height", (int)this.getDimensions().height+"");
		}

	/** 'True' if this component needs resized in the OpenGL context. */
	protected boolean needsResize;
	/** Sets this component to resize itself in the OpenGL context next render frame. */
	public void setResize() {needsResize = true;}




	// Enabled
	//---------------------------

	/** 'True' if this drawable component is enabled to be rendered and receive input. */
	protected boolean enabled;
	/** Returns 'true' if this drawable component is enabled to be rendered and receive input. */
	public boolean isEnabled() {return enabled;}
	/** Sets this drawable component disabled, will not be rendered or receive input. */
	public void disable() {enabled = false; needsRedrawn = false;}
	/** Sets this drawable component enabled, will now be rendered and receive input. */
	public void enable() {enabled = true; needsRedrawn = true;}


	// Input Receiver
	//---------------------------
	/** The Input Receiver for this drawable component. */
	protected FWTInputReceiver inputReceiver;
	/** Returns the Input Receiver for this drawable component. */
	public FWTInputReceiver getInputReceiver() {return inputReceiver;}
	/** Adds the given Input Receiver to the chain for this drawable component. */
	public void addInputReceiver(FWTInputReceiver receiver)
		{
			receiver.setComponent(this);
			if (inputReceiver == null)
				inputReceiver = receiver;
			else
				inputReceiver.addChainedInputReceiver(receiver);
		}
	/** Sets the given Input Receiver to this drawable component. 
	 * <br> Any previously chained receivers will be cleared. 
	 * <br>  NOTE: addInputReceiver() preferred to add functionality.*/
	public void setInputReceiver(FWTInputReceiver receiver)
		{
			clearInputReceivers();
			receiver.setComponent(this);
			inputReceiver = receiver;
		}
	/** Clears all Input Receivers in the chain for this drawable component. */
	public void clearInputReceivers()
		{
			if (inputReceiver != null)
				{
					inputReceiver.clearChainedInputReceivers();
					inputReceiver.setComponent(null);
				}
			inputReceiver = null;
		}

	/** 'True' if mouse is over this component. */
	protected boolean mouseOver;
	/** Returns 'True' if the mouse is currently over this component. */
	public boolean isMouseOver() {return mouseOver;}


	/** The number of pixels this component must be dragged before a dragged event is triggered. */
	protected int dragResistance;
	/** Returns the number of pixels this component must be dragged before a dragged event is triggered <br> [Default 15 pixels]. */
	public int getDragResistance() {return dragResistance;}
	/** Sets the number of pixels this component must be dragged before a dragged event is triggered <br> [Default 15 pixels]. */
	public void setDragResistance(int dr) {dragResistance = dr;}
	/** Returns 'true' if this component must be dragged a minimum distance before a dragged event is triggered. */
	public boolean hasDragResistance() {return dragResistance > 0;}



	// Drawable Appearance
	//---------------------------

	/** Border color. */
	protected Color borderColor;
	/** Returns this drawable component's border color. */
	public Color getBorderColor() {return borderColor;}
	/** Sets this drawable component's border color. */
	public void setBorderColor(Color bcolor) {borderColor = bcolor;}

	/** Highlight Border color. */
	protected Color highlightborderColor;
	/** Returns this drawable component's highlighted border color. */
	public Color getHighlightBorderColor() {return highlightborderColor;}
	/** Sets this drawable component's highlighted border color. */
	public void setHighlightBorderColor(Color hbcolor) {highlightborderColor = hbcolor;}


	/** Normal background color for this drawable component. */
	protected Color backgroundColor;
	/** Returns this drawable component's background color. */
	public Color getBackgroundColor() {return backgroundColor;}
	/** Sets this drawable component's background color. */
	public void setBackgroundColor(Color bcolor) {backgroundColor = bcolor;}


	/** Highlighted background color. */
	protected Color highlightColor;
	/** Returns this drawable's highlighted background color. */
	public Color getHighlightColor() {return highlightColor;}
	/** Sets this drawable's highlighted background color. */
	public void setHighlightColor(Color hcolor) {highlightColor = hcolor;}



	// Texture Control
	//---------------------------


	/** Image ID for a texture-based drawable component.  Overrides the background color. Null is no texture. */
	protected String bgTexture;
	/** Returns this drawable component's image texture ID. */
	public String getTexture() {return bgTexture;}
	/** Sets this drawable component's image texture ID. Set to 'null' to use background coloring instead. */
	public void setTexture(String imgID) {bgTexture = imgID;}

	/** 'True' if this drawable component should scale its texture to fit (stretched). 
		<br> 'False' indicates a tiled texture. */
	protected boolean scaleTexture;
	/** Returns 'True' if this drawable component will scale its texture to fit, 'False' if this texture is tiled. */
	public boolean isTextureScaled() {return scaleTexture;}
	/** Sets this drawable component to scale its texture to fit its size, set to false if this texture is to be tiled. */
	public void setTextureScaled(boolean texScale) {scaleTexture = texScale;}


	/** Image ID for a texture-based drawable component when highlighted.  Null is no texture. */
	protected String highlightTexture;
	/** Returns this drawable component's image texture ID when highlighted. */
	public String getTextureHighlighted() {return highlightTexture;}
	/** Sets this drawable component's image texture ID  when highlighted. Set to 'null' to use normal texture/background instead. */
	public void setTextureHighlighted(String imgID) {highlightTexture = imgID;}

	/** This drawable component's image texture ID for its border. */
	protected String borderTexture;
	/** Returns this drawable component's image texture ID for its border. */
	public String getBorderTexture() {return borderTexture;}
	/** Sets this drawable component's image texture ID for its border. */
	public void setBorderTexture(String border) {borderTexture = border;}




	// Drawable Behavior
	//---------------------------

	/** 'True' if this drawable component needs redrawn to its back buffer. */
	protected boolean needsRedrawn;
	/** Sets this drawable component to update its back buffer on the next render cycle. */
	public void redraw() {needsRedrawn = true;}



	// Draw Priority
	//---------------------------

	/** The draw priority of the component. Only affects draw order on sorted containers. */
	protected int drawPriority;
	/** Sets the draw priority of the component. Only affects draw order on sorted containers. */
	public void setDrawPriority(int priority) {drawPriority = priority;}
	/** Returns the draw priority of the component. Only affects draw order on sorted containers. */
	public int getDrawPriority() {return drawPriority;}


	// Animation
	//---------------------------

	/** 'True' if this component is currently animating (needs redrawn each frame). */
	protected boolean isAnimating;
	/** Sets this component's animating status (animating components are drawn each frame). */
	public void setAnimating(boolean an) {isAnimating = an;}




	// Component Data
	//---------------------------
	/** Data saved in this component.  Includes XML data during creation. */
	protected XMLDataPacket data;
	/** Returns the data saved in this component.  Includes XML data during creation. */
	public XMLDataPacket getData() {return data;}
	/** Sets the data saved in this component.  Should include XML data during creation. */
	public void setData(XMLDataPacket data) {this.data = data;}











	//  CONSTRUCTOR
	//********************************************************************
	//********************************************************************


	/** Creates a new blank drawable component with a random ID tag and given XML data. */
	public FWTComponent(XMLDataPacket data)
		{
			id = FWTController.generateObjectTag();
			setDefaults();

			if (data != null)
				applyDataParameters(data);
		}

	/** Creates a new blank drawable component with the given XML targets. */
	public FWTComponent(String parent, String object)
		{
			id = FWTController.generateObjectTag();
			setDefaults();

			if (parent != null && object != null)
				{
					data.put("parentfile", parent);
					data.put("objectname", object);
					applyDataParameters(XMLUIReader.getUISpecs(parent, object));
				}
		}


	//  XML DATA
	//********************************************************************
	//********************************************************************

	/** Set default variable values. */
	protected void setDefaults()
		{
			name = Long.toString(id);
			tooltipID = "";
			tooltipText = "";
			dims = new Rectangle(10,10,100,100);
			needsResize = true;
			enabled = true;
			dragResistance = 15;
			borderColor = Color.BLACK;
			highlightborderColor = Color.DARK_GRAY;
			backgroundColor = Color.GRAY;
			highlightColor = Color.LIGHT_GRAY;
			scaleTexture = true;
			needsRedrawn = true;
			drawPriority = Integer.MAX_VALUE-1;
			data = new XMLDataPacket();		
		}



	/** Reread and refresh the XML data from the parent XML file. */
	public void refreshData()
		{
			applyDataParameters(XMLUIReader.getUISpecs(data.get("parentfile"),data.get("objectname")));
		}



	/** Reads and sets the XML data for this drawable component. */
	protected void applyDataParameters(XMLDataPacket data)
		{
			// IF valid data packet
			//--------------------------------------
			try{if (data != null)
				{
					// Add each XML entry to data set
					for (Entry<String,String> entry : data.getMap().entrySet())
						this.data.put(entry.getKey(), entry.getValue());
						
					
					// NAME
					//========================
					if (data.get("name") != null)
						{
							this.name = data.get("name");
						}
					else this.name = Long.toString(id);

					// PARENT FILE
					//========================
					if (data.get("parentfile") != null)
						{
							this.XMLparentName = data.get("parentfile");
						}

					// TOOLTIP
					//========================
					if (data.get("tooltip") != null)
						{
							this.tooltipID = data.get("tooltip");
						}

					// BORDER COLOR
					//========================
					if (data.get("bordercolor") != null)
						{
							this.borderColor = data.getColor("bordercolor");
							this.highlightborderColor = borderColor.cpy();
						}

					// HIGHLIGHT BORDER COLOR
					//========================
					if (data.get("highlightbordercolor") != null)
						{
							this.highlightborderColor = data.getColor("highlightbordercolor");
						}

					// BACKGROUND COLOR
					//========================
					if (data.get("backgroundcolor") != null)
						{
							this.backgroundColor = data.getColor("backgroundcolor");
							this.highlightColor = this.backgroundColor.cpy();
						}

					// HIGHLIGHT COLOR
					//========================
					if (data.get("highlightcolor") != null)
						{
							this.highlightColor = data.getColor("highlightcolor");
						}

					// BACKGROUND TEXTURE
					//========================
					if (data.get("backgroundtexture") != null)
						{
							this.bgTexture = data.get("backgroundtexture");
						}

					// HIGHLIGHT TEXTURE
					//========================
					if (data.get("highlighttexture") != null)
						{						
							this.highlightTexture = data.get("highlighttexture");
						}

					// BORDER TEXTURE
					//========================
					if (data.get("bordertexture") != null)
						{
							this.borderTexture = data.get("bordertexture");
						}

					// BORDER
					//========================
					if (data.get("border") != null)
						{
							if (!data.getBoolean("border"))	
								{
									this.borderColor = FWTColors.INVISIBLE;
									this.highlightborderColor = FWTColors.INVISIBLE;
									this.borderTexture = null;
								}
						}

					// SCALE TEXTURE
					//========================
					if (data.get("texturefill") != null)
						{
							this.scaleTexture = data.get("texturefill").equals("stretched") ? true : false;
						}

					// COMPONENT DATA
					//========================
					if (data.get("data") != null)
						{
							StringTokenizer sT = new StringTokenizer(data.get("data"),"|");
							StringTokenizer sT2;
							while(sT.hasMoreTokens())
								{
									sT2 = new StringTokenizer(sT.nextToken(),":");
									try{
										data.put(sT2.nextToken(),sT2.nextToken());
									}catch(Exception ex)
										{FWTController.error("Invalid XML Data for UI: '"+this.name+"'. Cannot read component data.");}
								}
						}
					
					

				}
			// ELSE not valid data packet
			//--------------------------------------
			else throw new Exception();
			}catch(Exception ex){
				FWTController.error("Invalid XML Data for UI: '"+this.name+"'. Using defaults.");
			}

		}


	//  RESIZE
	//********************************************************************
	//********************************************************************


	/** Resizes the component and its back-buffer using its XML data dimensions or
	 * using its currently set dimensions if no data found.  Uses the given screen dimensions. 
	 * Should only be called from update/render (OpenGL context required).*/
	public void resize(int swidth, int sheight)
		{			
			try{
				// IF has data packet
				if (data != null)
					{
						// PARENT DIMENSIONS
						//========================
						// Get Parent Width/Height
						int pwidth = Gdx.graphics.getWidth();
						if (this.parent != null) pwidth = (int)this.getParent().getDimensions().width;
						int pheight = Gdx.graphics.getHeight();
						if (this.parent != null) pheight = (int)this.getParent().getDimensions().height;


						// MAXIMUM WIDTH
						//========================
						int maxWidth = Integer.MAX_VALUE;

						// IF has maximum width
						if (data.get("maxwidth") != null)
							{
								String mW = data.get("maxwidth");

								// IF has relative max width
								if (mW.contains("width"))
									{
										String s = mW.substring(mW.indexOf("width")+5, mW.length());
										int Woffset = Integer.parseInt(s);

										if (this.getParent() != null)
											{
												if (this.getParent() instanceof FWTWindow)
													{
														// Window component offsets
														FWTWindow win = (FWTWindow)this.getParent();
														if (win.hasBorderBar()) Woffset = Woffset-UIRenderer.THICK_BORDER_WIDTH;
														else Woffset = Woffset-UIRenderer.THIN_BORDER_WIDTH;
													}
												else Woffset = Woffset-UIRenderer.THIN_BORDER_WIDTH; // Container offset
											}
										// Set offset
										maxWidth = pwidth + Woffset;
									}
								// ELSE if percentile max width
								else if (mW.contains("%"))
									{
										String s = mW.substring(mW.indexOf("%")+1, mW.length());
										float Wmult = Float.parseFloat(s) / 100f;
										if (Wmult > 1f) Wmult = 1f;
										maxWidth = (int) ((float)pwidth * Wmult);
									}
								// ELSE absolute max width
								else maxWidth = data.getInt("maxwidth");
							}


						// WIDTH
						//========================
						int width = -1;

						// IF has width
						if (data.get("width") != null)
							{
								String W = data.get("width");

								// IF max width
								if (W.equals("max"))
									{
										if (this.getParent() != null)
											{
												width = pwidth - UIRenderer.THIN_BORDER_WIDTH; // Container offset
												if (this.getParent() instanceof FWTWindow)
													{
														// Window component offsets
														FWTWindow win = (FWTWindow)this.getParent();
														if (win.hasBorderBar()) width = pwidth-UIRenderer.THICK_BORDER_WIDTH;
														else width = pwidth - UIRenderer.THIN_BORDER_WIDTH;
													}
											}
										// ELSE no offset
										else width = pwidth;
									}
								// ELSE if relative width
								else if (W.contains("width"))
									{
										String s = W.substring(W.indexOf("width")+5, W.length());
										int Woffset = Integer.parseInt(s);

										if (this.getParent() != null)
											{
												if (this.getParent() instanceof FWTWindow)
													{
														// Window component offsets
														FWTWindow win = (FWTWindow)this.getParent();
														if (win.hasBorderBar()) Woffset = Woffset-UIRenderer.THICK_BORDER_WIDTH;
														else Woffset = Woffset-UIRenderer.THIN_BORDER_WIDTH;
													}
												else Woffset = Woffset-UIRenderer.THIN_BORDER_WIDTH; // Container offset
											}
										// Set offset
										width = pwidth + Woffset;
									}
								// ELSE if percentile
								else if (W.contains("%"))
									{
										String s = W.substring(W.indexOf("%")+1, W.length());
										float Wmult = Float.parseFloat(s) / 100f;
										if (Wmult > 1f) Wmult = 1f;
										width = (int) ((float)pwidth * Wmult);
									}
								// ELSE absolute width
								else width = data.getInt("width");
							}
						// Keep within limits
						if (width < 0) width = 100;
						if (width > maxWidth) width = maxWidth;


						// MAXIMUM HEIGHT
						//========================
						int maxHeight = Integer.MAX_VALUE;

						// IF has maximum height
						if (data.get("maxheight") != null)
							{
								String mH = data.get("maxheight");

								// IF relative max height
								if (mH.contains("height"))
									{
										String s = mH.substring(mH.indexOf("height")+6, mH.length());
										int Hoffset = Integer.parseInt(s);

										if (this.getParent() != null)
											{
												if (this.getParent() instanceof FWTWindow)
													{
														// Window component offsets
														FWTWindow win = (FWTWindow)this.getParent();
														if (win.hasBorderBar()) Hoffset = Hoffset-UIRenderer.THICK_BORDER_WIDTH;
														else Hoffset = Hoffset-UIRenderer.THIN_BORDER_WIDTH;
													}
												else Hoffset = Hoffset-UIRenderer.THIN_BORDER_WIDTH; // Container offset
											}
										// Set offset
										maxHeight = pheight + Hoffset;
									}
								// ELSE if percentile max height
								else if (mH.contains("%"))
									{
										String s = mH.substring(mH.indexOf("%")+1, mH.length());
										float Hmult = Float.parseFloat(s) / 100f;
										if (Hmult > 1f) Hmult = 1f;
										maxHeight = (int) ((float)pheight * Hmult);
									}
								// ELSE absolute max height
								else maxHeight = data.getInt("maxheight");
							}


						// HEIGHT
						//========================
						int height = -1;

						// IF has height
						if (data.get("height") != null)
							{
								String H = data.get("height");

								// IF max height
								if (H.equals("max"))
									{
										if (this.getParent() != null)
											{
												height = pheight-UIRenderer.THIN_BORDER_WIDTH; // Container offset
												if (this.getParent() instanceof FWTWindow)
													{
														// Window component offsets
														FWTWindow win = (FWTWindow)this.getParent();
														if (win.hasBorderBar()) height = pheight-UIRenderer.THICK_BORDER_WIDTH;
														else height = pheight-UIRenderer.THIN_BORDER_WIDTH;
													}
											}
										// ELSE no offset
										else height = pheight;
									}
								// ELSE if relative height
								else if (H.contains("height"))
									{
										String s = H.substring(H.indexOf("height")+6, H.length());
										int Hoffset = Integer.parseInt(s);

										if (this.getParent() != null)
											{
												if (this.getParent() instanceof FWTWindow)
													{
														// Window component offsets
														FWTWindow win = (FWTWindow)this.getParent();
														if (win.hasBorderBar()) Hoffset = Hoffset-UIRenderer.THICK_BORDER_WIDTH;
														else Hoffset = Hoffset-UIRenderer.THIN_BORDER_WIDTH;
													}
												else Hoffset = Hoffset-UIRenderer.THIN_BORDER_WIDTH; // Container offset
											}
										// Set offset
										height = pheight + Hoffset;
									}
								// ELSE if relative height (no borders)
								else if (H.contains("pheight"))
									{
										String s = H.substring(H.indexOf("pheight")+7, H.length());
										int Hoffset = Integer.parseInt(s);
										// Set offset
										height = pheight - Hoffset;
									}
								// ELSE if percentile
								else if (H.contains("%"))
									{
										String s = H.substring(H.indexOf("%")+1, H.length());
										float Hmult = Float.parseFloat(s) / 100f;
										if (Hmult > 1f) Hmult = 1f;
										height = (int) ((float)pheight * Hmult);
									}
								// ELSE absolute width
								else height = data.getInt("height");
							}
						// Keep within limits
						if (height < 0) height = 100;
						if (height > maxHeight) height = maxHeight;


						// POSITION
						//========================

						if (data.has("position"))
							{
								// Calculate Position
								StringTokenizer sT = new StringTokenizer(data.get("position"),"|,");
								// IF has X/Y Specified positions
								if (sT.countTokens() > 1)
									{
										int x=0, y=0;

										// X:
										//--------------
										String X = sT.nextToken();
										// IF has relative to width
										if (X.contains("width"))
											{
												String s = X.substring(X.indexOf("width")+5, X.length());
												int Xoffset = Integer.parseInt(s);
												x = pwidth + Xoffset;
											}
										// IF has relative to screen width
										else if (X.contains("swidth"))
											{
												String s = X.substring(X.indexOf("swidth")+6, X.length());
												int Xoffset = Integer.parseInt(s);
												x = Gdx.graphics.getWidth() + Xoffset;
											}
										// IF centered
										else if (X.contains("center"))
											{
												x = (pwidth/2)-(width/2);
											}
										// IF left
										else if (X.contains("left"))
											{
												x = 0;
											}
										// IF right
										else if (X.contains("right"))
											{
												x = pwidth-width;
											}
										// IF percentile
										else if (X.contains("%"))
											{
												String s = X.substring(X.indexOf("%")+1, X.length());
												float Xmult = Float.parseFloat(s) / 100f;
												if (Xmult > 1f) Xmult = 1f;
												if (Xmult < 0.0001f) Xmult = 0.0001f;
												x = (int) ((float)pwidth * Xmult);
												if (x > (pwidth-width)) x = (pwidth-width);
											}
										else // ELSE absolute
											x = Integer.parseInt(X);


										// Y:
										//--------------
										String Y = sT.nextToken();
										// IF has relative to height
										if (Y.contains("height"))
											{
												String s = Y.substring(Y.indexOf("height")+6, Y.length());
												int Yoffset = Integer.parseInt(s);
												y = pheight + Yoffset;
											}
										// IF has relative to screen height
										else if (Y.contains("sheight"))
											{
												String s = Y.substring(Y.indexOf("sheight")+7, Y.length());
												int Yoffset = Integer.parseInt(s);
												y = Gdx.graphics.getHeight() + Yoffset;
											}
										// IF centered
										else if (Y.contains("center"))
											{
												y = (pheight/2)-(height/2);
											}
										// IF top
										else if (Y.contains("top"))
											{
												y = pheight-height;
											}
										// IF bottom
										else if (Y.contains("bottom"))
											{
												y = 0;
											}
										// IF percentile
										else if (Y.contains("%"))
											{
												String s = Y.substring(Y.indexOf("%")+1, Y.length());
												float Ymult = Float.parseFloat(s) / 100f;
												if (Ymult > 1f) Ymult = 1f;
												if (Ymult < 0.0001f) Ymult = 0.0001f;
												y = (int) ((float)pheight * Ymult);
												if (y > (pheight-height)) y = (pheight-height);
											}
										else // ELSE absolute
											y = Integer.parseInt(Y);

										// Resize based on position
										resize(new Rectangle(x,y,width,height)); 
									}
								// ELSE has relative position
								else
									{
										int offset = 0; // Window/widget offset
										if (this.getParent() != null)
											{
												offset = 3; // Container offset
												if (this.getParent() instanceof FWTWindow)
													{
														// Window component offsets
														FWTWindow win = (FWTWindow)this.getParent();
														if (win.hasBorderBar()) offset = 15;
														else offset = 10;
													}
											}

										// Switch on Relative Position
										String relPos = data.get("position");
										if (relPos.equals("topleft"))
											{resize(new Rectangle(offset,pheight-height-offset,width,height));}
										else if (relPos.equals("topright"))
											{resize(new Rectangle(pwidth-width-offset,pheight-height-offset,width,height));}
										else if (relPos.equals("bottomleft"))
											{resize(new Rectangle(offset,offset,width,height));}
										else if (relPos.equals("bottomright"))
											{resize(new Rectangle(pwidth-width-offset,offset,width,height));}
										else if (relPos.equals("topcenter"))
											{resize(new Rectangle((pwidth/2)-(width/2),pheight-height-offset,width,height));}
										else if (relPos.equals("bottomcenter"))
											{resize(new Rectangle((pwidth/2)-(width/2),offset,width,height));}
										else if (relPos.equals("leftcenter"))
											{resize(new Rectangle(offset,(pheight/2)-(height/2),width,height));}
										else if (relPos.equals("rightcenter"))
											{resize(new Rectangle(pwidth-width-offset,(pheight/2)-(height/2),width,height));}
										else if (relPos.equals("center"))
											{resize(new Rectangle((pwidth/2)-(width/2),(pheight/2)-(height/2),width,height));}
									}
								sT = null;
							} 
						else // ELSE no position
							{		
								// Set position to bottom-left corner
								resize(new Rectangle(1,1,width,height));
							}

					} // END IF has data

				// ELSE not valid data packet
				//--------------------------------------
				else throw new Exception();
			}catch(Exception ex){
				FWTController.error("Invalid XML Data for UI: '"+this.name+"'. Using defaults.");
				// Already set size
				resize(this.getDimensions());
			}

			// On-Resize method call
			onResize();
		}


	/** Method called after a resize operation on this component. Expected to be overridden for specification. */
	public void onResize()
		{

		}



	/** Resizes this drawable component to the new given dimensions.  
	 * Should only be called from update/render (OpenGL context required).*/
	public void resize(Rectangle dims)
		{
			this.dims = dims;

			// No buffer changes outside of OpenGL loop
			if (Thread.currentThread().getId() == FWTController.getOpenGLThreadID())
				{
					FWTUIFrameBufferController.resizeUIBuffer(this);
					needsResize = false;
				}
			else
				needsResize = true; // Indicate for next render call

			needsRedrawn = true;

		}






	//  UPDATE
	//********************************************************************
	//********************************************************************

	/** Updates this drawable component's data. Expected to be overridden by subclasses.*/
	public void update()
		{ 

		}



	//  RENDER
	//********************************************************************
	//********************************************************************

	/** Render this drawable component using its back-buffer texture. */
	public void render()
		{

			SpriteBatch spriteBatch = FWTController.getSpriteBatch();

			try {
				// Update component data
				update();
			}catch (Exception ex)
				{
					FWTController.error("Error updating component: "+this.getName());
					FWTController.error(ex.getMessage());
				}

			try{
				if (FWTWindowManager.RENDER_DEBUG_MODE)
					FWTController.log(this.name+": Rendering");

				// Resize if required
				if (needsResize)
					{FWTUIFrameBufferController.resizeUIBuffer(this); needsResize = false; needsRedrawn = true;}

				// Redraw back buffer if required
				if (needsRedrawn || isAnimating)
					{redrawBuffer(); needsRedrawn = false;}

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
					if (spriteBatch.isDrawing()) spriteBatch.end();
					FWTController.error("Error rendering component: "+this.getName());
					FWTController.error(ex.getMessage());
				}
		}




	//  REDRAW
	//********************************************************************
	//********************************************************************

	/** Render this drawable component using its back-buffer texture. */
	public void redrawBuffer()
		{
			SpriteBatch spriteBatch = FWTController.getSpriteBatch();
			ShapeRenderer shapeRenderer = FWTController.getShapeRenderer();

			// Enter frame buffer
			FWTUIFrameBufferController.pushDrawingContext(FWTUIFrameBufferController.getUIBuffer(this));

			// Clear buffer
			Gdx.gl.glClearColor(0f,0f,0f,0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			spriteBatch.enableBlending();
			spriteBatch.setColor(Color.WHITE);

			// Enable blending
			FWTController.enableBlending();

			// Draw Component
			//*************************************************************************
			drawBefore(spriteBatch,shapeRenderer);
			draw(spriteBatch,shapeRenderer);
			drawAfter(spriteBatch,shapeRenderer);

			// Leave frame buffer
			FWTUIFrameBufferController.popDrawingContext();


		}


	//  DRAW
	//********************************************************************
	//********************************************************************


	/** Draws this component to its frame buffer.  
	 * <br> This method is called during the back-buffer rendering.*/
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
									UIRenderer.drawRectangle(spriteBatch,backgroundColor,0,0,dims.width,dims.height);
									spriteBatch.end();
								}
						}

					// ELSE has a texture image
					else
						{
							spriteBatch.begin();
							UIRenderer.drawUIImage(spriteBatch, id, bgTexture, 0, 0,(int)dims.width,(int)dims.height, scaleTexture, !scaleTexture);
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
											UIRenderer.drawRectangle(spriteBatch,highlightColor,0,0,dims.width,dims.height);
											spriteBatch.end();
										}
								}

							// ELSE has a texture image
							else
								{
									spriteBatch.begin();
									UIRenderer.drawUIImage(spriteBatch, id, bgTexture, 0, 0,(int)dims.width,(int)dims.height, scaleTexture, !scaleTexture);
									spriteBatch.end();
								}
						}

					// ELSE has a highlighted texture image
					else
						{
							spriteBatch.begin();
							UIRenderer.drawUIImage(spriteBatch, id, highlightTexture, 0, 0,(int)dims.width,(int)dims.height, scaleTexture, !scaleTexture);
							spriteBatch.end();
						}
				}


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
									UIRenderer.drawThinBorder(this.getID()+1L,spriteBatch,borderColor,0,0,dims.width,dims.height);
									spriteBatch.end();
								}
						}
					else
						{
							// Draw highlighted border color
							if (highlightborderColor.a > 0f)
								{
									spriteBatch.begin();
									UIRenderer.drawThinBorder(this.getID()+1L,spriteBatch,highlightborderColor,0,0,dims.width,dims.height);
									spriteBatch.end();
								}

						}
				}
			// ELSE has a texture image
			else
				{
					spriteBatch.begin();
					UIRenderer.drawUIImage(spriteBatch, id, borderTexture, 0, 0,(int)dims.width,(int)dims.height, scaleTexture, !scaleTexture);
					spriteBatch.end();
				}

		}


	/** Additional draw commands before drawing the component.  Expected to be overridden. */
	public void drawBefore(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {}

	/** Additional draw commands after drawing the component.  Expected to be overridden. */
	public void drawAfter(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {}





	//  DISPOSE
	//********************************************************************
	//********************************************************************

	/** Dispose of this drawable component. */
	public void dispose()
		{
			inputReceiver = null;
			FWTUIFrameBufferController.removeUIBuffer(this);
			FWTImageManager.removeBorderNinePatch(this.getID());
		}





	//  ADDED
	//********************************************************************
	//********************************************************************

	/** This method is invoked when this object is added to another component. */
	public void onAdded()
		{
			// Expected to be overridden by specific behavior.
		}





	//  REMOVED
	//********************************************************************
	//********************************************************************

	/** This method is invoked when this object is removed from another component. */
	public void onRemoved()
		{
			// Expected to be overridden by specific behavior.
		}







	//  MOUSE INPUT
	//********************************************************************
	//********************************************************************



	/** Touch down on this drawable component. Returns 'true' if this drawable component managed the touch event.*/
	public boolean touchDown(int mX, int mY, int pointer, int button) throws FWTInputException
		{
			if (enabled)
				{
					if (FWTWindowManager.DEBUG_MODE) FWTController.log("Touch DOWN: "+this.name);
					// Set component as mouse-down component if it receives event.
					getWindowManager().setMouseDownComponent(this);

					if (inputReceiver != null)
						{
							return inputReceiver.tryTouchDown(mX, mY, pointer, button);
						}
				}
			return false;
		}


	/** Touch up on this drawable component. Returns 'true' if this drawable component managed the touch event.*/
	public boolean touchUp(int mX, int mY, int pointer, int button, FWTComponent touchDownComponent)  throws FWTInputException
		{
			if (enabled && inputReceiver != null)
				{
					if (FWTWindowManager.DEBUG_MODE) FWTController.log("Touch UP: "+this.name);
					return inputReceiver.tryTouchUp(mX, mY, pointer, button, touchDownComponent);
				}
			return false;
		}


	/** Mouse moved on this drawable component. Returns 'true' if this drawable component managed the moved event.*/
	public boolean mouseMoved(int mX, int mY) throws FWTInputException
		{
			// IF enabled
			if (enabled)
				{
					if (!mouseOver)
						redraw();
					mouseOver = true;

					// Tool-tip
					this.getWindowManager().getTooltip().update(getToolTipString());

					// IF has input receiver
					if (inputReceiver != null)
						return inputReceiver.tryMouseMoved(mX, mY);
				}
			return false;
		}


	/** Mouse entered the dimensions of this drawable component.*/
	public void onEnter() throws FWTInputException
		{
			if (FWTWindowManager.DEBUG_MODE) FWTController.log("ENTER: "+this.name);
			mouseOver = true; 
			redraw();			
			if (enabled && inputReceiver != null)
				{
					inputReceiver.tryOnEnter();
				}
		}


	/** Mouse exited the dimensions of this drawable component.*/
	public void onExit() throws FWTInputException
		{
			if (FWTWindowManager.DEBUG_MODE) FWTController.log("EXIT: "+this.name);
			mouseOver = false;  
			redraw();
			if (enabled && inputReceiver != null)
				{
					inputReceiver.tryOnExit();
				}
		}


	/** A component has been dropped on this component. Returns 'true' if this drawable component managed the drop event.*/
	public boolean dragNDrop(int mX, int mY, int pointer, int button, FWTComponent dragComponent)  throws FWTInputException
		{
			if (enabled && inputReceiver != null)
				{
					if (FWTWindowManager.DEBUG_MODE) FWTController.log("Drag 'n' Drop: "+this.name);
					return inputReceiver.tryDragNDrop(mX, mY, pointer, button, dragComponent);
				}
			return false;
		}


	/** Touch dragged on this drawable component. Returns 'true' if this drawable component managed the dragged event.
	 *   NOTE: mX,mY are NOT local coordinates. Use getParentOffsetX() and getParentOffsetY() for proper offsets. */
	public boolean touchDragged(int mX, int mY, int pointer)  throws FWTInputException
		{
			if (enabled && inputReceiver != null)
				{
					if (FWTWindowManager.DEBUG_MODE) FWTController.log("Touch Dragged: "+this.name);
					this.getWindowManager().setDraggedComponent(this);
					return inputReceiver.tryTouchDragged(mX, mY, pointer);
				}
			return false;
		}


	/** This component is no longer being dragged. Returns 'true' if this drawable component managed the touch event.*/
	public boolean dragRelease(int mX, int mY, int pointer)  throws FWTInputException
		{
			if (enabled && inputReceiver != null)
				{
					if (FWTWindowManager.DEBUG_MODE) FWTController.log("Drag Release: "+this.name);
					return inputReceiver.tryDragRelease(mX, mY, pointer);
				}
			return false;
		}


	/** Mouse scrolled on this drawable component. Returns 'true' if this drawable component managed the scrolled event.*/
	public boolean scrolled(float amountX, float amountY) throws FWTInputException
		{
			if (enabled && inputReceiver != null)
				{
					if (FWTWindowManager.DEBUG_MODE) FWTController.log("Scrolled: "+this.name);
					return inputReceiver.tryScrolled(amountX, amountY);
				}
			return false;
		}





	//  KEYBOARD INPUT
	//********************************************************************
	//********************************************************************


	/** Key down on this drawable component. Returns 'true' if this drawable component managed the key event.*/
	public boolean keyDown(int keycode) throws FWTInputException
		{
			if (enabled && inputReceiver != null)
				{
					if (FWTWindowManager.DEBUG_MODE) FWTController.log("Key DOWN: "+this.name);
					return inputReceiver.tryKeyDown(keycode);
				}
			return false;
		}


	/** Key up on this drawable component. Returns 'true' if this drawable component managed the key event.*/
	public boolean keyUp(int keycode) throws FWTInputException
		{
			if (enabled && inputReceiver != null)
				{
					if (FWTWindowManager.DEBUG_MODE) FWTController.log("Key UP: "+this.name);
					return inputReceiver.tryKeyUp(keycode);
				}
			return false;
		}


	/** Key typed on this drawable component. Returns 'true' if this drawable component managed the key event.*/
	public boolean keyTyped(char keychar) throws FWTInputException
		{
			if (enabled && inputReceiver != null)
				{
					if (FWTWindowManager.DEBUG_MODE) FWTController.log("Key TYPED: "+this.name);
					return inputReceiver.tryKeyTyped(keychar);
				}
			return false;
		}










}
