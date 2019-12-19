public class ControleurBombermanGame implements InterfaceControleur {
    Game game;
    ViewBombermanGame viewGame;
    boolean gameInPause=false;
    boolean resumed=false;

    public ControleurBombermanGame(String layout) throws Exception {
        this.game = new BombermanGame(150,1000, layout);
        this.viewGame = new ViewBombermanGame(this, layout);
        game.addObserver(viewGame);
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
        viewGame.viewCommand.jButtonPause.setEnabled(true);
        game.launch();
    }

	public void stop(){
        game.isRunning = false;
        viewGame.viewCommand.jButtonPause.setEnabled(false);
        viewGame.viewCommand.jButtonRun.setEnabled(true);
        viewGame.viewCommand.jButtonStep.setEnabled(true);
    }
    
	public void setTime(double time) {
        game.setTime(time);
    }

}
