package com.arboreantears.fwt.components;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.arboreantears.fwt.FWTController;
import com.arboreantears.fwt.FWTWindowManager;
import com.arboreantears.fwt.XMLDataPacket;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;



/** An FWTIListScrollable[E,T extends FWTComponent] container (panel) in which only elements of the specified type can be presented. 
 * <br> Acts as a converter between a data model 'E' and a UI component 'T'. */
public class FWTListScrollable<E, T extends FWTComponent> extends FWTScrollable
{

	//  VARIABLES
	//********************************************************************
	//********************************************************************


	@Override
	public String toString()
		{
			return "ListScrollable: "+this.name;
		}



	/** Distance between components. */
	int gapDistance;
	public int getGapDistance() {return gapDistance;}
	public void setGapDistance(int gapDistance) {this.gapDistance = gapDistance;}


	/** Width of the displayed components. */
	String componentWidth;
	/** Returns the height of the displayed components. */
	public void setComponentWidth(String width) {componentWidth = width;}	
	/** Returns the width of the displayed components. */
	public String getComponentWidth() {return componentWidth;}
	/** Height of the displayed components. */
	String componentHeight;	
	/** Returns the height of the displayed components. */
	public void setComponentHeight(String height) {componentHeight = height;}	
	/** Returns the height of the displayed components. */
	public String getComponentHeight() {return componentHeight;}	
	/** Sets the size of the displayed components. */
	public void setComponentSize(String size) {this.componentHeight = size; this.componentWidth = size;}




	// Update
	//-------------------------

	/** 'True' if this needs to repopulate the component list. */
	boolean needsListUpdate;

	/** Sets this ListScrollable to repopulate the component list. */
	public void refreshList() {needsListUpdate = true;}



	// Scroll Orientation
	//-------------------------

	/** 'True' if this panel is vertically oriented (false is horizontal). */
	boolean orientation;

	/** Sets this list to scroll vertically. */
	public void setListVertical() 
		{orientation = true; setHorizontalScrolling(false); setVerticalScrolling(true);}

	/** Sets this list to scroll horizontally. */
	public void setListHorizontal()
		{orientation = false; setVerticalScrolling(false); setHorizontalScrolling(true);}



	// List Data
	//-------------------------
	/** List of internal data being represented by the list components. 
	 * <br> The corresponding data is made available to each createNewListComponent() call. */
	ArrayList<E> componentList;
	/** Sets the internal data being represented by the list components. 
	 * <br> The corresponding data is made available to each createNewListComponent() call. */
	public void setComponentListData(ArrayList<E> list) {componentList = list;}
	/** Returns the internal data being represented by the list components. 
	 * <br> The corresponding data is made available to each createNewListComponent() call. */
	public ArrayList<E> getComponentListData() {return componentList;}
	/** Adds the given component to the component list. Automatically updates the list. */
	public void addComponentToList(E component)
		{
			componentList.add(component);
			needsListUpdate = true;
		}
	/** Removes the given component from the component list. Automatically updates the list. */
	public void removeComponentFromList(E component)
		{
			componentList.remove(component);
			needsListUpdate = true;
		}



	// Component Data
	//-------------------------

	/** Component data packet. */
	XMLDataPacket componentData;













	//  CONSTRUCTOR
	//********************************************************************
	//********************************************************************

	/** Create a new FWTIListScrollable[E,T extends FWTComponent] container (panel) in which only 
	 * elements of the specified type can be presented. Use setComponentListData() to set list data.
	 * <br> Acts as a converter between a data model 'E' and a UI component 'T'. */
	public FWTListScrollable(XMLDataPacket data)
		{
			this(data,new ArrayList<E>());
		}


	/** Create a new FWTIListScrollable[E,T extends FWTComponent] container (panel) in which only 
	 * elements of the specified type can be presented. Uses the given list-data.
	 * <br> Acts as a converter between a data model 'E' and a UI component 'T'. */
	public FWTListScrollable(XMLDataPacket data, ArrayList<E> listData)
		{
			super(data);
			componentList = listData;
		}
	
	/** Create a new FWTIListScrollable[E,T extends FWTComponent] container (panel) in which only 
	 * elements of the specified type can be presented. Uses the given list-data.
	 * <br> Acts as a converter between a data model 'E' and a UI component 'T'. */
	public FWTListScrollable(String parent, String object, ArrayList<E> listData)
		{
			super(parent, object);
			componentList = listData;
		}



	//  XML DATA
	//********************************************************************
	//********************************************************************
	
	@Override
	public void refreshData()
		{
			this.clearComponents();
			this.components.clear();
			super.refreshData();
			updateList();
		}

