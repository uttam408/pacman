//Mar 5, 2013
//Pacman.java
/*Objective: The user directs Pacman around a grid eating pieces of cheese and
avoiding red monsters. The objective is to eat all the cheese before running 
into a monster. The 'r' key should reset the game, monsters can overlap monsters
and cheese. 
*/

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;

public class Pacman {
	
	JFrame frame; //the entire frame
	BoardPanel boardpanel; //the game board itself
	DirectionsPanel directionspanel;// a panel with the blue background,
	//and the directions to the game
	
	public static void main (String [] args) {
		Pacman pac = new Pacman();
		pac.Run();
	}
	
	public void Run() {//initialize the frame and its panels
	
		frame = new JFrame ("pacman");//create JFrame
		
		frame.setSize(625,690);//size 
		frame.setLocation(100,100);//and locate the JFrame
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//set the parameters of the JFrame
		frame.setVisible(true); //false hides frame
		frame.setResizable(false); //makes the window a fixed size
		
		boardpanel = new BoardPanel();//create JPanel for the board
		frame.addKeyListener(boardpanel);//add a key listener for the keyboard
		boardpanel.setSize(560,560);//set the panel's size
		frame.getContentPane().add(boardpanel,BorderLayout.CENTER);//locate the panel
		boardpanel.setLocation(25,25);//locate the panel
		
		directionspanel = new DirectionsPanel();//create the other JPanel for the directions
		directionspanel.setSize(615,690);//size the panel
		frame.getContentPane().add(directionspanel,BorderLayout.CENTER);//locate the panel
	}
}

class DirectionsPanel extends JPanel {
	
	public DirectionsPanel() {
		repaint();//repaints to avoid an initial white screen
	}
	public void paintComponent(Graphics g) { 
		super.paintComponent(g); // execute the superclass method first
		
		setBackground(Color.blue); // Set background color
		
		g.setColor(Color.red);
		g.fillRect(0,615,615,60); //fill the region for the directions
		g.setColor(Color.white);
		g.setFont(new Font("SansSerif",Font.PLAIN,22)); 
		g.drawString("Directions: w-up, a-left, s-down, d-right, r-reset",35,640); 
		//print the directions
	}	
}	

class BoardPanel extends JPanel implements KeyListener {
	int[][] locarr = new int [13][2];//the 0 is the pacman position,
	//1-6 are the monsters, 6-12 are the cheese.
	//The second slot has two spaces, [][0] for x coordinate, and
	//[][1] for the y coordinate.
	int objnum;//the number of pieces that have already been assigned a
	//random location
	boolean open = false;// the current state of Pacman's mouth
	Timer timer;// a timer for the monsters' movement
	int cheeseeaten = 0;// a count for the number of cheese eaten
	boolean end = false;// a boolean for endgame
	boolean win = false;// a boolean for winning the game
	
		
	public BoardPanel() {
		addKeyListener(this); //add a key listener
		setPositions(); //sets the initial locations for all the characters/pieces
		moveMonsters movemonsters = new moveMonsters();//a class to randomly
		//move the monsters
		timer = new Timer(300, movemonsters);//call to move the monsters every
		// 3/10 ths of a second 
		timer.start();//start the timer
	}
	
	private class moveMonsters implements ActionListener { //randomly move
	//the monsters whenever the timer triggers the actionEvent 
		public void actionPerformed(ActionEvent e) {
			for (int count=1; count<7; count++) { //repeat for each monster
				int monsterMove = (int)(4.0*Math.random());//pick a direction
				//in the form of an integer from 0 to 3
				switch (monsterMove) { 
					case 0:locarr[count][1]--;break;//move up
					case 1:locarr[count][1]++;break;//move down
					case 2:locarr[count][0]--;break;//move left
					case 3:locarr[count][0]++;break;//move right
				}
			}
			repaint();//repaint to show the new monster positions
		}	
	}
	
	public void setPositions () {//randomly locates the characters/pieces,
	//making sure that they do not initially overlap
		for(objnum = 0; objnum<13; objnum ++) {//repeat for each character/piece
			do{
				setPosition();//randomly locates one character/piece	
			}while (checkPositions());//makes sure the location is not taken
		}		
	}
	
	public boolean checkPositions () {//returns true if the location
	//is already taken
		for (int count = 0; count < (objnum-1); count++) //checks against
		//all values in the array for all the objects that have already 
		//been located
			if (locarr[objnum][0]==locarr[count][0])
				if (locarr[objnum][1]==locarr[count][1]) 
					return true;
		return false;
	} 
	
	public void setPosition () {//randomly locates one character/piece
		locarr[objnum][0] = (int)(10.0*Math.random());//choose a random x
		//location on the 10x10 board(starting from the top left)
		locarr[objnum][1] = (int)(10.0*Math.random());//choose a random y
		//location on the 10x10 board(starting from the top left)
	}
	
	public void checkEdge () { //checks if a location needs to be wrapped
		for (int count=0; count<7; count++) {//repeat for all the movable
		//pieces, the pacman and the monsters, which take up array locations
		//from 0 to 6
			switch (locarr[count][0]) { //check the x loc
				case -1:locarr[count][0]=9;break;//wrap left edge
				case 10:locarr[count][0]=0;break;//wrap right edge
			}
			switch (locarr[count][1]) { //check the y loc 
				case -1:locarr[count][1]=9;break;//wrap top edge
				case 10:locarr[count][1]=0;break;//wrap bottom edge
			}
		}
	}	
	
