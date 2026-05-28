//Mar 5, 2013
//Pacman.java
/*Objective: 
*/

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;

public class AIPacman {
	
	JFrame frame; //the entire frame
	AIBoardPanel boardpanel;
	AIDirectionsPanel directionspanel;
	
	public static void main (String [] args) {
		AIPacman pac = new AIPacman();
		pac.Run();
	}
	
	public void Run() {//initialize the frame and execute
	
		frame = new JFrame ("pacman");//create JFrame
		
		frame.setSize(625,690);//size 
		frame.setLocation(100,100);//and locate the JFrame
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//set the parameters of the JFrame
		frame.setVisible(true); //false hides frame
		frame.setResizable(false); //makes the window a fixed size
		
		boardpanel = new AIBoardPanel();//create JPanel 
		frame.addKeyListener(boardpanel);//add a listener for the keyboard
		boardpanel.setSize(560,560);//set the draw panel size
		
		directionspanel = new AIDirectionsPanel();
		directionspanel.setSize(615,690);
		frame.getContentPane().add(directionspanel,BorderLayout.CENTER);//locate the panel
		frame.getContentPane().add(boardpanel,BorderLayout.CENTER);//locate the panel
		boardpanel.setLocation(25,25);
	}
}


class AIDirectionsPanel extends JPanel {

	public AIDirectionsPanel() {
		repaint();
	}
	public void paintComponent(Graphics g) { 
		super.paintComponent(g); // execute the superclass method first
		
		setBackground(Color.blue); // Set background color
		
		g.setColor(Color.red);
		g.fillRect(0,615,615,60);
		g.setColor(Color.white);
		g.setFont(new Font("SansSerif",Font.PLAIN,22)); 
		g.drawString("Directions: w-up, a-left, s-down, d-right, r-reset",35,640); 
	}	
}	


class AIBoardPanel extends JPanel implements KeyListener {
	int[][] locarr = new int [13][2];//the 0 is the pacman position,
	//1-6 are the monsters, 6-12 are the cheese
	int[][] maparr = new int[10][10]; //x,y
	int objnum;
	boolean open = false;
	Timer monsttimer;
	Timer pactimer;
	int cheeseeaten = 0;
	boolean end = false;
	boolean win = false;
	
	
	public void valueArray () {
		//clear array
		for (int x = 0; x < 10; x++) 
			for (int y = 0; y < 10; y++) 
				maparr[x][y] = 0;

				
		for (int count = 1; count <7; count++) {
			addPoints ('m', locarr[count][0], locarr[count][1]);
		}
		for (int count = 7; count <13; count++) {
			addPoints ('c', locarr[count][0], locarr[count][1]);
		}
		
	}
	
