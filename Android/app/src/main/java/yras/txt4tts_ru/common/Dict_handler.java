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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Dict_handler {

	enum Processing_status_enum {
		STATE_WORK, STATE_SKIP, STATE_EXIT
	}

	private String Group_for_Find;
	private String Group_for_Replace;

	private Dict_File_TYPE_enum Rxp_File_TYPE;
	private Processing_status_enum Processing;

	private Pattern pt_escape = Pattern.compile("(^#ESCAPE.*$)"); // Конец
																	// словаря.
	private Pattern pt_read_off = Pattern.compile("(^#READ[\\s|_]+OFF.*$)"); // Пауза
																			// считывания
	private Pattern pt_read_on = Pattern.compile("(^#READ[\\s|_]+ON.*$)"); // Возобновление
																		// считывания
	private Pattern pt_comment = Pattern.compile("(^#+.*$)|(^\\s*$)"); // Комментарии,
																		// пустые
																		// строки
																		// и
																		// строки
																		// из
																		// пробелов
	private Pattern pt_dic_string = Pattern.compile("^(.+)=(.*)$"); // Строчка
																	// словаря

	private Matcher mt;

	private String Full_Dict_File_Name;

	public Dict_handler() {
		Rxp_File_TYPE = Dict_File_TYPE_enum.REX;
		Processing = Processing_status_enum.STATE_WORK;
		this.Full_Dict_File_Name = (String) "";
	}

	public Dict_handler(String Full_Dict_File_Name) {
		Rxp_File_TYPE = Dict_File_TYPE_enum.REX;
		Processing = Processing_status_enum.STATE_WORK;
		this.Full_Dict_File_Name = Full_Dict_File_Name;
	}

	public Dict_handler(String Full_Dict_File_Name, Dict_File_TYPE_enum Rxp_File_TYPE) {
		this.Rxp_File_TYPE = Rxp_File_TYPE;
		Processing = Processing_status_enum.STATE_WORK;
		this.Full_Dict_File_Name = Full_Dict_File_Name;
	}

	public void set_Full_Dict_File_Name(String Full_Dict_File_Name) {
		this.Full_Dict_File_Name = Full_Dict_File_Name;
		return;
	}

	public void set_Dict_TYPE(Dict_File_TYPE_enum File_TYPE) {
		this.Rxp_File_TYPE = File_TYPE;
	}

	public String replaceAll(String Sentence_line) {
		String Processed_line = Sentence_line;
		String line_DIC;
		Processing = Processing_status_enum.STATE_WORK;
		try {

		//	Log.d("4", "TXT_to_XML <-----Processed_line---first--->" + Processed_line);

			// инициализируем поток вывода из файлу
			FileInputStream fis_DIC = new FileInputStream(Full_Dict_File_Name);
			InputStreamReader isr_DIC = new InputStreamReader(fis_DIC, Charset.forName("UTF-8"));
			BufferedReader br_DIC = new BufferedReader(isr_DIC);
			int i = 0;
			boolean escape = false;

			while (((line_DIC = br_DIC.readLine()) != null) && (escape == false)) {
				i++;
				// System.out.println("line " + i + ": ");

				if (get_Groups(line_DIC)) {
					// rxp.Find(rxp.get_Group_for_Find(), line_IN);
					// System.out.println(">>>>>> 1 >>>>>>");
                  //  Log.d("4", "TXT_to_XML <-----Processed_line---before--->" + Processed_line);

					//if(Group_for_Replace.isEmpty()) {
                        String temp = Processed_line;
					//	Processed_line = Processed_line.replaceAll(Group_for_Find, "");

					//}
					//else {
					Processed_line = Processed_line.replaceAll(Group_for_Find, Group_for_Replace);
					Processed_line = Processed_line.replaceAll("null", "");

					//}

					//	Log.d("4", "TXT_to_XML <-----3------" + Group_for_Find + "!!!!!!!!!!" + Group_for_Replace + "----" + Processed_line + "++++" + temp);




					//  Log.d("4", "TXT_to_XML <-----Processed_line---after--->" + Processed_line);
                  //  Log.d("4", "TXT_to_XML <-----Group_for_Find------>" + Group_for_Find);
                  //  Log.d("4", "TXT_to_XML <-----Group_for_Replace------>" + Group_for_Replace);

                    // System.out.println(Processed_line);
					// System.out.println("<<<<<< 2 <<<<<<");
				} 
				else if (Processing == Processing_status_enum.STATE_EXIT) {
					escape = true;
				}
			}
			br_DIC.close();
			isr_DIC.close();
			// закрываем поток чтения файла
			fis_DIC.close();
		} catch (IOException e) {
			e.printStackTrace();
			Processed_line = Sentence_line;
			Log.d("4", "TXT_to_XML <--ERROR-->" + e.getMessage()  );
		}

		//Log.d("4", "TXT_to_XML <-----Processed_line---return--->" + Processed_line);
		return Processed_line;
	}

	private boolean get_Groups(String Dict_line) {
		mt = pt_escape.matcher(Dict_line);

		if (mt.find()) {
			// System.out.println("ESCAPE_line---->'" + Dict_line + "'<---");
			Group_for_Find = "";
			Group_for_Replace = "";
			Processing = Processing_status_enum.STATE_EXIT;
			return false;
		}

		mt = pt_read_off.matcher(Dict_line);

		if (mt.find()) {
			// System.out.println("READ_OFF_line---->'" + Dict_line + "'<---");
			Group_for_Find = "";
			Group_for_Replace = "";

			switch (Processing) {
			case STATE_EXIT:
			case STATE_SKIP:
				break;
			case STATE_WORK:
				Processing = Processing_status_enum.STATE_SKIP;
				break;
			}
			return false;
		}

		mt = pt_read_on.matcher(Dict_line);

		if (mt.find()) {
			// System.out.println("READ_ON_line---->'" + Dict_line + "'<---");
			Group_for_Find = "";
			Group_for_Replace = "";
			switch (Processing) {
			case STATE_EXIT:
				break;
			case STATE_SKIP:
				Processing = Processing_status_enum.STATE_WORK;
				break;
			case STATE_WORK:
				Processing = Processing_status_enum.STATE_WORK;
				break;
			}
			return false;
		}

		mt = pt_comment.matcher(Dict_line);

		if (mt.find()) {
			// System.out.println("COMMENT_line---->'" + Dict_line + "'<---");
			Group_for_Find = "";
			Group_for_Replace = "";
			switch (Processing) {
			case STATE_EXIT:
			case STATE_SKIP:
			case STATE_WORK:
				break;
			}
			return false;
		}

		switch (Processing) {
		case STATE_EXIT:
			return false;
		case STATE_SKIP:
			return false;
		case STATE_WORK:
			break;
		}

		mt = pt_dic_string.matcher(Dict_line);

		if (mt.find()) {
			// System.out.println(mt.group(1));

			switch (Rxp_File_TYPE) {
			case REX:
				Group_for_Find = mt.group(1); // (\d)\s*\-\s*(\bго\b)=$1-ого
				break;
			case DIC:
				Group_for_Find = "\\b" + mt.group(1) + "\\b"; // веденая=ведёная
				break;
			}

			// System.out.println(mt.group(1));
			// System.out.println(mt.groupCount());
			
			if(mt.groupCount() == 2)
			{
              Group_for_Replace = mt.group(2);

				if(Group_for_Replace.length() == 0)
					Group_for_Replace = "";
			}
			else
			{
			  Group_for_Replace = "";
			}
			return true;
		} else {
			// System.out.println("get_Groups Not found");
			Group_for_Find = "";
			Group_for_Replace = "";
			return false;
		}
	}

	public boolean Find(String RegExp, String line) {
		Pattern pt = Pattern.compile(RegExp);
		Matcher mt = pt.matcher(line);
		if (mt.find()) {
			for (int i = 1; i <= mt.groupCount(); i++) {
				System.out.println("Find " + i + " >>> '" + mt.group(i) + "' <<<<");
			}
			return true;
		} else {
			System.out.println("Find not found");
			return false;
		}

	}

}