	@Override
	protected void setDefaults()
		{
			super.setDefaults();
			gapDistance = 10;
			componentWidth = "10";
			componentHeight = "10";	
			orientation = true;
			componentData = new XMLDataPacket();
		}

	@Override
	public void applyDataParameters(XMLDataPacket data)
		{
			super.applyDataParameters(data);

			// DATA
			//----------------------------------
			try {
				if (data != null)
					{
						// GAP DISTANCE
						//========================
						if (data.get("gapdistance") != null)
							{						
								this.gapDistance = data.getInt("gapdistance");
							}

						// COMPONENT WIDTH
						//========================
						if (data.get("compwidth") != null)
							{						
								componentWidth = data.get("compwidth");
							}

						// COMPONENT HEIGHT
						//========================
						if (data.get("compheight") != null)
							{						
								componentHeight = data.get("compheight");
							}

						// COMPONENT SIZE
						//========================
						if (data.get("compsize") != null)
							{						
								this.setComponentSize(data.get("compsize"));
							}

						// ORIENTATION
						//========================
						if (data.get("orientation") != null)
							{						
								if (data.get("orientation").equals("horizontal"))
									setListHorizontal();
								else setListVertical(); 
							}


						// Component Properties
						//--------------------------------------------------------------

						// COMPONENT BORDER
						//========================
						if (data.get("compborder") != null)
							{
								componentData.put("border", data.get("compborder"));
							}
						else
							componentData.put("border", "false");

						// COMPONENT BORDER COLOR
						//========================
						if (data.get("compbordercolor") != null)
							{
								componentData.put("bordercolor", data.get("compbordercolor"));
							}
						else
							componentData.put("bordercolor", "0|0|0|1");


						// COMPONENT BACKGROUND COLOR
						//========================
						if (data.get("compbackgroundcolor") != null)
							{
								componentData.put("backgroundcolor", data.get("compbackgroundcolor"));
							}
						else
							componentData.put("backgroundcolor", "0.5|0.5|0.5|1");

						// COMPONENT HIGHLIGHT COLOR
						//========================
						if (data.get("comphighlightcolor") != null)
							{
								componentData.put("highlightcolor", data.get("comphighlightcolor"));
							}
						else
							componentData.put("highlightcolor", "0.7|0.7|0.7|1");

						// COMPONENT PRESSED COLOR
						//========================
						if (data.get("comppressedcolor") != null)
							{
								componentData.put("pressedcolor", data.get("comppressedcolor"));
							}
						else
							componentData.put("pressedcolor", "0.3|0.3|0.3|1");

						// COMPONENT BACKGROUND TEXTURE
						//========================
						if (data.get("compbackgroundtexture") != null)
							{
								componentData.put("backgroundtexture", data.get("compbackgroundtexture"));
							}
						else
							componentData.put("backgroundtexture", "");

						// COMPONENT PRESSED TEXTURE
						//========================
						if (data.get("comppressedtexture") != null)
							{
								componentData.put("pressedtexture", data.get("comppressedtexture"));
							}
						else
							componentData.put("pressedtexture", "");

						// COMPONENT HIGHLIGHT TEXTURE
						//========================
						if (data.get("comphighlighttexture") != null)
							{
								componentData.put("highlighttexture", data.get("comphighlighttexture"));
							}
						else
							componentData.put("highlighttexture", "");

						// COMPONENT SCALE TEXTURE
						//========================
						if (data.get("comptexturefill") != null)
							{
								componentData.put("texturefill", data.get("comptexturefill"));
							}
						else
							componentData.put("texturefill", "stretched");
					}
			}catch(Exception ex){
				FWTController.error("Invalid XML Data for UI:ListScrollable: '"+this.name+"'. Using defaults.");
			}
		}




	//  UPDATE
	//********************************************************************
	//********************************************************************

	@Override
	public void update()
		{
			super.update();

			if (needsListUpdate)
				{needsListUpdate = false; updateList();}

		}


