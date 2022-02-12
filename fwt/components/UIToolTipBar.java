package com.arboreantears.fwt.components;

import com.arboreantears.fwt.FWTController;
import com.arboreantears.fwt.Fonts;
import com.arboreantears.fwt.UIRenderer;
import com.arboreantears.fwt.XMLDataPacket;
import com.arboreantears.fwt.XMLUIReader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;




/** A ToolTip Bar object used for Mouse-Over information. */
public class UIToolTipBar
{



	//******************************************************************************************************************
	// VARIABLES
	//******************************************************************************************************************

	/** Unique ID. */
	long ID;

	/** Tooltip Text */
	String text;
	public void setText(String txt){this.text = txt;}
	GlyphLayout glyphs = new GlyphLayout();

	/** 'True' if should render */
	boolean show;
	/** Set tooltip to show when the ToolTipDelay option has been reached. */
	public void show() {this.show = true; counter = 0f;}
	/** Set tooltip to show immediately. */
	public void showNow() {this.show = true; counter = FWTController.getToolTipDelay();}
	/** Set tooltip to hide from view. */
	public void hide() {this.show = false; this.visible = false;}

	/** 'True' if is visible (actually rendering)*/
	boolean visible;


	/** Counter for tool-tip delay */
	float counter;

	/** Changes the text of the tooltip to the given String and sets the timer to show. */
	public void update(String txt)
	{
		if (txt != null && !txt.isEmpty()) 
			{this.text = txt; show();}
	}




	// Graphical Appearance
	//------------------------------------------------

	/** Whether this drawable component has a rendered border or not. */
	protected boolean hasBorder = true;
	/** Returns 'true' if this drawable component has a rendered border. */
	public boolean hasBorder() {return hasBorder;}
	/** Set if this drawable component has a rendered border. */
	public void setBorder(boolean border) {hasBorder = border;}


	/** Border color. */
	protected Color borderColor = Color.DARK_GRAY;
	/** Returns this drawable component's border color. */
	public Color getBorderColor() {return borderColor;}
	/** Sets this drawable component's border color. */
	public void setBorderColor(Color bcolor) {borderColor = bcolor;}

	/** Normal background color for this drawable component. */
	protected Color backgroundColor = Color.GRAY;
	/** Returns this drawable component's background color. */
	public Color getBackgroundColor() {return backgroundColor;}
	/** Sets this drawable component's background color. */
	public void setBackgroundColor(Color bcolor) {backgroundColor = bcolor;}

	/** Image ID for a texture-based drawable component.  Overrides the background color. Null is no texture. */
	String bgTexture = null;
	/** Returns this drawable component's image texture ID. */
	public String getTexture() {return bgTexture;}
	/** Sets this drawable component's image texture ID. Set to 'null' to use background coloring instead. */
	public void setTexture(String imgID) {bgTexture = imgID;}

	

	// Font for text
	//-----------------------------
	/** BitmapFont for text drawing. */
	BitmapFont font;
	/** Returns the BitmapFont for text drawing. */
	public BitmapFont getFont() {return font;}
	/** Sets the BitmapFont for text drawing. */
	public void setFont(BitmapFont font) {this.font = font;}




	// Component Data
	//---------------------------
	/** Data saved in this component.  Includes XML data during creation. */
	protected XMLDataPacket data = new XMLDataPacket();
	/** Returns the data saved in this component.  Includes XML data during creation. */
	public XMLDataPacket getData() {return data;}
	/** Sets the data saved in this component.  Should include XML data during creation. */
	public void setData(XMLDataPacket data) {this.data = data;}




	
	
	








	//******************************************************************************************************************
	// CONSTRUCTOR
	//******************************************************************************************************************

	/** Creates a new empty tool-tip bar. */
	public UIToolTipBar()
	{
		this.text = "";
		this.ID = FWTController.generateObjectTag();

		XMLDataPacket data = XMLUIReader.getUISpecs("tooltipBar", "tooltipBar");

		// IF valid data packet
		//--------------------------------------
		try{if (data != null)
			{
				this.data = data;

				// BORDER
				//========================
				if (data.get("border") != null)
					this.hasBorder = data.getBoolean("border");

				// BORDER COLOR
				//========================
				if (data.get("bordercolor") != null)
					this.borderColor = data.getColor("bordercolor");

				// BACKGROUND COLOR
				//========================
				if (data.get("backgroundcolor") != null)
					this.backgroundColor = data.getColor("backgroundcolor");

				// BACKGROUND TEXTURE
				//========================
				if (data.get("backgroundtexture") != null)
					this.bgTexture = data.get("backgroundtexture");

				// FONT SIZE
				//========================
				if (data.get("fontsize") != null)
					{
						font = Fonts.getFont(data.getInt("fontsize"));
					}
			}
		// ELSE not valid data packet
		//--------------------------------------
		else {
			font = Fonts.getFont(20);
			throw new Exception();
		}
		}catch(Exception ex){
			FWTController.error("Invalid XML Data for UI: 'tooltipBar'. Using defaults.");
		}
	}





	//******************************************************************************************************************
	// RENDER
	//******************************************************************************************************************

	public void render(SpriteBatch spriteBatch, float delta)
	{
		if (show)
			{
				// IF visible (counter fired) OR 'show tool-tip button' is pressed
				if (visible || FWTController.isShowTooltipKeyDown())
					{
						int mX = Gdx.input.getX();
						int mY = Gdx.graphics.getHeight()-Gdx.input.getY();

						// Get Font
						font.setColor(Color.BLACK);
						// Enable mark-up
						font.getData().markupEnabled = true;
						// Get Width and Height of Text
						glyphs.setText(font, text);

						float width = glyphs.width;
						float height = glyphs.height;

						// IF not enough room to the right
						if (mX+width+46f > Gdx.graphics.getWidth())
							// adjust to make it appear on the left of the cursor
							mX = (int) (mX - (width+40f));

						// IF not enough room to the top
						if (mY+height+46f > Gdx.graphics.getHeight())
							// adjust to make it appear below the cursor
							mY = (int) (mY - (height+40f));

						
						
						// IF has background
						if (bgTexture == null || bgTexture.isEmpty() || !UIRenderer.hasUIImage(bgTexture))
							{
								// Draw fill color
								if (backgroundColor.a > 0f)
									{
										spriteBatch.begin();
										UIRenderer.drawRectangle(spriteBatch, backgroundColor, mX+15,mY-10,width+20,height+10);
										UIRenderer.drawThinBorder(this.ID, spriteBatch, borderColor, mX+15,mY-10,width+20,height+10);
										spriteBatch.end();
									}
							}
						// ELSE has a texture image
						else
							{
								spriteBatch.begin();
								UIRenderer.drawUIImage(spriteBatch, this.ID, bgTexture, mX+15, mY-10, ((int)width)+20, ((int)height)+10, true, false);
								spriteBatch.end();
							}

						// Set Text Color (black)
						font.setColor(Color.BLACK);

						// Draw String
						spriteBatch.begin();
						font.draw(spriteBatch, glyphs, mX+23, mY+(height)-5);
						spriteBatch.end();

						// Disable mark-up
						font.getData().markupEnabled = false;
					}
				// ELSE not visible, counting
				else
					{
						counter = counter + delta;
						if (counter > FWTController.getToolTipDelay())
							visible = true;
					}
			}

	}





	//******************************************************************************************************************
	// UTILITY METHODS
	//******************************************************************************************************************








}























