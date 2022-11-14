package com.gambrell.musicplayer; 

import javax.swing.filechooser.FileFilter; 
import java.io.File; 

public class AudioFilter extends FileFilter // abstract class with abstract methods accept() and getDescription() 
{
 static String [] supportedFiles = {"mp3", "wav", "ogg"}; // contains supported file extensions 
 public boolean accept(File f) // called every time a filter checks a file
 {
  if(f.isDirectory()) 
  {
   return true;
  }
   
  String extension = getFileExtension(f); // gets file extension of file currently being checked 
  
  if(extension.equals("wav") || extension.equals("mp3") || extension.equals("ogg"))
  {
   return true;  // file is a supported file
  }else
  {
   return false; // file isn't a supported file 
  }
 }
 
 public String getDescription() // describes audio file formats supported by the file chooser 
 {
  return "WAV/MP3/OGG Vorbis Files (.wav/.mp3/.ogg)";  
 }
 
 // getFileExtension() finds the last possible file extension in the file name and returns it  
 // last possible file extension is found as file name could contain extra extensions in the name 
 
 public static String getFileExtension(File f) 
 {
  String ext = "";
  String s = f.getName();
  int i = s.lastIndexOf('.');
  
  if (i > 0 &&  i < s.length() - 1) 
  {
   ext = s.substring(i+1).toLowerCase();
  }
   
  return ext;
  }
 
 public static boolean checkForSupportedFormat(String extension)
 {
  boolean supported = false; // never changes if extension isn't supported 
  for(String file : supportedFiles)
  {
   if(extension.equals(file))
   {
	supported = true;    
	return supported; // exit call if file is supported 
   }
  }
  return supported; 
 }
}