	public void addPoints (char c, int cx, int cy) {
		for (int x = 0; x<10; x++) 
			for (int y = 0; y < 10; y++) {
				
				int xdist = Math.abs(cx-x);
				if (xdist > 4) 
					xdist = 9 - xdist;
						 
				int ydist = Math.abs(cy-y);
				if (ydist > 4) 
					ydist = 9 - ydist;
				
				int minusdist = 4 - (xdist+ydist);
				if (minusdist < 0) minusdist = 0;
				
				
				if (c=='m') {
					//maparr[x][y] = maparr[x][y] + (int)(Math.pow(9-(xdist+ydist),5)/1000/3*1.5);
					maparr[x][y] = maparr[x][y] + (int)(Math.pow(minusdist,2));							 
					if (x==cx && y==cy) 
						maparr[x][y] = 77777;
				} else {
					//maparr[x][y] = maparr[x][y] - (int)Math.pow(11-(xdist+ydist),2/5);					 
					if (x==cx && y==cy) 
						maparr[x][y] = -50;
				}				
			}
	}		
	
	
	private class moveMonsters implements ActionListener {
		int monsterMove;
		public void actionPerformed(ActionEvent e) {
			for (int count=1; count<7; count++) {
				monsterMove = (int)(Math.random()*4);
				//AI(count);
				switch (monsterMove) { //save the new position
					case 0:locarr[count][1]--;break; // up
					case 1:locarr[count][1]++;break; // down
					case 2:locarr[count][0]--;break; // left
					case 3:locarr[count][0]++;break; // right
				}
			}
			valueArray();
			repaint();
		}	
		///Jeremy's Code
		public void AI(int i) {
			int randomMove = (int)(Math.random() * 100) + 1;
			int anothervariable = (int)(Math.random() * 2);
			if ((locarr[i][0]< locarr[0][0]) && (locarr[i][1]< locarr[0][1])) {
				if (anothervariable == 0) anothervariable = 3;
			}	
			else if ((locarr[i][0]< locarr[0][0]) && (locarr[i][1]> locarr[0][1])) {
				if (anothervariable == 1) anothervariable = 3;
			}	
			else if ((locarr[i][0]> locarr[0][0]) && (locarr[i][1]< locarr[0][1])) {
				if (anothervariable == 0) anothervariable = 2;
			}
			else if ((locarr[i][0]> locarr[0][0]) && (locarr[i][1]> locarr[0][1])) {
				if (anothervariable == 1) anothervariable = 2;
			}
			else if(locarr[i][0]< locarr[0][0]) anothervariable = 3;
			else if (locarr[i][0]> locarr[0][0]) anothervariable = 2;
			else if (locarr[i][1]< locarr[0][1]) anothervariable = 1;
			else if (locarr[i][1]> locarr[0][1]) anothervariable = 0;
			if (randomMove > 60) { if (randomMove > 80) {anothervariable = 4;} else {anothervariable = 5;}}
			switch (anothervariable) {
				case 0: monsterMove = 0; break; // go up
				case 1: monsterMove = 1; break; // go down
				case 2: monsterMove = 2; break; // go left
				case 3: monsterMove = 3; break; // go right
				case 4: monsterMove = 4; break; // don't move
				case 5: monsterMove = (int)(Math.random() * 4); break; // go a random direction
			}
		}
		///End Jeremy's Code
	}
	
	
	public AIBoardPanel() {
		addKeyListener(this); //add a listener
		setPositions();
		moveMonsters movemonsters = new moveMonsters();
		monsttimer = new Timer(30, movemonsters);
		monsttimer.start();
		movePacman movepacman = new movePacman();
		pactimer = new Timer(6, movepacman);
		pactimer.start();
		
	}
	
	public void setPositions () {
		for(objnum = 0; objnum<13; objnum ++) {
			do{
				setPosition();	
			}while (checkPositions());
		}		
	}
	
	public boolean checkPositions () {//returns true if the location
	//is already taken
		for (int count = 0; count < (objnum-1); count++) 
			if (locarr[objnum][0]==locarr[count][0])
				if (locarr[objnum][1]==locarr[count][1]) 
					return true;
		return false;
	} 
	
	public void setPosition () {
		locarr[objnum][0] = (int)(10.0*Math.random());
		locarr[objnum][1] = (int)(10.0*Math.random());
	}
	
	public void checkEdge () { 
		for (int count=0; count<7; count++) {
			switch (locarr[count][0]) { //save the new position
				case -1:locarr[count][0]=9;break;
				case 10:locarr[count][0]=0;break;
			}
			switch (locarr[count][1]) { //save the new position
				case -1:locarr[count][1]=9;break;
				case 10:locarr[count][1]=0;break;
			}
		}
	}	
	
	public void checkCheese () { 
		for (int count=7; count<13; count++)
			if (locarr[0][0]==locarr[count][0])
				if (locarr[0][1]==locarr[count][1]) { 
					locarr[count][0]=-2;
					cheeseeaten++;
					if (cheeseeaten==6) {
						monsttimer.stop();
						pactimer.stop();
						end = true;
						win = true;
						System.out.println("endgame-youwin");	
					}
				}	
	}	
	
	public void checkEndGame () {
		for (int count=1; count<7; count++)
			if (locarr[0][0]==locarr[count][0])
				if (locarr[0][1]==locarr[count][1]){
					monsttimer.stop();
					pactimer.stop();
					end = true;
					win = false;
					System.out.println("endgame-youlose");	
				}	
	}	
	
