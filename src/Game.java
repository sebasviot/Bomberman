import java.util.Observable;

public abstract class Game extends Observable implements Runnable{
	protected int turn;
	protected int maxTurn;
	protected boolean isRunning;
	protected Thread thread;
	protected double time;
	
	//Constructeur
	public Game(int mt, double t) {
		maxTurn = mt;
		time = t ;
	}
		
	//Méthodes Abstraites
	
	public abstract void initializeGame() ;
	public abstract void takeTurn();
	public abstract void gameOver();
	public abstract boolean gameContinue();
	
	//Methodes concrètes
	
	public int getTurn() {
		return turn;
	}
	
	public void setTime(double t) {
		time = t;
	}
	
	public void init() {
		turn = 0;
		isRunning = true;
		initializeGame();
	} // END METHODE
	
	
	public void step() {
		if (gameContinue()) { 
			turn++;		
			takeTurn();
			this.setChanged(); //Permet de spécifier que l'état de l'observable a changé
	        this.notifyObservers(); //Permet de notifier tous les observateurs
		} 
		else {
			isRunning = false;
			gameOver();
			this.setChanged(); //Permet de spécifier que l'état de l'observable a changé
	        this.notifyObservers(); //Permet de notifier tous les observateurs
		} //ENDIF	

	} //END METHODE
	
	public void run() {
		while (isRunning) {
			step();
			try {
				Thread.sleep((long)time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} //END WHILE
	} // END METHODE
	
	public void stop() {
		isRunning = false;
		setChanged();
		notifyObservers();
	}
	
	public void launch() {
		isRunning=true;
		setChanged();
		notifyObservers();
		thread = new Thread(this);
		thread.start();
	}
}
