package com.arboreantears.fwt;

import java.util.Random;

import com.badlogic.gdx.math.Rectangle;


/** A 3D Location of a object. (X, Y, Z). Integer format.*/

public class Position
{

	// VARIABLES
	//**************************************************************************************************
	//**************************************************************************************************

	public int x = -1;
	public int y = -1;
	public int z = -1;







	// CONSTRUCTORS
	//**************************************************************************************************
	//**************************************************************************************************



	/** Creates a new blank position (-1,-1,-1). */
	public Position()
		{

		}

	/** Creates a new position (X, Y, 0). */
	public Position(int X, int Y)
		{
			x=X;y=Y;z=0;
		}

	/** Creates a new position (X, Y, Z). */
	public Position(int X, int Y, int Z)
		{
			x=X;y=Y;z=Z;
		}


	/** Creates a new position (X, Y, Z) with an int array. */
	public Position(int[] pos)
		{
			if (pos != null)
				{x=pos[0];y=pos[1];z=pos[2];}
		}



	// GETTERS & SETTERS
	//**************************************************************************************************
	//**************************************************************************************************

	public final void setX(int X) {this.x=X;}
	public final int getX() {return this.x;}

	public final void setY(int Y) {this.y=Y;}
	public final  int getY() {return this.y;}

	public final void setZ(int Z) {this.z=Z;}
	public final int getZ() {return this.z;}




	// EQUALS & CLONE
	//**************************************************************************************************
	//**************************************************************************************************


	@Override
	/** Returns 'True' if given object is a position with the same coordinates (X,Y,Z). */
	public boolean equals(Object obj)
		{
			if (obj instanceof Position)
				{
					Position pos = (Position) obj;
					if (x == pos.getX() && y == pos.getY() && z == pos.getZ())
						return true;
				}
			return false;
		}

	@Override
	/** Returns the hashcode for these coordinates. */
	public int hashCode()
		{
			int result = x;
			result = 31 * result + y;
			result = 31 * result + z;
			return result;
		}

	@Override
	/** Creates a copy of this position. */
	public Position clone()
		{
			Position pos = new Position();
			pos.setX(x); pos.setY(y); pos.setZ(z);
			return pos;
		}


	@Override
	public String toString()
		{
			return "[x: "+x+"; y: "+y+"; z: "+z+"]";
		}


	/** String representation of this position for XML saving, 'x|y|z'. */
	public String toXMLSaveString()
		{
			return x+"|"+y+"|"+z;
		}




	// DISTANCE
	//**************************************************************************************************
	//**************************************************************************************************


	/** Returns 'True' if the given position is adjacent.*/
	public boolean isAdjacent(Position pos)
		{
			int xDist = (this.x-pos.x);
			int yDist = (this.y-pos.y);

			if (Math.abs(xDist) < 2  &&  Math.abs(yDist) < 2)
				return true;

			return false;
		}

	/** Returns 'True' if the given position is adjacent (no diagonals).*/
	public boolean isAdjacentNoDiag(Position pos)
		{
			int xDist = Math.abs(this.x-pos.x);
			int yDist = Math.abs(this.y-pos.y);

			if ((xDist == 1 && yDist == 0) || (xDist == 0 && yDist == 1))
				return true;

			return false;
		}


	/** Calculates absolute distance between the two given points.*/
	public static int getAbsoluteDistance(int x1, int y1, int x2, int y2)
		{
			int dist = 0;

			int xDist = (x1-x2);
			int yDist = (y1-y2);

			dist = Math.max(Math.abs(xDist), Math.abs(yDist));

			return dist;
		}



	/** Calculates absolute distance from this point to the given point.*/
	public int getAbsoluteDistance(int x, int y)
		{
			int dist = 0;

			int xDist = (this.x-x);
			int yDist = (this.y-y);

			dist = Math.max(Math.abs(xDist), Math.abs(yDist));

			return dist;
		}

	/** Calculates absolute distance from this point to the given point.*/
	public int getAbsoluteDistance(Position pos)
		{
			int dist = 0;

			int xDist = (this.x-pos.x);
			int yDist = (this.y-pos.y);

			dist = Math.max(Math.abs(xDist), Math.abs(yDist));

			return dist;
		}


