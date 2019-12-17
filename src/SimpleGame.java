
public class SimpleGame extends Game {
	
	public SimpleGame(int mt, long t) {
		super(mt, t);
	}
	
	public void initializeGame() {
		System.out.println("Jeu initialisé");
	}
	
	public void takeTurn(){
		System.out.println("Tour " + this.turn + " du jeu en cours");
	}
	
	public void gameOver(){
		System.out.println("Fin du jeu");
		this.thread=null;
	}
	
	public boolean gameContinue(){
		return (super.maxTurn>super.turn);
	}

}
