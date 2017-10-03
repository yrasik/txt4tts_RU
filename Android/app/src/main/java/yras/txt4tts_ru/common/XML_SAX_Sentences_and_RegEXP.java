/*  Program 'txt4tts_RU'. Text for TTS formating for RU language.
    Copyright (C) 2017 Yuri Stepanenko stepanenkoyra@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU Library General Public
    License along with this program. If not, see http://www.gnu.org/licenses/
*/

package yras.txt4tts_ru.common;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XML_SAX_Sentences_and_RegEXP {


	private String Full_Fin_Name;
	private String Full_Fout_Name;
	private String Full_Dict_File_Name;
	private Dict_File_TYPE_enum Rxp_File_TYPE;

	public XML_SAX_Sentences_and_RegEXP() {

	}

	public boolean find_and_replace(String Full_Fin_Name, String Full_Fout_Name, String Full_Dict_File_Name,
			Dict_File_TYPE_enum Rxp_File_TYPE) {

		this.Full_Fin_Name = Full_Fin_Name;
		this.Full_Fout_Name = Full_Fout_Name;
		this.Full_Dict_File_Name = Full_Dict_File_Name;
		this.Rxp_File_TYPE = Rxp_File_TYPE;

		return exec();
	}

	private boolean exec() {
		try {
			File File_I = new File(Full_Fin_Name);
			InputStream Stream_I = new FileInputStream(File_I);
			Reader reader = new InputStreamReader(Stream_I, "UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			SAXPars_REX xmlr = new SAXPars_REX(Full_Fout_Name, Full_Dict_File_Name, Rxp_File_TYPE);
			saxParser.parse(is, (DefaultHandler) xmlr);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}






class SAXPars_REX extends DefaultHandler {
	
	enum TAG_S_enum {
		BEGIN_TAG__S, END_TAG__S
	}

	private TAG_S_enum TAG_S;
	private Dict_handler Dict;	
	private String thisElement = "";
	private Writer out;
	private String paragraph_content;

	public SAXPars_REX(String Full_Fout_Name, String Full_Dict_File_Name,
			Dict_File_TYPE_enum Rxp_File_TYPE) {
		try {
			Dict = new Dict_handler(Full_Dict_File_Name, Dict_File_TYPE_enum.REX);
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Full_Fout_Name), "UTF-8"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void startDocument() throws SAXException {
		//System.out.println("Start parse XML...");
		TAG_S = TAG_S_enum.END_TAG__S;

		try {
			out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		thisElement = qName;

		try {

			out.write("<" + qName + ">");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (qName.equals("s")) {
			TAG_S = TAG_S_enum.BEGIN_TAG__S;
			paragraph_content = "";
		}
	}


	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

		if (qName.equals("s")) {
			TAG_S = TAG_S_enum.END_TAG__S;

				String Sentence = Dict.replaceAll(paragraph_content);
				Log.d("4", "TXT_to_XML <-----endElement--->" + paragraph_content);
				try {
					out.write(Sentence);
					//System.out.println(Sentence);
				} catch (Exception e) {
					e.printStackTrace();
				}

		}


		try {

			out.write("</" + qName + ">");
		} catch (Exception e) {
			e.printStackTrace();
		}


		thisElement = "";
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (thisElement.equals("metadata")) {
			// doc.setId(new Integer(new String(ch, start, length)));
		}
		
		if (thisElement.equals("s")) {
			paragraph_content += String.copyValueOf(ch, start, length);

		
		if (TAG_S == TAG_S_enum.BEGIN_TAG__S) {

		} else {
			try {
			out.write(String.copyValueOf(ch, start, length));
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		
		
		
		}
		


	}

	@Override
	public void endDocument() {
		//System.out.println("Stop parse XML...");

		try {
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}


