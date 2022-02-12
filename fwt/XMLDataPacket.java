package com.arboreantears.fwt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import com.badlogic.gdx.graphics.Color;



/** A packet of data read from an XML file. */
public class XMLDataPacket
{

	// VARIABLES
	//****************************************************************
	//****************************************************************

	/** The HashMap of key-value pairs for this packet. */
	HashMap<String,String> data = null;
	/** Returns the HashMap of key-value pairs for this packet. */
	public HashMap<String,String> getMap() {return data;}
	/** Sets the HashMap of key-value pairs for this packet. */
	public void setMap(HashMap<String,String> map) {data = map;}




	// CONSTRUCTOR
	//****************************************************************
	//****************************************************************

	/** Creates a new empty XML data packet. */
	public XMLDataPacket()
	{
		data = new HashMap<String,String>();
	}

	/** Creates a new XML data packet with the given XML attribute data. */
	public XMLDataPacket(HashMap<String,String> attribs)
	{
		data = attribs;
	}




	// CLONE
	//****************************************************************
	//****************************************************************

	/** Creates an identical copy of this data packet. */
	public XMLDataPacket clone()
	{
		XMLDataPacket newData = new XMLDataPacket();
		if (data != null)
			{
				Set<Entry<String,String>> entries = data.entrySet();
				Iterator<Entry<String,String>> itr = entries.iterator();
				while (itr.hasNext())
					{
						Entry<String,String> next = itr.next();
						newData.put(next.getKey(), next.getValue());
					}
			}
		return newData;
	}










	// DATA ADDITION
	//****************************************************************
	//****************************************************************


	/** Puts the given data in the data packet. */
	public void put(String key, String val) {if (data != null) data.put(key, val);}

	/** Puts the int value in the data packet. */
	public void putInt(String key, int val) {if (data != null) data.put(key, Integer.toString(val));}

	/** Puts the long value in the data packet. */
	public void putLong(String key, long val) {if (data != null) data.put(key, Long.toString(val));}

	/** Puts the float value in the data packet. */
	public void putFloat(String key, float val) {if (data != null) data.put(key, Float.toString(val));}

	/** Puts the double value in the data packet. */
	public void putDouble(String key, double val) {if (data != null) data.put(key, Double.toString(val));}

	/** Puts the boolean value in the data packet. */
	public void putBoolean(String key, boolean val) {if (data != null) data.put(key, Boolean.toString(val));}

	/** Puts the Position value in the data packet. */
	public void putPos(String key, Position val)
		{
			if (data != null) 
				{
					String str = "";
					str = Integer.toString(val.x) + "|" +
							Integer.toString(val.y) + "|" +
							Integer.toString(val.z);
					data.put(key, str);
				}
		}
	
	/** Puts the Area value in the data packet. */
	public void putArea(String key, Area val) 
		{
			if (data != null) 
				{
					String str = "";
					str = Integer.toString(val.x) + "|" +
							Integer.toString(val.y) + "|" +
							Integer.toString(val.width) + "|" +
							Integer.toString(val.height);
					data.put(key, str);
				}
		}

	/** Puts the Color value in the data packet. */
	public void putColor(String key, Color val) 
		{
			if (data != null) 
				{
					String str = "";
					str = Float.toString(val.r) + "|" +
							Float.toString(val.g) + "|" +
							Float.toString(val.b) + "|" +
							Float.toString(val.a);
					data.put(key, str);
				}
		}

	/** Puts the list of String values in the data packet. */
	public void putList(String key, String[] val)
		{
			if (data != null) 
				{
					String str = "";
					for (int ss=0; ss<val.length-1; ss++)
						str = val[ss] + "|";
					str = val[val.length-1];
					data.put(key, str);
				}
		}

	/** Puts the list of String values in the data packet. */
	public void putList(String key, ArrayList<String> val)
		{
			if (data != null) 
				{
					String str = "";
					for (int ss=0; ss<val.size()-1; ss++)
						str = val.get(ss) + "|";
					str = val.get(val.size()-1);
					data.put(key, str);
				}
		}










