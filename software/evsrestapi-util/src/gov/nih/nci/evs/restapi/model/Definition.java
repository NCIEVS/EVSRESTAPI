package gov.nih.nci.evs.restapi.model;

import java.io.*;
import java.util.*;
import java.net.*;

import com.google.gson.*;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.XStream;

public class Definition
{

// Variable declaration
	private String definition;
	private String type;
	private String source;

// Default constructor
	public Definition() {
	}

// Constructor
	public Definition(
		String definition,
		String type,
		String source) {

		this.definition = definition;
		this.type = type;
		this.source = source;
	}

// Set methods
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSource(String source) {
		this.source = source;
	}


// Get methods
	public String getDefinition() {
		return this.definition;
	}

	public String getType() {
		return this.type;
	}

	public String getSource() {
		return this.source;
	}
	public String toXML() {
		XStream xstream_xml = new XStream(new DomDriver());
		String xml = xstream_xml.toXML(this);
		xml = escapeDoubleQuotes(xml);
		StringBuffer buf = new StringBuffer();
		String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		buf.append(XML_DECLARATION).append("\n").append(xml);
		xml = buf.toString();
		return xml;
	}

	public String toJson() {
		JsonParser parser = new JsonParser();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
	}

	public String escapeDoubleQuotes(String inputStr) {
		char doubleQ = '"';
		StringBuffer buf = new StringBuffer();
		for (int i=0;  i<inputStr.length(); i++) {
			char c = inputStr.charAt(i);
			if (c == doubleQ) {
				buf.append(doubleQ).append(doubleQ);
			}
			buf.append(c);
		}
		return buf.toString();
	}
}
