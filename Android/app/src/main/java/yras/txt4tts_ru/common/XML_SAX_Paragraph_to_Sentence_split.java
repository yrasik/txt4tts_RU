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

public class XML_SAX_Paragraph_to_Sentence_split {
	private String Full_Fin_Name;
	private String Full_Fout_Name;
	private String AppPath;


	public XML_SAX_Paragraph_to_Sentence_split() {

	}

	public boolean split(String AppPath, String Full_Fin_Name, String Full_Fout_Name) {

		this.AppPath = AppPath;
		this.Full_Fin_Name = Full_Fin_Name;
		this.Full_Fout_Name = Full_Fout_Name;

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
			SAXPars xmlr = new SAXPars(AppPath, Full_Fout_Name);
			saxParser.parse(is, (DefaultHandler) xmlr);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}




class SAXPars extends DefaultHandler {

	enum TAG_P_enum {
		BEGIN_TAG__P, END_TAG_P
	}

	private TAG_P_enum TAG_P;
	private String thisElement = "";
	private Dict_handler Dict;
	private Writer out;
	private String AppPath;
    private String paragraph_content;

    public SAXPars(String AppPath, String Full_Fout_Name) {
		try {
			this.AppPath = AppPath;
			Dict = new Dict_handler(this.AppPath +"dic/paragraph_split_to_sentences.rex", Dict_File_TYPE_enum.REX);
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Full_Fout_Name), "UTF-8"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void startDocument() throws SAXException {
		//System.out.println("Start parse XML...");
		TAG_P = TAG_P_enum.END_TAG_P;

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

		if (qName.equals("p")) {
			TAG_P = TAG_P_enum.BEGIN_TAG__P;
			paragraph_content = "";
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {


        if (qName.equals("p")) {
            TAG_P = TAG_P_enum.END_TAG_P;

			String Sentences = Dict.replaceAll(paragraph_content);

            try {
			//	Log.d("4", "TXT_to_XML <-----endElement--->" + paragraph_content);

                String Sentence[] = Sentences.split("/n");

                for (int i = 0; i < Sentence.length; i++) {
                    if (Sentence[i].length() != 0) {
                        //	System.out.println(Sentence[i]);
                       //   Log.d("4", "TXT_to_XML <-----Sentence------>" + Sentence[i]);

                        try {
                            out.write("<s>");
                            out.write(Sentence[i]);
                            out.write("</s>");
                            out.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

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
		if (thisElement.equals("p")) {
            paragraph_content += String.copyValueOf(ch, start, length);
			//Log.d("4", "TXT_to_XML <-----text--->" + paragraph_content);

			if (TAG_P == TAG_P_enum.BEGIN_TAG__P) {


			} else {

				try {

					out.write(String.copyValueOf(ch, start, length));//Неведомая фигня
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
