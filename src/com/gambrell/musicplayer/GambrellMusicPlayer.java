package com.gambrell.musicplayer; 

import java.io.File;
import java.util.*;
import javax.swing.JLabel;
import javazoom.jlgui.basicplayer.*; 

public class GambrellMusicPlayer implements Runnable 
{ 
 static GambrellMusicPlayer main; // Main class reference (for use in starting file play back check stop status thread and for track insertion into player)    
 static BasicPlayer player; // BasicPlayer class reference (player used for playing files) 
 static Random rand; // Random class reference (used in play list shuffle algorithm) 
 
 static AudioFile audioFile; // AudioFile reference (currently playing file's display name)   
 static ArrayList<AudioFile> playList; // ArrayList AudioFile reference (play list for playing an audio file collection in a folder)
 static JLabel playableFileName; // JLabel reference (audio file name, default is not playing)  
 
 static boolean isPlayList = false; // true if play list is being played, false if single track is being placed  
 static int playListIndex = 0; // play list ArrayList index value 
 static int playListSize = 0; // size of play list 
 static File playListDirectory; // directory of play list folder 
 static boolean playListShuffle = false; // whether or not the play list should be shuffled; true if so, false otherwise 
 static boolean playListIsLooped = false; // play list is looped if true, false if not 
 static int loopCount = 1; // represents the amount of times a play list is played 
 
 static final int SINGLE_AUDIO_FILE_PLAYBACK = 0; // single audio file is playing 
 static final int PLAY_LIST_PLAYBACK = 1; // play list is being played 
 static final int PLAY_LIST_PLAYBACK_COMPLETED = 2; // play list has been completed 
 
 public static void main(String [] args) throws InterruptedException 
 {
  playableFileName = new JLabel("No Song Playing..."); // creates audio file name label 
  main = new GambrellMusicPlayer(); // creates main object  
  player = new BasicPlayer(); // creates Basic Player 
  rand = new Random(); // creates Random object 
  playList = new ArrayList<AudioFile>(); // play list is created 
  new BuildGUI().start();  // starts new BuildGUI thread; calls run() method in BuildGUI; builds all main window components  
 } 
 
 // play list folder files are added to play list ArrayList 
 public void buildPlayList(File [] folder) // embedded folders' contents will be added to play list  
 {
  for(int i = 0; i < folder.length; i++)
  {
	// files are added to play list if they are supported
	if(!folder[i].isDirectory() && (AudioFilter.checkForSupportedFormat(AudioFilter.getFileExtension(folder[i]))))   
	{
	 playList.add(new AudioFile(folder[i])); 	
	}else
	{ // recursive call for to include embedded folders' audio files 
	 if(folder[i].isDirectory())
	  buildPlayList(folder[i].listFiles()); 
	}
  }
  
  for(AudioFile tempFile: playList)
  {
   System.out.println(tempFile.getFileName());  
  }
  System.out.println("\n");
  
  playListSize = playList.size(); // play list size member is initialized or reassigned to size of play list  
  if(playListShuffle)
  {
   shufflePlayList();   
  }
 }
 
 // Removes AudioFile objects from play list 
 public void unloadPlayList()
 {
  int removedTracks = 0; 
  while(removedTracks < playList.size())
  {
   playList.remove(0); // objects shift to front of list as front ones are removed 
  }
 }
 
 // Shuffle play list files 
 public void shufflePlayList()
 {
  int randomIndex = 0; // used for holding calculated random values 
  AudioFile currentAudioFile; // refers to current play list file 
  
  for(int i = 0; i < playListSize - 1; i++)
  {
   randomIndex = rand.nextInt(playListSize);
   // continues generating random indices if generated index is same as current index 
   while(randomIndex == i) {randomIndex = rand.nextInt(playListSize);} 
   // gets AudioFile at random index and assign it to current index, original AudioFile at current index is returned, and assigned to the random index  
   currentAudioFile = playList.set(i, playList.get(randomIndex));  
   playList.set(randomIndex, currentAudioFile); 
  }
  System.out.println("PlayList shuffled. \n");
  
  for(AudioFile tempFile: playList)
  {
   System.out.println(tempFile.getFileName());    
  }
  
  System.out.println(""); 
 }
 
