package com.arboreantears.fwt;

import java.util.Random;


/** Facing in a specific direction. */
public enum Direction
{
	/** Looking up*/
	UP,
	/** Looking Northwest */
	NORTHWEST, 
	/** Looking North */
	NORTH, 
	/** Looking Northeast */
	NORTHEAST, 
	/** Looking East */
	EAST, 
	/** Looking Southeast */
	SOUTHEAST, 
	/** Looking South */
	SOUTH, 
	/** Looking Southwest */
	SOUTHWEST, 
	/** Looking West */
	WEST;



	/** Returns the reverse of the direction of this facing. */
	public Direction reverse()
		{
			switch(this)
			{
				case EAST: return WEST;
				case NORTH: return SOUTH;
				case NORTHEAST: return SOUTHWEST;
				case NORTHWEST: return SOUTHEAST;
				case SOUTH: return NORTH;
				case SOUTHEAST: return NORTHWEST;
				case SOUTHWEST: return NORTHEAST;
				case UP: return UP;
				case WEST: return EAST;
				default: return UP;
			}
		}


	/** Returns the Direction of the given String. */
	public static Direction getDirectionByString(String facing)
		{
			for (Direction face : values())
				if (facing.equals(face.name()))
					return face;
			return UP;
		}


	/** Returns the String of the given Direction. */
	public static String directionString(Direction facing)
		{
			//Language: actions.xml
			
			return Language.get("action_facing_"+facing.name());
		}


	/** Returns a random direction (besides UP). "Spinning the arrow!" */
	public static Direction random()
		{
			Random r = new Random();
			int roll = (r.nextInt(8) +1);
			switch(roll)
			{
				case 1: return WEST;
				case 2: return SOUTH;
				case 3: return SOUTHWEST;
				case 4: return SOUTHEAST;
				case 5: return NORTH;
				case 6: return NORTHWEST;
				case 7: return NORTHEAST;
				case 8: return EAST;
				default: return UP;
			}
		}
	
	/** Returns a random direction (besides UP). "Spinning the arrow!" */
	public static Direction random(Random rndGen)
		{
			int roll = (rndGen.nextInt(8) +1);
			switch(roll)
			{
				case 1: return WEST;
				case 2: return SOUTH;
				case 3: return SOUTHWEST;
				case 4: return SOUTHEAST;
				case 5: return NORTH;
				case 6: return NORTHWEST;
				case 7: return NORTHEAST;
				case 8: return EAST;
				default: return UP;
			}
		}

}