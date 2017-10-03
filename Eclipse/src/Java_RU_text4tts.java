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



import yras.txt4tts_ru.common.Dict_File_TYPE_enum;
import yras.txt4tts_ru.common.XML_Formatter;
import yras.txt4tts_ru.common.TXT_to_XML;
import yras.txt4tts_ru.common.XML_SAX_Paragraph_to_Sentence_split;
import yras.txt4tts_ru.common.XML_SAX_Sentences_and_RegEXP;



public class Java_RU_text4tts 
  {
	private static  String AppPath = "../Android/app/src/main/assets/";
	
	
	
	
	public static void main(String[] args) 
	{				
		TXT_to_XML  txt2xml = new TXT_to_XML();
			
	    System.out.println("txt -> xml");
	 
	    txt2xml.convert(AppPath + "tests/test1/test1.txt", AppPath + "tests/test1/res.xml");
	    

        XML_Formatter.Format_simple(AppPath + "tests/test1/res.xml", AppPath + "tests/test1/res_p.xml");

        XML_SAX_Paragraph_to_Sentence_split p2s = new XML_SAX_Paragraph_to_Sentence_split();
        p2s.split(AppPath, AppPath + "tests/test1/res_p.xml", AppPath + "tests/test1/resp.xml");

        XML_Formatter.Format_simple(AppPath + "tests/test1/resp.xml", AppPath + "tests/test1/resp_s.xml");


        XML_SAX_Sentences_and_RegEXP p2s_Rex = new XML_SAX_Sentences_and_RegEXP();

        p2s_Rex.find_and_replace(AppPath + "tests/test1/resp_s.xml", AppPath + "tests/test1/resp_s3.xml", AppPath + "dic/chisla.rex", Dict_File_TYPE_enum.REX);
        XML_Formatter.Format_simple(AppPath + "tests/test1/resp_s3.xml", AppPath + "tests/test1/resp_s4.xml");
	    
		
    }
	
  }
			
		
		


