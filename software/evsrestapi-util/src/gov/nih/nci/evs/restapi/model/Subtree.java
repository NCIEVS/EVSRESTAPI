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

public class Subtree
{

// Variable declaration
	private String code;
	private String label;
	private boolean leaf;
	private List<Subtree> children;

// Default constructor
	public Subtree() {
	}

// Constructor
	public Subtree(
		String code,
		String label,
		boolean leaf,
		List<Subtree> children) {

		this.code = code;
		this.label = label;
		this.leaf = leaf;
		this.children = children;
	}

// Set methods
	public void setCode(String code) {
		this.code = code;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public void setChildren(List<Subtree> children) {
		this.children = children;
	}


// Get methods
	public String getCode() {
		return this.code;
	}

	public String getLabel() {
		return this.label;
	}

	public boolean getLeaf() {
		return this.leaf;
	}

	public List<Subtree> getChildren() {
		return this.children;
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
