package com.arboreantears.fwt.components;

import com.arboreantears.fwt.Fonts;
import com.arboreantears.fwt.Language;
import com.arboreantears.fwt.UIRenderer;
import com.arboreantears.fwt.XMLDataPacket;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;



/** A progress bar component. */
public class FWTProgressBar extends FWTComponent
{

	//********************************************************************
	//  VARIABLES
	//********************************************************************


	@Override
	public String toString()
	{
		return "ProgressBar: "+this.name;
	}

	

	// Status
	//-----------------------
	/** Current progress displayed on the bar. */
	float progress;
	/** Returns the current progress on the bar. */
	public float getProgress() {return progress;}
	/** Sets the current progress on the bar and redraws the component.*/
	public void setProgress(float p) {progress = p; this.redraw();}


	// Appearance
	//-----------------------	

	/** Bar color for displayed progress. */
	public Color barColor;
	/** Sets the progress bar color. */
	public Color getBarColor() {return barColor;}
	/** Sets the progress bar color. */
	public void setBarColor(Color pcolor) {barColor = pcolor;}


	/** Image ID for this bar's progress.  Null is no texture. */
	String barTexture;
	/** Returns the texture for the bar's progress meter. */
	public String getTextureBar() {return barTexture;}
	/** Sets the texture for the bar's progress meter. Set to 'null' to use color instead. */
	public void setTextureBar(String imgID) {barTexture = imgID;}

	
	/** 'True' if this bar fills vertically, else 'false' for horizontally. */
	boolean isVertical;
	/** 'True' if this bar fills vertically, else 'false' for horizontally. */
	public boolean isVerticallyOriented() {return isVertical;}
	/** Sets the bar's progress direction. 'True' for fills vertically, else 'false' for horizontally. */
	public void setVerticalOrientation(boolean isVert) {isVertical = isVert;}

	
	/** 'True' if this bar should render the progress percentage in text. */
	boolean showProgressPercentage;
	/** Returns if this bar renders the progress percentage in text. */
	public boolean isShowingPercentage() {return showProgressPercentage;}
	/** Set if this bar should render the progress percentage in text. */
	public void setShowPercentage(boolean show) {showProgressPercentage = show;}
	

	// Font for text
	//-----------------------------
	/** BitmapFont for text drawing. */
	BitmapFont font;
	/** Returns the BitmapFont for text drawing. */
	public BitmapFont getFont() {return font;}
	/** Sets the BitmapFont for text drawing. */
	public void setFont(BitmapFont font) {this.font = font;}
	
	
	// Text Label on Progress Bar
	//-----------------------------
	/** String ID in Language to draw on button. Null is none. */
	String labelID;
	/** Returns the String label ID in Language to draw on button. */
	public String getLabelID() {return labelID;}
	/** Sets the String label ID in Language to draw on button. */
	public void setLabelID(String labelID) {this.labelID = labelID;}

	
	/** String to display instead of Language ID. */
	String labelText;
	/** Returns the String to display instead of Language ID. Null is disabled. */
	public String getLabelText() {return this.labelText;}
	/** Sets the String to display instead of Language ID. Set to Null to disable. */
	public void setLabelText(String text) {this.labelText = text; this.redraw();}


	/** Returns the label string used for displaying.*/
	public String getLabelString()
	{
		if (labelText != null)
			return labelText;
		else if (labelID != null)
			return Language.get(labelID);
		return null;
	}	

	/** Normal font color. */
	Color drawFontColor;
	/** Returns this button's normal font color. */
	public Color getFontColor() {return drawFontColor;}
	/** Sets this button's normal font color. */
	public void setFontColor(Color fcolor) {drawFontColor = fcolor;}
	








	//********************************************************************
	//  CONSTRUCTOR
	//********************************************************************


	/** Creates a new blank FWTProgressBar with the given data packet. */
	public FWTProgressBar(XMLDataPacket data)
		{
			super(data);
		}
	
