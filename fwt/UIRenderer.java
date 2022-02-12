package com.arboreantears.fwt;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

/** Renders UI assets using the libGDX SpriteBatch implementation. */
public class UIRenderer
{

	/** Width (pixels) padding around window/containers with thick borders. */
	public static int THICK_BORDER_WIDTH = 30;

	/** Width (pixels) padding around window/containers with thin borders. */
	public static int THIN_BORDER_WIDTH = 20;


	// Borders
	//====================================================

	/** Draws a thin border box using the "border" nine-patch at the given location.  MUST begin spritebatch before this method! */
	public static void drawThinBorder(long ID, SpriteBatch spriteBatch, Color color, float x, float y, float width, float height)
		{
			spriteBatch.setColor(color);
			FWTImageManager.getBorderNinePatch(ID, "border", (int)width, (int)height).draw(spriteBatch, x, y, width, height);
			spriteBatch.setColor(Color.WHITE);
		}

	/** Draws a thick border box using the "borderthick" nine-patch at the given location.  MUST begin spritebatch before this method! */
	public static void drawThickBorder(long ID, SpriteBatch spriteBatch, Color color, float x, float y, float width, float height)
		{
			spriteBatch.setColor(color);
			FWTImageManager.getBorderNinePatch(ID, "borderthick", (int)width, (int)height).draw(spriteBatch, x, y, width, height);
			spriteBatch.setColor(Color.WHITE);
		}






	// Rectangles
	//====================================================


	/** Draws a rectangle at the given location.  MUST begin sprite-batch before this method! */
	public static void drawRectangle(SpriteBatch spriteBatch, Color color, float x, float y, float width, float height)
		{
			AtlasRegion fillReg = FWTImageManager.getUIAtlas().findRegion("fill");
			if (fillReg != null)
				{
					spriteBatch.setColor(color);
					spriteBatch.draw(fillReg.getTexture(),x,y,width,height,
							fillReg.getRegionX(),fillReg.getRegionY(),fillReg.getRegionWidth(),fillReg.getRegionHeight(),false,false);
					spriteBatch.setColor(Color.WHITE);
				}
		}






	// Lines
	//====================================================


	/** Draws a horizontal line at the given location.  MUST begin sprite-batch before this method! */
	public static void drawHorizontalLine(SpriteBatch spriteBatch, Color color, float x1, float x2, float y, float thickness)
		{
			drawRectangle(spriteBatch, color, x1, y, x2-x1, thickness);
		}


	/** Draws a vertical line at the given location.  MUST begin sprite-batch before this method! */
	public static void drawVerticalLine(SpriteBatch spriteBatch, Color color, float y1, float y2, float x, float thickness)
		{
			drawRectangle(spriteBatch, color, x, y1, thickness, y2-y1);
		}












	/** Returns 'true' if the given imageID is currently loaded. */
	public static boolean hasUIImage(String imageID)
		{
			String iID = imageID;
			if (imageID.substring(imageID.length()-2, imageID.length()).equals(".9"))
				iID = imageID.substring(0, imageID.length()-2);

			AtlasRegion uiRegion = FWTImageManager.getUIAtlas().findRegion(iID);
			if (uiRegion == null)
				uiRegion = FWTImageManager.getUIAtlas().findRegion(iID);

			if (uiRegion == null)
				return false;
			return true;				
		}



//
//	/** Draws the image of the given UI image with the given parameters.  Returns true if successful.
//	 * <br> Draw starts at 0,0.
//	 * <br> Scaled - Stretches the image to fill the given width/height.
//	 * <br> MUST begin sprite batch before this method. */
//	public static boolean drawUIImage(SpriteBatch spriteBatch, long id, String imageName, int width, int height, boolean scaleTexture)
//		{
//			return drawUIImage(spriteBatch, id, imageName, 0, 0, width, height, scaleTexture,  !scaleTexture);
//		}
//
//
//



	/** Draws the image of the given UI image with the given parameters .  Returns true if successful.
	 * <br> Scaled - Stretches the image to fill the given width/height, otherwise these are ignored. 
	 * <br> Tiled - Tiles the image in the given space. 
	 * <br> MUST begin sprite batch before this method. */
	public static boolean drawUIImage(SpriteBatch spriteBatch, long id, String imageName, int gX, int gY, int width, int height, boolean scaled, boolean tiled)
		{
			return drawUIImage(spriteBatch, id, imageName, gX, gY, width, height, scaled, tiled, false, false);
		}






