package com.arboreantears.fwt;





/** A 2-dimensional rectangular area with an X,Y and Width/Height. */
public class Area
{

	public int x, y;
	public int width, height;


	/** Constructs a new rectangular area with all values set to zero */
	public Area()
		{}

	/** Constructs a new rectangular area with the given corner point in the top left and dimensions. */
	public Area(int x, int y, int width, int height) 
		{
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}


	/** Constructs a new rectangular area with the given two corner points in the top-left and bottom-right. */
	public Area(Position topLeft, Position bottomRight) 
		{
			this.x = topLeft.x;
			this.y = topLeft.y;
			this.width = bottomRight.x - topLeft.x ;
			this.height = bottomRight.y - topLeft.y ;
		}



	// CLONE
	//======================================

	public Area clone()
		{
			return new Area(this.x,this.y,this.width,this.height);
		}



	// CONTAINS
	//======================================

	public boolean contains(int x, int y) 
		{
			return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
		}

	public boolean contains(Position pos) 
		{
			return this.x <= pos.x && this.x + this.width >= pos.x && this.y <= pos.y && this.y + this.height >= pos.y;
		}



	// TOSTRING
	//======================================

	@Override
	/** Returns the area in the following format: "x|y|width|height". */
	public String toString() 
		{
			return (this.x+"|"+this.y+"|"+this.width+"|"+this.height);
		}



	// SAVESTRING
	//======================================
	/** String representation of this area for XML saving. */
	public String toXMLSaveString()
		{
			return (this.x+"|"+this.y+"|"+this.width+"|"+this.height);
		}




	// UTILITY
	//======================================


	/** Returns a Position in this area's center. */
	public Position getMiddlePosition() {return new Position(x+(width/2),y+(height/2));}

	
	/** Returns a Position in this area's top-left. */
	public Position getTopLeftPosition() {return new Position(x,y);}

	/** Returns a Position in this area's bottom-right. */
	public Position getBottomRightPosition() {return new Position(x+width-1,y+height-1);}














}
