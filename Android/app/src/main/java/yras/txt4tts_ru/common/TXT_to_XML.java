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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class TXT_to_XML {
	private String Full_Fin_Name;
	private String Full_Fout_Name;

	public TXT_to_XML() {

	}

	public boolean convert(String Full_Fin_Name, String Full_Fout_Name) {

		this.Full_Fin_Name = Full_Fin_Name;
		this.Full_Fout_Name = Full_Fout_Name;
		Log.d("4", "TXT_to_XML -----Full_Fout_Name------>" + Full_Fout_Name);
		return exec();
	}

	private boolean exec() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(Full_Fin_Name), "UTF-8"));
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Full_Fout_Name), "UTF-8"));
			String str;

			out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.write("<speak>");
			out.write("<metadata></metadata>");

			while ((str = in.readLine()) != null) { // while loop begins here
				// System.out.println(thisLine);
				out.write("<p>");
				//str = str.replaceAll("/n/r", "");
				//str = str.replaceAll("/r/n", "");
				//str = str.replaceAll("/n", "");
				//str = str.replaceAll("/r", "");
				out.write(str); // FIXME !!! Заменить <>
				out.write("</p>");
			} // end while

			out.write("</speak>");
			out.flush();

			in.close();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
