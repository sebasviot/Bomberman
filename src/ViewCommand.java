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

	//JFrame jFrameCommand;
	JPanel topPanel;

	JPanel buttonsPanel;
	JPanel sliderPanel;
	JPanel comboboxPanel;
	JPanel downbandPanel;

	GridLayout topLayout;
	GridLayout buttonsLayout;
	GridLayout downbandLayout;
	GridLayout comboboxLayout;
	GridLayout sliderLayout;

	JButton jButtonRestart;
	JButton jButtonRun;
	JButton jButtonStep;
	JButton jButtonPause;
	JLabel jLabelTitle;
	JSlider jSliderTurnPerSecond;
	JLabel jLabelTurn;
	JFileChooser jLayoutChooser;

	InterfaceControleur controleurGame;

	public ViewCommand(InterfaceControleur c, Game g) {

		controleurGame = c;
		g.addObserver(this);

		jSliderTurnPerSecond = new JSlider();
		jSliderTurnPerSecond.setMinimum(1);
		jSliderTurnPerSecond.setMaximum(10);
		jSliderTurnPerSecond.setPaintTicks(true);
		jSliderTurnPerSecond.setPaintLabels(true);
		jSliderTurnPerSecond.setMinorTickSpacing(1);
		jSliderTurnPerSecond.setMajorTickSpacing(1);

		jLabelTurn = new JLabel();
		jLabelTurn.setHorizontalAlignment(JLabel.CENTER);

		Icon icon_restart = new ImageIcon(getClass().getResource("assets/icones/icon_restart.png"));
		jButtonRestart = new JButton(icon_restart);

		jButtonRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evenement) {
				controleurGame.start();
			}
		});

		Icon icon_run = new ImageIcon(getClass().getResource("assets/icones/icon_run.png"));
		jButtonRun = new JButton(icon_run);
		jButtonRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evenement) {
				controleurGame.run();
			}
		});

		Icon icon_step = new ImageIcon(getClass().getResource("assets/icones/icon_step.png"));
		jButtonStep = new JButton(icon_step);
		jButtonStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evenement) {
				controleurGame.step();
			}
		});

		Icon icon_pause = new ImageIcon(getClass().getResource("assets/icones/icon_pause.png"));
		jButtonPause = new JButton(icon_pause);
		jButtonPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evenement) {
				controleurGame.stop();
			}
		});

		jButtonRun.setEnabled(false);
		jButtonStep.setEnabled(false);
		jButtonPause.setEnabled(false);

		jLabelTitle = new JLabel();
		jLabelTitle.setText("Number of turns per second");
		jLabelTitle.setHorizontalAlignment(JLabel.CENTER);

		jSliderTurnPerSecond = new JSlider();
		jSliderTurnPerSecond.setMinimum(1);
		jSliderTurnPerSecond.setMaximum(10);
		jSliderTurnPerSecond.setValue(1);

		jSliderTurnPerSecond.setPaintTicks(true);
		jSliderTurnPerSecond.setPaintLabels(true);
		jSliderTurnPerSecond.setMinorTickSpacing(1);
		jSliderTurnPerSecond.setMajorTickSpacing(1);

		jLabelTurn = new JLabel();
		jLabelTurn.setHorizontalAlignment(JLabel.CENTER);

		jLayoutChooser = new JFileChooser(new File("src/layouts"));
		/*jLayoutChooser.setMultiSelectionEnabled(false);
		int res = jLayoutChooser.showDialog(new JFrame(), "Layout");
		if (res == JFileChooser.APPROVE_OPTION) {
			File layout = jLayoutChooser.getSelectedFile();
			choosenLayoutFileName = layout.getName().substring(0, layout.getName().length() - 4);
		}*/

		// Layouts

		topLayout = new GridLayout(3, 1);

		buttonsLayout = new GridLayout(1, 4);

		sliderLayout = new GridLayout(2, 1);
		comboboxLayout = new GridLayout(1, 1);

		downbandLayout = new GridLayout(1, 2);

		// Panels
		topPanel = new JPanel();
		buttonsPanel = new JPanel();
		sliderPanel = new JPanel();
		comboboxPanel = new JPanel();
		downbandPanel = new JPanel();

		topPanel.setLayout(topLayout);
		buttonsPanel.setLayout(buttonsLayout);
		sliderPanel.setLayout(sliderLayout);
		comboboxPanel.setLayout(comboboxLayout);
		downbandPanel.setLayout(downbandLayout);

		buttonsPanel.add(jButtonRestart);
		buttonsPanel.add(jButtonRun);
		buttonsPanel.add(jButtonStep);
		buttonsPanel.add(jButtonPause);

		sliderPanel.add(jLabelTitle);
		sliderPanel.add(jSliderTurnPerSecond);

		comboboxPanel.add(jLayoutChooser);

		downbandPanel.add(sliderPanel);
		downbandPanel.add(jLabelTurn);

		topPanel.add(buttonsPanel);
		topPanel.add(downbandPanel);
		topPanel.add(comboboxPanel);

	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		jLabelTurn.setText("Turn : " + controleurGame.getGame().getTurn());
		controleurGame.setTime(1000/jSliderTurnPerSecond.getValue());
	}

}