	/** Draws the image of the given UI image with the given parameters.  Returns true if successful.
	 * <br> Scaled - Stretches the image to fill the given width/height, otherwise these are ignored. 
	 * <br> Tiled - Tiles the image in the given space. 
	 * <br> FlipX/Y - Flips the image. 
	 * <br> MUST begin sprite batch before this method. */
	public static boolean drawUIImage(SpriteBatch spriteBatch, long id, String imageName, int gX, int gY, int width, int height, 
			boolean scaled, boolean tiled, boolean flipX, boolean flipY)
		{
			try{
				boolean isNinePatch = imageName.substring(imageName.length()-2, imageName.length()).equals(".9");

				// IF Nine Patch
				if (isNinePatch)
					{
						if (scaled)	
							FWTImageManager.getNinePatch(id, imageName.substring(0, imageName.length()-2), width, height).draw(spriteBatch, gX, gY, width, height);
						else // tiled
							{
								AtlasRegion uiRegion = FWTImageManager.getUIAtlas().findRegion(imageName.substring(0, imageName.length()-2));
								if (uiRegion == null)
									uiRegion = FWTImageManager.getUIAtlas().findRegion(imageName.substring(0, imageName.length()-2));

								if (uiRegion.findValue("splits") != null && uiRegion.findValue("pads") != null)
									{
										// L / R / T / B
										int[] splits = uiRegion.findValue("splits");
										int regX = uiRegion.getRegionX();
										int regY = uiRegion.getRegionY();
										int regWidth = uiRegion.getRegionWidth();
										int regHeight = uiRegion.getRegionHeight();

										// Width of texture mid-section
										int midWidth = regWidth - splits[0] - splits[1];
										int midHeight = regHeight - splits[2] - splits[3];
										int numX = (int)Math.ceil((double)width / (double)midWidth);
										int numY = (int)Math.ceil((double)height / (double)midHeight);

										// Draw Middle
										//-----------------------
										// Starting positions
										int X = gX+splits[0];
										int Y = gY+splits[3];
										for (int xx=0; xx<numX; xx++)
											{
												for (int yy=0; yy<numY; yy++)
													{
														spriteBatch.draw(uiRegion.getTexture(),X,Y,midWidth,midHeight,
																regX+splits[0], regY+splits[2], midWidth, midHeight,  false,false);
														Y = Y + midHeight;
													}
												X = X + midWidth;
												Y = gY+splits[3];
											}


										// Draw Left
										Y = gY+splits[3];
										for (int yy=0; yy<numY; yy++)
											{
												spriteBatch.draw(uiRegion.getTexture(),gX,Y,splits[0],midHeight,
														regX, regY+regHeight-splits[3]-midHeight,splits[0], midHeight,  false,false);
												Y = Y + midHeight;
											}


										// Draw Right
										Y = gY+splits[3];
										for (int yy=0; yy<numY; yy++)
											{
												spriteBatch.draw(uiRegion.getTexture(),gX+width-splits[1],Y,splits[1],midHeight,
														regX+regWidth-splits[1], regY+splits[2],splits[1], midHeight,  false,false);
												Y = Y + midHeight;
											}

										// Draw Top
										X = gX+splits[0];
										for (int xx=0; xx<numY; xx++)
											{
												spriteBatch.draw(uiRegion.getTexture(),X,gY+height-splits[2],midWidth,splits[2],
														regX+splits[0], regY, midWidth, splits[2],  false,false);
												X = X + midWidth;
											}

										// Draw Bottom
										X = gX+splits[0];
										for (int xx=0; xx<numY; xx++)
											{
												spriteBatch.draw(uiRegion.getTexture(),X, gY, midWidth, splits[3],
														regX+splits[0], regY+regHeight-splits[3], midWidth, splits[3],  false,false);
												X = X + midWidth;
											}



										// Top-Left
										spriteBatch.draw(uiRegion.getTexture(),gX,gY+height-splits[2],splits[0], splits[2],
												regX, regY, splits[0], splits[2],false,false);	

										// Top-Right
										spriteBatch.draw(uiRegion.getTexture(),gX+width-splits[1],gY+height-splits[2],splits[1], splits[2],
												regX+regWidth-splits[1],regY, splits[1], splits[2],false,false);	

										// Bottom-Left
										spriteBatch.draw(uiRegion.getTexture(),gX,gY,splits[0], splits[3],
												regX,regY+regHeight-splits[3], splits[0], splits[3],false,false);	

										// Botom-Right
										spriteBatch.draw(uiRegion.getTexture(),gX+width-splits[1],gY,splits[1], splits[3],
												regX+regWidth-splits[1],regY+regHeight-splits[3], splits[1], splits[3],false,false);	



									}
								else {FWTController.error("Error: Couldn't draw UI image: '"+imageName+"'. No splits defined.");return false;}
							}
					}

				// ELSE Not a NinePatch
				else
					{
						AtlasRegion uiRegion = FWTImageManager.getUIAtlas().findRegion(imageName);
						if (uiRegion == null)
							uiRegion = FWTImageManager.getUIAtlas().findRegion(imageName);

						// Scaled rendering (fill space)
						if (scaled)
							{
								spriteBatch.draw(uiRegion.getTexture(),
										gX,gY,width, height,
										uiRegion.getRegionX(), uiRegion.getRegionY(), uiRegion.getRegionWidth(), uiRegion.getRegionHeight(), 
										flipX, flipY);	
							}

						// Tiled rendering
						else if (tiled)
							{
								int numX = (int) Math.floor((float)width/(float)uiRegion.getRegionWidth());
								int numY = (int) Math.floor((float)height/(float)uiRegion.getRegionHeight());

								int leftWidth = width;
								for (int xx=0; xx < numX; xx++)
									{
										int leftHeight = height;
										for (int yy=0; yy < numY; yy++)
											{
												spriteBatch.draw(uiRegion.getTexture(),
														gX+(uiRegion.getRegionWidth()*xx) ,gY+(uiRegion.getRegionHeight()*yy) ,uiRegion.getRegionWidth(),uiRegion.getRegionHeight(),
														uiRegion.getRegionX(), uiRegion.getRegionY(), uiRegion.getRegionWidth(), uiRegion.getRegionHeight(), 
														flipX, flipY);	
												leftHeight = leftHeight - uiRegion.getRegionHeight();
											}
										if (leftHeight > 0)
											{
												spriteBatch.draw(uiRegion.getTexture(),
														gX+(uiRegion.getRegionWidth()*xx) ,gY+(uiRegion.getRegionHeight()*numY) , uiRegion.getRegionWidth(), leftHeight,
														uiRegion.getRegionX(), uiRegion.getRegionY(), uiRegion.getRegionWidth(), leftHeight, 
														flipX, flipY);		
											}
										leftWidth = leftWidth - uiRegion.getRegionWidth();
									}

								if (leftWidth > 0)
									{
										int leftHeight = height;
										for (int yy=0; yy < numY; yy++)
											{
												spriteBatch.draw(uiRegion.getTexture(),
														gX+(uiRegion.getRegionWidth()*numX) ,gY+(uiRegion.getRegionHeight()*yy) ,leftWidth,uiRegion.getRegionHeight(),
														uiRegion.getRegionX(), uiRegion.getRegionY(), leftWidth, uiRegion.getRegionHeight(), 
														flipX, flipY);	
												leftHeight = leftHeight - uiRegion.getRegionHeight();
											}
										if (leftHeight > 0)
											{
												spriteBatch.draw(uiRegion.getTexture(),
														gX+(uiRegion.getRegionWidth()*numX) ,gY+(uiRegion.getRegionHeight()*numY) , leftWidth, leftHeight,
														uiRegion.getRegionX(), uiRegion.getRegionY(), leftWidth, leftHeight, 
														flipX, flipY);		
											}
									}

							}

						// Single rendering (drawn as texture size)
						else
							{
								spriteBatch.draw(uiRegion.getTexture(),
										gX,gY,uiRegion.getRegionWidth(),uiRegion.getRegionHeight(),
										uiRegion.getRegionX(), uiRegion.getRegionY(), uiRegion.getRegionWidth(), uiRegion.getRegionHeight(), 
										flipX, flipY);	
							}
					}
			}catch (Exception ex) {FWTController.error("Error: Couldn't draw UI image: '"+imageName+"'."); return false;}

			return true;
		}






