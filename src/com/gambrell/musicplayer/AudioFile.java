package com.gambrell.musicplayer; 

import java.io.File; 

public class AudioFile // class encapsulates an audio file with the name it will be displayed with when it is loaded into the player 
{
 private String fileName; // name of audio file (reference) 
 private File abstractFile; // location of audio file (reference) 
 
 public AudioFile(File file)
 {
  fileName = file.getName(); 
  abstractFile = file;  
 }
 
 public File getAbstractFile()
 {
  return abstractFile;  // returns abstract representation of audio file 
 }
 
 public String getFileName() // returns name of audio file as String 
 {
  return fileName; 
 }
}