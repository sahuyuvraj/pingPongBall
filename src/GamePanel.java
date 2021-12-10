import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
	static final int GAME_WIDTH=1000;
	static final int GAME_HIGHT=(int)(GAME_WIDTH*(0.5555));
    static final Dimension SCREEN_SIZE=new Dimension(GAME_WIDTH,GAME_HIGHT);
    static final int BALL_DIAMETER=20;
    static final int PADDLE_WIDTH=25;
    static final int PADDLE_HIGHT=100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;
    
	GamePanel(){
	
		newPaddle();
		newBall();
		score=new Score(GAME_WIDTH,GAME_HIGHT);
		this.setBackground(Color.MAGENTA);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.setPreferredSize(SCREEN_SIZE);
		gameThread=new Thread(this);
		gameThread.start();
		
	}
	public void newBall() {
		random=new Random();
		ball=new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HIGHT-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
	}
	public void newPaddle() {
		paddle1=new Paddle(0,(GAME_HIGHT/2)-(PADDLE_HIGHT/2),PADDLE_WIDTH,PADDLE_HIGHT,1);
		paddle2=new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HIGHT/2)-(PADDLE_HIGHT/2),PADDLE_WIDTH,PADDLE_HIGHT,2);
	}
	public void paint(Graphics g) {
		image=createImage(getWidth(),getHeight());
		graphics =image.getGraphics();
		draw(graphics);
		g.drawImage(image,0,0,this);
		
		
	}
	public void draw(Graphics g) {
		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g);
		score.draw(g);
	}
	public void move() {
		paddle1.move();
		paddle2.move();
		ball.move();	
	}

	
	public void checkCollision() {
		//bounce ball off top & bottom window edges
		if(ball.y<=0) {
			ball.setYDirection(-ball.yVelocity);
		}
		if(ball.y>=GAME_HIGHT-BALL_DIAMETER) {
			ball.setYDirection(-ball.yVelocity);
		}
		//bounce ball off paddles
		if(ball.intersects(paddle1)) {
			ball.xVelocity=Math.abs(ball.xVelocity);
			ball.xVelocity++;//optional for more Difficulty
			if(ball.yVelocity>0)
				ball.yVelocity++;
			else
				ball.yVelocity--;
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		if(ball.intersects(paddle2)) {
			ball.xVelocity=Math.abs(ball.xVelocity);
			ball.xVelocity++;//optional for more Difficulty
			if(ball.yVelocity>0)
				ball.yVelocity++;
			else
				ball.yVelocity--;
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		//stops paddle at window edges
		if(paddle1.y<=0)
			paddle1.y=0;
		if(paddle1.y>=(GAME_HIGHT-PADDLE_HIGHT))
			paddle1.y=GAME_HIGHT-PADDLE_HIGHT;
		if(paddle2.y<=0)
			paddle2.y=0;
		if(paddle2.y>=(GAME_HIGHT-PADDLE_HIGHT))
			paddle2.y=GAME_HIGHT-PADDLE_HIGHT;
		//give a player 1 point and creates new paddle & ball
       if(ball.x<=0) {
    	   score.player2++;
    	   newPaddle();
    	   newBall();
    	   System.out.println("Player 2:"+score.player2);
       }
       if(ball.x>=GAME_WIDTH-BALL_DIAMETER) {
    	   score.player1++;
    	   newPaddle();
    	   newBall();
    	   System.out.println("Player 1:"+score.player1);
       }
	}
	
	public void gameOver() {
		if(score.player1==10) {
		  
		}
		if(score.player2==10) {
			
		}
	}
	public void run() {
		//Game loop
		long lastTime= System.nanoTime();
		double amountofTics=60.0;
		double ns=(1000000000)/amountofTics;
		double delta=0;
		while(true) {
			long now=System.nanoTime();
			delta+=(now -lastTime)/ns;
			lastTime=now;
			if(delta >=1) {
				move();
				checkCollision();
				repaint();
				delta--;
			    
			}
		}
	}
	public class AL extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
		paddle1.keyPressed(e);
		paddle2.keyPressed(e);
		
		}
    public void keyReleased(KeyEvent e) {
    	paddle1.keyReleased(e);
		paddle2.keyReleased(e);
			
		}
	}

}