	// DATA RETRIEVAL
	//****************************************************************
	//****************************************************************

	/** Returns 'true' if the given key is contained in the data packet. */
	public boolean has(String key) {return data.containsKey(key);}




	// GET DATA
	//------------------------------------------

	/** Returns the String value for the given key if it exists, otherwise null. */
	public String get(String key) {return data.get(key);}



	/** Returns the int value for the given key if it exists, otherwise '-1'. */
	public int getInt(String key)
	{
		String val = data.get(key);
		if (val != null)
			{
				int intVal = -1;
				try{
					intVal = Integer.parseInt(val);
				}catch(NumberFormatException ex){FWTController.error("Format Int Error: "+key);} // Ignore format error
				return intVal;
			}
		return -1;
	}

	/** Returns the int value for the given key if it exists, otherwise returns the given default value. */
	public int getInt(String key, int def)
	{
		String val = data.get(key);
		if (val != null)
			{
				int intVal = -1;
				try{
					intVal = Integer.parseInt(val);
				}catch(NumberFormatException ex){FWTController.error("Format Int Error: "+key);} // Ignore format error
				return intVal;
			}
		return def;
	}


	/** Returns the long value for the given key if it exists, otherwise '-1L'. */
	public long getLong(String key)
	{
		String val = data.get(key);
		if (val != null)
			{
				long longVal = -1L;
				try{
					longVal = Long.parseLong(val);
				}catch(NumberFormatException ex){FWTController.error("Format Long Error: "+key);} // Ignore format error
				return longVal;
			}
		return -1L;
	}
	/** Returns the long value for the given key if it exists, otherwise returns the given default value. */
	public long getLong(String key, long def)
	{
		String val = data.get(key);
		if (val != null)
			{
				long longVal = -1L;
				try{
					longVal = Long.parseLong(val);
				}catch(NumberFormatException ex){FWTController.error("Format Long Error: "+key);} // Ignore format error
				return longVal;
			}
		return def;
	}


	/** Returns the float value for the given key if it exists, otherwise '-1f'. */
	public float getFloat(String key)
	{
		String val = data.get(key);
		if (val != null)
			{
				float floatVal = -1f;
				try{
					floatVal = Float.parseFloat(val);
				}catch(NumberFormatException ex){FWTController.error("Format Float Error: "+key);} // Ignore format error
				return floatVal;
			}
		return -1f;
	}

	/** Returns the float value for the given key if it exists, otherwise returns the given default value. */
	public float getFloat(String key, float def)
	{
		String val = data.get(key);
		if (val != null)
			{
				float floatVal = -1f;
				try{
					floatVal = Float.parseFloat(val);
				}catch(NumberFormatException ex){FWTController.error("Format Float Error: "+key);} // Ignore format error
				return floatVal;
			}
		return def;
	}


	/** Returns the double value for the given key if it exists, otherwise '-1.0'. */
	public double getDouble(String key)
	{
		String val = data.get(key);
		if (val != null)
			{
				double doubleVal = -1.0;
				try{
					doubleVal = Double.parseDouble(val);
				}catch(NumberFormatException ex){FWTController.error("Format Double Error: "+key);} // Ignore format error
				return doubleVal;
			}
		return -1.0;
	}

	/** Returns the double value for the given key if it exists, otherwise returns the given default value. */
	public double getDouble(String key, double def)
	{
		String val = data.get(key);
		if (val != null)
			{
				double doubleVal = -1.0;
				try{
					doubleVal = Double.parseDouble(val);
				}catch(NumberFormatException ex){FWTController.error("Format Double Error: "+key);} // Ignore format error
				return doubleVal;
			}
		return def;
	}


	/** Returns the boolean value for the given key if it exists, otherwise 'false'. */
	public boolean getBoolean(String key)
	{
		String val = data.get(key);
		if (val != null)
			{
				boolean boolVal = false;
				try{
					boolVal = Boolean.parseBoolean(val);
				}catch(NumberFormatException ex){FWTController.error("Format Boolean Error: "+key);} // Ignore format error
				return boolVal;
			}
		return false;
	}


