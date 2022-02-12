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



/** A button object with a label text. */
public class FWTTextButton extends FWTButton
{


	//********************************************************************
	//  VARIABLES
	//********************************************************************


	@Override
	public String toString()
		{
			return "TextButton: "+this.name;
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

	/** Highlight font color when mouse is over button. */
	Color highlightFontColor;
	/** Returns this button's highlight font color. */
	public Color getFontHightlightColor() {return highlightFontColor;}
	/** Sets this button's highlight font color. */
	public void SetFontHightlightColor(Color hcolor) {highlightFontColor = hcolor;}

	/** Font color when button is being pressed. */
	Color pressedFontColor;
	/** Returns this button's pressed font color. */
	public Color getFontPressedColor() {return pressedFontColor;}
	/** Sets this button's pressed font color. */
	public void setFontPressedColor(Color pcolor) {pressedFontColor = pcolor;}


	// Automatically Adjust Size
	//-----------------------------
	/** Text auto-adjustment of size. */
	boolean autoSize;
	/** Returns 'true' if this component automatically adjusts its size to fit the text. */
	public boolean isAutoAdjusting() {return autoSize;}
	/** Sets if this component automatically adjusts its size to fit the text. */
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
	//  CONSTRUCTOR
	//********************************************************************


	/** Creates a new FWTTextButton with the given data. */
	public FWTTextButton(XMLDataPacket data)
		{
			super(data);
		}
	
	/** Creates a new FWTTextButton with the given XML target. */
	public FWTTextButton(String parent, String object)
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
			
			font = Fonts.getFont(22);
			glyphs = new GlyphLayout();
			alignment = Direction.UP;
			txtPadding = 3;
			currentFontColor = Color.BLACK.cpy();
			drawFontColor = Color.BLACK.cpy();
			highlightFontColor = Color.BLACK.cpy();
			pressedFontColor = Color.BLACK.cpy();
			
		}

	@Override
	public void applyDataParameters(XMLDataPacket data)
		{
			super.applyDataParameters(data);

			if (data != null)
				{
					// Font Color
					if (data.get("fontcolor") != null)
						{
							drawFontColor = data.getColor("fontcolor");
							currentFontColor = drawFontColor.cpy();
							highlightFontColor = drawFontColor.cpy();
							pressedFontColor = drawFontColor.cpy();
						}

					// Highlight Font Color
					if (data.get("highlightfontcolor") != null)
						{
							highlightFontColor = data.getColor("highlightfontcolor");
						}

					// Pressed Font Color
					if (data.get("pressedfontcolor") != null)
						{
							pressedFontColor = data.getColor("pressedfontcolor");
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

					// Text Label Text
					if (data.get("labeltext") != null)
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
	//  UPDATE
	//********************************************************************


	@Override
	public void update()
		{
			super.update();


			if (this.needsRedrawn)
				{
					// Ensure proper width for text
					if (font != null && getLabelString() != null)
						{
							// Get Alignment
							int align = Align.center;
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

							// Update color
							if (pressed)
								currentFontColor = pressedFontColor;
							else if (mouseOver)
								currentFontColor = highlightFontColor;
							else
								currentFontColor = drawFontColor;

							// Prepare glyphs	
							font.getData().markupEnabled = true;
							if (!autoSize)
								glyphs.setText(font, getLabelString(), currentFontColor, this.getDimensions().width, align, true);
							else // Auto-Size
								{
									glyphs.setText(font, getLabelString()); 

									int dblPadding = txtPadding * 2;
									// IF label not big enough for text, resize						
									int newWidth = (int)this.getDimensions().width;
									int newHeight = (int)this.getDimensions().height;
									if (this.getDimensions().width != glyphs.width+dblPadding)
										newWidth = (int) glyphs.width+dblPadding;
									if (this.getDimensions().height != font.getLineHeight()+dblPadding)
										newHeight = (int) font.getLineHeight()+dblPadding;
									if (newWidth != (int)this.getDimensions().width || newHeight != (int)this.getDimensions().height)
										this.resize(new Rectangle(dims.x, dims.y, newWidth, newHeight));

									glyphs.setText(font, getLabelString(), currentFontColor, this.getDimensions().width, align, true);
								}
							font.getData().markupEnabled = false;


						}
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
					font.getData().markupEnabled = false;
					spriteBatch.end();
				}


		}

















}
