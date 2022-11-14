package com.gambrell.musicplayer; 

import javax.swing.*;
import java.awt.*;  

public class BuildGUI extends Thread  
{
 static JFrame frame; 
 static JPanel audioFileNameAndVolManipulatorContainer = new JPanel(); // JPanel reference (holds currently-playing audio file name and volume slider)   
 
 public void run()  
 {
  JFrame.setDefaultLookAndFeelDecorated(true); // Java Look And Feel provides window decorations 
  frame = new JFrame("GambrellMusicPlayer v6.0"); // JFrame reference (main application window)  
  frame.getContentPane().setBackground(new Color(100, 149, 237)); // sets content pane to have a background color of light blue 
  
  audioFileNameAndVolManipulatorContainer.setOpaque(false); // container becomes transparent to show background color 
  
  BuildGUI.customizeLabel(GambrellMusicPlayer.playableFileName); // call for to customize file name label   
  
  audioFileNameAndVolManipulatorContainer.add(GambrellMusicPlayer.playableFileName); // add file name label to audio components container
  
  Thread menusCreation = new Thread(new MusicPlayerMenus()); 
  menusCreation.start(); // runs the thread responsible for building and adding the application's menus; calls run() in MusicPlayerMenus
  
  try
  {
   menusCreation.join(); // waits for menu creation thread to die 
  }catch(InterruptedException interruptErr)
  {
   interruptErr.printStackTrace();   
  }
  
  Thread volSliderCreation = new Thread(new VolumeSlider()); 
  volSliderCreation.start(); // runs the thread responsible for building and adding a volume slider; calls run() in VolumeSlider  
	
  try
  {
   volSliderCreation.join(); // waits for volume slider creation thread to die 
  }catch(InterruptedException interruptErr)
   {
	interruptErr.printStackTrace();  
   }  
	  
  Thread buttonCreation = new Thread(new ButtonSetup()); 
  buttonCreation.start(); // runs thread that builds pause/play and repeat buttons; calls run() in ButtonSetup  
	
  try
  {
   buttonCreation.join(); // waits for pause/play and repeat button creation thread to die 
  }catch(InterruptedException interruptErr)
   {
	interruptErr.printStackTrace();  
   } 
  
  frame.add(audioFileNameAndVolManipulatorContainer, BorderLayout.NORTH); // adds audio components container to top of main frame 
  frame.setResizable(false); // prevents user from being able to resize window 
  frame.pack(); // resizes main window to fit embedded components 
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // application will close when window is exited 
  frame.setLocationRelativeTo(null); // positions main application window in middle of screen 
  frame.setVisible(true); // makes window visible 
 }
 
 // customizeLabel() designs JLabels in app's style 
 public static void customizeLabel(JLabel tempLabel)
 {
  tempLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14)); // sets font of JLabel 
  tempLabel.setOpaque(true); // allows for the JLabel to be painted on 
  tempLabel.setBackground(new Color(51, 51, 255)); // sets color of JLabel 
  tempLabel.setForeground(new Color(255, 128, 0)); // sets color of text within JLabel 	 
 }
}