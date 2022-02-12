package com.arboreantears.fwt.components;

import java.util.ArrayList;

import com.arboreantears.fwt.Direction;
import com.arboreantears.fwt.FWTColors;
import com.arboreantears.fwt.FWTController;
import com.arboreantears.fwt.Fonts;
import com.arboreantears.fwt.XMLDataPacket;
import com.arboreantears.fwt.events.FWTInputException;
import com.arboreantears.fwt.events.FWTInputReceiver;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;

/** A graphical value slider object with a designated integer range from MIN [0] to MAX [10].
 *     Each slider position separated by 1. */
public class FWTSlider extends FWTContainer
{



	//  VARIABLES
	//********************************************************************
	//********************************************************************


	@Override
	public String toString()
		{
			return "Slider: "+this.name;
		}



	/** Current value of the slider. */
	int currentValue;
	/** Return the current value of the slider. */
	public int getCurrentValue() {return currentValue;}
	/** Sets the current value of the slider and redraws the component. */
	public void setCurrentValue(int v) 
		{
			// Keep within range
			if (v < minValue || v > maxValue)
				return;

			currentValue = v;
			resizeSliderComponents();
			updateBarPosition();
		}

	/** Minimum value of the slider. */
	int minValue;
	/** Return the minimum value of the slider. */
	public int getMinValue() {return minValue;}
	/** Sets the minimum value of the slider and redraws the component. */
	public void setMinValue(int mv) 
		{
			if (mv < maxValue)
				{minValue = mv; setResize();}
			if (minValue > currentValue)
				setCurrentValue(minValue);
		}

	/** Maximum value of the slider. */
	int maxValue;
	/** Return the maximum value of the slider. */
	public int getMaxValue() {return maxValue;}
	/** Sets the maximum value of the slider and redraws the component. */
	public void setMaxValue(int mv) 
		{
			if (mv > minValue)
				{maxValue = mv; setResize();}
			if (maxValue < currentValue)
				setCurrentValue(maxValue);
		}




	/** Label strings for values. */
	ArrayList<String> valueStrings;
	/** Return the value labels of the slider. */
	public ArrayList<String> getValueStrings() {return valueStrings;}
	/** Sets the value labels of the slider and redraws the component. */
	public void setValueStrings(ArrayList<String> vs) 
		{
			valueStrings = vs; 
			currentLabel.setLabelText(getCurrentValueString());
			resizeSliderComponents();
		}

	/** Returns the string of the current value.  By default returns a String of the Integer value.
	 * Can be overidden for more complex feedback. If valueStrings are set, will return the value string. */
	public String getCurrentValueString()
		{
			if (valueStrings != null)
				return valueStrings.get(currentValue);
			else return Integer.toString(currentValue);
		}



	/** 'True' if this value-slider is vertically oriented (false is horizontal). */
	boolean orientation;
	/** 'True' if this value-slider is vertically oriented  (false is horizontal). */
	public boolean isVerticallyOriented() {return orientation;}
	/** Set if this value-slider can be scrolled vertically (false is horizontal). */
	public void setVerticalOrientation(boolean vert){orientation = vert; this.redraw();}



	/** Size of the slider bar in pixels. */
	int barSize;
	/** Sets the size of the slider bar in pixels. */
	public void setBarSize(int size) {barSize = size; this.redraw();}
	/** Returns the size of the slider bar in pixels. */
	public int getBarSize() {return barSize;}

	/** Padding around components in pixels. */
	int padding;
	/** Sets the padding around components in pixels. */
	public void setPadding(int p) {padding = p; this.redraw();}
	/** Returns the padding around components in pixels. */
	public int getPadding() {return padding;}




	// Slider Bar Components
	//----------------------------------

	/** Text label for the slider */
	FWTLabel txtLabel;
	/** Returns the text label for this slider. */
	public FWTLabel getTextLabel() {return txtLabel;}

	/** Text label for the min slider value. */
	FWTLabel minLabel;
	/** Returns the min value label for this slider. */
	public FWTLabel getMinLabel() {return minLabel;}

