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


import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class File_Name_Finder {
	private static String Group_for_Name;
	private static String Group_for_Ext;  
	private static String Group_for_Path;
  		
    public static String get_Name()
    {
    	return Group_for_Name;
    }
    
    
    public static String get_Ext()
    {
    	return Group_for_Ext;
    }
    
    
    public static String get_Path()
    {
    	return Group_for_Path;
    }    
    
    
    public static boolean Find(String Full_File_Name) 
    {
    	
      Pattern pt_name_ext = Pattern.compile("([:\\.\\w/]*)/([\\w]*).(\\w*)$"); //Р?РјСЏ Рё СЂР°СЃС€РёСЂРµРЅРёРµ
      Matcher mt = pt_name_ext.matcher(Full_File_Name);
    	
      if (mt.find()) 
      {
    	  Group_for_Path = mt.group(1);
    	  Group_for_Name = mt.group(2);
    	  Group_for_Ext = mt.group(3);
    	  
    //	 for(int i = 1; i <= mt.groupCount(); i++)
    //	 {
    //		 System.out.println("Find "+ i + " >>> '" + mt.group(i) + "' <<<<");
    //	 }
         return true;
      } 
      else 
      {
        System.out.println("ERROR: File_Name_Finder.Find : Full_File_Name not found");
        return false;
      }
    }    
    
    
    
}


