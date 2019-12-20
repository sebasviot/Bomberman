import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class ViewCommand implements Observer {

	// Initialisation des Panel
	protected JPanel topPanel = new JPanel();
	private JPanel buttonsPanel = new JPanel();
	private JPanel sliderPanel = new JPanel();
	private JPanel downbandPanel = new JPanel();

	// Initialisation des gridLayout pour la conception
	private GridLayout gridTopLayout;
	private GridLayout gridButtonsLayout;
	private GridLayout gridDownbandLayout;
	private GridLayout gridSliderLayout;

	// Initialisation des icones
	private Icon icon_restart = new ImageIcon(getClass().getResource("assets/icones/icon_restart.png"));
	private Icon icon_run = new ImageIcon(getClass().getResource("assets/icones/icon_run.png"));
	private Icon icon_step = new ImageIcon(getClass().getResource("assets/icones/icon_step.png"));
	private Icon icon_pause = new ImageIcon(getClass().getResource("assets/icones/icon_pause.png"));

	// Initialisation des différents bouton pour le contrôle du jeu
	protected JButton jButtonRestart = new JButton(icon_restart);
	protected JButton jButtonRun = new JButton(icon_run);
	protected JButton jButtonStep = new JButton(icon_step);
	protected JButton jButtonPause = new JButton(icon_pause);
	protected JButton jButtonChoisirLayout = new JButton("Nouvelle partie");

	// Initialisation des deux JLabel et du JSlider
	protected JLabel jLabelTitle = new JLabel("Number of turns per second");
	private JSlider jSliderTurnPerSecond;
	private JLabel jLabelTurn = new JLabel();

	private InterfaceControleur controleurGame;

	public ViewCommand(InterfaceControleur c, Game g) {
		controleurGame = c;
		g.addObserver(this);
		createViewCommand();
		actionButton();
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		jLabelTurn.setText("Turn : " + controleurGame.getGame().getTurn());
		controleurGame.setTime(1000/jSliderTurnPerSecond.getValue());
	}

	private void createViewCommand(){
		// Initialise les gridLayouts
		gridTopLayout = new GridLayout(2, 1);
		gridButtonsLayout = new GridLayout(1, 5);
		gridSliderLayout = new GridLayout(2, 1);
		gridDownbandLayout = new GridLayout(1, 2);

		jButtonRestart.setEnabled(true);
		jButtonRun.setEnabled(false);
		jButtonStep.setEnabled(false);
		jButtonPause.setEnabled(false);

		jLabelTitle.setHorizontalAlignment(JLabel.CENTER);
		jLabelTurn.setHorizontalAlignment(JLabel.CENTER);

		jSliderTurnPerSecond = new JSlider();
		jSliderTurnPerSecond.setMinimum(1);
		jSliderTurnPerSecond.setMaximum(10);
		jSliderTurnPerSecond.setValue(1);
		jSliderTurnPerSecond.setPaintTicks(true);
		jSliderTurnPerSecond.setPaintLabels(true);
		jSliderTurnPerSecond.setMinorTickSpacing(1);
		jSliderTurnPerSecond.setMajorTickSpacing(1);


		// On ajoute les composants à leur parent
		topPanel.setLayout(gridTopLayout);
		buttonsPanel.setLayout(gridButtonsLayout);
		sliderPanel.setLayout(gridSliderLayout);
		downbandPanel.setLayout(gridDownbandLayout);

		buttonsPanel.add(jButtonRestart);
		buttonsPanel.add(jButtonRun);
		buttonsPanel.add(jButtonStep);
		buttonsPanel.add(jButtonPause);
		buttonsPanel.add(jButtonChoisirLayout);

		sliderPanel.add(jLabelTitle);
		sliderPanel.add(jSliderTurnPerSecond);

		downbandPanel.add(sliderPanel);
		downbandPanel.add(jLabelTurn);

		topPanel.add(buttonsPanel);
		topPanel.add(downbandPanel);
	}


	private void actionButton(){
		// Permet d'initialiser le jeu
		jButtonRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evenement) {
				controleurGame.start();
			}
		});

		// Permet de lancer le jeu
		jButtonRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evenement) {
				controleurGame.run();
			}
		});

		// Permet de lancer le jeu par étape
		jButtonStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evenement) {
				controleurGame.step();
			}
		});

		// Permet de mettre le jeu en pause
		jButtonPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evenement) {
				controleurGame.stop();
			}
		});

		// Permet des créer une nouvelle partie en fonction de la map choisis
		jButtonChoisirLayout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evenement) {
				JFileChooser dialogue = new JFileChooser(new File("src/layouts"));
				File fichier;

				if (dialogue.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					fichier = dialogue.getSelectedFile();
					try {
						controleurGame.newGame(fichier.getPath());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