	public void checkCheese () { //check if a cheese has been eaten by 
	//pacman
		for (int count=7; count<13; count++)//checks for the cheese 
		//locations in the array: 7-12
			if (locarr[0][0]==locarr[count][0])//check if the xcor is the same as pacman's
				if (locarr[0][1]==locarr[count][1]) { //check if the ycor is the same as pacman's
					locarr[count][0]=-2;//change the "location" to -2, warning the
					//print program to not print the cheese
					cheeseeaten++;//increment cheese eaten
					if (cheeseeaten==6) {//check if all the cheese has been eaten
						timer.stop();//stop the monster timer
						end = true;//end the game
						win = true;//set win to true
						System.out.println("endgame-youwin");	
					}
				}	
	}	
	
	public void checkEndGame () { //checks if the pacman has been eaten by a ghost
		for (int count=1; count<7; count++) // check for the ghost locations,
		//from 1-7
			if (locarr[0][0]==locarr[count][0])//check if the xcor is the same as pacman's
				if (locarr[0][1]==locarr[count][1]){//check if the ycor is the same as pacman's
					timer.stop();//stop the timer
					end = true;//end the game
					win = false;//set win to false
					System.out.println("endgame-youlose");	
				}	
	}	
	
	public void paintComponent(Graphics g) { //draw the panels 
		super.paintComponent(g); // execute the superclass method first
		
		setBackground(Color.white); // Set background color

		g.setColor(Color.gray); //set the color to grey
		for (int count=0; count<11; count++) { //draw the horizontal lines
			g.drawLine(25,25+count*50,525,25+count*50);
		}
		for (int count=0; count<11; count++) { //draw the vertical lines
			g.drawLine(25+count*50,25,25+count*50,525);
		}
		
		checkEdge(); //check for wraparound
		checkCheese(); //check for cheese eaten & all cheese eaten
		checkEndGame();	//check for a monster eating pacman				
		
		//cheese
		for (int count= 7; count<13; count++) { //draw the cheese
			if (locarr[count][0]>-1) { //if the cheese has not been eaten
				g.setColor(Color.yellow);
				g.fillRoundRect(locarr[count][0]*50+30,locarr[count][1]*50+30,40,40,10,10);
				g.setColor(Color.white);
				//holes in the cheese
				g.fillOval(locarr[count][0]*50+35,locarr[count][1]*50+40,9,9);
				g.fillOval(locarr[count][0]*50+40,locarr[count][1]*50+52,12,12);
				g.fillOval(locarr[count][0]*50+55,locarr[count][1]*50+35,10,10);
				g.fillOval(locarr[count][0]*50+60,locarr[count][1]*50+50,15,15);
			} 				
		}
		
		//monsters
		for (int count=1; count<7; count++) {//draw the monsters
			g.setColor(Color.red);
			g.fillOval(locarr[count][0]*50+30,locarr[count][1]*50+30,40,40);
			g.fillRoundRect(locarr[count][0]*50+30,locarr[count][1]*50+50,40,20,10,10);
			//the monsters' eyes
			g.setColor(Color.black);
			g.fillRoundRect(locarr[count][0]*50+38,locarr[count][1]*50+43,10,15,3,3);
			g.fillRoundRect(locarr[count][0]*50+58,locarr[count][1]*50+43,10,15,3,3);
		}
		
		//pacman
		if (open) { //draw with an open mouth
			g.setColor(Color.blue);
			g.fillOval(locarr[0][0]*50+30,locarr[0][1]*50+30,40,40);
			g.setColor(Color.white);
			
			//the mouth
			g.fillArc(locarr[0][0]*50+30,locarr[0][1]*50+30,40,40,-30,60);
			open = false;//toggle boolean open
		} else {
			g.setColor(Color.blue);
			g.fillOval(locarr[0][0]*50+30,locarr[0][1]*50+30,40,40);
			g.setColor(Color.white);
			
			//the mouth
			g.fillArc(locarr[0][0]*50+30,locarr[0][1]*50+30,40,40,-10,20);
			open = true;//toggle open
		}	
		
		if (end) { //check if the game has ended
			g.setFont(new Font("SansSerif",Font.PLAIN,100));
			g.setColor(Color.red); 
			if (win) {
				g.setColor(Color.green); 
				g.drawString("YOU WIN",25,300); //print win
			} else {
				g.drawString("YOU LOSE",25,300); //print lose
			}	
		}		
	}	// end paintComponent
		
	public void keyPressed (KeyEvent e) { //watch for keystrokes
		char c = e.getKeyChar(); //record keystroke
		System.out.println("keyPressed, c="+c); //report the keystroke
		if (c=='r') { //if 'r', or reset
			setPositions(); //call the initial locatio setting method
			end = false; //reset end to false
			cheeseeaten = 0; //reset cheese eaten
			timer.start(); //start the timer again
		}	
		if (!end) {
			switch (c) { //save the new position
				case 'w':locarr[0][1]--;break; //move up for w
				case 's':locarr[0][1]++;break; //move down for s
				case 'a':locarr[0][0]--;break; //move left for a
				case 'd':locarr[0][0]++;break; //move right for d
			}
			repaint(); //repaint with the new location
		}
	}
	
	//the remaining keyListener methods
	public void keyReleased (KeyEvent e) {}
	public void keyTyped (KeyEvent e) {}

}
