package com.arboreantears.fwt;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;


/** Static library of pre-defined colors. */
public class FWTColors
	{
		

		// EXTRA COLORS
		//=====================================================================
		
		/** Brighten and returns the given color as a new color. */
		public static Color brighten(Color color)
		{
			return getGradientColor(color, Color.WHITE, 0.15f);
		}

		/** Darken and returns the given color as a new color. */
		public static Color darken(Color color)
		{
			return getGradientColor(color, Color.BLACK, 0.15f);
		}	
		
		
		/** Creates a new color using integers (max 255). */
		public static Color newColor(int r, int g, int b, int a)
		{
			return new Color((float)r/255f, (float)g/255f, (float)b/255f, (float)a/255f);
		}

		
		
		// PRIMARY & SECONDARY COLORS
		
		public static final Color LIGHT_RED = new Color(1f, 0.3f, 0.3f, 1f);
		public static final Color MID_RED = new Color(0f, 0.6f, 0f, 1f);
		public static final Color DARK_RED = new Color(0.4f, 0f, 0f, 1f);

		public static final Color LIGHTER_GREEN = new Color(0.5f, 1f, 0.5f, 1f);
		public static final Color LIGHT_GREEN = new Color(0.3f, 1f, 0.3f, 1f);
		public static final Color MID_GREEN = new Color(0f, 0.6f, 0f, 1f);
		public static final Color DARK_GREEN = new Color(0f, 0.4f, 0f, 1f);
		
		public static final Color LIGHT_BLUE = new Color(0.3f, 0.3f, 1f, 1f);
		public static final Color MID_BLUE = new Color(0f, 0f, 0.6f, 1f);
		public static final Color DARK_BLUE = new Color(0f, 0f, 0.4f, 1f);

		public static final Color LIGHT_YELLOW = new Color(1f, 1f, 0.3f, 1f);
		public static final Color MID_YELLOW = new Color(0.6f, 0.6f, 0f, 1f);
		public static final Color DARK_YELLOW = new Color(0.4f, 0.4f, 0f, 1f);

		public static final Color LIGHT_MAGENTA = new Color(1f, 0.3f, 1f, 1f);
		public static final Color MID_MAGENTA = new Color(0.6f, 0f, 0.6f, 1f);
		public static final Color DARK_MAGENTA = new Color(0.4f, 0f, 0.4f, 1f);

		public static final Color LIGHT_CYAN = new Color(0.3f, 1f, 1f, 1f);
		public static final Color MID_CYAN = new Color(0f, 0.6f, 0.6f, 1f);
		public static final Color DARK_CYAN = new Color(0f, 0.4f, 0.4f, 1f);
		

		
		// TERTIARY COLORS

		public static final Color YELLOW_GREEN = new Color(0.678f, 1f, 0.1843f, 1f);
		public static final Color ORANGE = new Color(1f, 0.64f, 0f, 1f);
		public static final Color BROWN = new Color(0.588f, 0.294f, 0f, 1f);
		public static final Color DARK_BROWN = new Color(0.294f, 0.147f, 0f, 1f);
		public static final Color PURPLE = new Color(0.5f, 0.0f, 5f, 1f);
		
		
		
		
		// METALLIC COLORS
		
		public static final Color COPPER = new Color(0.71875f, 0.44921875f, 0.19921875f, 1f);
		public static final Color BRONZE = new Color(0.80078125f, 0.49609375f, 0.1953125f, 1f);
		public static final Color SILVER = new Color(0.75f, 0.75f, 0.8f, 1f);
		public static final Color GOLD = new Color(0.828125f, 0.68359375f, 0.21484375f, 1f);
		public static final Color PLATINUM = new Color(0.89453125f, 0.890625f, 0.8828125f, 1f);
		
		
		
		// GUI COLORS
		
		/** Standard (lightest) parchment color (Background standard). */
		public static final Color PARCHMENT = new Color(1f,0.9216f,0.7333f,1f);
		/** Darker parchment color (Panel standard). */
		public static final Color DARK_PARCHMENT = new Color(0.95f,0.8716f,0.6833f,1f);
		/** Lighter background parchment color (TextField color). */
		public static final Color LIGHT_PAPER = new Color(0.95098f,0.8333f,0.65294f, 1f);
		/** Darker background parchment color. (Button color)*/
		public static final Color PAPER_BROWN = new Color(0.85098f,0.705882f,0.48235f, 1f);
		/** Darkest background parchment color (Button-pressed color). */
		public static final Color DARK_PAPER = new Color(0.75098f,0.605882f,0.38235f, 1f);
		/** Wood Brown (Standard text-box color). */
		public static final Color WOOD_BROWN = new Color(0.8f,0.7216f,0.5333f,1f);
		/** Lighter background color for slider and scroll bar objects. */
		public static final Color SLIDER_BG = new Color(0.99216f,0.92156f,0.5686f,1f);
		/** Darker color for slider and scroll bar boxes. */
		public static final Color SLIDER_BOX = new Color(0.77647f,0.70588f,0.35294f,1f);
		/** Bar color for some slider bars. */
		public static final Color SLIDER_BAR = new Color(0.90f,0.8216f,0.6333f,1f);
		
		// METER COLORS
		public static final Color THIRST_BAR = new Color(0, 0.8f, 0.8f, 1f);
		public static final Color NUTRITION_BAR = new Color(0.8f, 0.58f, 0f, 1f);
		public static final Color SLEEP_BAR = new Color(0.8f, 0f, 0.8f, 1f);
		
		public static final Color HEALTH_BAR = new Color(0.8f, 0f, 0f, 1f);
		public static final Color STAMINA_BAR = new Color(0f, 0.8f, 0f, 1f);
		
		

		// TRANSPARENCY COLORS
		
		public static final Color INVISIBLE = new Color(0f, 0f, 0f, 0f);
		public static final Color DIM_GRAY = new Color(0f, 0f, 0f, 0.4f);
		public static final Color HIGHLIGHT_WHITE = new Color(1f, 1f, 1f, 0.35f);
		public static final Color HIGHLIGHT_GREEN = new Color(0f, 1f, 0f, 0.35f);
		public static final Color HIGHLIGHT_RED = new Color(1f, 0f, 0f, 0.35f);
		public static final Color HIGHLIGHT_YELLOW = new Color(1f, 1f, 0f, 0.35f);

		
		
		// LIGHTING COLORS
		
		/** Light color for Fires. */
		public static final Color LIGHTS_FIRE = new Color(1f,0.6923f,0.5f,0.9f);
		/** Blue color for the sky. */
		public static final Color SKY_BLUE = new Color(0.6353f,0.7725f,0.9725f,1f);
		/** Blue color for the sea. */
		public static final Color SEA_BLUE = new Color(0f,0.6353f,0.9098f,1f);
		
		
		
		
		// UI COLORS
		// dfc1ae - Button 
		// e5c188 - Button Highlight
		// e5c188 - Button Pressed
		
		

		// PEOPLE COLORS

		public static final Color EYE_WHITE = new Color(226f/255f, 226f/255f, 224f/255f, 1f);

		public static final Color LINEN = new Color(250f/255f, 240f/255f, 230f/255f, 1f);

		/** Array of skin tone colors from dark to light (0-17). */
		public static final Color[] skinTones = {
				new Color(45f/255f,34f/255f,30f/255f,1f),
				new Color(60f/255f,46f/255f,40f/255f,1f),
				new Color(75f/255f,57f/255f,50f/255f,1f),
				new Color(90f/255f,69f/255f,60f/255f,1f),
				new Color(105f/255f,80f/255f,70f/255f,1f),
				new Color(120f/255f,92f/255f,80f/255f,1f),
				new Color(135f/255f,103f/255f,90f/255f,1f),
				new Color(150f/255f,114f/255f,100f/255f,1f),
				new Color(165f/255f,126f/255f,110f/255f,1f),
				new Color(180f/255f,138f/255f,120f/255f,1f),
				new Color(195f/255f,149f/255f,130f/255f,1f),
				new Color(210f/255f,161f/255f,140f/255f,1f),
				new Color(225f/255f,172f/255f,150f/255f,1f),
				new Color(240f/255f,184f/255f,160f/255f,1f),
				new Color(255f/255f,195f/255f,170f/255f,1f),
				new Color(255f/255f,206f/255f,180f/255f,1f),
				new Color(255f/255f,218f/255f,190f/255f,1f),
				new Color(255f/255f,229f/255f,200f/255f,1f),
		};

		/** Array of hair tone colors from dark to light (0-17). */
		public static final Color[] hairTones = {
				new Color(0f/255f,0f/255f,0f/255f,1f),
				new Color(14f/255f,14f/255f,14f/255f,1f),
				new Color(28f/255f,28f/255f,28f/255f,1f),
				new Color(40f/255f,40f/255f,40f/255f,1f),
				new Color(38f/255f,7f/255f,2f/255f,1f),
				new Color(64f/255f,17f/255f,9f/255f,1f),
				new Color(77f/255f,19f/255f,8f/255f,1f),
				new Color(88f/255f,34f/255f,8f/255f,1f),
				new Color(98f/255f,42f/255f,17f/255f,1f),
				new Color(111f/255f,63f/255f,41f/255f,1f),
				new Color(131f/255f,90f/255f,72f/255f,1f),
				new Color(102f/255f,75f/255f,64f/255f,1f),
				new Color(130f/255f,94f/255f,62f/255f,1f),
				new Color(160f/255f,101f/255f,23f/255f,1f),
				new Color(203f/255f,137f/255f,40f/255f,1f),
				new Color(230f/255f,192f/255f,80f/255f,1f),
				new Color(248f/255f,245f/255f,168f/255f,1f),
				new Color(215f/255f,99f/255f,58f/255f,1f),
		};
		
		/** Array of eye tone colors from dark to light (0-17). */
		public static final Color[] eyeTones = {
				new Color(25f/255f,24f/255f,20f/255f,1f),
				new Color(35f/255f,26f/255f,21f/255f,1f),
				new Color(48f/255f,38f/255f,26f/255f,1f),
				new Color(60f/255f,37f/255f,23f/255f,1f),
				new Color(76f/255f,49f/255f,28f/255f,1f),
				new Color(97f/255f,84f/255f,76f/255f,1f),
				new Color(90f/255f,60f/255f,32f/255f,1f),
				new Color(97f/255f,67f/255f,43f/255f,1f),
				new Color(116f/255f,70f/255f,36f/255f,1f),
				new Color(130f/255f,88f/255f,63f/255f,1f),
				new Color(137f/255f,96f/255f,52f/255f,1f),
				new Color(157f/255f,125f/255f,84f/255f,1f),
				new Color(119f/255f,102f/255f,72f/255f,1f),
				new Color(145f/255f,139f/255f,105f/255f,1f),
				new Color(119f/255f,118f/255f,72f/255f,1f),
				new Color(145f/255f,160f/255f,105f/255f,1f),
				new Color(94f/255f,148f/255f,210f/255f,1f),
				new Color(169f/255f,227f/255f,228f/255f,1f),
		};

		


		/** Returns the appropriate hair color for the given index [0f-17f]. */
		public static Color getHairColorByIndex(float hairColorIndex)
		{
			int start = (int)(Math.floor(hairColorIndex));
			int end = (int)(Math.ceil(hairColorIndex));
			float percent = 1f - (hairColorIndex - (float)Math.floor(hairColorIndex));

			return getGradientColor(hairTones[start], hairTones[end], percent);

		}

		

		/** Returns the appropriate skin color for the given index [0f-17f]. */
		public static Color getSkinColorByIndex(float skinColorIndex)
		{
			int start = (int)(Math.floor(skinColorIndex));
			int end = (int)(Math.ceil(skinColorIndex));
			float percent = 1f - (skinColorIndex - (float)Math.floor(skinColorIndex));

			return getGradientColor(skinTones[start], skinTones[end], percent);

		}


		/** Returns the appropriate eye color for the given index [0f-17f]. */
		public static Color getEyeColorByIndex(float eyeColorIndex)
		{
			int start = (int)(Math.floor(eyeColorIndex));
			int end = (int)(Math.ceil(eyeColorIndex));
			float percent = 1f - (eyeColorIndex - (float)Math.floor(eyeColorIndex));

			return getGradientColor(eyeTones[start], eyeTones[end], percent);

		}
		
		
		
		


		// INITIALIZATION
		//=====================================================================
		
		
		/** Initialize colors to memory. */
		public static void initialize()
		{						
			// Add mark-up colors
			Colors.put("LIGHT_RED", LIGHT_RED); 
			Colors.put("BLOOD_RED", new Color(0.3984375f,0f,0f,1f)); 
			Colors.put("BROWN", BROWN); 
			Colors.put("MID_YELLOW", MID_YELLOW); 
			Colors.put("ORANGE",ORANGE);
			Colors.put("WOOD_BROWN",WOOD_BROWN);
			Colors.put("DARK_CYAN",DARK_CYAN);
			Colors.put("MID_GREEN",MID_GREEN);
			Colors.put("DARK_GREEN",DARK_GREEN);
			Colors.put("INVISIBLE",INVISIBLE);

			// Unnecessary ?
			//-------------------------------
			//			for (Field field: FWTColors.class.getDeclaredFields())
			//				{
			//					if (field.getType() == Color.class)
			//						{
			//							try {
			//								String name = field.getName();
			//								Color col = (Color) field.get(null);
			//								Colors.put(name, col);
			//							}catch(Exception ex) {FWTController.error("Could not load colors.");}
			//							
			//						}
			//				}
			//			FWTController.log("FWTColors: Colors initialized.");
		}
		
		


		//********************************************************************
		//  COLOR EFFECTS
		//********************************************************************



		/** Returns the gradient color using the given start and end colors using the percentage of the second color (percent). */
		public static Color getGradientColor(Color startColor, Color endColor, float percent)
		{
			Color col;

			float diffRed = endColor.r - startColor.r;
			float diffGreen = endColor.g - startColor.g;
			float diffBlue = endColor.b - startColor.b;

			diffRed = (diffRed * percent) + startColor.r;
			if (diffRed > 1f ||  diffRed < 0f)
				diffRed = 0;
			diffGreen = (diffGreen * percent) + startColor.g;
			if (diffGreen > 1f ||  diffGreen < 0f)
				diffGreen = 0;
			diffBlue = (diffBlue * percent) + startColor.b;
			if (diffBlue > 1f ||  diffBlue < 0f)
				diffBlue = 0;

			col = new Color(diffRed,diffGreen,diffBlue, startColor.a);
			return col;
		}


		

	}
