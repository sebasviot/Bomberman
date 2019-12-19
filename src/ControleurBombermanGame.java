public class ControleurBombermanGame implements InterfaceControleur {
    Game game;
    ViewBombermanGame viewGame;
    boolean gameInPause=false;
    boolean resumed=false;
    protected Map map = new Map("src/layouts/alone.lay");


    public ControleurBombermanGame() throws Exception {
        this.game = new BombermanGame(10,1000, this.map);
        this.viewGame = new ViewBombermanGame(this, this.map);
        game.addObserver(viewGame);
    }

    public void newGame(String layout) throws Exception {
        this.viewGame.destroyView();
        this.map = new Map(layout);
        this.game = new BombermanGame(10,1000, this.map);
        this.viewGame = new ViewBombermanGame(this, this.map);
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