 // Used for opening up selected audio file or folder with BasicPlayer 
 // playState - > Refers to state of player when it comes to playing single files or folders 
 // playState - > 0. play single audio file    1. play back loaded play list   2. completion of play list play back (base case for recursive insertTrackIntoPlayer())  
 public void insertTrackIntoPlayer(int playState)
 {
  try
  {
   if(playState == PLAY_LIST_PLAYBACK)
   {
	isPlayList = true; 
	player.open(playList.get(playListIndex).getAbstractFile()); 	
    GambrellMusicPlayer.playableFileName.setText(playList.get(playListIndex).getFileName());    
    playTrack();   
   }else
   {
	if(playState == SINGLE_AUDIO_FILE_PLAYBACK)
	{
	 player.open(audioFile.getAbstractFile()); // opens BasicPlayer with audio file selected using file chooser  	
	 GambrellMusicPlayer.playableFileName.setText(audioFile.getFileName()); // displays name of audio file being played using file name label in BuildGUI  
	 playTrack();   
	}
	if(playState == PLAY_LIST_PLAYBACK_COMPLETED)
	{
	 playListIndex = 0; // resets play list index  
     if(loopCount == 1 && playListIsLooped && isPlayList) // will replay play list if loop was checked in PlayList menu 
	 {
      loopCount++; // loop will stop as next recursive call to insertTrackIntoPlayer() cannot be made 
      playListSize = 0; // resets play
	  insertTrackIntoPlayer(1); 	 
	 }else
	 {
	  GambrellMusicPlayer.playableFileName.setText("No Song Playing...");  // resets audio file name label to original text 
	  unloadPlayList(); // remove files from play list 
	  ButtonSetup.disablePausePlayBtns(); // disable pause/play buttons until new file or folder is selected via File menu 
	  loopCount = 1; // loopCount needs to be reassigned to original value so that two consecutive plays of play list can occur  
	  playListShuffle = false; // in case shuffle check box was checked, single track was played, and play list was loaded (play list would be shuffled) 
	  return; 	
	 }
	}
   }
  }catch(BasicPlayerException insertionErr)
  {
   insertionErr.printStackTrace();   
  }
 }
 
 // Used for playing audio file opened with BasicPlayer 
 public void playTrack()
 {
  try
   {
    player.play();  // plays audio file  
    player.setGain(.5); // sets player to 50 % volume 
    ButtonSetup.pauseBtn.setEnabled(true); // enables pause button after file starts playing
    
    new Thread(main).start(); // starts new GambrellMusicPlayer thread; calls run() in GambrellMusicPlayer; tests to see if audio file has stopped playing 
    
    }catch(BasicPlayerException playerErr)
    {
     playerErr.printStackTrace();  
    }
   }
 
  // run() of new GambrellMusicPlayer thread checks to see if audio file has stopped playing and enables a repeat button to replay the audio file 
  public void run() 
  {
   while(true)
   {
	try
	{
	 Thread.sleep(1000); 
	}catch(InterruptedException interruptErr)
	{
	 interruptErr.printStackTrace(); 	
	}
	if(player.getStatus() == BasicPlayer.STOPPED) // if BasicPlayer has a stopped status 
	{
     System.out.println("Playback has stopped.");
     break; // break out of stopped-status checking loop 
	}
   }
   
   // Only executed once current file has stopped playing 
   playListIndex++; // increase play list index 
   
   if(playListIndex < playListSize) // continue to play back for the next file if it exists (condition always false for a single file) 
   {
	insertTrackIntoPlayer(1); // there is another file in play list 
   }
   else // single track or play list has ended 
   {
	// Enables File menu and PlayList menu operations
	MusicPlayerMenus.toggleFileSystemChoosers(true);
    MusicPlayerMenus.togglePlayListOperationMenuBtnsAccess(true); 
	
    insertTrackIntoPlayer(2); // calls insertTrackIntoPlayer(), reach base case condition - > play back continues if loop play list check box was selected 
   }
  }
 }