
public class ControleurBombermanGame implements InterfaceControleur {
    Game game;
    ViewBombermanGame viewGame;
    boolean gameInPause=false;
    boolean resumed=false;

    public ControleurBombermanGame(String layout) throws Exception {
        this.game = new BombermanGame(10,1000, layout);
        this.viewGame = new ViewBombermanGame(this, layout);
        game.addObserver(viewGame);
        //Commentaire pour voir dans quelle branche je suis
    }

    public Game getGame() {
        return this.game;
    }

	public void start() {
        this.game.init();

		viewGame.viewCommand.jButtonRestart.setEnabled(false);
		viewGame.viewCommand.jButtonRun.setEnabled(true);
		viewGame.viewCommand.jButtonStep.setEnabled(true);
        viewGame.viewCommand.jButtonPause.setEnabled(true);
    }
    
	public void step() {
        game.step();
    }

	public void run() {
        if (!gameInPause) {
            game.launch();
        }
        else {
            gameInPause = false;
            game.resume();
        }
    }

	public void stop(){
        gameInPause = true;
        game.pause();
        viewGame.viewCommand.jButtonPause.setEnabled(false);
        viewGame.viewCommand.jButtonRun.setEnabled(true);
        viewGame.viewCommand.jButtonStep.setEnabled(true);
    }
    
	public void setTime(double time) {
        game.setTime(time);
    }

}
