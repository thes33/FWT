package com.arboreantears.fwt;


import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/** XML Reader for language files. */
public class XMLLanguageReader extends DefaultHandler

{


	// VARIABLES
	//***********************************************************************************************
	//***********************************************************************************************

	
	/** Data for all language mappings. */
	HashMap<String,String> data = new  HashMap<String,String>(); 

	/** 'True' if reader is finished reading a file. */
	boolean finished = false;




	// CONSTRUCTORS
	//***********************************************************************************************
	//***********************************************************************************************

	/** Create an XML reader that reads all elements for language data. */
	public XMLLanguageReader()
		{

		}





	// PARSING METHODS
	//***********************************************************************************************
	//***********************************************************************************************


	@Override
	public void startDocument()
		{
			// Reset data list
			data = new HashMap<String,String>(); 
		}


	@Override
	public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException
	{
		// Get Element Name
		String eName = sName; // element name
		if ("".equals(eName))
			eName = qName; // not namespace-aware

		String s = "";

		// IF attributes are present
		if (attrs.getLength() > 0)
			{
				// FOR each attribute
				for (int i = 0; i < attrs.getLength(); i++)
					{
						// Get Attribute name
						String aName = attrs.getLocalName (i);
						// IF local name empty
						if ("".equals(aName))
							// Get qualified name
							aName = attrs.getQName(i);
						
						// Get String
						s = attrs.getValue(i);
						if (s.contains("\\n"))
							{
								s = s.replaceAll("\\\\n", "\n");
							}
						
						// Add data to HashMap
						data.put(aName,s);
					}				
			}
	}

	@Override
	public void endElement(String namespaceURI, String sName, String qName) throws SAXException
	{


	}





	@Override
	public void endDocument()
		{
			this.finished = true;
		}







	// READ FILE
	//***********************************************************************************************
	//***********************************************************************************************

	/** Reads an XML language file and returns a <String,String> HashMap of the language data for each object. */
	public HashMap<String,String> read(String document) throws Exception
	{
		// Use the default (non-validating) parser
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		// Prepare Logger and Notify
		FWTController.log("Language: Reading XML file: "+document);

		try{
			// Parse the input
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(new File(document), this);
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

		// Return Data List
		return data;
	}



}