	public void paintComponent(Graphics g) { 
		super.paintComponent(g); // execute the superclass method first
		
		setBackground(Color.white); // Set background color

		g.setColor(Color.gray);
		for (int count=0; count<11; count++) {
			g.drawLine(25,25+count*50,525,25+count*50);
		}
		for (int count=0; count<11; count++) {
			g.drawLine(25+count*50,25,25+count*50,525);
		}
		
		checkEdge();
		checkCheese();
		checkEndGame();

		
		//cheese
		for (int count= 7; count<13; count++) {
			if (locarr[count][0]>-1) {
				g.setColor(Color.yellow);
				g.fillRoundRect(locarr[count][0]*50+30,locarr[count][1]*50+30,40,40,10,10);
				g.setColor(Color.white);
				g.fillOval(locarr[count][0]*50+35,locarr[count][1]*50+40,9,9);
				g.fillOval(locarr[count][0]*50+40,locarr[count][1]*50+52,12,12);
				g.fillOval(locarr[count][0]*50+55,locarr[count][1]*50+35,10,10);
				g.fillOval(locarr[count][0]*50+60,locarr[count][1]*50+50,15,15);
			} 				
		}
		

		
		//monsters
		for (int count=1; count<7; count++) {
			g.setColor(Color.green);
			g.fillOval(locarr[count][0]*50+30,locarr[count][1]*50+30,40,40);
			g.fillRoundRect(locarr[count][0]*50+30,locarr[count][1]*50+50,40,20,10,10);
			g.setColor(Color.black);
			g.fillRoundRect(locarr[count][0]*50+38,locarr[count][1]*50+43,10,15,3,3);
			g.fillRoundRect(locarr[count][0]*50+58,locarr[count][1]*50+43,10,15,3,3);
		}
		
		//pacman
		if (open) {
			g.setColor(Color.blue);
			g.fillOval(locarr[0][0]*50+30,locarr[0][1]*50+30,40,40);
			g.setColor(Color.white);
			g.fillArc(locarr[0][0]*50+30,locarr[0][1]*50+30,40,40,-30,60);
			open = false;
		} else {
			g.setColor(Color.blue);
			g.fillOval(locarr[0][0]*50+30,locarr[0][1]*50+30,40,40);
			g.setColor(Color.white);
			g.fillArc(locarr[0][0]*50+30,locarr[0][1]*50+30,40,40,-10,20);
			open = true;
		}	
		
		if (end) {
			g.setFont(new Font("SansSerif",Font.PLAIN,100));
			g.setColor(Color.red); 
			if (win)
				g.drawString("YOU WIN",25,300); 
			else 
				g.drawString("YOU LOSE",25,300);
		}		
	
		g.setColor(Color.blue);
			g.setFont(new Font("SansSerif",Font.PLAIN,15)); 
		for(int y = 0; y<10; y++) 
			for(int x = 0; x<10; x++)
				if ((x!=locarr[0][0]%10) && (y!=locarr[0][0]%10)) 
					g.drawString(maparr[x][y]+" ",x*50+35,y*50+50);
				else {
					g.setColor(Color.green);
					g.drawString(maparr[x][y]+" ",x*50+35,y*50+50);
					g.setColor(Color.blue);
				} 
					
	
	}	// end paintComponent
	
	private class movePacman implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!end) {
				valueArray();
				int min = maparr[locarr[0][0]][locarr[0][1]];
				char pacmanMove = ' ';
				
				
				if (maparr[locarr[0][0]][Math.abs(locarr[0][1]-1)%9] <= min) {
					min = maparr[locarr[0][0]][Math.abs(locarr[0][1]-1)%9];
					pacmanMove = 'w';
				}
				if (maparr[locarr[0][0]][(locarr[0][1]+1)%9] <= min) {
					min = maparr[locarr[0][0]][(locarr[0][1]+1)%9];
					pacmanMove = 's';
				}
				if (maparr[Math.abs(locarr[0][0]-1)%9][locarr[0][1]] <= min) {
					min = maparr[Math.abs(locarr[0][0]-1)%9][locarr[0][1]];
					pacmanMove = 'a';
				}
				if (maparr[(locarr[0][0]+1)%9][locarr[0][1]] <= min) {
					min = maparr[(locarr[0][0]+1)%9][locarr[0][1]];
					pacmanMove = 'd';
				}	

						
				
				switch (pacmanMove) { //save the new position
					case 'w':locarr[0][1]--;break;
					case 's':locarr[0][1]++;break;
					case 'a':locarr[0][0]--;break;
					case 'd':locarr[0][0]++;break;
				}
		
				repaint();
			}
		}	
	}
	
	public void keyPressed (KeyEvent e) { //watch for keystrokes
		char c = e.getKeyChar(); //record keystroke
		System.out.println("keyPressed, c="+c);
		if (c=='r') {
			setPositions();
			end = false;
			cheeseeaten = 0;
			monsttimer.start();
			pactimer.start();
		}
		if (c=='p') {
			if (!end) {
				monsttimer.stop();
				pactimer.stop();
				end = true;
			}else {
				monsttimer.start();
				pactimer.start();
				end = false;	
			}
		}	
	}
	
	public void keyReleased (KeyEvent e) {}
	public void keyTyped (KeyEvent e) {}

}	