	/** Updates the elements of the list using the current component list with the given expected number of list components. */
	private void updateList()
		{
			if (FWTWindowManager.DEBUG_MODE) FWTController.log("ListScrollable: "+this.name+": Component List Updated");

			// Clear components
			this.clearComponents();
			this.components.clear();

			// IF Vertical scrolling
			if (this.orientation)
				{
					// Width of scroll pane area
					int scrollWidth = ((int)this.getDimensions().width) - this.getBarSize();
					int compHeight = (getComponentHeightFromData(componentHeight,!this.orientation) + gapDistance);
					int compWidth = (getComponentWidthFromData(componentWidth,this.orientation));

					// Number of slots per row
					int numPerRow = (scrollWidth - gapDistance) / compWidth;

					// Number of rows
					int numRows = Math.max(1, (int)Math.ceil((float)componentList.size() / (float)numPerRow));

					// Set new virtual height
					int newHeight = (numRows * compHeight) + gapDistance;
					Rectangle virtDims = new Rectangle(0f,0f, this.getVirtualDimensions().width, Math.max(this.getDimensions().height, newHeight));
					this.setVirtualDimensions(virtDims);
					this.setVerticalScroll(this.getVirtualDimensions().height);

					// Set to resize and redraw
					this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
					this.resizeScrollComponents();
					this.redraw();

					// Hide bar if not needed
					if (this.getVirtualDimensions().height <= this.getDimensions().height)
						this.setShowVerticalBar(false);
					else this.setShowVerticalBar(true);
					this.setShowHorizontalBar(false);


					int X = gapDistance, Y = (int)this.getVirtualDimensions().getHeight() - compHeight;

					// FOR each component in list
					int count = 0;
					for (int ll=0; ll<componentList.size(); ll++)
						{
							// Change position of slot
							if (count != 0)
								{
									X = X + compWidth;
									if ((count % numPerRow) == 0)
										{
											X = gapDistance;
											Y = Y - (compHeight);
										}
								}

							// Prepare component data
							XMLDataPacket compData = componentData.clone();
							compData.put("name", this.getData().get("compname")+ll);
							compData.put("pwidth", Integer.toString((int)this.getVirtualDimensions().width));
							compData.put("pheight", Integer.toString((int)this.getVirtualDimensions().height));
							compData.put("depth", Integer.toString(3));
							compData.put("width", Integer.toString(compWidth));
							compData.put("height", Integer.toString(compHeight-gapDistance));
							compData.put("position", Integer.toString(X)+"|"+Integer.toString(Y));		
							
							// Create new component
							T newComp = createNewListComponent(compData, componentList.get(ll), ll);
							if (newComp != null)
								{this.addComponent(newComp); count++;}

						}
				}		

			// ELSE horizontal scrolling
			else
				{
					// Height of scroll pane area
					int scrollHeight = ((int)this.getDimensions().height) - this.getBarSize();
					int compHeight = (getComponentHeightFromData(componentHeight,!this.orientation));
					int compWidth = (getComponentWidthFromData(componentWidth,this.orientation) + gapDistance);

					// Number of slots per column
					int numPerCol = (scrollHeight - gapDistance) / compHeight;

					// Number of columns
					int numCols = Math.max(1, (int)Math.ceil((float)componentList.size() / (float)numPerCol));

					// Set new virtual width
					int newWidth = (numCols * compWidth) + gapDistance;
					Rectangle virtDims = new Rectangle(0f,0f, Math.max(this.getDimensions().width, newWidth), this.getVirtualDimensions().height);
					this.setVirtualDimensions(virtDims);
					this.setHorizontalScroll(0);

					// Set to resize and redraw
					this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
					this.resizeScrollComponents();
					this.redraw();

					// Hide bar if not needed
					if (this.getVirtualDimensions().width <= this.getDimensions().width)
						this.setShowHorizontalBar(false);
					else this.setShowHorizontalBar(true);
					this.setShowVerticalBar(false);

					int X = gapDistance, Y = (int)this.getVirtualDimensions().getHeight() - compHeight;

					// FOR each component in list
					int count = 0;
					for (int ll=0; ll<componentList.size(); ll++)
						{
							// Change position of slot
							if (count != 0)
								{
									Y = Y - compHeight;
									if ((count % numPerCol) == 0)
										{
											Y = (int)this.getVirtualDimensions().getHeight() - compHeight;
											X = X + (compWidth);
										}
								}

							// Prepare component data
							XMLDataPacket compData = componentData.clone();
							compData.put("name", this.getData().get("compname")+ll);
							compData.put("cnum", Integer.toString(ll));		
							compData.put("pwidth", Integer.toString((int)this.getVirtualDimensions().width));
							compData.put("pheight", Integer.toString((int)this.getVirtualDimensions().height));
							compData.put("depth", Integer.toString(3));
							compData.put("width", Integer.toString(compWidth - gapDistance));
							compData.put("height", Integer.toString(compHeight));
							compData.put("position", Integer.toString(X)+"|"+Integer.toString(Y));	

							// Create new component
							T newComp = createNewListComponent(compData,componentList.get(ll), ll);
							if (newComp != null)
								{this.addComponent(newComp); count++;}

						}
				}

			//**************************************
			this.redraw();
		}



