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

public class XML_SAX_Find_Dialogs_in_Sentence {
	private String Full_Fin_Name;
	private String Full_Fout_Name;
	private String AppPath;

	public XML_SAX_Find_Dialogs_in_Sentence() {

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
			SAXParsDialogs xmlr = new SAXParsDialogs(AppPath, Full_Fout_Name);
			saxParser.parse(is, (DefaultHandler) xmlr);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}

class SAXParsDialogs extends DefaultHandler {

	enum TAG_P_enum {
		BEGIN_TAG__P, // ������ ������
		CONTINUE_TAG__P, // �����������
							// ����������� �
							// ������
		END_TAG__P // ��������������
					// ����������� � ������
	}

	enum DIALOG_enum {
		IN_AUTOR, IN_PERSON_1, IN_PERSON_2,
	}

	enum PERSON_enum {
		PERSON_1, PERSON_2, PERSON_UNKNOWN,
	}

	private TAG_P_enum TAG_P;
	private DIALOG_enum DIALOG;
	private PERSON_enum person_last;

	private Dict_handler Dict_begin_autor;
	private Dict_handler Dict_begin_person;
	private Dict_handler Dict_continue_autor;
	private Dict_handler Dict_continue_person;
	private String thisElement = "";
	private Writer out;
	private String AppPath;
	private String paragraph_content;

	public SAXParsDialogs(String AppPath, String Full_Fout_Name) {
		try {
			this.AppPath = AppPath;
			Dict_begin_autor = new Dict_handler(this.AppPath + "dic/find_dialogs/begin_autor.rex",
					Dict_File_TYPE_enum.REX);
			Dict_begin_person = new Dict_handler(this.AppPath + "dic/find_dialogs/begin_person.rex",
					Dict_File_TYPE_enum.REX);

			Dict_continue_autor = new Dict_handler(this.AppPath + "dic/find_dialogs/continue_autor.rex",
					Dict_File_TYPE_enum.REX);
			Dict_continue_person = new Dict_handler(this.AppPath + "dic/find_dialogs/continue_person.rex",
					Dict_File_TYPE_enum.REX);

			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Full_Fout_Name), "UTF-8"));

			TAG_P = TAG_P_enum.END_TAG__P;
			DIALOG = DIALOG_enum.IN_AUTOR;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void startDocument() throws SAXException {
		// System.out.println("Start parse XML...");
		TAG_P = TAG_P_enum.END_TAG__P;
		DIALOG = DIALOG_enum.IN_AUTOR;

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
		}

		if (qName.equals("s")) {

		}

	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		int index_Dict_begin_autor;
		int index_Dict_begin_person;

		if (qName.equals("p")) {
			TAG_P = TAG_P_enum.END_TAG__P;
		}

