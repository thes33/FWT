package com.arboreantears.fwt.components;

import com.arboreantears.fwt.FWTController;
import com.arboreantears.fwt.FWTWindowManager;
import com.arboreantears.fwt.Fonts;
import com.arboreantears.fwt.Language;
import com.arboreantears.fwt.UIRenderer;
import com.arboreantears.fwt.XMLDataPacket;
import com.arboreantears.fwt.events.FWTInputException;
import com.arboreantears.fwt.events.FWTInputReceiver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.GlyphLayout.GlyphRun;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.TimeUtils;



/** A text field object for inputing text. */
public class FWTTextField extends FWTComponent
{
	// Much of this code was modified from libGDX TextField class, Much thanks to authors: mzechner & Nathan Sweet */


	//********************************************************************
	//  VARIABLES
	//********************************************************************


	@Override
	public String toString()
		{
			return "TextField: "+this.name;
		}


	// Font for text
	//-----------------------------
	/** BitmapFont for text drawing. */
	BitmapFont font;
	/** Returns the BitmapFont for text drawing. */
	public BitmapFont getFont() {return font;}
	/** Sets the BitmapFont for text drawing. */
	public void setFont(BitmapFont font) 
		{
			this.font = font;
			textHeight = font.getCapHeight() - font.getDescent() * 2;
		}



	// GlyphLayout for Text Rendering
	//-----------------------------
	/** GlyphLayout for text rendering. */
	protected GlyphLayout glyphs;
	/** Array of glyph positions. */
	protected final FloatArray glyphPositions = new FloatArray();

	// Points of the visible text.
	private int visibleTextStart, visibleTextEnd;

	// Offsets and font statistics
	protected float fontOffset, textHeight, textOffset, renderOffset;


	// Text
	//-----------------------------
	/** Text to display. */
	String text;
	/** Returns the text to display. */
	public String getText() {return this.text;}
	/** Sets the text to display. */
	public void setText(String text) 
		{
			if (text == null) text = "";
			if (text.equals(this.text)) return;

			clearSelection();
			String oldText = text;
			text = "";
			paste(text);
			this.text = oldText;
			cursor = 0;
		}


	// Default Message Text
	//-----------------------------
	/** Text to display when no text has been entered. */
	String messageText;
	/** Returns the text to display when no text has been entered. */
	public String getMessageText() {return this.messageText;}
	/** Sets the text to display when no text has been entered. */
	public void setMessageText(String msg)  { this.messageText = msg; }




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
	/** Normal font color. */
	Color drawFontColor;
	/** Returns this textfield's normal font color. */
	public Color getFontColor() {return drawFontColor;}
	/** Sets this textfield's normal font color. */
	public void setFontColor(Color fcolor) {drawFontColor = fcolor;}

	/** Highlight font color for selected text. */
	Color highlightFontColor;
	/** Returns this textfield's highlight font color. */
	public Color getFontHightlightColor() {return highlightFontColor;}
	/** Sets this textfield's highlight font color. */
	public void SetFontHightlightColor(Color hcolor) {highlightFontColor = hcolor;}





	// Automatically Word Wrapping
	//-----------------------------
	/** Text word wrapping. */
	boolean wordWrap;
	/** Returns 'true' if this component automatically wraps text that is too long to additional lines. */
	public boolean isWordWrapping() {return wordWrap;}
	/** Sets if this component automatically wraps text that is too long to additional lines. */
	public void setWordWrapping(boolean wordWrap) {this.wordWrap = wordWrap;}



	// Maximum Character Length
	//-----------------------------
	/** Maximum character length. */
	int maxLength;
	/** Returns the maximum number of characters. */
	public int getMaxLength() {return maxLength;}
	/** Sets the maximum number of characters. */
	public void setMaxLength(int ml) {this.maxLength = ml;}

	
	/** List of allowed characters. Empty is all characters. 'numeric' is for numeric only.*/
	String allowedChars;
	/** Sets the list of allowed characters. Empty is all characters. 'numeric' is for numeric only.*/
	public void setAllowedCharacters(String chrs) {allowedChars = chrs;}
	/** Gets the list of allowed characters. Empty is all characters. 'numeric' is for numeric only.*/
	public String getAllowedCharacters() {return allowedChars;}