	/** Text label for the max slider value. */
	FWTLabel maxLabel;
	/** Returns the max value label for this slider. */
	public FWTLabel getMaxLabel() {return maxLabel;}

	/** Text current value label for the current slider value. */
	FWTLabel currentLabel;
	/** Returns the text label for this slider. */
	public FWTLabel getCurrentLabel() {return currentLabel;}

	/** Component for the horizontal bar. */
	FWTComponent sliderBar;
	/** Returns the slider bar for this slider. */
	public FWTComponent getSliderBar() {return sliderBar;}

	/** Button for the center bar button. */
	FWTButton barButton;
	/** Returns the bar button for this slider. */
	public FWTButton getBarButton() {return barButton;}




	// Wheel Scrolling
	//----------------------------------

	/** 'True' if this slider can be scrolled with the mouse wheel. */
	boolean wheelScrolling;
	/** Returns 'True' if this slider can be scrolled with the mouse wheel. */
	public boolean isWheelScrolling() {return wheelScrolling;}
	/**Sets if this slider can be scrolled with the mouse wheel. */
	public void setWheelScrolling(boolean ws) {wheelScrolling = ws;}


	// Key Scrolling
	//----------------------------------

	/** 'True' if this slider can be scrolled with keyboard input. */
	boolean keyScrolling;
	/** Returns 'True' if this slider can be scrolled with keyboard input. */
	public boolean isKeyScrolling() {return keyScrolling;}
	/**Sets if this slider can be scrolled with keyboard input. */
	public void setKeyScrolling(boolean ks) {keyScrolling = ks;}











	//  CONSTRUCTOR
	//********************************************************************
	//********************************************************************

	/** Create a new FWTSlider with the given XML data packet. */
	public FWTSlider(XMLDataPacket data)
		{
			super(data);

			// Create input receiver
			createInputReceiver();			

		}

	/** Create a new FWTSlider with the given XML target. */
	public FWTSlider(String parent, String object)
		{
			super(parent, object);

			// Create input receiver
			createInputReceiver();			

		}



	//  XML DATA
	//********************************************************************
	//********************************************************************

	@Override
	protected void setDefaults()
		{
			super.setDefaults();
			currentValue = 0;
			minValue = 0;
			maxValue = 10;
			barSize = 20;
			padding = 3;			
		}