	/** Creates a new component for this list scrollable with the given component data packet, list-data element, and slot number. 
	 * <br> This component is automatically added to the scrollable's drawable area.*/
	public T createNewListComponent(XMLDataPacket compData, E listData, int slot)
		{
			// Other behavior expected to be overridden by a specific implementations.
			return null;
		}






	//  COMPONENT DATA TRANSFER
	//********************************************************************
	//********************************************************************

	/** Adds all XML data in the list scrollable that is preceded with 'comp' to the given XML data packet.
	 * <br> This allows any amount of component specific data to be transfered to created components.
	 * <br><br> Example:  Data key 'compfontsize' in this list scrollable will be added to the given XMLDataPacket as
	 * 'fontsize' key with the same value.
	 * <br><br> Returns the reference to the given component data packet. 
	 * <br><br> This method is optional and should be called in a customized 'createNewListComponent' method.*/
	protected XMLDataPacket addComponentDataToPacket(XMLDataPacket compData)
		{
			if (compData != null && this.getData() != null)
				{
					// FOR each entry in list-scrollable data set
					for (Entry<String,String> dat : this.getData().getMap().entrySet())
						// IF data meant for component AND acceptable
						if (dat.getKey().startsWith("comp") && isAcceptableData(dat.getKey()))
							compData.put(dat.getKey().substring(4, dat.getKey().length()), dat.getValue());
				}
			return compData;		
		}


	/** Returns 'true' if the given key is acceptable to pass on to the component. 
	 * <br> Stops improper settings being transfered to the component.*/
	private boolean isAcceptableData(String key)
		{
			if (key.equals("compname")) return false;
			else if (key.equals("comppwidth")) return false;
			else if (key.equals("comppheight")) return false;
			else if (key.equals("compdepth")) return false;
			else if (key.equals("compwidth")) return false;
			else if (key.equals("compheight")) return false;
			else if (key.equals("compposition")) return false;

			return true;
		}







	//  COMPONENT SIZING
	//********************************************************************
	//********************************************************************


	/** Returns the size of the component width based on the given XML String data. */
	private int getComponentWidthFromData(String widthData, boolean vertScroll)
		{
			// WIDTH
			//========================
			int width = 10;
			// IF has width
			if (widthData != null)
				{
					String W = widthData;
					int pwidth = (int)dims.width;

					// IF max width
					if (W.equals("max") && vertScroll)
						{
							width = pwidth - gapDistance - gapDistance;	
						}
					// ELSE if relative width
					else if (W.contains("width") && vertScroll)
						{
							String s = W.substring(W.indexOf("width")+5, W.length());
							int Woffset = Integer.parseInt(s);
							// Set offset
							width = pwidth + Woffset - gapDistance - gapDistance;
						}
					// ELSE if percentile
					else if (W.contains("%") && vertScroll)
						{
							String s = W.substring(W.indexOf("%")+1, W.length());
							float Wmult = Float.parseFloat(s) / 100f;
							if (Wmult > 1f) Wmult = 1f;
							width = (int) ((float)(pwidth - gapDistance - gapDistance) * Wmult);
						}
					// ELSE absolute width
					else width = Integer.parseInt(W);
				}
			// Keep within limits
			if (width < 0) width = 10;

			return width;
		}





	/** Returns the size of the component height based on the given XML String data. */
	private int getComponentHeightFromData(String heightData, boolean horzScroll)
		{
			// HEIGHT
			//========================
			int height = 10;
			// IF has height
			if (heightData != null)
				{
					String H = heightData;
					int pheight = (int)dims.height;

					// IF max height
					if (H.equals("max") && horzScroll)
						{
							height = pheight - gapDistance - gapDistance;		
						}
					// ELSE if relative height
					else if (H.contains("height") && horzScroll)
						{
							String s = H.substring(H.indexOf("height")+6, H.length());
							int Hoffset = Integer.parseInt(s);
							// Set offset
							height = pheight + Hoffset - gapDistance - gapDistance;
						}
					// ELSE if percentile
					else if (H.contains("%") && horzScroll)
						{
							String s = H.substring(H.indexOf("%")+1, H.length());
							float Hmult = Float.parseFloat(s) / 100f;
							if (Hmult > 1f) Hmult = 1f;
							height = ((int) ((float)(pheight - gapDistance - gapDistance) * Hmult)) ;
						}
					// ELSE absolute height
					else height = Integer.parseInt(H);
				}
			// Keep within limits
			if (height < 0) height = 10;

			return height;
		}










}