	/** Returns 'true' if the given character is allowed for the current allowed characters. */
	protected boolean isCharAllowed(char c)
	{
		// Accepts all
		if (allowedChars.isEmpty())
			return true;
		// Accepts Numeric only
		if (allowedChars.equals("numeric"))
			return Character.isDigit(c);
		// Complex filtering
		if (allowedChars.contains(""+c))
			return true;
				
		return false;
	}






	// Cursor Variables
	//-----------------------------

	/** Current cursor glyph position. */
	protected int cursor;
	/** Sets the current cursor position based on the given mouse coordinates. */
	protected void setCursorPosition (int mX, int mY)  { cursor = letterUnderCursor(mX); }


	// Text Selection Variables
	//-----------------------------

	/** 'True' if text is selected. */
	protected boolean hasSelection;	
	/** Clears the selected text. */
	protected void clearSelection() {hasSelection = false;}

	// Selection statistics
	private float selectionX, selectionWidth;

	/** Starting position of the selected text. */
	protected int selectionStart;	
	/** Sets the selected text. */
	public void setSelection (int selectionStart, int selectionEnd) 
		{
			if (selectionStart < 0) throw new IllegalArgumentException("selectionStart must be >= 0");
			if (selectionEnd < 0) throw new IllegalArgumentException("selectionEnd must be >= 0");
			selectionStart = Math.min(text.length(), selectionStart);
			selectionEnd = Math.min(text.length(), selectionEnd);
			if (selectionEnd == selectionStart) 
				{ clearSelection(); return; }
			if (selectionEnd < selectionStart) 
				{
					int temp = selectionEnd;
					selectionEnd = selectionStart;
					selectionStart = temp;
				}
			hasSelection = true;
			this.selectionStart = selectionStart;
			cursor = selectionEnd;
		}

	protected boolean allowEnters;
	

	/** Last text stored for undo command. */
	String undoText = "";

	/** Clipboard implementation for copy/paste. */
	Clipboard clipboard;


	static protected final char ENTER_DESKTOP = '\r';
	static protected final char ENTER_ANDROID = '\n';


	// Cursor components
	private float blinkTime = 0.32f;
	boolean cursorOn = true;
	long lastBlink;
	
	
	
	
	
	
	
	
	
	
	


	//********************************************************************
	//  CONSTRUCTOR
	//********************************************************************


	/** Creates a new FWTTextField with the given data. */
	public FWTTextField(XMLDataPacket data)
		{
			super(data);
			createInputReceiver();
		}
	

	/** Creates a new FWTTextField with the given XML target. */
	public FWTTextField(String parent, String object)
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
			setFont(Fonts.getFont(22));
			glyphs = new GlyphLayout();
			text = "";
			txtPadding = 3;
			drawFontColor = Color.BLACK.cpy();
			highlightFontColor = Color.WHITE.cpy();
			maxLength = 10;
			allowedChars = "";
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

				// Highlight Selection Color
				//------------------------
				if (data.get("highlightfontcolor") != null)
					{
						highlightFontColor = data.getColor("highlightfontcolor");
					}

				// Font Size
				//------------------------
				if (data.get("fontsize") != null)
					{
						font = Fonts.getFont(data.getInt("fontsize"));
					}