	@Override
	public void applyDataParameters(XMLDataPacket data)
		{
			super.applyDataParameters(data);

			// Slider Bar
			XMLDataPacket xml = new XMLDataPacket();
			xml.put("name", this.name+":"+"sliderBar");
			xml.put("position", "1|1");
			xml.put("width", "1");
			xml.put("height", "1");

			if (sliderBar != null) sliderBar.dispose();
			sliderBar = new FWTComponent(xml);
			this.addComponent(sliderBar);
			sliderBar.setInputReceiver(new FWTInputReceiver()
				{
					@Override
					public boolean touchDown(int mX, int mY, int pointer, int button) throws FWTInputException
						{
							// Horizontal
							if (!orientation)
								{ 
									if (mX < barButton.dims.x)
										{setCurrentValue(getCurrentValue()-1); onCurrentValueUpdate();}
									if (mX > barButton.dims.x+barButton.dims.width)
										{setCurrentValue(getCurrentValue()+1); onCurrentValueUpdate();}
									return true;
								}

							// Vertical
							else
								{
									if (mY < barButton.getDimensions().y)
										{setCurrentValue(getCurrentValue()-1); onCurrentValueUpdate();}
									if (mY > barButton.getDimensions().y)	
										{setCurrentValue(getCurrentValue()+1); onCurrentValueUpdate();}
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
			xml.put("width", "1");
			xml.put("height", "1");

			if (barButton != null) barButton.dispose();
			barButton = new FWTButton(xml);
			this.addComponent(barButton);
			barButton.addInputReceiver(new FWTInputReceiver()
				{
					@Override
					public boolean touchDragged(int mX, int mY, int pointer) throws FWTInputException
						{ 							
							if (!orientation)
								{
									mX = mX - barButton.getParentOffsetX();
									int cV = (int)(((mX + (barButton.dims.width/2f))/sliderBar.dims.width) * (float)(maxValue-minValue)) + minValue;
									setCurrentValue(cV);;		
									updateBarPosition(); 
									onCurrentValueUpdate();
								}
							else
								{
									mY = mY - barButton.getParentOffsetY();
									int cV = (int)(((mY + (barButton.dims.height/2f))/sliderBar.dims.height) * (float)(maxValue-minValue)) + minValue;
									setCurrentValue(cV);;		
									updateBarPosition(); 
									onCurrentValueUpdate();
								}
							return true; 
						}

				});


			// Text Label
			xml = new XMLDataPacket();
			xml.put("name", this.name+":"+"txtLabel");
			xml.put("position", "1|1");
			xml.put("width", "1");
			xml.put("height", "1");

			if (txtLabel != null) txtLabel.dispose();
			txtLabel = new FWTLabel(xml);
			txtLabel.setAutoAdjusting(false);
			txtLabel.setTextAlignment(Direction.WEST);
			txtLabel.setBorderColor(FWTColors.INVISIBLE);
			txtLabel.setHighlightBorderColor(FWTColors.INVISIBLE);
			txtLabel.setBackgroundColor(FWTColors.INVISIBLE);
			txtLabel.setHighlightColor(FWTColors.INVISIBLE);
			this.addComponent(txtLabel);

			// Minimum Number Label
			xml = new XMLDataPacket();
			xml.put("name", this.name+":"+"minLabel");
			xml.put("position", "1|1");
			xml.put("width", "1");
			xml.put("height", "1");

			if (minLabel != null) minLabel.dispose();
			minLabel = new FWTLabel(xml);
			minLabel.setAutoAdjusting(false);
			minLabel.setTextAlignment(Direction.WEST);
			minLabel.setBorderColor(FWTColors.INVISIBLE);
			minLabel.setHighlightBorderColor(FWTColors.INVISIBLE);
			minLabel.setBackgroundColor(FWTColors.INVISIBLE);
			minLabel.setHighlightColor(FWTColors.INVISIBLE);
			minLabel.disable();
			this.addComponent(minLabel);

			// Maximum Number Label
			xml = new XMLDataPacket();
			xml.put("name", this.name+":"+"maxLabel");
			xml.put("position", "1|1");
			xml.put("width", "1");
			xml.put("height", "1");

			if (maxLabel != null) maxLabel.dispose();
			maxLabel = new FWTLabel(xml);
			maxLabel.setAutoAdjusting(false);
			maxLabel.setTextAlignment(Direction.WEST);
			maxLabel.setBorderColor(FWTColors.INVISIBLE);
			maxLabel.setHighlightBorderColor(FWTColors.INVISIBLE);
			maxLabel.setBackgroundColor(FWTColors.INVISIBLE);
			maxLabel.setHighlightColor(FWTColors.INVISIBLE);
			maxLabel.disable();
			this.addComponent(maxLabel);

			// Current Value Label
			xml = new XMLDataPacket();
			xml.put("name", this.name+":"+"currentLabel");
			xml.put("position", "1|1");
			xml.put("width", "1");
			xml.put("height", "1");

			if (currentLabel != null) currentLabel.dispose();
			currentLabel = new FWTLabel(xml);
			currentLabel.setAutoAdjusting(false);
			currentLabel.setTextAlignment(Direction.UP);
			currentLabel.setBorderColor(FWTColors.INVISIBLE);
			currentLabel.setHighlightBorderColor(FWTColors.INVISIBLE);
			currentLabel.setBackgroundColor(FWTColors.INVISIBLE);
			currentLabel.setHighlightColor(FWTColors.INVISIBLE);
			this.addComponent(currentLabel);

			try{if (data != null)
				{
					// DATA
					//----------------------------------


					// ORIENTATION
					//========================
					if (data.get("orientation") != null)
						{
							if (data.get("orientation").contains("vert"))
								this.orientation = true;
							else this.orientation = false;
						}

					// BAR SIZE
					//========================
					if (data.get("barsize") != null)
						{
							this.barSize = data.getInt("barsize");
						}

					// COMPONENT PADDING
					//========================
					if (data.get("padding") != null)
						{
							this.padding = data.getInt("padding");
						}

					// KEY SCROLLING
					//========================
					if (data.get("keyscrolling") != null)
						{
							this.keyScrolling = data.getBoolean("keyscrolling");
						}

					// WHEEL SCROLLING
					//========================
					if (data.get("wheelscrolling") != null)
						{
							this.wheelScrolling = data.getBoolean("wheelscrolling");
						}
					

					// VALUE RANGE
					//========================
					if (data.get("valuerange") != null)
						{
							ArrayList<String> vals = data.getList("valuerange");
							this.minValue = Integer.parseInt(vals.get(0));
							int mx = Integer.parseInt(vals.get(1));
							if (mx > this.minValue)
								this.maxValue = mx;
							else throw new Exception("Max value must be greater than minimum value.");
						}


					// CURRENT VALUE
					//========================
					if (data.get("value") != null)
						{
							int v = data.getInt("value");
							if (v >= minValue && v <= maxValue)
								this.currentValue = v;
							else throw new Exception("Current value must be set within min and max.");
						}



					// Labels
					//----------------------------------


					// FONT SIZE
					//========================
					if (data.get("fontsize") != null)
						{
							this.txtLabel.font = Fonts.getFont(data.getInt("fontsize"));	
							this.minLabel.font = Fonts.getFont(data.getInt("fontsize"));	
							this.maxLabel.font = Fonts.getFont(data.getInt("fontsize"));	
							this.currentLabel.font = Fonts.getFont(data.getInt("fontsize"));							
						}					

					// LABEL FONT SIZE
					//========================
					if (data.get("labelfontsize") != null)
						{
						this.txtLabel.font = Fonts.getFont(data.getInt("labelfontsize"));
						}

					// NUMBER FONT SIZE
					//========================
					if (data.get("numfontsize") != null)
						{
							this.minLabel.font = Fonts.getFont(data.getInt("numfontsize"));	
							this.maxLabel.font = Fonts.getFont(data.getInt("numfontsize"));	
							this.currentLabel.font = Fonts.getFont(data.getInt("numfontsize"));					
						}

					// FONT COLOR
					//========================
					if (data.get("fontcolor") != null)
						{
							this.txtLabel.drawFontColor = data.getColor("fontcolor");
							this.minLabel.drawFontColor = data.getColor("fontcolor");
							this.maxLabel.drawFontColor = data.getColor("fontcolor");
							this.currentLabel.drawFontColor = data.getColor("fontcolor");
						}

					// LABEL FONT COLOR
					//========================
					if (data.get("labelfontcolor") != null)
						{
							this.txtLabel.drawFontColor = data.getColor("labelfontcolor");
						}

					// NUMBER FONT COLOR
					//========================
					if (data.get("numfontcolor") != null)
						{
							this.minLabel.drawFontColor = data.getColor("numfontcolor");
							this.maxLabel.drawFontColor = data.getColor("numfontcolor");
							this.currentLabel.drawFontColor = data.getColor("numfontcolor");
						}

					// LABEL ID
					//========================
					if (data.get("labelid") != null)
						{
							this.txtLabel.labelID = data.get("labelid");
						}

					// LABEL STRING
					//========================
					if (data.get("labelstring") != null)
						{
							this.txtLabel.labelText = data.get("labelstring");
						}

					// NUMBER LABELS
					//========================
					if (data.get("numlabels") != null)
						{
							if (data.getBoolean("numlabels"))
								{
									minLabel.enable();
									maxLabel.enable();
								}
						}

					// SHOW NUMBER LABELS
					//========================
					if (data.get("shownumbers") != null)
						{
							if (data.getBoolean("shownumbers"))
								{
									minLabel.enable();
									maxLabel.enable();
									currentLabel.enable();
								}
							else {currentLabel.disable();}
						}

					// VALUE STRINGS
					//========================
					if (data.get("valuestrings") != null)
						{
							valueStrings = data.getList("valuestrings");
							currentLabel.enable();
							currentLabel.setLabelText(valueStrings.get(currentValue));
						}



					// Center Button
					//----------------------------------


					// BUTTON BORDER TEXTURE
					//========================
					if (data.get("buttonbordertexture") != null)
						{
							barButton.borderTexture = data.get("buttonbordertexture");
						}

					// BUTTON BORDER COLOR
					//========================
					if (data.get("buttonbordercolor") != null)
						{
							barButton.borderColor = data.getColor("buttonbordercolor");
						}

					// BUTTON BACKGROUND COLOR
					//========================
					if (data.get("buttonbackgroundcolor") != null)
						{
							barButton.backgroundColor = data.getColor("buttonbackgroundcolor");
						}

					// BUTTON HIGHLIGHT COLOR
					//========================
					if (data.get("buttonhighlightcolor") != null)
						{
							barButton.highlightColor = data.getColor("buttonhighlightcolor");
						}

					// BUTTON PRESSED COLOR
					//========================
					if (data.get("buttonpressedcolor") != null)
						{
							barButton.pressedColor = data.getColor("buttonpressedcolor");
						}

					// BUTTON BACKGROUND TEXTURE
					//========================
					if (data.get("buttonbackgroundtexture") != null)
						{
							barButton.bgTexture = data.get("buttonbackgroundtexture");
						}

					// BUTTON HIGHLIGHT TEXTURE
					//========================
					if (data.get("buttonhighlighttexture") != null)
						{
							barButton.highlightTexture = data.get("buttonhighlighttexture");
						}

					// BUTTON PRESSED TEXTURE
					//========================
					if (data.get("buttonpressedtexture") != null)
						{
							barButton.pressedTexture = data.get("buttonpressedtexture");
						}

					// BUTTON SCALE TEXTURE
					//========================
					if (data.get("buttontexturefill") != null)
						{
							barButton.scaleTexture = data.get("buttontexturefill").equals("stretched") ? true : false;
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
						}



					// Bars
					//----------------------------------

					// BAR BORDER TEXTURE
					//========================
					if (data.get("barbordertexture") != null)
						{
							sliderBar.borderTexture = data.get("barbordertexture");
						}

					// BAR BORDER COLOR
					//========================
					if (data.get("barbordercolor") != null)
						{
							sliderBar.borderColor = data.getColor("barbordercolor");
						}

					// BAR BACKGROUND COLOR
					//========================
					if (data.get("barbackgroundcolor") != null)
						{
							sliderBar.backgroundColor = data.getColor("barbackgroundcolor");
						}

					// BAR HIGHLIGHT COLOR
					//========================
					if (data.get("barhighlightcolor") != null)
						{
							sliderBar.highlightColor = data.getColor("barhighlightcolor");
						}

					// BAR BACKGROUND TEXTURE
					//========================
					if (data.get("barbackgroundtexture") != null)
						{
							sliderBar.bgTexture = data.get("barbackgroundtexture");
						}

					// BAR HIGHLIGHT TEXTURE
					//========================
					if (data.get("barhighlighttexture") != null)
						{
							sliderBar.highlightTexture = data.get("barhighlighttexture");
						}

					// BAR SCALE TEXTURE
					//========================
					if (data.get("bartexturefill") != null)
						{
							sliderBar.scaleTexture = data.get("bartexturefill").equals("stretched") ? true : false;
						}

					// BAR BORDER
					//========================
					if (data.get("barborder") != null)
						{
							if (!data.getBoolean("barborder"))	
								{
									sliderBar.setBorderColor(FWTColors.INVISIBLE);
									sliderBar.setBorderTexture(null);
								}
						}
				}
			}catch(Exception ex){
				FWTController.error("Invalid XML Data for UI:Slider: '"+this.name+"'. Using defaults.");
			}
		}








	//  RESIZE
	//********************************************************************
	//********************************************************************

	@Override
	public void resize(int width, int height)
		{		
			super.resize(width, height);
			resizeSliderComponents();
		}



	//  ON ADDED
	//********************************************************************
	//********************************************************************

	@Override
	public void onAdded()
		{		
			super.onAdded();
			resizeSliderComponents();
		}





	//  RESIZE SLIDER COMPONENTS
	//********************************************************************
	//********************************************************************

	/** Define the display properties of the slider components. */
	private void resizeSliderComponents()
		{
			int width = (int)this.dims.width;
			int height = (int)this.dims.height;
			int numSize= (int) (currentLabel.getFont().getLineHeight()-currentLabel.font.getDescent());
			if (!currentLabel.enabled) numSize = 0;

			// Determine if label is visible
			int lblSize;
			if (txtLabel.getLabelString() == null) 
				{
					lblSize = 0;
					txtLabel.disable();
				}
			else {
				txtLabel.enable();
				lblSize = (int) (txtLabel.getFont().getLineHeight()-txtLabel.font.getDescent());
			}


			// Horizontal Slider
			//------------------------
			// Minimum height = padding + numFontSize + barSize + lblFontSize + 3;
			// Minimum width = padding + barWidth + padding;
			if (!orientation)
				{
					// Slider Bar
					int sliderBarHeight;
					if (txtLabel.enabled) sliderBarHeight = height - padding - lblSize - padding - padding - numSize - padding;
					else sliderBarHeight = height - padding - numSize - padding - padding;

					if (sliderBarHeight < 0) {sliderBarHeight = 10; FWTController.error("Invalid Size for UI: '"+this.name+"'. Component height too small.");}
					sliderBar.setDimensions(new Rectangle(padding, 
							padding+numSize+padding,
							width - padding - padding,
							sliderBarHeight));	
					sliderBar.pushDimensionsToData();
					sliderBar.setResize();
					sliderBar.redraw();

					// Label Text
					if (txtLabel.enabled)
						{
							txtLabel.updateGlyphs();
							txtLabel.setDimensions(new Rectangle(padding, 
									padding+numSize+padding+sliderBarHeight+padding,
									width - padding - padding,
									lblSize));	
							txtLabel.pushDimensionsToData();
							txtLabel.setResize();
							txtLabel.redraw();
						}

					// Min Label Text
					if (minLabel.enabled)
						{
							minLabel.updateGlyphs();
							minLabel.setDimensions(new Rectangle(padding, 
									padding,
									minLabel.glyphs.width+padding+padding,
									numSize));	
							minLabel.setLabelText(minValue+"");
							minLabel.pushDimensionsToData();
							minLabel.setResize();
							minLabel.redraw();
						}

					// Max Label Text
					if (maxLabel.enabled)
						{
							maxLabel.updateGlyphs();
							maxLabel.setDimensions(new Rectangle(width-(maxLabel.glyphs.width+padding+padding), 
									padding,
									maxLabel.glyphs.width+padding+padding,
									numSize));	
							maxLabel.setLabelText(maxValue+"");
							maxLabel.pushDimensionsToData();
							maxLabel.setResize();
							maxLabel.redraw();
						}

					// Current Value Label Text
					if (currentLabel.enabled)
						{
							if (valueStrings != null) currentLabel.setLabelText(valueStrings.get(currentValue));
							else currentLabel.setLabelText(getCurrentValueString());
							currentLabel.updateGlyphs();
							currentLabel.setDimensions(new Rectangle((width/2)-((currentLabel.glyphs.width+padding+padding)/2), 
									padding,
									currentLabel.glyphs.width+padding+padding,
									numSize));	
							currentLabel.pushDimensionsToData();
							currentLabel.setResize();
							currentLabel.redraw();
						}

					// Slider Bar Button
					int barWidth = Math.max((int)((float)width/(float)(maxValue-minValue)),10);

					barButton.setDimensions(new Rectangle(barButton.dims.x, 
							padding+numSize+padding,
							barWidth,
							sliderBarHeight));	
					barButton.pushDimensionsToData();
					barButton.setResize();
					updateBarPosition();
					barButton.redraw();

				}


			// Vertical Slider
			//------------------------
			// Minimum height = 3 + max(barSize,labelWidth) + 3;
			// Minimum width = 3 + barHeight + numWidths + 3;
			else
				{
					//TODO: FWTSlider: Vertical Bars.

				}

			this.redraw();
			this.setResize();
		}




	//  UPDATE BAR POSITION
	//********************************************************************
	//********************************************************************

	/** Updates the position of the bar button on the slider, such as when the current position has been altered. */
	public void updateBarPosition()
		{
			// HORIZONTAL
			if (!orientation)
				{
					int barWidth = (int)barButton.getDimensions().width;
					int sliderWidth = (int)sliderBar.dims.width - (barWidth/2);
					float ratio = ((float)(currentValue - minValue)/(float)(maxValue - minValue));

					int barX = (int)(sliderWidth * ratio) - (barWidth/2);
					barButton.setDimensions(new Rectangle(barX, 
							barButton.dims.y,
							barButton.dims.width,
							barButton.dims.height));
				}

			// VERTICAL
			else
				{
					int barHeight = (int)barButton.getDimensions().height;
					int sliderHeight = (int)sliderBar.dims.height - (barHeight/2);
					float ratio = ((float)(currentValue - minValue)/(float)(maxValue - minValue));

					int barY = (int)(sliderHeight * ratio) - (barHeight/2);
					barButton.setDimensions(new Rectangle(barButton.dims.x, 
							barY,
							barButton.dims.width,
							barButton.dims.height));
				}

			// Update bar values, and resize/redraw
			barButton.pushDimensionsToData();
			barButton.setResize();
			barButton.redraw();
			this.redraw();
		}




	//  SLIDER FUNCTION
	//********************************************************************
	//********************************************************************

	/** Called whenever the current value of the slider was changed by user input.
	 * <br> Expected to be overridden for more complex behavior. */
	public void onCurrentValueUpdate() {};








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

					@Override
					public boolean scrolled(float amountX, float amountY) throws FWTInputException 
						{
							if (wheelScrolling)
								{
									if (amountY > 0)
										setCurrentValue(getCurrentValue()-1);
									else
										setCurrentValue(getCurrentValue()+1);

									onCurrentValueUpdate();
									return true;
								}
							return false;
						}



					//  KEYBOARD INPUT
					//********************************************************************


					// @Override
					public boolean keyDown(int keycode) throws FWTInputException 
						{
							if (keyScrolling)
								{
									// Vertical
									if (orientation)
										{
											if (keycode == Keys.DOWN)
												{setCurrentValue(getCurrentValue()-1); onCurrentValueUpdate(); return true;}
											if (keycode == Keys.UP)
												{setCurrentValue(getCurrentValue()+1); onCurrentValueUpdate(); return true;}
										}
									else // Horizontal
										{
											if (keycode == Keys.LEFT)
												{setCurrentValue(getCurrentValue()-1); onCurrentValueUpdate(); return true;}
											if (keycode == Keys.RIGHT)
												{setCurrentValue(getCurrentValue()+1); onCurrentValueUpdate(); return true;}
										}

									// Minimum
									if (keycode == Keys.PAGE_UP || keycode == Keys.HOME)
										{setCurrentValue(getMinValue()); onCurrentValueUpdate(); return true;}

									// Maximum
									if (keycode == Keys.PAGE_DOWN || keycode == Keys.END)
										{setCurrentValue(getMaxValue()); onCurrentValueUpdate(); return true;}
								}
							return false;
						}

				});
		}























}