		if (qName.equals("s")) {

			if (TAG_P == TAG_P_enum.BEGIN_TAG__P) {
				switch (DIALOG) {
				case IN_AUTOR:
					if (0 != (index_Dict_begin_person = Dict_begin_person.FindFirst(paragraph_content))) {
						switch (person_last) {
						case PERSON_1:
							person_last = PERSON_enum.PERSON_2;
							DIALOG = DIALOG_enum.IN_PERSON_2;
							break;
						case PERSON_2:
							person_last = PERSON_enum.PERSON_1;
							DIALOG = DIALOG_enum.IN_PERSON_2;
							break;
						case PERSON_UNKNOWN:
							person_last = PERSON_enum.PERSON_1;
							DIALOG = DIALOG_enum.IN_PERSON_1;
							break;
						}
						paragraph_content = paragraph_content.substring(index_Dict_begin_person);
					}
					break;
				case IN_PERSON_1:
					if (0 != (index_Dict_begin_person = Dict_begin_person.FindFirst(paragraph_content))) {
						switch (person_last) {
						case PERSON_1:
							person_last = PERSON_enum.PERSON_2;
							DIALOG = DIALOG_enum.IN_PERSON_2;
							break;
						case PERSON_2:
							person_last = PERSON_enum.PERSON_1;
							DIALOG = DIALOG_enum.IN_PERSON_2;
							break;
						case PERSON_UNKNOWN:
							person_last = PERSON_enum.PERSON_1;
							DIALOG = DIALOG_enum.IN_PERSON_1;
							break;
						}
						
					paragraph_content = paragraph_content.substring(index_Dict_begin_person);
					} else if (0 != (index_Dict_begin_autor = Dict_begin_autor.FindFirst(paragraph_content))) {
						DIALOG = DIALOG_enum.IN_AUTOR;
						
						paragraph_content = paragraph_content.substring(index_Dict_begin_autor);
					}
					break;
				case IN_PERSON_2:
					if (0 != (index_Dict_begin_person = Dict_begin_person.FindFirst(paragraph_content))) {
						switch (person_last) {
						case PERSON_1:
							person_last = PERSON_enum.PERSON_2;
							DIALOG = DIALOG_enum.IN_PERSON_2;
							break;
						case PERSON_2:
							person_last = PERSON_enum.PERSON_1;
							DIALOG = DIALOG_enum.IN_PERSON_2;
							break;
						case PERSON_UNKNOWN:
							person_last = PERSON_enum.PERSON_1;
							DIALOG = DIALOG_enum.IN_PERSON_1;
							break;
						}
						paragraph_content = paragraph_content.substring(index_Dict_begin_person);

					} else if (0 != (index_Dict_begin_autor = Dict_begin_autor.FindFirst(paragraph_content))) {
						DIALOG = DIALOG_enum.IN_AUTOR;
						paragraph_content = paragraph_content.substring(index_Dict_begin_autor);

					}
					break;
				default:
				}
				TAG_P = TAG_P_enum.CONTINUE_TAG__P;
				
			}

			while (true) {

				switch (DIALOG) {
				case IN_AUTOR:
					if (0 != (index_Dict_begin_person = Dict_begin_person.FindFirst(paragraph_content))) {
						switch (person_last) {
						case PERSON_1:
							person_last = PERSON_enum.PERSON_2;
							DIALOG = DIALOG_enum.IN_PERSON_2;
							break;
						case PERSON_2:
							person_last = PERSON_enum.PERSON_1;
							DIALOG = DIALOG_enum.IN_PERSON_2;
							break;
						case PERSON_UNKNOWN:
							person_last = PERSON_enum.PERSON_1;
							DIALOG = DIALOG_enum.IN_PERSON_1;
							break;
						}

					}
					break;
				case IN_PERSON_1:
					if (0 != (index_Dict_begin_person = Dict_begin_person.FindFirst(paragraph_content))) {
						switch (person_last) {
						case PERSON_1:
							person_last = PERSON_enum.PERSON_2;
							DIALOG = DIALOG_enum.IN_PERSON_2;
							break;
						case PERSON_2:
							person_last = PERSON_enum.PERSON_1;
							DIALOG = DIALOG_enum.IN_PERSON_2;
							break;
						case PERSON_UNKNOWN:
							person_last = PERSON_enum.PERSON_1;
							DIALOG = DIALOG_enum.IN_PERSON_1;
							break;
						}
					} else if (0 != (index_Dict_begin_autor = Dict_begin_autor.FindFirst(paragraph_content))) {
						DIALOG = DIALOG_enum.IN_AUTOR;
					}
					break;
				case IN_PERSON_2:
					if (0 != (index_Dict_begin_person = Dict_begin_person.FindFirst(paragraph_content))) {
						switch (person_last) {
						case PERSON_1:
							person_last = PERSON_enum.PERSON_2;
							DIALOG = DIALOG_enum.IN_PERSON_2;
							break;
						case PERSON_2:
							person_last = PERSON_enum.PERSON_1;
							DIALOG = DIALOG_enum.IN_PERSON_2;
							break;
						case PERSON_UNKNOWN:
							person_last = PERSON_enum.PERSON_1;
							DIALOG = DIALOG_enum.IN_PERSON_1;
							break;
						}
					} else if (0 != (index_Dict_begin_autor = Dict_begin_autor.FindFirst(paragraph_content))) {
						DIALOG = DIALOG_enum.IN_AUTOR;
					}
					break;
				default:
				}				
				
			}

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
		// System.out.println("Stop parse XML...");

		try {
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