	/** Calculates absolute distance from this point to the given point.*/
	public int getAbsoluteDistance3D(Position pos)
		{
			int dist = 0;

			int xDist = (this.x-pos.x);
			int yDist = (this.y-pos.y);
			int zDist = (this.z-pos.z);

			dist = Math.abs(xDist)+Math.abs(yDist)+Math.abs(zDist);

			dist = Math.max(Math.abs(xDist), Math.abs(yDist));
			dist = Math.max(dist, Math.abs(zDist));

			return dist;
		}
	

	/** Calculates grid distance from this point to the given point.*/
	public int getGridDistance(int x, int y)
		{
			int dist = 0;

			int xDist = Math.abs(this.x-x);
			int yDist = Math.abs(this.y-y);

			dist = xDist + yDist;

			return dist;
		}

	/** Calculates grid distance from this point to the given point.*/
	public int getGridDistance(Position pos)
		{
			int dist = 0;

			int xDist = Math.abs(this.x-pos.x);
			int yDist = Math.abs(this.y-pos.y);

			dist = xDist + yDist;

			return dist;
		}



	/** Calculates relative distance from this point to the given point.*/
	public Position getRelativeDistance(int x, int y)
		{
			Position dist = new Position(0,0);

			int xDist = (this.x-x);
			int yDist = (this.y-y);

			dist.setX(xDist);
			dist.setY(yDist);

			return dist;
		}

	/** Calculates relative distance from this point to the given point.*/
	public Position getRelativeDistance(Position pos)
		{
			Position dist = new Position(0,0);

			int xDist = (this.x-pos.x);
			int yDist = (this.y-pos.y);

			dist.setX(xDist);
			dist.setY(yDist);

			return dist;
		}


	/** Calculates relative distance from this point to the given point.*/
	public Position getRelativeDistance3D(Position pos)
		{
			Position dist = new Position(0,0);

			int xDist = (this.x-pos.x);
			int yDist = (this.y-pos.y);
			int zDist = (this.z-pos.z);

			dist.setX(xDist);
			dist.setY(yDist);
			dist.setZ(zDist);

			return dist;
		}




	/** Calculates absolute X axis distance from this point to the given point.*/
	public int getAbsXDistance(Position pos)
		{
			int dist = 0;
			int xDist = (this.x-pos.x);
			dist = Math.abs(xDist);
			return dist;
		}


	/** Calculates absolute Y axis distance from this point to the given point.*/
	public int getAbsYDistance(Position pos)
		{
			int dist = 0;
			int yDist = (this.y-pos.y);
			dist = Math.abs(yDist);
			return dist;
		}
	
	

	/** Calculates Euclidean distance between the two given points.*/
	public static float getEuclidianDistance(int x1, int y1, int x2, int y2)
		{
			float dist = 0;

			int xDist = (x1-x2);
			int yDist = (y1-y2);
			
			dist = (float) Math.sqrt(Math.pow(Math.abs((double)xDist),2.0) + Math.pow(Math.abs((double)yDist),2.0));

			return dist;
		}
	
	
	/** Calculates Euclidean distance from this point to the given point.*/
	public float getEuclidianDistance(Position pos)
		{
			return getEuclidianDistance(pos.x, pos.y);
		}
	
	/** Calculates Euclidean distance from this point to the given point.*/
	public float getEuclidianDistance(int x, int y)
		{
			float dist = 0;

			int xDist = (this.x-x);
			int yDist = (this.y-y);
			
			dist = (float) Math.sqrt(Math.pow(Math.abs((double)xDist),2.0) + Math.pow(Math.abs((double)yDist),2.0));

			return dist;
		}







	// COMPARISONS
	//**************************************************************************************************
	//**************************************************************************************************


	/** Returns 'true' if this point is more north (<y) than the given point.*/
	public boolean isMoreNorth(Position pos)
		{
			return (this.y < pos.y);
		}
	/** Returns 'true' if this point is more south (>y) than the given point.*/
	public boolean isMoreSouth(Position pos)
		{
			return (this.y > pos.y);
		}
	/** Returns 'true' if this point is more west (<x) than the given point.*/
	public boolean isMoreWest(Position pos)
		{
			return (this.x < pos.x);
		}
	/** Returns 'true' if this point is more east (>x) than the given point.*/
	public boolean isMoreEast(Position pos)
		{
			return (this.x > pos.x);
		}


	/** Returns 'True' if this position is within the limits of the given max size. */
	public boolean isValid(int max)
		{
			if (x > -1 && x < max && y > -1 && y < max)
				return true;
			else
				return false;
		}

