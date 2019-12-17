import java.util.Observable;
import java.util.Observer;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ViewBombermanGame implements Observer {

	protected ControleurBombermanGame controleurBomberman;
	protected ViewCommand viewCommand;
	protected Map map;
	protected PanelBomberman panelBomberman;
	protected JPanel topPanel;
	protected JFrame frameBomberman;
	protected GridLayout topLayout;

	public ViewBombermanGame(ControleurBombermanGame controlleur, String layout) throws Exception {
		this.controleurBomberman = controlleur;
		this.viewCommand = new ViewCommand(controlleur, controlleur.game);
		this.map = new Map(layout);
		this.panelBomberman = new PanelBomberman(this.map);

		this.topLayout = new GridLayout(2,1);
		this.topPanel = new JPanel();
		this.frameBomberman = new JFrame();

		frameBomberman.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme l'application quand on quitte la vue

		frameBomberman.setTitle("Bomberman");
		frameBomberman.setSize(new Dimension(1000, 1000));
		Dimension windowSize = frameBomberman.getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2 ;
		int dy = centerPoint.y - windowSize.height / 2 - 350;
		frameBomberman.setLocation(dx, dy);

		topPanel.setLayout(topLayout);

		topPanel.add(viewCommand.topPanel);
		topPanel.add(panelBomberman);
		
		frameBomberman.setContentPane(topPanel);
		frameBomberman.setVisible(true);
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		BombermanGame bombermanGame = (BombermanGame) arg0;

		panelBomberman.setInfoGame(bombermanGame.getBreakableWalls(), bombermanGame.getInfoAgents(), bombermanGame.getItems(), bombermanGame.getBombs());

		frameBomberman.repaint();

	}

}
