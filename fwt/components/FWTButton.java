package com.arboreantears.fwt.components;

import com.arboreantears.fwt.UIRenderer;
import com.arboreantears.fwt.XMLDataPacket;
import com.arboreantears.fwt.events.FWTButtonListener;
import com.arboreantears.fwt.events.FWTInputException;
import com.arboreantears.fwt.events.FWTInputReceiver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;



/** A button component that can handle button events. */
public class FWTButton extends FWTComponent
{


	//********************************************************************
	//  VARIABLES
	//********************************************************************


	@Override
	public String toString()
	{
		return "Button: "+this.name;
	}


	// Status
	//-----------------------

	/** 'True' if button is being pressed. */
	protected boolean pressed;
	/** Returns 'True' if button is currently being pressed down. */
	public boolean isPressed() {return pressed;}
	/** Sets the button as currently pressed.  Does not trigger button listeners. Rendering only. */
	public void setPressed(boolean p) {pressed = p; this.redraw();}



	// Appearance
	//-----------------------

	/** Button color when button is being pressed. */
	protected Color pressedColor;
	/** Sets this drawable's pressed color. */
	public Color getPressedColor() {return pressedColor;}
	/** Sets this drawable's pressed color. */
	public void setPressedColor(Color pcolor) {pressedColor = pcolor;}


	/** Image ID for this button when pressed.  Null is no texture. */
	protected String pressedTexture;
	/** Returns the texture for the button when pressed. */
	public String getTexturePressed() {return pressedTexture;}
	/** Sets the texture for the button when pressed. Set to 'null' to use normal texture/background instead. */
	public void setTexturePressed(String imgID) {pressedTexture = imgID;}


	/** Image ID for this button's icon (if any).  Null is disabled. */
	protected String iconTexture;
	/** Returns the texture for this button's icon (if any).  Null is disabled. */
	public String getTextureIcon() {return iconTexture;}
	/** Sets the texture for this button's icon. Set to 'null' to disable. */
	public void setTextureIcon(String imgID) {iconTexture = imgID;}




	// Listener
	//-----------------------

	/** The button listener handling events for this button. */
	FWTButtonListener buttonListener;
	/** Sets the button listener handling events for this button. */
	public void setButtonListener(FWTButtonListener listener) {buttonListener = listener; buttonListener.setComponent(this);}
	/** Adds the given Button Listener to the chain for this button. */
	public void addButtonListener(FWTButtonListener listener)
		{
			listener.setComponent(this);
			if (buttonListener == null)
				buttonListener = listener;
			else
				buttonListener.addChainedButtonListener(listener);
		}











	//********************************************************************
	//  CONSTRUCTOR
	//********************************************************************

	
	/** Creates a new blank FWTButton with the given data packet. */
	public FWTButton(XMLDataPacket data)
		{
			super(data);
			createInputReceiver();
		}
	