				// Text (Starting text from Language)
				//------------------------
				if (data.get("text") != null)
					{
						text = Language.get(data.get("text"));
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

				// Max Length
				//------------------------
				if (data.get("maxlength") != null)
					{
						maxLength = data.getInt("maxlength");
					}

				// Allowed Characters
				//------------------------
				if (data.get("allowedchars") != null)
					{
						allowedChars = data.get("allowedchars");
					}

				// Default Message Text
				//------------------------
				if (data.get("msgtext") != null)
					{
						messageText = Language.get(data.get("msgtext"));
					}
			}
		clipboard = Gdx.app.getClipboard();
		this.setAnimating(true);
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
					if (font != null && text != null)
						{
							updateDisplayText();
						}
				}

		}




	//********************************************************************
	//  DRAW
	//********************************************************************

	/** Draws the textfield according to its current properties. */
	public void draw(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
		{
			// Draw background
			super.draw(spriteBatch, shapeRenderer);

			// Draw text
			if (font != null && text != null && drawFontColor != null)
				{
					float textY = getTextY(font);
					calculateOffsets();
					
					spriteBatch.begin();

					// IF has selection, draw background
					if (hasSelection && highlightFontColor != null) 
						{
							UIRenderer.drawRectangle(spriteBatch, highlightFontColor, 
									textOffset + selectionX + fontOffset, 
									(int)(textY-font.getDescent()-textHeight), 
									selectionWidth, textHeight);
						}

					// IF no text, show default message text
					if (text.length() == 0 && messageText != null) 
						{
							font.setColor(Color.DARK_GRAY);
							font.draw(spriteBatch, messageText, txtPadding, textY, 0, messageText.length(),
									(dims.width-txtPadding-txtPadding), Align.left, false, "..."); 
						} 
					else // ELSE draw real text
						{
							font.setColor(drawFontColor);
							font.draw(spriteBatch, text, textOffset, textY, visibleTextStart, visibleTextEnd, 
									0, Align.left, false);	
						}

					// Draw Cursor
					long time = TimeUtils.nanoTime();
					if ((time - lastBlink) / 1000000000.0f > blinkTime) 
						{ cursorOn = !cursorOn; lastBlink = time;}
					if (cursorOn && this.getParentWindow().getKeyFocus() == this) 
						{
							UIRenderer.drawUIImage(spriteBatch, 0L, "cursor", 
									(int)(textOffset + glyphPositions.get(cursor) - glyphPositions.get(visibleTextStart) + fontOffset + font.getData().cursorX),
									(int)(textY-font.getDescent()-textHeight), 3, (int)textHeight, true, false);
						}
					
					spriteBatch.end();

				}


		}

	/** Get text Y position based on the given font. */
	protected float getTextY(BitmapFont font) 
		{
			float height = dims.height;
			float textY = (textHeight / 2) + font.getDescent();
			textY = textY + (height / 2);
			if (font.usesIntegerPositions()) textY = (int)textY;
			return textY;
		}





	//********************************************************************
	//  INPUT
	//********************************************************************

	/** Create the input receiver for this text field. */
	protected void createInputReceiver()
		{
			this.setInputReceiver(new FWTInputReceiver()
				{
					@Override
					public boolean touchDown(int mX, int mY, int pointer, int button) throws FWTInputException
						{
							if (button == Buttons.LEFT)
								{
									setCursorPosition(mX,mY);
									selectionStart = cursor;
									hasSelection = true;
								}

							// Right-click selects all text
							else if (button == Buttons.RIGHT)
								{
									selectAll();
								}
							// Give this text field keyboard focus
							this.getComponent().getParentWindow().setKeyFocus(this.getComponent());
							
							return true;
						}

					@Override
					public boolean touchUp(int mX, int mY, int pointer, int button, FWTComponent touchDownComponent) throws FWTInputException
						{
							if (selectionStart == cursor) hasSelection = false;
							return true;
						}


					@Override
					public boolean touchDragged(int mX, int mY, int pointer)  throws FWTInputException
						{
							setCursorPosition(mX,mY);
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
					public boolean keyDown(int keycode) throws FWTInputException
						{
							if (FWTWindowManager.DEBUG_MODE) FWTController.log("Key DOWN: "+this.getComponent().name);
							
							boolean ctrl = Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || 
									Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT) || 
									Gdx.input.isKeyPressed(Keys.SYM);
							boolean shift = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || 
									Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT);

							// IF control down
							if (ctrl) 
								{
									// Ctrl + V: Paste
									if (keycode == Keys.V) 
										{
											paste(clipboard.getContents());
											onTextChanged();
										}
									// Ctrl + C // Ctrl + Insert:  Copy
									if (keycode == Keys.C || keycode == Keys.INSERT) 
										{
											copy();
											onTextChanged();
											return true;
										}
									// Ctrl + X: Cut
									if (keycode == Keys.X) 
										{
											cut();
											onTextChanged();
											return true;
										}
									// Ctrl + A: Select All
									if (keycode == Keys.A) 
										{
											selectAll();
											return true;
										}
									// Ctrl + Z: Undo
									if (keycode == Keys.Z) 
										{
											String oldText = text;
											setText(undoText);
											undoText = oldText;
											updateDisplayText();
											onTextChanged();
											return true;
										}
								}

							// IF shift is down
							if (shift) 
								{
									// Shift + Insert: Insert
									if (keycode == Keys.INSERT) 
										{
											paste(clipboard.getContents());
											onTextChanged();
										}

									// Shift+ Forward Delete: Cut
									if (keycode == Keys.FORWARD_DEL) 
										{
											cut();
											onTextChanged();
										}

									selection: // Selection Label
									{
										int temp = cursor;
										keys:  // Keys Label
										{
											if (keycode == Keys.LEFT) 
												{
													moveCursor(false, ctrl);
													break keys;
												}
											if (keycode == Keys.RIGHT) 
												{
													moveCursor(true, ctrl);
													break keys;
												}
											if (keycode == Keys.HOME) 
												{
													cursor = 0;
													break keys;
												}
											if (keycode == Keys.END) 
												{
													cursor = text.length();
													break keys;
												}
											break selection;
										}
										// IF no selection
										if (!hasSelection) 
											{
												// Select text
												selectionStart = temp;
												hasSelection = true;
											}
									}
								} 
							else // ELSE no shift down
								{

									if (keycode == Keys.LEFT) 
										{
											moveCursor(false, ctrl);
											clearSelection();
										}
									if (keycode == Keys.RIGHT) 
										{
											moveCursor(true, ctrl);
											clearSelection();
										}
									if (keycode == Keys.HOME) 
										{
											cursor = 0;
											clearSelection();
										}
									if (keycode == Keys.END) 
										{
											cursor = text.length();
											clearSelection();
										}
								}

							// Ensure cursor within bounds
							cursor = MathUtils.clamp(cursor, 0, text.length());

							return true;
						}

					@Override
					public boolean keyTyped(char keyChar) throws FWTInputException
						{
							if (FWTWindowManager.DEBUG_MODE) FWTController.log("Key TYPED: "+this.getComponent().name);

							// Disallow "typing" most ASCII control characters, which would show up as a space when onlyFontChars is true.
							switch (keyChar) 
							{
							case 8: // BACKSPACE
							case '\t': // TAB
							case ENTER_ANDROID: // CARRIAGE RETURN
							case ENTER_DESKTOP: // NEWLINE
								break;
							default:
								if (keyChar < 32) return false;
							}

							boolean delete = keyChar == 127;
							boolean backspace = keyChar == 8;
							boolean enter = keyChar == ENTER_DESKTOP || keyChar == ENTER_ANDROID;
							boolean add = enter ? allowEnters : (font.getData().hasGlyph(keyChar));
							boolean remove = backspace || delete;
							if (add || remove) 
								{
									if (hasSelection)
										cursor = delete();
									else 
										{
											if (backspace && cursor > 0) 
												{
													text = text.substring(0, cursor - 1) + text.substring(cursor--);
													renderOffset = 0;
												}
											if (delete && cursor < text.length()) 
												{
													text = text.substring(0, cursor) + text.substring(cursor + 1);
												}
										}
									if (add && !remove) 
										{
											// IF not allowed, return
											if (!enter && !allowedChars.isEmpty() && !isCharAllowed(keyChar)) return true;
											// IF out of space, return
											if (!withinMaxLength(text.length())) return true;
											// Character added to the text.
											String insertion = enter ? "\n" : String.valueOf(keyChar);
											text = insert(cursor++, insertion, text);
										}
									updateDisplayText();
									onTextChanged();
								}

							return true;
						}
				});
		}





	//********************************************************************
	//  MISC METHODS
	//********************************************************************


	protected void calculateOffsets () 
		{
			float visibleWidth = dims.width - txtPadding - txtPadding;			
			int glyphCount = glyphPositions.size;
			float[] glyphPositions = this.glyphPositions.items;

			// Check if the cursor has gone out the left or right side of the visible area and adjust renderOffset.
			float distance = glyphPositions[Math.max(0, cursor - 1)] + renderOffset;
			if (distance <= 0)
				renderOffset -= distance;
			else 
				{
					int index = Math.min(glyphCount - 1, cursor + 1);
					float minX = glyphPositions[index] - visibleWidth;
					if (-renderOffset < minX) renderOffset = -minX;
				}

			// Prevent renderOffset from starting too close to the end, e.g. after text was deleted.
			float maxOffset = 0;
			float width = glyphPositions[glyphCount - 1];
			for (int i = glyphCount - 2; i >= 0; i--) 
				{
					float x = glyphPositions[i];
					if (width - x > visibleWidth) break;
					maxOffset = x;
				}
			if (-renderOffset > maxOffset) renderOffset = -maxOffset;

			// calculate first visible char based on render offset
			visibleTextStart = 0;
			float startX = 0;
			for (int i = 0; i < glyphCount; i++) {
				if (glyphPositions[i] >= -renderOffset) 
					{
						visibleTextStart = Math.max(0, i);
						startX = glyphPositions[i];
						break;
					}
			}

			// calculate last visible char based on visible width and render offset
			int length = Math.min(text.length(), glyphPositions.length - 1);
			visibleTextEnd = Math.min(length, cursor + 1);
			for (; visibleTextEnd <= length; visibleTextEnd++)
				if (glyphPositions[visibleTextEnd] > startX + visibleWidth) break;
			visibleTextEnd = Math.max(0, visibleTextEnd - 1);

			// Left-aligned text
			textOffset = txtPadding + startX + renderOffset;

			// calculate selection x position and width
			if (hasSelection) 
				{
					int minIndex = Math.min(cursor, selectionStart);
					int maxIndex = Math.max(cursor, selectionStart);
					float minX = Math.max(glyphPositions[minIndex] - glyphPositions[visibleTextStart], -textOffset);
					float maxX = Math.min(glyphPositions[maxIndex] - glyphPositions[visibleTextStart], visibleWidth - textOffset);
					selectionX = minX;
					selectionWidth = maxX - minX - font.getData().cursorX;
				}
		}



	/** Moves the cursor either forward or reverse (!forward). Jump moves cursor to next word-edge. */
	protected void moveCursor (boolean forward, boolean jump) 
		{
			int limit = forward ? text.length() : 0;
			int charOffset = forward ? 0 : -1;
			// Move cursor, continuously if jump enabled
			while ((forward ? ++cursor < limit : --cursor > limit) && jump) 
				{
					// Find next non-character to stop
					if (!Character.isLetterOrDigit(text.charAt(cursor + charOffset))) break;
				}
		}


	/** Updates the display text for rendering. */
	void updateDisplayText() 
		{
			BitmapFont font = this.font;
			BitmapFontData data = font.getData();
			String text = this.text;
			int textLength = text.length();

			// Add each string character to the glyph listing
			StringBuilder buffer = new StringBuilder();
			for (int i = 0; i < textLength; i++) 
				{
					char c = text.charAt(i);
					buffer.append(data.hasGlyph(c) ? c : ' ');
				}
			String displayText = buffer.toString();

			// Set new text to layout
			glyphs.setText(font, displayText);
			glyphPositions.clear();
			float x = 0;
			if (glyphs.runs.size > 0) 
				{
					GlyphRun run = glyphs.runs.first();
					FloatArray xAdvances = run.xAdvances;
					fontOffset = xAdvances.first();
					for (int i = 1, n = xAdvances.size; i < n; i++) 
						{
							glyphPositions.add(x);
							x += xAdvances.get(i);
						}
				} 
			else fontOffset = 0;
			glyphPositions.add(x);

			// Adjust selection position 
			if (selectionStart > displayText.length()) selectionStart = textLength;
			
		}



	/** Returns the letter under the mouse cursor. */
	protected int letterUnderCursor(int mX) 
		{
			float x = (float)mX - dims.x + (float)txtPadding + fontOffset - font.getData().cursorX - glyphPositions.get(visibleTextStart);
			int n = this.glyphPositions.size;
			float[] glyphPositions = this.glyphPositions.items;
			for (int i = 1; i < n; i++) {
				if (glyphPositions[i] > x) {
					if (glyphPositions[i] - x <= x - glyphPositions[i - 1]) return i;
					return i - 1;
				}
			}
			return n - 1;
		}


	/** Returns the character positions of the word under the selection cursor. */
	protected int[] wordUnderCursor(int at) 
		{
			String text = this.text;
			int start = at, right = text.length(), left = 0, index = start;
			if (at >= text.length()) 
				{
					left = text.length();
					right = 0;
				} 
			else 
				{
					for (; index < right; index++) 
						{
							if (!Character.isLetterOrDigit(text.charAt(index))) 
								{
									right = index;
									break;
								}
						}
					for (index = start - 1; index > -1; index--) 
						{
							if (!Character.isLetterOrDigit(text.charAt(index))) 
								{
									left = index + 1;
									break;
								}
						}
				}
			return new int[] {left, right};
		}








	//********************************************************************
	//  COPY, CUT, PASTE METHODS
	//********************************************************************


	/** Copies the contents of this TextField to the Clipboard. */
	public void copy () 
		{
			if (hasSelection) 
				{
					// Copy selection to clipboard
					clipboard.setContents(text.substring(Math.min(cursor, selectionStart), Math.max(cursor, selectionStart)));
				}
		}

	/** Copies the selected contents of this TextField to the Clipboard, then removes the text. */
	public void cut () 
		{
			if (hasSelection)
				{
					// Copy selection to clipboard
					copy();
					// Delete selected text and move cursor
					cursor = delete();
					// Update text
					updateDisplayText();
				}
		}


	/** Pastes the text from the Clipboard to the cursor position or over the selection. */
	void paste (String content) 
		{
			if (content == null) return;
			StringBuilder buffer = new StringBuilder();
			int textLength = text.length();
			if (hasSelection) textLength -= Math.abs(cursor - selectionStart);
			BitmapFontData data = font.getData();

			// FOR each pasted character
			for (int i = 0, n = content.length(); i < n; i++) 
				{
					if (!withinMaxLength(textLength + buffer.length())) break;
					char c = content.charAt(i);
					if (!(allowEnters && (c == ENTER_ANDROID || c == ENTER_DESKTOP))) 
						{
							if (c == '\r' || c == '\n') continue;
							if (!data.hasGlyph(c)) continue;
						}
					// Add to buffer
					buffer.append(c);
				}
			content = buffer.toString();

			// Delete any selection
			if (hasSelection) cursor = delete();
			// Insert text at the cursor
			text = insert(cursor, content, text);
			// Update text
			updateDisplayText();
			// Move cursor to end of pasted text
			cursor += content.length();
		}

	/** Returns 'true' if the given character size is within the maximum length. */
	boolean withinMaxLength (int size) 
		{
			return maxLength <= 0 || size < maxLength;
		}

	/** Inserts the given text at the given point in the target string. Returns the modified string. */
	String insert(int position, CharSequence text, String target) 
		{
			if (target.length() == 0) return text.toString();
			return target.substring(0, position) + text + target.substring(position, target.length());
		}


	/** Deletes the selected text and returns the new cursor position. */
	int delete() 
		{
			int from = selectionStart;
			int to = cursor;
			int minIndex = Math.min(from, to);
			int maxIndex = Math.max(from, to);
			String newText = (minIndex > 0 ? text.substring(0, minIndex) : "")
					+ (maxIndex < text.length() ? text.substring(maxIndex, text.length()) : "");
			text = newText;
			clearSelection();
			return minIndex;
		}

	/** Selects all the text. */
	public void selectAll () 
		{
			setSelection(0, text.length());
		}

	
	
	

	//********************************************************************
	//  TEXT CHANGE CALLBACK
	//********************************************************************

	/** Callback method called whenever the text changes by user input on this text field.
	 * <br> Expected to be overridden for more complex behavior. */
	public void onTextChanged() {}
	
	
	




}
