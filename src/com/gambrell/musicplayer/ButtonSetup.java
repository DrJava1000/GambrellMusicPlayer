package com.gambrell.musicplayer; 

import javax.swing.*;

import javazoom.jlgui.basicplayer.*;

import java.awt.*;  
import java.awt.event.*; 

public class ButtonSetup implements Runnable 
{
 static JButton pauseBtn; // JButton reference (pause button) 
 static JButton playBtn; // JButton reference (play button) 
 static JPanel buttonHolder = new JPanel(); // JPanel reference (container for holding play/pause buttons)  
 
 public void run()
 {
  BuildGUI.frame.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(204, 204, 0), 5)); // sets straight line border for root pane of main window 
  FlowLayout rowLayout = new FlowLayout(); 
  rowLayout.setHgap(20); // sets how many horizontal pixels are between components in a container using the FlowLayout manager 
  buttonHolder.setLayout(rowLayout); 
  
  buttonHolder.setOpaque(false); // container takes content pane's color 
  
  // Play and Pause Buttons created with pause and play icons 
  pauseBtn = new JButton(new ImageIcon("pause-256.png"));   
  playBtn = new JButton(new ImageIcon("play-512.png"));   
  
  Dimension pausePlayMeasure = new Dimension(300, 150); // creates Dimension reference (width of 300 and height of 150) 
  pauseBtn.setPreferredSize(pausePlayMeasure); // sets size of pause button with Dimension reference 
  pauseBtn.setBackground(new Color(178,34,34)); // sets background colors to set RGB values for coloring pause button 
  pauseBtn.setEnabled(false); // disables pause button until file is played 
  buttonHolder.add(pauseBtn); // adds pause button to pause/play buttons generic container 
  
  playBtn.setPreferredSize(pausePlayMeasure); // sets size of play button with Dimension reference 
  playBtn.setBackground(new Color(107,142,35)); // sets background colors to set RGB values for coloring play button 
  playBtn.setEnabled(false); // disables play button until file is played 
  buttonHolder.add(playBtn); // adds play button to pause/play buttons generic container 
  
  // Register pause button to halt play back 
  pauseBtn.addActionListener((ActionEvent e) -> 
  {
   try
   {
    GambrellMusicPlayer.player.pause(); // pause audio play back 
    pauseBtn.setEnabled(false); // disables pause button 
    playBtn.setEnabled(true); // enables play button  
   }catch(BasicPlayerException pausePlayerErr)
   {
	pausePlayerErr.printStackTrace();    
   }
  }); 
  
  // Register play button to resume play back 
  playBtn.addActionListener((ActionEvent e) -> 
  {
   try
   {
    GambrellMusicPlayer.player.resume(); // resume audio play back 
    playBtn.setEnabled(false); // disables play button 
    pauseBtn.setEnabled(true); // enables pause button 
   }catch(BasicPlayerException resumePlayerErr)
   {
	resumePlayerErr.printStackTrace();    
   }
  }); 
  
  BuildGUI.frame.add(buttonHolder); // adds pause/play buttons to main window 
 }
 
 // Disables access to track pause and play buttons 
 public static void disablePausePlayBtns()
 {
  pauseBtn.setEnabled(false); 	 
  playBtn.setEnabled(false);
  System.out.println("Pause/Play buttons disabled."); 
 }
}