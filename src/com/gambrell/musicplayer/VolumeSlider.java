package com.gambrell.musicplayer;

import java.awt.*;
import javax.swing.*; 
import javax.swing.event.*;
import javazoom.jlgui.basicplayer.*; 

public class VolumeSlider implements Runnable, ChangeListener {
  
  static JPanel volumeComponentsContainer = new JPanel(); // JPanel reference (creates container for volume label and slider) 
  static JSlider volumeSlider = new JSlider(0, 100, 50); // JSlider reference (creates volume slider for changing play back volume)  
  static JLabel volumeControlMessage =  new JLabel("Volume: "); // JLabel reference (volume label)  
  static int sliderValue = 0; // value taken from volume slider (0 to 100) 
  static double newVolume = 0; // setGain volume input value (0.0 to 1.0) 
  
  public void run()
  {
   volumeSlider.setPreferredSize(new Dimension(160, 70)); // sets volume slider's size to a width of 160 pixels and a height of 70 pixels 
   volumeSlider.setMajorTickSpacing(10); // sets spacing for large ticks (spacing of 10) 
   volumeSlider.setMinorTickSpacing(1); // sets spacing for small ticks (spacing of 1)
   volumeSlider.setPaintTicks(true); // draws ticks on volume slider 
   
   volumeSlider.addChangeListener(this); // registers VolumeSlider class as listener for ChangeEvents 
   
   volumeSlider.setOpaque(false); // allows for painting in volume components container to take effect on volume slider 
   volumeComponentsContainer.setOpaque(false); // allows for content pane background paint to take effect on volume components container
   
   BuildGUI.customizeLabel(volumeControlMessage); // customize volume label 
   
   // Adds volume label and slider to a container for volume components 
   volumeComponentsContainer.add(volumeControlMessage); 
   volumeComponentsContainer.add(volumeSlider);
   
   // Changes audio components container layout manager to vertical BoxLayout (components stacked on top of each other) and adds volume components below audio file display label 
   BuildGUI.audioFileNameAndVolManipulatorContainer.setLayout(new BoxLayout(BuildGUI.audioFileNameAndVolManipulatorContainer, BoxLayout.Y_AXIS));
   BuildGUI.audioFileNameAndVolManipulatorContainer.add(volumeComponentsContainer); 
  }
  
  // Called when volume slider is moved at any point 
  public void stateChanged(ChangeEvent e)
  { 
   try
   {  
    sliderValue = volumeSlider.getValue(); // gets volume from volume slider 
    System.out.println("\nVolume: " + sliderValue + "\n"); 
    
    newVolume = (sliderValue * 1.0) / 100; 
	GambrellMusicPlayer.player.setGain(newVolume); // sets volume 
    
    System.out.println("Volume changed."); 
   }catch(BasicPlayerException playerErr)
    {
	 playerErr.printStackTrace();   
    } 
   }
 }