	/** Returns the Position value for the given key if it exists, otherwise 'null'. */
	public Position getPos(String key)
	{
		String val = data.get(key);
		if (val != null)
			{
				StringTokenizer st = new StringTokenizer(val,"|,");
				try{
					int[] pos = {Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken())};
					return new Position(pos[0],pos[1],pos[2]);
				}catch(NumberFormatException ex){FWTController.error("Format Pos|Int Error: "+key);} // Ignore format error
			}
		return null;
	}

	/** Returns the Color value for the given key if it exists, otherwise 'null'. */
	public Color getColor(String key)
	{
		String val = data.get(key);
		if (val != null)
			{
				StringTokenizer st = new StringTokenizer(val,"|,");
				int numTokens = st.countTokens();
				try{
					if (numTokens == 4) // Alpha included
						{
							float[] color = {Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()),Float.parseFloat(st.nextToken()),Float.parseFloat(st.nextToken())};
							return new Color(color[0], color[1], color[2], color[3]);
						}
					else if (numTokens == 3) // No alpha (solid color)
						{
							float[] color = {Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()),Float.parseFloat(st.nextToken())};
							return new Color(color[0], color[1], color[2], 1f);
						}
					else // String color from Fonts or Color list.
						{
							try {  return (Color) FWTColors.class.getField(val.toUpperCase()).get(null);  } catch (Exception ex) {;}
							try {  return (Color) Color.class.getField(val.toUpperCase()).get(null);  } catch (Exception ex) {;}
							FWTController.error("No color value: "+val+" found.");
							return null;
						}
				}catch(NumberFormatException ex){FWTController.error("Format Color|Float Error: "+key);} // Ignore format error
			}
		return null;
	}



	/** Returns the Area value for the given key if it exists, otherwise 'null'. */
	public Area getArea(String key)
	{
		String val = data.get(key);
		if (val != null)
			{
				StringTokenizer st = new StringTokenizer(val,"|,");
				try{
					int[] pos = {Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()),
							Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken())};
					return new Area(pos[0],pos[1],pos[2],pos[3]);
				}catch(NumberFormatException ex){FWTController.error("Format Area|Int Error: "+key);} // Ignore format error
			}
		return null;
	}
	
	
	
	/** Returns the list of Strings for the given key if it exists, otherwise 'null'. */
	public ArrayList<String> getList(String key)
	{
		String val = data.get(key);
		if (val != null)
			{
				StringTokenizer st = new StringTokenizer(val,"|,");
				ArrayList<String> list = new ArrayList<String>(st.countTokens());
				while (st.hasMoreTokens())
					{
						list.add(st.nextToken());
					}
				return list;
			}
		return null;
	}





	// GET REQUIRED DATA
	//------------------------------------------

	/** Returns the String value for the given key if it exists, otherwise throws an exception. */
	public String getRequired(String key) throws Exception
	{
		String val =  data.get(key);
		if (val == null) throw new Exception("XMLData: Required key '"+key+"' not found.");
		return val;
	}




	/** Returns the int value for the given key if it exists, otherwise throws an exception. */
	public int getRequiredInt(String key) throws Exception
	{
		String val = data.get(key);
		if (val != null)
			{
				int intVal = -1;
				try{
					intVal = Integer.parseInt(val);
				}catch(NumberFormatException ex){FWTController.error("Format Int Error: "+key);} // Ignore format error
				return intVal;
			}
		throw new Exception("XMLData: Required key '"+key+"' not found.");
	}









	// DATA SAVING
	//****************************************************************
	//****************************************************************

	
	/** Returns a long-string version of this data packet in 'key:value' pairs separated by '|'. */
	public String toXMLSaveString()
	{
		StringBuilder strB = new StringBuilder();
		
		boolean first = true;
		Iterator<Entry<String,String>> itr = data.entrySet().iterator();
		while (itr.hasNext())
			{
				if (!first) strB.append("|");
				Entry<String,String> entry = itr.next();
				strB.append(entry.getKey()+":"+entry.getValue());
				first = false;
			}		
		return strB.toString();
	}




	
	
	
	
	
	
	
	
	
	
	
	
	
	
	







}