	/** Creates a new blank FWTButton with the XML targets. */
	public FWTButton(String parent, String object)
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
			pressedColor = Color.DARK_GRAY;
		}
	
	@Override
	public void applyDataParameters(XMLDataPacket data)
	{
		super.applyDataParameters(data);
		
		if (data != null)
			{					
				// Pressed Color
				if (data.get("pressedcolor") != null)
					{
						pressedColor = data.getColor("pressedcolor");
					}
				else if (data.get("backgroundcolor") != null)
					{
						pressedColor = data.getColor("backgroundcolor");
					}

				// Pressed Texture
				if (data.get("pressedtexture") != null)
					{
						pressedTexture = data.get("pressedtexture");
					}

				// Icon Texture
				if (data.get("icontexture") != null)
					{
						iconTexture = data.get("icontexture");
					}
			}		
	}




	//********************************************************************
	//  DRAW
	//********************************************************************

	@Override
	public void draw(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
		{
			// IF button pressed
			if (pressed)
				{
					// IF has no pressed background
					if (pressedTexture == null || pressedTexture.isEmpty() || !UIRenderer.hasUIImage(pressedTexture))
						{
							// IF has no background texture
							if (bgTexture == null || bgTexture.isEmpty() || !UIRenderer.hasUIImage(bgTexture))
								{
									// Draw fill color
									if (pressedColor.a > 0f)
										{
											spriteBatch.begin();
											UIRenderer.drawRectangle(spriteBatch, pressedColor, 0, 0, dims.width, dims.height);
											spriteBatch.end();
										}
								}

							// ELSE has a texture image
							else
								{
									spriteBatch.begin();
									UIRenderer.drawUIImage(spriteBatch, id, bgTexture, 0, 0,(int)dims.width,(int)dims.height, scaleTexture, false);
									spriteBatch.end();
								}
						}

					// ELSE has a pressed texture image
					else
						{
							spriteBatch.begin();
							UIRenderer.drawUIImage(spriteBatch, id, pressedTexture, 0, 0,(int)dims.width,(int)dims.height, scaleTexture, false);
							spriteBatch.end();
						}
				}

			// IF mouse not over
			else if (!mouseOver)
				{

					// IF has background
					if (bgTexture == null || bgTexture.isEmpty() || !UIRenderer.hasUIImage(bgTexture))
						{
							// Draw fill color
							if (backgroundColor.a > 0f)
								{
									spriteBatch.begin();
									UIRenderer.drawRectangle(spriteBatch, backgroundColor, 0, 0, dims.width, dims.height);
									spriteBatch.end();
								}
						}

					// ELSE has a texture image
					else
						{
							spriteBatch.begin();
							UIRenderer.drawUIImage(spriteBatch, id, bgTexture, 0, 0,(int)dims.width,(int)dims.height, scaleTexture, false);
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
											UIRenderer.drawRectangle(spriteBatch, highlightColor, 0, 0, dims.width, dims.height);
											spriteBatch.end();
										}
								}

							// ELSE has a texture image
							else
								{
									spriteBatch.begin();
									UIRenderer.drawUIImage(spriteBatch, id, bgTexture, 0, 0,(int)dims.width,(int)dims.height, scaleTexture, false);
									spriteBatch.end();
								}
						}

					// ELSE has a highlighted texture image
					else
						{
							spriteBatch.begin();
							UIRenderer.drawUIImage(spriteBatch, id, highlightTexture, 0, 0,(int)dims.width,(int)dims.height, scaleTexture, false);
							spriteBatch.end();
						}
				}


			// Draw icon
			if (iconTexture != null && !iconTexture.isEmpty())
				{
					drawIcon(spriteBatch, shapeRenderer, iconTexture);
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



	/** Draws this button's icon.  Can be overridden for more complex overlays. */
	public void drawIcon(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, String iconTexture)
		{
			if (iconTexture != null)
				{
					spriteBatch.begin();
					UIRenderer.drawUIImage(spriteBatch, id, iconTexture, 0, 0, (int)dims.width, (int)dims.height, true, false);
					spriteBatch.end();
				}
		}




	//********************************************************************
	//  INPUT
	//********************************************************************

	/** Create the input receiver for this button to convert mouse events to button listener events. */
	protected void createInputReceiver()
		{
			setInputReceiver(new FWTInputReceiver()
				{
					@Override
					public boolean touchDown(int mX, int mY, int pointer, int button) throws FWTInputException
						{
							pressed = true; 
							if (buttonListener != null)
								buttonListener.tryButtonDown();
							needsRedrawn = true;

							return true;
						}

					@Override
					public boolean touchUp(int mX, int mY, int pointer, int button, FWTComponent touchDownComponent) throws FWTInputException
						{
							pressed = false;
							if (buttonListener != null)
								{
									buttonListener.tryButtonUp();
									buttonListener.tryButtonPressed(button);
								}
							needsRedrawn = true;
							return true;
						}


					@Override
					public boolean dragNDrop(int mX, int mY, int pointer, int button, FWTComponent dragComponent) throws FWTInputException
						{
							// IF drag'n' drop on itself, treat as touch-up event
							if (dragComponent.getID() == this.getComponent().getID())
								{
									return touchUp(mX, mY, pointer, button, dragComponent);
								}
							else
								{
									pressed = false;
									needsRedrawn = true;
									return true;
								}
						}

					@Override
					public boolean dragRelease(int mX, int mY, int pointer)  throws FWTInputException
						{
							pressed = false;
							needsRedrawn = true;
							return true;
						}

				});

			addButtonListener(new FWTButtonListener());
		}




















}