	/** Returns 'True' if the given x/y position is within the limits of 0 to the given max size. */
	public static boolean isValid(int x, int y, int max)
		{
			if (x > -1 && x < max && y > -1 && y < max)
				return true;
			else
				return false;
		}
	
	
	/** Returns 'True' if this position is within the limits by the starts (inclusive) to the ends (exclusive). */
	public boolean isWithin(int xStart, int xEnd, int yStart, int yEnd)
	{
		return x >= xStart && x < xEnd && y >= yStart && y < yEnd;
	}










	// DIRECTION
	//**************************************************************************************************
	//**************************************************************************************************


	/** Returns the direction (facing) of this position to the given position. */
	public Direction getDirection(Position start)
		{
			// X+/Y+ - Bottom Right
			if (this.x > start.x && this.y > start.y)
				return Direction.SOUTHEAST;
			// X-/Y+ - Bottom Left
			else if (this.x < start.x && this.y > start.y)
				return Direction.SOUTHWEST;
			// X-/Y- - Top Left
			else if (this.x < start.x && this.y < start.y)
				return Direction.NORTHWEST;
			// X+/Y- - Top Right
			else if (this.x > start.x && this.y < start.y)
				return Direction.NORTHEAST;
			// X/Y+ - Bottom
			else if (this.x == start.x && this.y > start.y)
				return Direction.SOUTH;
			// X/Y- - Top
			else if (this.x == start.x && this.y < start.y)
				return Direction.NORTH;
			// X-/Y - Left
			else if (this.x < start.x && this.y == start.y)
				return Direction.WEST;
			// X+/Y - Right
			else if (this.x > start.x && this.y == start.y)
				return Direction.EAST;

			else return Direction.UP;
		}







	// AVAILABLE DISTANT
	//**************************************************************************************************
	//**************************************************************************************************


	/** Returns a position the given distance away from this one, in the given direction.
	 *  <br> Truncated by map limits.*/
	public Position getDistantPosition(int maxSize, Direction dir, int distance)
		{
			int X=0, Y=0;

			// Switch on direction
			switch (dir)
			{
			case EAST: X=x+distance; Y=y; break;
			case NORTH: X=x; Y=y-distance; break;
			case WEST: X=x-distance; Y=y; break;
			case SOUTH: X=x; Y=y+distance; break;
			case NORTHEAST: X=x+distance; Y=y-distance; break;
			case NORTHWEST: X=x-distance; Y=y-distance; break;
			case SOUTHEAST: X=x+distance; Y=y+distance; break;
			case SOUTHWEST: X=x-distance; Y=y+distance; break;
			case UP: 
			default: X=x; Y=y; break;
			}

			// Keep within limits
			if (X > maxSize) X = maxSize-1;
			if (Y > maxSize) Y = maxSize-1;
			if (X < 0) X = 0;
			if (Y < 0) Y = 0;

			return new Position(X,Y);
		}

	

	/** Returns a random position within the given distance away from this one.
	 *  <br> Truncated by map limits.*/
	public Position getRandomPosition(int maxSize, int distance, Random rndGen)
		{
			int X = this.x - (distance) + (int)(rndGen.nextFloat()*(float)distance*2f);
			int Y = this.y - (distance) + (int)(rndGen.nextFloat()*(float)distance*2f);
			
			// Keep within limits
			if (X > maxSize-1) X = maxSize-1;
			if (Y > maxSize-1) Y = maxSize-1;
			if (X < 0) X = 0;
			if (Y < 0) Y = 0;

			return new Position(X,Y);
		}
	
	
	/** Returns the midpoint position between this and the given position. */
	public Position getMidpoint(Position otherPos)
	{
		int xDist = otherPos.x - x;
		int yDist = otherPos.y - y;
		
		return new Position(x + Math.round(xDist/2), y + Math.round(yDist/2));
		
	}





	// DISTANCE TO EDGE
	//**************************************************************************************************
	//**************************************************************************************************


	/** Returns the Euclidean distance a coordinates is from the center of a map of the given size. */
	public static int calcDistanceToCenter(int x, int y, int size)
		{
			int hSize = size / 2;
			int dist = Position.getAbsoluteDistance(x,y, hSize,hSize);
			return dist;
		}





	// CONVERSION
	//**************************************************************************************************
	//**************************************************************************************************

	/** Returns a Rectangle of this Position.
	 * <br> x,y = x,y;  w,h = 1.0f
	 */
	public Rectangle getRectangle()
		{
			return new Rectangle(x, y, 1f, 1f);
		}





}
