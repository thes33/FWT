package com.arboreantears.fwt.components;

import com.arboreantears.fwt.UIRenderer;
import com.arboreantears.fwt.XMLDataPacket;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;



/** An icon component that displays an image. */
public class FWTIcon extends FWTComponent
{


	//********************************************************************
	//  VARIABLES
	//********************************************************************


	@Override
	public String toString()
	{
		return "Icon: "+this.name;
	}


	// Appearance
	//-----------------------

	/** Image ID for this button's icon (if any).  Null is disabled. */
	protected String iconTexture;
	/** Returns the texture for this button's icon (if any).  Null is disabled. */
	public String getTextureIcon() {return iconTexture;}
	/** Sets the texture for this button's icon. Set to 'null' to disable. */
	public void setTextureIcon(String imgID) {iconTexture = imgID;}











	//********************************************************************
	//  CONSTRUCTOR
	//********************************************************************


	/** Creates a new blank FWTIcon with the given data packet. */
	public FWTIcon(XMLDataPacket data)
		{
			super(data);
		}
	
	/** Creates a new blank FWTIcon with the given XML target. */
	public FWTIcon(String parent, String object)
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
		}
	

	@Override
	protected void applyDataParameters(XMLDataPacket data)
	{
		super.applyDataParameters(data);
		
		if (data != null)
			{				
				// Icon Texture
				if (data.get("icontexture") != null)
					{
						iconTexture = data.get("icontexture");
					}

			}		
	}

	




	//********************************************************************
	//  RENDER
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
									UIRenderer.drawRectangle(spriteBatch,backgroundColor,0,0,dims.width,dims.height);
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
											UIRenderer.drawRectangle(spriteBatch,highlightColor,0,0,dims.width,dims.height);
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
					drawIcon(spriteBatch, shapeRenderer);
				}



			// Draw border
			// IF has border
			if (borderTexture == null || borderTexture.isEmpty() || !UIRenderer.hasUIImage(borderTexture))
				{
					// Draw fill color
					if (borderColor.a > 0f)
						{
							spriteBatch.begin();
							UIRenderer.drawThinBorder(this.getID()+1L,spriteBatch,borderColor,0,0,dims.width,dims.height);
							spriteBatch.end();
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



	/** Draws this image icon.  Can be overridden for more complex overlays. */
	public void drawIcon(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
		{
			spriteBatch.begin();
			UIRenderer.drawUIImage(spriteBatch, id, iconTexture, 0, 0, (int)dims.width, (int)dims.height, true, false);
			spriteBatch.end();
		}




















}
