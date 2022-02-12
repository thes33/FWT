package com.arboreantears.fwt.components;

import com.arboreantears.fwt.UIRenderer;
import com.arboreantears.fwt.XMLDataPacket;
import com.arboreantears.fwt.events.FWTButtonListener;
import com.arboreantears.fwt.events.FWTInputException;
import com.arboreantears.fwt.events.FWTInputReceiver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** A button component that is toggled on or off with each press. */
public class FWTToggleButton extends FWTButton
{


	//********************************************************************
	//  VARIABLES
	//********************************************************************


	@Override
	public String toString()
		{
			return "ToggleButton: "+this.name;
		}


	/** Sets this toggle buttons pressed status. Doesn't trigger button listeners. */
	public void setPressed(boolean pressed) {this.pressed = pressed; this.redraw();}



	/** Image ID for this button's pressed icon (if any).  Null is disabled. */
	protected String pressedIcon;
	/** Returns the texture for this button's pressed icon (if any).  Null is disabled. */
	public String getTextureIcon() {return pressedIcon;}
	/** Sets the texture for this button's pressed icon. Set to 'null' to disable. */
	public void setTextureIcon(String imgID) {pressedIcon = imgID;}





	//********************************************************************
	//  CONSTRUCTOR
	//********************************************************************


	/** Creates a new blank FWTToggleButton with the given data packet. */
	public FWTToggleButton(XMLDataPacket data)
		{
			super(data);
			createInputReceiver();
		}

	/** Creates a new blank FWTToggleButton with the given XML target. */
	public FWTToggleButton(String parent, String object)
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
			pressedIcon = null;
		}

	@Override
	public void applyDataParameters(XMLDataPacket data)
		{
			super.applyDataParameters(data);

			if (data != null)
				{					
					// Icon Pressed Texture
					if (data.get("pressedicontexture") != null)
						{
							pressedIcon = data.get("pressedicontexture");
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
			if (pressed)
				{

					if (pressedIcon != null && !pressedIcon.isEmpty())
						drawIcon(spriteBatch, shapeRenderer, pressedIcon);
					else if (iconTexture != null && !iconTexture.isEmpty())
						drawIcon(spriteBatch, shapeRenderer, iconTexture);
				}
			else
				{
					if (iconTexture != null && !iconTexture.isEmpty())
						{
							drawIcon(spriteBatch, shapeRenderer, iconTexture);
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
							pressed = !pressed; 
							if (buttonListener != null)
								buttonListener.tryButtonDown();
							needsRedrawn = true;

							return true;
						}

					@Override
					public boolean touchUp(int mX, int mY, int pointer, int button, FWTComponent touchDownComponent)  throws FWTInputException
						{
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
									needsRedrawn = true;
									return true;
								}
						}

					@Override
					public boolean dragRelease(int mX, int mY, int pointer)  throws FWTInputException
						{
							needsRedrawn = true;
							return true;
						}

				});

			buttonListener = new FWTButtonListener();
		}






}
