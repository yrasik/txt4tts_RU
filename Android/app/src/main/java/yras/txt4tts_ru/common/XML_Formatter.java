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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


public class XML_Formatter {
	public static void Format_simple(String Full_Fin_Name, String Full_Fout_Name) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			// установка используемого XSL-преобразования
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			// установка исходного XML-документа и конечного XML-файла
			//Log.d("4", "TXT_to_XML -----Full_Fin_Name------->" + Full_Fin_Name);
			//Log.d("4", "TXT_to_XML -----Full_Fout_Name------>" + Full_Fout_Name);
			File infile = new File(Full_Fin_Name);
			File outfile = new File(Full_Fout_Name);
			transformer.transform(new StreamSource(new FileInputStream(infile)), new StreamResult(new FileOutputStream(outfile)));

		} catch (Exception e) {
			e.printStackTrace();
			//Log.d("4", "TXT_to_XML <-----ERRRR------");
		}
	}
}