	/** Creates a new blank FWTProgressBar with the given XML target. */
	public FWTProgressBar(String parent, String object)
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
			progress = 0f;
			isVertical = true;
			showProgressPercentage = true;
			font = Fonts.getFont(20);
			drawFontColor = Color.WHITE.cpy();

			backgroundColor = Color.DARK_GRAY.cpy();
			highlightColor = Color.LIGHT_GRAY.cpy();
			barColor = Color.GREEN.cpy();
			borderColor = Color.BLACK.cpy();
		}
	

	@Override
	public void applyDataParameters(XMLDataPacket data)
	{
		super.applyDataParameters(data);

		if (data != null)
			{
				// Bar Color
				if (data.get("barcolor") != null)
					{
						barColor = data.getColor("barcolor");
					}

				// Bar Texture
				if (data.get("bartexture") != null)
					{
						barTexture = data.get("bartexture");
					}

				// Bar Orientation
				if (data.get("orientation") != null)
					{
						isVertical = data.get("orientation").equals("vertical") ? true : false;
					}

				// Bar Progress
				if (data.get("progress") != null)
					{
						progress = data.getFloat("progress");
					}
				if (progress < 0f) progress = 0f;
				if (progress > 1f) progress = 1f;

				// Font Color
				if (data.get("fontcolor") != null)
					{
						drawFontColor = data.getColor("fontcolor");
					}

				// Font Size
				if (data.get("fontsize") != null)
					{
						font = Fonts.getFont(data.getInt("fontsize"));
					}

				// Text Label ID
				if (data.get("labelid") != null)
					{
						labelID = data.get("labelid");
					}

				// Text Label
				if (data.get("labeltext") != null)
					{
						labelText = data.get("labeltext");
					}

				// Show Percentage
				if (data.get("showpercentage") != null)
					{
						showProgressPercentage = data.getBoolean("showpercentage");
					}

			}
	}





	//********************************************************************
	//  RENDER
	//********************************************************************

	@Override
	public void draw(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
		{
			// Draw background
			super.draw(spriteBatch, shapeRenderer);

			// Draw Progress Bar
			if (barTexture != null && !barTexture.isEmpty())
				{
					if (isVertical)
						{
							spriteBatch.begin();
							UIRenderer.drawUIImage(spriteBatch, id, barTexture, 0, 0, 
									(int)(getDimensions().width),(int)(getDimensions().height*progress), scaleTexture, !scaleTexture);
							spriteBatch.end();
						}
					else
						{
							spriteBatch.begin();
							UIRenderer.drawUIImage(spriteBatch, id, barTexture, 0, 0, 
									(int)(getDimensions().width*progress),(int)(getDimensions().height), scaleTexture, !scaleTexture);
							spriteBatch.end();
						}
				}
			else if (barColor.a > 0f)
				{
					if (isVertical)
						{
							spriteBatch.begin();
							UIRenderer.drawRectangle(spriteBatch,barColor,0,0,getDimensions().width,getDimensions().height*progress);
							spriteBatch.end();
						}
					else
						{
							spriteBatch.begin();
							UIRenderer.drawRectangle(spriteBatch,barColor,0,0,getDimensions().width*progress,getDimensions().height);
							spriteBatch.end();
						}
				}
			

			// Draw text labels
			if (showProgressPercentage || getLabelString() != null)
				{
					spriteBatch.begin();
					font.setColor(drawFontColor);
					if (showProgressPercentage && getLabelString() != null)
						font.draw(spriteBatch, String.format("%3.0f", progress*100f)+" % "+getLabelString(), dims.width*0.1f, (dims.height/2)-font.getDescent());
					else if (showProgressPercentage)
						font.draw(spriteBatch,String.format("%3.0f", progress*100f)+" %", dims.width*0.1f, (dims.height/2)-font.getDescent());
					else 
						font.draw(spriteBatch, getLabelString(), dims.width*0.1f, (dims.height/2)-font.getDescent());
					spriteBatch.end();
				}


		}
	
	@Override
	public void drawAfter(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
	{

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


















}
