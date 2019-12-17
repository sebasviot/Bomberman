import java.util.ArrayList;

public class BombermanGame extends Game implements Runnable {

	protected Map map;
	protected AgentFactory factory;
	protected ArrayList<Agent> agents;
	protected ArrayList<InfoItem> items;
	protected boolean[][] breakableWalls;
	protected ArrayList<InfoBomb> bombs;
	protected int randMove = 0;
	protected boolean resumed = false;

	public BombermanGame(int mt, long t, String layout) throws Exception {
		super(mt, t);
		this.map = new Map(layout);
		this.factory = new AgentFactory();
		this.agents = new ArrayList<Agent>();
		this.items = new ArrayList<InfoItem>();
		this.bombs = new ArrayList<InfoBomb>();
		this.breakableWalls = map.getStart_brokable_walls();
	}

	@Override
	public void initializeGame() {
		/**
		 * Question 4.2.4 Le design pattern intéressant à utiliser est la Factory Simple
		 * Il va alors permettre d'utiliser une factory qui va pouvoir créer des Agents
		 * Bomberman, des Agents Rajion, des Agents Bird, et des Agents Ennemy
		 */
		for (InfoAgent info : map.getStart_agents()) {
			Agent a = factory.fabrique(info.getX(), info.getY(), info.getColor(), info.getType());
			if (a != null)
				agents.add(a);
		}
	}

	@Override
	public void takeTurn() {
		for (Agent agent : agents) {
			switch (randMove) {
			case 0:
				moveAgent(agent, AgentAction.MOVE_DOWN);
				break;
			case 1:
				moveAgent(agent, AgentAction.MOVE_UP);
				break;
			case 2:
				moveAgent(agent, AgentAction.MOVE_LEFT);
				break;
			case 3:
				moveAgent(agent, AgentAction.MOVE_RIGHT);
				break;
			case 4:
				moveAgent(agent, AgentAction.STOP);
				break;
			case 5:
				moveAgent(agent, AgentAction.PUT_BOMB);
				break;
			default:
				break;
			}
		}
		setChanged();
		notifyObservers();
	}

	@Override
	public void gameOver() {
		isRunning = false;
		this.thread = null;
		System.out.println("Fin du jeu");

	}

	public boolean gameContinue() {
		return (maxTurn > turn);
	}

	public boolean isLegalMove(Agent agent, AgentAction action) {
		switch (action) {
		case MOVE_DOWN:
			return !(map.get_walls()[agent.get_x()][agent.get_y() + 1]);
		case MOVE_UP:
			return !(map.get_walls()[(agent.get_x())][agent.get_y() - 1]);
		case MOVE_LEFT:
			return !(map.get_walls()[(agent.get_x() - 1)][agent.get_y()]);
		case MOVE_RIGHT:
			return !(map.get_walls()[agent.get_x() + 1][agent.get_y()]);
		case STOP:
			return true;
		case PUT_BOMB:
			return noBomb(agent.get_x(), agent.get_y());
		default:
			return false;
		}
	} // END METHODE isLegalMove

	public boolean noBomb(int x, int y) {
		boolean ret = true;
		for (InfoBomb bomb : bombs) {
			ret = ret && (bomb.getX() != x) && (bomb.getY() != y);
		}
		return ret;
	}

	public void moveAgent(Agent agent, AgentAction action) {
		switch (action) {
		case MOVE_DOWN:
			if (isLegalMove(agent, action)) {
				agent.set_y(agent.get_y() + 1);
			}
			;
			break;
		case MOVE_UP:
			if (isLegalMove(agent, action)) {
				agent.set_y(agent.get_y() - 1);
			}
			break;
		case MOVE_LEFT:
			if (isLegalMove(agent, action)) {
				agent.set_x(agent.get_x() - 1);
			}
			break;
		case MOVE_RIGHT:
			if (isLegalMove(agent, action)) {
				agent.set_x(agent.get_x() + 1);
			}
			break;
		case STOP:
			break;

		case PUT_BOMB:
			InfoBomb newbomb = new InfoBomb(agent.get_x(), agent.get_y(), agent.get_power(), StateBomb.Step1);
			bombs.add(newbomb);
			break;
		default:

		}

	} // END METHODE moveAgent

	public ArrayList<Agent> getAgents() {
		return agents;
	}

	public boolean[][] getBreakableWalls() {
		return breakableWalls;
	}

	public ArrayList<InfoBomb> getBombs() {
		return bombs;
	}

	public ArrayList<InfoItem> getItems() {
		return items;
	}

	public ArrayList<InfoAgent> getInfoAgents() {
		ArrayList<InfoAgent> infoAgents = new ArrayList<>();
		AgentAction move;
		double six = 6;
		randMove = (int) Math.round(((Math.random()) * six));
		switch (randMove) {
		case 0:
			move = AgentAction.MOVE_UP;
			break;
		case 1:
			move = AgentAction.MOVE_DOWN;
			break;
		case 2:
			move = AgentAction.MOVE_LEFT;
			break;
		case 3:
			move = AgentAction.MOVE_RIGHT;
			break;
		case 4:
			move = AgentAction.STOP;
			break;
		case 5:
			move = AgentAction.PUT_BOMB;
			break;
		default:
			move = null;
			break;

		}
		InfoAgent infoAgent;
		for (Agent agent : agents) {
			switch (agent.getClass().getName()) {
			case "AgentBomberman":
				infoAgent = new InfoAgent(agent.get_x(), agent.get_y(), move, 'B', agent.getColor(), false, false);
				break;
			case "AgentBird":
				infoAgent = new InfoAgent(agent.get_x(), agent.get_y(), move, 'V', agent.getColor(), false, false);
				break;
			case "AgentEnnemy":
				infoAgent = new InfoAgent(agent.get_x(), agent.get_y(), move, 'E', agent.getColor(), false, false);
				break;
			case "AgentRaijon":
				infoAgent = new InfoAgent(agent.get_x(), agent.get_y(), move, 'R', agent.getColor(), false, false);
				break;
			default:
				infoAgent = null;
				break;
			}
			if (infoAgent != null)
				infoAgents.add(infoAgent);
		}
		return infoAgents;
	}

}