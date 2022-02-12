package com.arboreantears.fwt;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.badlogic.gdx.files.FileHandle;



/** Read XML specifications for manual creation of FWT UI components. */
public class XMLUIReader extends DefaultHandler
{



	// VARIABLES
	//***********************************************************************************************
	//***********************************************************************************************

	/** 'True' if reader is finished reading a file. */
	boolean finished = false;

	/** Target component to return data. */
	String targetName = "";

	/** The data packet to return. */
	XMLDataPacket dataPacket;

	/** Current depth of component. 0 is none (root), 1 is a parent window/widget. */
	int componentDepth = -1;


	// CONSTRUCTORS
	//***********************************************************************************************
	//***********************************************************************************************

	/** Create an XML reader that reads UI specifications. */
	public XMLUIReader()
	{

	}






	
	


	// CHECK FOR XML
	//***********************************************************************************************
	//***********************************************************************************************


	/** Checks to see if UI specifications for the component exist, with the given object name and parent-file (no extension [.xml]). */
	public static boolean hasUISpecs(String parentfile, String objectName)
		{
			XMLUIReader inflater = new XMLUIReader();
			XMLDataPacket dataPacket = null;

			FileHandle overrideFile = new FileHandle(FWTController.getOverrideUIFilePath()+"/"+parentfile+".xml");
			if (overrideFile.exists())
				{
					try{
						dataPacket = inflater.read(overrideFile.path(), objectName);
					}catch(Exception ex) { }
				}

			// IF no override version found, check core version
			if (dataPacket == null)
				{
					FileHandle coreFile = new FileHandle(FWTController.getUIFilePath()+"/"+parentfile+".xml");
					// Read UI XML file [Core]
					if (coreFile.exists())
						try{
							dataPacket = inflater.read(coreFile.path(), objectName);
						}catch(Exception ex) { }	
				}

			// IF still no data found, report error
			if (dataPacket == null)
				return false;
			else
				{
					return true;
				}
		}



	// INFLATE XML
	//***********************************************************************************************
	//***********************************************************************************************


	/** Returns the UI specifications for the component with the given object name and parent-file (no extension [.xml]). 
	 * <br> Returns null if no valid file found.  */
	public static XMLDataPacket getUISpecs(String parentfile, String objectName)
	{
		XMLUIReader inflater = new XMLUIReader();
		XMLDataPacket dataPacket = null;

		// Read UI XML file [override]
		FileHandle overrideFile = new FileHandle(FWTController.getOverrideUIFilePath()+"/"+parentfile+".xml");
		if (overrideFile.exists())
			try{
				dataPacket = inflater.read(overrideFile.path(), objectName);
			}catch(Exception ex)
		{
			// Error message variables
			FWTController.error("Error in UI XML file:");
			FWTController.error("   "+ex.getMessage());
			// Return failure
			return null;
		}

		// IF no override version found, load core version
		if (dataPacket == null)
			{
				FileHandle coreFile = new FileHandle(FWTController.getUIFilePath()+"/"+parentfile+".xml");
				// Read UI XML file [Core]
				if (coreFile.exists())
					try{
						dataPacket = inflater.read(coreFile.path(), objectName);
					}catch(Exception ex)
				{
					// Error message variables
					FWTController.error("Error in UI XML file:");
					FWTController.error("   "+ex.getMessage());
					// Return failure
					return null;
				}	
			}

		// IF still no data found, report error
		if (dataPacket == null)
			FWTController.error("UI Component: "+objectName+" specifications not found.");
		else
			{
				// Add XML content information
				dataPacket.put("parentfile", parentfile);
				dataPacket.put("objectname", objectName);
			}

		return dataPacket;
	}







	// READ FILE
	//***********************************************************************************************
	//***********************************************************************************************

	/** Reads an XML data contained in the given file and returns a new data packet. */
	public XMLDataPacket read(String document, String targetName) throws Exception
	{
		FWTController.log("XML: "+document+" : "+targetName);
		// Use the default (non-validating) parser
		SAXParserFactory factory = SAXParserFactory.newInstance();
		this.targetName = targetName;

		try{
			// Parse the input
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse( new File(document), this );
		}catch (Exception ex) {if (FWTWindowManager.DEBUG_MODE) ex.printStackTrace(); 
		FWTController.error("ERROR: Sax exception."); throw new Exception("File: "+document+"\n      "+ex.getLocalizedMessage());}

		// Prepare Logger and Notify
		FWTController.log("Reading in XML file: UI:  "+document+" : "+targetName);

		// Enter Waiting Loop
		while(!finished)
			{
				try{
					Thread.sleep(1);
				}
				catch (Exception ex) {ex.printStackTrace(); throw new Exception(ex.getLocalizedMessage());}
			}
		finished = false;

		// Return the data packet
		return dataPacket;
	}








	// START METHODS
	//***********************************************************************************************
	//***********************************************************************************************


	@Override
	public void startDocument()
	{

	}


	@Override
	public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException
	{
		// Get Element Name
		String eName = sName; // element name
		if ("".equals(eName))
			eName = qName; // not namespace-aware

		// Increment depth
		componentDepth++;

		// IF component name matches target
		if (attrs.getValue("name") != null && attrs.getValue("name").equals(targetName))
			{

				// Prepare data packet
				dataPacket = new XMLDataPacket();

				// IF has attributes
				if (attrs.getLength() > 0)
					{
						// FOR each attribute
						for (int i = 0; i < attrs.getLength(); i++)
							{
								// Get Attribute name
								String aName = attrs.getLocalName(i);
								// IF local name empty
								if ("".equals(aName))
									// Get qualified name
									aName = attrs.getQName(i);
								// Add data to HashMap
								dataPacket.put(aName,attrs.getValue(i));
							}

						// Add parent component depth
						dataPacket.put("depth", Integer.toString(componentDepth));

						// Add component type
						dataPacket.put("type", eName);
					}
			}

	} // END startElement method





	// END METHODS
	//***********************************************************************************************
	//***********************************************************************************************




	@Override
	public void endElement(String namespaceURI, String sName, String qName) throws SAXException
	{
		// Decrement depth
		componentDepth--;
	}




	@Override
	public void endDocument()
	{

		this.finished = true;
	}











}
