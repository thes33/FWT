package com.arboreantears.fwt;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/** A generic XML data file reader. */
public class XMLDataReader extends DefaultHandler
{

	// VARIABLES
	//***********************************************************************************************
	//***********************************************************************************************

	/** 'True' if reader is finished reading a file. */
	boolean finished = false;

	/** Data packet to return. */
	XMLDataPacket dataPacket = new XMLDataPacket();




	// CONSTRUCTORS
	//***********************************************************************************************
	//***********************************************************************************************

	/** Create an XML reader that reads generic XML data files. */
	public XMLDataReader()
		{

		}





	// READ FILE
	//***********************************************************************************************
	//***********************************************************************************************

	/** Reads an XML data contained in the given file and returns the data packet. */
	public XMLDataPacket read(String document) throws Exception
	{
		// Use the default (non-validating) parser
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		try{
			// Parse the input
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse( new File(document), this );
		}catch (Exception ex) 
			{
				ex.printStackTrace(); 
				throw new Exception(this.getClass().getSimpleName()+": XML Read Error: "+document+"\n      "+ex.getMessage());
			}

		// Enter Waiting Loop
		while(!finished)
			{
				try{
					Thread.sleep(1);
				}
				catch (Exception ex) {ex.printStackTrace(); throw new Exception(ex.getLocalizedMessage());}
			}
		finished = false;

		// Return constructed packet
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

		// IF has attributes
		if (attrs.getLength() > 0)
			{
				// Add eName to data
				dataPacket.put("eName", eName);

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
			}


	} // END startElement method











	// END METHODS
	//***********************************************************************************************
	//***********************************************************************************************




	@Override
	public void endElement(String namespaceURI, String sName, String qName) throws SAXException
	{
		// Get Element Name
		String eName = sName; // element name
		if ("".equals(eName))
			eName = qName; // not namespace-aware
		

	}




	@Override
	public void endDocument()
		{
			this.finished = true;
		}



















}
