package com.arboreantears.fwt.components;

import com.arboreantears.fwt.Direction;
import com.arboreantears.fwt.Fonts;
import com.arboreantears.fwt.Language;
import com.arboreantears.fwt.XMLDataPacket;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;



/** A FWT Label to display text. */
public class FWTLabel extends FWTComponent
{


	//********************************************************************
	//  VARIABLES
	//********************************************************************


	@Override
	public String toString()
	{
		return "Label: "+this.name;
	}

	

	// Font for text
	//-----------------------------
	/** BitmapFont for text drawing. */
	BitmapFont font;
	/** Returns the BitmapFont for text drawing. */
	public BitmapFont getFont() {return font;}
	/** Sets the BitmapFont for text drawing. */
	public void setFont(BitmapFont font) {this.font = font;}

	
	
	// GlyphLayout for Text Rendering
	//-----------------------------
	/** GlyphLayout for text rendering. */
	protected GlyphLayout glyphs;

	
	

	// Text Label on Button
	//-----------------------------
	/** String ID in Language to draw on button. */
	String labelID;
	/** Returns the String label ID in Language to draw on button. */
	public String getLabelID() {return labelID;}
	/** Sets the String label ID in Language to draw on button. */
	public void setLabelID(String labelID) {this.labelID = labelID; this.updateGlyphs(); this.redraw();}

	
	/** String to display instead of Language ID. */
	String labelText;
	/** Returns the String to display instead of Language ID. Null is disabled. */
	public String getLabelText() {return this.labelText;}
	/** Sets the String to display instead of Language ID. Set to Null to disable. */
	public void setLabelText(String text) {this.labelText = text; this.updateGlyphs(); this.redraw();}


	/** Returns the label string used for displaying.*/
	public String getLabelString()
	{
		if (labelText != null)
			return labelText;
		else if (labelID != null)
			return Language.get(labelID);
		return null;
	}	


	// Text Alignment
	//-----------------------------
	/** Text alignment. */
	Direction alignment;
	/** Returns the text alignment of this label: <br>[WEST (left), EAST (right), NORTH (up), SOUTH (down), or UP (center)]. */
	public Direction getTextAlignment() {return alignment;}
	/** Returns the text alignment of this label: <br>[WEST (left), EAST (right), NORTH (up), SOUTH (down), or UP (center)]. */
	public void setTextAlignment(Direction align) {alignment = align;}

	
	
	// Text Padding
	//----------------------------
	/** Number of pixels to pad around the text. */
	int txtPadding;
	/** Returns the number of pixels to pad around the text. */
	public int getTextPadding() {return txtPadding;}
	/** Sets the number of pixels to pad around the text. */
	public void setTextPadding(int tp) {this.txtPadding = tp; this.redraw();}
	
	

	// Font Colors
	//-----------------------------
	/** Currently used font color */
	Color currentFontColor;
	
	/** Normal font color. */
	Color drawFontColor;
	/** Returns this button's normal font color. */
	public Color getFontColor() {return drawFontColor;}
	/** Sets this button's normal font color. */
	public void setFontColor(Color fcolor) {drawFontColor = fcolor;}


	

	// Automatically Adjust Size (Height)
	//-----------------------------
	/** Text auto-adjustment of component height. */
	boolean autoSize;
	/** Returns 'true' if this component automatically adjusts its height to fit the text. */
	public boolean isAutoAdjusting() {return autoSize;}
	/** Sets if this component automatically adjusts its height to fit the text. */
	public void setAutoAdjusting(boolean autoSize) {this.autoSize = autoSize;}
	
	

	// Automatically Word Wrapping
	//-----------------------------
	/** Text word wrapping. */
	boolean wordWrap;
	/** Returns 'true' if this component automatically wraps text that is too long to additional lines. */
	public boolean isWordWrapping() {return wordWrap;}
	/** Sets if this component automatically wraps text that is too long to additional lines. */
	public void setWordWrapping(boolean wordWrap) {this.wordWrap = wordWrap;}

	
	
	



	//********************************************************************
	//  CONSTRUCTORS
	//********************************************************************

	/** Creates a new blank FWTLabel with the given data. */
	public FWTLabel(XMLDataPacket data)
	{
		super(data);
	}
	

	/** Creates a new blank FWTLabel with the given XML target. */
	public FWTLabel(String parent, String object)
	{
		super(parent, object);
	}

	

	//  XML DATA
	//********************************************************************
	//********************************************************************
	
	@Override
	protected void setDefaults()
		{
			super.setDefaults();
			font = Fonts.getFont(22);
			glyphs = new GlyphLayout();
			alignment = Direction.WEST;
			txtPadding = 3;
			currentFontColor = Color.BLACK.cpy();
			drawFontColor = Color.BLACK.cpy();
		}
	
