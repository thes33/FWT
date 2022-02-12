package com.arboreantears.fwt;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.arboreantears.fwt.components.FWTComponent;
import com.arboreantears.fwt.components.FWTContainer;
import com.arboreantears.fwt.components.FWTWindow;
import com.badlogic.gdx.files.FileHandle;



/** Create FWT components and windows based on UI XML specifications */
public class XMLUICreator extends DefaultHandler
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


	/** Stack of components. */
	ArrayList<FWTComponent> componentStack = new ArrayList<FWTComponent>();
	/** Current component level. 0 is root window level. */
	int componentLevel = -1;









	// CONSTRUCTORS
	//***********************************************************************************************
	//***********************************************************************************************

	/** Create an XML creator that reads UI specifications. */
	public XMLUICreator()
		{

		}










	// CHECK FOR XML
	//***********************************************************************************************
	//***********************************************************************************************


	/** Checks to see if UI specifications for the UI window exist, with the given parent-file (no extension [.xml]). */
	public static boolean hasUISpecs(String parentfile)
		{
			boolean hasSpecFile = false;

			FileHandle overrideFile = new FileHandle(FWTController.getOverrideUIFilePath()+"/"+parentfile+".xml");
			if (overrideFile.exists())
				hasSpecFile = true;

			// IF no override version found, check core version
			if (!hasSpecFile)
				{
					FileHandle coreFile = new FileHandle(FWTController.getUIFilePath()+"/"+parentfile+".xml");
					// Read UI XML file [Core]
					if (coreFile.exists())
						hasSpecFile = true;
				}

			return hasSpecFile;
		}



	// INFLATE XML
	//***********************************************************************************************
	//***********************************************************************************************


	/** Returns the FWTWindow for the UI specifications with the given parent-file (no extension [.xml]). 
	 * <br> Returns null if no valid file found.  */
	public static FWTWindow create(String parentfile)
		{
			XMLUICreator creator = new XMLUICreator();
			FWTWindow window = null;

			// Read UI XML file [override]
			FileHandle overrideFile = new FileHandle(FWTController.getOverrideUIFilePath()+"/"+parentfile+".xml");
			if (overrideFile.exists())
				try{
					window = creator.read(overrideFile.path());
				}catch(Exception ex)
				{
					// Error message variables
					FWTController.error("XMLUICreator: Error in UI XML file:");
					FWTController.error("   "+ex.getMessage());
					// Return failure
					return null;
				}

			// IF no override version found, load core version
			if (window == null)
				{
					FileHandle coreFile = new FileHandle(FWTController.getUIFilePath()+"/"+parentfile+".xml");
					// Read UI XML file [Core]
					if (coreFile.exists())
						try{
							window = creator.read(coreFile.path());
						}catch(Exception ex)
						{
							// Error message variables
							FWTController.error("XMLUICreator: Error in UI XML file:");
							FWTController.error("   "+ex.getMessage());
							// Return failure
							return null;
						}	
				}

			return window;
		}







	// READ FILE
	//***********************************************************************************************
	//***********************************************************************************************

	/** Reads an XML data contained in the given file and returns a new FWTWindow with all the components. */
	public FWTWindow read(String document) throws Exception
		{
			targetName = document;

			FWTController.log("XMLUICreator: XML UI Creation: "+document);

			// Use the default (non-validating) parser
			SAXParserFactory factory = SAXParserFactory.newInstance();

			try{
				// Parse the input
				SAXParser saxParser = factory.newSAXParser();
				saxParser.parse( new File(document), this );
			}catch (Exception ex) {ex.printStackTrace(); 
			FWTController.error("XMUICreator: XML Sax exception."); throw new Exception("File: "+document+"\n      "+ex.getMessage());}

			// Enter Waiting Loop
			while(!finished)
				{
					try{
						Thread.sleep(1);
					}
					catch (Exception ex) {ex.printStackTrace(); throw new Exception(ex.getLocalizedMessage());}
				}
			finished = false;

			// Return the root window
			return (FWTWindow) componentStack.get(0);
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

			// IF component is properly named
			if (attrs.getValue("name") != null)
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

							// Increment component level
							componentLevel++;	

							// Add parent component depth
							dataPacket.put("depth", Integer.toString(componentLevel+1));

							// Add component type
							dataPacket.put("type", eName);

							// Create component based on type
							FWTComponent comp = FWTComponentFactory.create(dataPacket, eName);

							// Add to stack
							componentStack.add(comp);

							// Add component to container
							if (componentLevel > 0)
								((FWTContainer)componentStack.get(componentLevel-1)).addComponent(comp);

						}
				}

		} // END startElement method





	// END METHODS
	//***********************************************************************************************
	//***********************************************************************************************




	@Override
	public void endElement(String namespaceURI, String sName, String qName) throws SAXException
		{		

			if (componentLevel > 0)
				{
					// Clear the current component
					componentStack.remove(componentLevel);
					// Decrement level
					componentLevel--;
				}
		}





	@Override
	public void endDocument()
		{

			this.finished = true;
		}











}