	/** Draws the image of the given UI image with the given parameters.  Returns true if successful.
	 * <br> Does NOT support NinePatch or tiled images.
	 * <br> FlipX/Y - Flips the image. 
	 * <br> MUST begin sprite batch before this method. 
	 * <br> Also includes texture source percentages. (Automatically flips Y values to texture space).*/
	public static boolean drawUIImage(SpriteBatch spriteBatch, long id, String imageName, int gX, int gY, int width, int height, 
			boolean flipX, boolean flipY, float srcX, float srcY, float srcWidth, float srcHeight)
		{
			try{

				AtlasRegion uiRegion = FWTImageManager.getUIAtlas().findRegion(imageName);
				if (uiRegion == null)
					uiRegion = FWTImageManager.getUIAtlas().findRegion(imageName);

				spriteBatch.draw(uiRegion.getTexture(),
						(float)gX,(float)gY,(float)width,(float)height,
						uiRegion.getRegionX()+(int)(srcX*(float)uiRegion.getRegionWidth()), 
						uiRegion.getRegionY() + (uiRegion.getRegionHeight()-(int)(srcY*(float)uiRegion.getRegionHeight())-(int)(srcHeight*(float)uiRegion.getRegionHeight())), 
						(int)(srcWidth*(float)uiRegion.getRegionWidth()), (int)(srcHeight*(float)uiRegion.getRegionHeight()), 
						flipX, flipY);	
			}catch (Exception ex) {FWTController.error("Error: Couldn't draw UI image: '"+imageName+"'."); return false;}

			return true;
		}








	/** Draws the image of the given UI image with the given parameters.  Returns true if successful.
	 * <br> rotation - Amount (degrees) to rotate image counterclockwise. 
	 * <br> MUST begin sprite batch before this method. */
	public static boolean drawUIImage(SpriteBatch spriteBatch, long id, String imageName, int gX, int gY, int width, int height, float rotation)
		{
			try{
				AtlasRegion uiRegion = FWTImageManager.getUIAtlas().findRegion(imageName);
				if (uiRegion == null)
					uiRegion = FWTImageManager.getUIAtlas().findRegion(imageName);

				spriteBatch.draw(uiRegion, gX, gY, width/2f, height/2f, width, height, 1.0f, 1.0f, rotation);
			}catch (Exception ex) {FWTController.error("Error: Couldn't draw UI image: '"+imageName+"'."); return false;}
			return true;
		}















}