	@Override
	public void applyDataParameters(XMLDataPacket data)
	{
		super.applyDataParameters(data);
		
		if (data != null)
			{
				// Font Color
				//------------------------
				if (data.get("fontcolor") != null)
					{
						drawFontColor = data.getColor("fontcolor");
					}

				// Font Size
				//------------------------
				if (data.get("fontsize") != null)
					{
						font = Fonts.getFont(data.getInt("fontsize"));
					}

				// Text Label ID
				//------------------------
				if (data.get("labelid") != null && !data.get("labelid").equals(""))
					{
						labelID = data.get("labelid");
					}

				// Text Label String
				//------------------------
				if (data.get("labeltext") != null && !data.get("labeltext").equals(""))
					{
						labelText = data.get("labeltext");
					}

				// Auto-Size Adjustment
				//------------------------
				if (data.get("autosize") != null)
					{
						autoSize = data.getBoolean("autosize");
					}

				// Word Wrapping
				//------------------------
				if (data.get("wordwrap") != null)
					{
						wordWrap = data.getBoolean("wordwrap");
					}

				// Text Padding
				//------------------------
				if (data.get("padding") != null)
					{
						txtPadding = data.getInt("padding");
					}

				// Text Alignment
				//------------------------
				if (data.get("textalignment") != null)
					{
						String strAlign = data.get("textalignment");
						if (strAlign.equals("left"))
							alignment = Direction.WEST; // left
						else if (strAlign.equals("right"))
							alignment = Direction.EAST; // right
						else if (strAlign.equals("up"))
							alignment = Direction.NORTH; // top
						else if (strAlign.equals("down"))
							alignment = Direction.SOUTH; // bottom
						else if (strAlign.equals("bottomleft"))
							alignment = Direction.SOUTHWEST; // bottom left
						else if (strAlign.equals("bottomright"))
							alignment = Direction.SOUTHEAST; // bottom right
						else if (strAlign.equals("topleft"))
							alignment = Direction.NORTHWEST; // top left
						else if (strAlign.equals("topright"))
							alignment = Direction.NORTHEAST; // top right
						else alignment = Direction.UP; // center
					}
			}	
	}






	//********************************************************************
	//  RESIZE
	//********************************************************************
	

	@Override
	public void resize(int width, int height)
		{
			super.resize(width,height);
			updateGlyphs();

		}

	
	
	


	//********************************************************************
	//  GLYPH UPDATE
	//********************************************************************
	
	
	
	/** Updates the size and glyph layout attached to this component. */
	public void updateGlyphs()
	{
		// Too verbose
		// if (FWTWindowManager.DEBUG_MODE) GameLog.log("Label: "+this.name+" : Glyphs Updated");
		
		
		// Ensure proper width for text
		if (font != null && getLabelString() != null)
			{
				// Get Alignment
				int align = Align.left;
				switch (alignment)
				{
					case NORTH: // TOP
						align = Align.top; break;
					case SOUTH: // BOTTOM
						align = Align.bottom; break;
					case WEST: // LEFT
						align = Align.left; break;
					case EAST: // RIGHT
						align = Align.right; break;

					case NORTHWEST: // TOP LEFT
						align = Align.topLeft; break;
					case NORTHEAST: // TOP RIGHT
						align = Align.topRight; break;
					case SOUTHWEST: // BOTTOM LEFT
						align = Align.bottomLeft; break;
					case SOUTHEAST: // BOTTOM RIGHT
						align = Align.bottomRight; break;

					default: // CENTER
						align = Align.center; break;
				}

				// Prepare glyphs	
				currentFontColor = drawFontColor;
				font.getData().markupEnabled = true;
				String str = getLabelString();
				if (str == null) str = " ";
				if (!autoSize)
					glyphs.setText(font, str, currentFontColor, dims.width, align, wordWrap);
				else // Auto-Size
					{
						glyphs.setText(font, str, currentFontColor, dims.width, align, wordWrap); 

						int dblPadding = txtPadding * 2;
						// IF label not big enough for text, resize				
						int newHeight = (int)dims.height;
						if (dims.height != (glyphs.height)+dblPadding)
							newHeight = (int) glyphs.height+dblPadding;
						if (newHeight != (int)dims.height)
							{
								this.resize(new Rectangle(dims.x, dims.y, dims.width, newHeight));
							}
					}
				font.getData().markupEnabled = false;
			}
	}




	//********************************************************************
	//  DRAW
	//********************************************************************

	/** Draws the button according to its current properties. */
	public void draw(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
	{
		String label = getLabelString();

		// Draw background
		super.draw(spriteBatch, shapeRenderer);

		// Draw text
		if (font != null && label != null)
			{
				// Draw Label
				spriteBatch.begin();
				font.setColor(Color.WHITE);
				font.getData().markupEnabled = true;

				try {
				switch(alignment)
				{
				case EAST: font.draw(spriteBatch, glyphs, 
						-txtPadding, glyphs.height/2+txtPadding+(dims.height/2)); break;
				case WEST: font.draw(spriteBatch, glyphs, 
						txtPadding, glyphs.height/2+txtPadding+(dims.height/2)); break;							
				case NORTH: font.draw(spriteBatch, glyphs, 
						(dims.width/2)-glyphs.width/2, dims.height-txtPadding); break;					
				case SOUTH: font.draw(spriteBatch, glyphs, 
						(dims.width/2)-glyphs.width/2, txtPadding+glyphs.height); break;

				case NORTHEAST: font.draw(spriteBatch, glyphs, 
						-txtPadding, dims.height-txtPadding); break;
				case NORTHWEST: font.draw(spriteBatch, glyphs, 
						txtPadding, dims.height-txtPadding); break;
				case SOUTHEAST: font.draw(spriteBatch, glyphs, 
						-txtPadding, txtPadding+glyphs.height); break;
				case SOUTHWEST: font.draw(spriteBatch, glyphs, 
						txtPadding, txtPadding+glyphs.height); break;

				case UP: font.draw(spriteBatch, glyphs, 
						txtPadding, glyphs.height/2+txtPadding+(dims.height/2)); break;
				default:
					break;

				}
				}catch (Exception ex) {ex.printStackTrace();}
				font.getData().markupEnabled = false;
				spriteBatch.end();
			}


	}













}
