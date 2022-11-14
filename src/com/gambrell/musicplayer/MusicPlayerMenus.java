package com.gambrell.musicplayer;

import javax.swing.*; 
import javax.swing.filechooser.FileFilter; 
import java.awt.event.*; 
import java.io.*; 

public class MusicPlayerMenus implements Runnable {
  
  static JMenuBar mainMenuBar; // JMenuBar reference (menu bar for music player) 
  static JMenu fileMenu; // JMenu reference (File menu in music player) 
  static JMenu playListMenu; // JMenu reference (PlayList Operations menu in player) 
  static JMenuItem audioFileOpenerMenuBtn; // JMenuItem reference (file opener) 
  static JMenuItem playListOpenerMenuBtn; // JMenuItem reference (folder opener) 
  static JCheckBoxMenuItem shuffleBtn; // JMenuItem reference (shuffle play list check box) 
  static JCheckBoxMenuItem loopBtn; // JMenuItem reference (loop play list check box) 
  
  public void run()
  {
   mainMenuBar = new JMenuBar(); // creates menu bar 
   
   fileMenu = new JMenu("File"); // creates File menu 
   audioFileOpenerMenuBtn = new JMenuItem("Open file... ");  // create file opener menu button 
   playListOpenerMenuBtn = new JMenuItem("Open folder... "); // create folder opener menu button 
   
   playListMenu = new JMenu("PlayList Operations"); // creates PlayList operations menu 
   shuffleBtn = new JCheckBoxMenuItem("Shuffle"); // creates shuffle check box for shuffling play list songs 
   loopBtn = new JCheckBoxMenuItem("Loop"); // creates loop check box for play list play back (only loops once) 
   
   JFileChooser fileSelection = new JFileChooser(); // file chooser for opening files (single audio files)  
   JFileChooser folderSelection = new JFileChooser(); // file chooser for opening folders (collection of audio files as play list) 
   
   // disables default filtering mechanism for only filtering out hidden files
   fileSelection.setAcceptAllFileFilterUsed(false); 
   folderSelection.setAcceptAllFileFilterUsed(false);
   
   // Register button in File menu to open file chooser for single audio file selection 
   audioFileOpenerMenuBtn.addActionListener((ActionEvent e) -> 
   {
	fileSelection.setFileFilter(new AudioFilter()); // sets AudioFilter as a file filter for selecting audio files of supported formats   
	   
	int fileChooserState = fileSelection.showOpenDialog(GambrellMusicPlayer.playableFileName); // opens file opener and returns a integer value based on what the user does next 
		  
	if(fileChooserState == JFileChooser.APPROVE_OPTION) // if the user selects a file and chooses to open it 
	{
     GambrellMusicPlayer.audioFile = new AudioFile(fileSelection.getSelectedFile()); // the absolute file is taken and used to create an AudioFile  
	}else
     {
	  return; // cancel the button click that led to opening of the file chooser if the file chooser was exited out 
	 }
		 
	//Disable all menu operations 
	toggleFileSystemChoosers(false); 
    togglePlayListOperationMenuBtnsAccess(false); 
	
    GambrellMusicPlayer.main.insertTrackIntoPlayer(0); // begins play back of single audio file      
   });
   
   // Register button in File menu to open file chooser for play list folder selection 
   playListOpenerMenuBtn.addActionListener((ActionEvent e) -> 
   {
    folderSelection.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // folder opener will only show directories (folders) 
    folderSelection.setFileFilter(new FileFilter() // creates new FileFilter for play list opener (anonymous class only needed to change filter text) 
    		{
    	     public boolean accept(File f)
    	     {
    	      return true;  // all folders are accepted
    	     }
    	     public String getDescription()
    	     {
    	      return "Folders Only"; 	 
    	     }
    		}); 
    
    int dirChooserState = folderSelection.showOpenDialog(GambrellMusicPlayer.playableFileName); // opens file chooser and returns a integer value based on what the user does next 
			  
    if(dirChooserState == JFileChooser.APPROVE_OPTION) // if the user selects a folder and chooses to open it 
    {
	 GambrellMusicPlayer.playListDirectory = folderSelection.getSelectedFile(); // the absolute directory is taken and used to create an AudioFile  
	 if(GambrellMusicPlayer.playListDirectory.listFiles().length == 0) // if selected directory is empty, an error message is brought up and the chooser is exited out 
	 {
	  JOptionPane.showMessageDialog(fileMenu, "This folder has no files. Try another one.", "Empty Folder", JOptionPane.ERROR_MESSAGE);
	  return; 	 
	 }
    }else
	 {
	  return; // cancel the button click that led to opening of the file chooser if the file chooser was exited out 
	 }
    
    // Add folder audio files to play list 
    GambrellMusicPlayer.main.buildPlayList(GambrellMusicPlayer.playListDirectory.listFiles());		 
    
    // Disable menu operations 
    toggleFileSystemChoosers(false); 
    togglePlayListOperationMenuBtnsAccess(false); 
    
    GambrellMusicPlayer.main.insertTrackIntoPlayer(1); // begins play back of first audio file in play list    
   }); 
   
   // Register shuffle check box to set shuffle value to true if checked or false if unchecked 
   shuffleBtn.addActionListener((ActionEvent e) -> 
   {
	if(shuffleBtn.isSelected()) // if shuffle check box is selected
	{	
	 GambrellMusicPlayer.playListShuffle = true; 
	}else
	{
	 GambrellMusicPlayer.playListShuffle = false;  
	}
   });
   
   // Register loop check box to set loop value to true if checked or false if unchecked 
   loopBtn.addActionListener((ActionEvent e) -> 
   {
	if(loopBtn.isSelected()) // if loop check box is selected 
	{
	 GambrellMusicPlayer.playListIsLooped = true; 	
	}else
	{
	 GambrellMusicPlayer.playListIsLooped = false; 	
	}
   });
   
   // Add file and folder opener buttons to File menu, and File menu to menu bar
   fileMenu.add(audioFileOpenerMenuBtn); 
   fileMenu.addSeparator(); // adds a separator between file and folder openers 
   fileMenu.add(playListOpenerMenuBtn); 
   mainMenuBar.add(fileMenu); 
   
   // Add shuffle and loop check boxes to PlayList Operations menu, and PlayList Operations menu to menu bar 
   playListMenu.add(shuffleBtn); 
   playListMenu.addSeparator(); // adds a separator between shuffle and loop check boxes 
   playListMenu.add(loopBtn); 
   mainMenuBar.add(playListMenu); 
   
   BuildGUI.frame.setJMenuBar(mainMenuBar); // sets the menu bar of the main window 
  }
  
  // Enables or disables play list menu check boxes (enable if passed true, disable if passed false) 
  public static void togglePlayListOperationMenuBtnsAccess(boolean enable)
  {
   shuffleBtn.setEnabled(enable);   
   loopBtn.setEnabled(enable); 
  }
  
  // Enables or disables file and folder openers (enable if passed true, disable if passed false) 
  public static void toggleFileSystemChoosers(boolean enableOpener)
  {
   audioFileOpenerMenuBtn.setEnabled(enableOpener);
   playListOpenerMenuBtn.setEnabled(enableOpener);
  }
}
