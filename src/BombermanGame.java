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

	public BombermanGame(int mt, long t, Map map) throws Exception {
		super(mt, t);
		this.map = map;
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
		ArrayList<InfoBomb> bombExploded = new ArrayList<>();
		for (InfoBomb b : bombs) {
			switch (b.getStateBomb()) {
				case Boom:
					bombExploded.add(b);
					break;
				case Step3:
					b.setStateBomb(StateBomb.Boom);
					break;
				case Step2:
					b.setStateBomb(StateBomb.Step3);
					break;
				case Step1:
					b.setStateBomb(StateBomb.Step2);
					break;
				default:
					break;
			}	
		}
		for (Agent agent : agents) {
			randMove = (int) Math.round(((Math.random()) * 6));
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
		damages(bombExploded);
		takeItem();
		
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
		/**
		 * C'est un mouvement légal s'il n'y a pas de mur 
		 * ET s'il n'y a pas de mur cassable.
		 */

		switch (action) {
		case MOVE_DOWN:
			return (! (map.get_walls()[agent.getX()][agent.getY() + 1]) && !(breakableWalls[agent.getX()][agent.getY() + 1]) );
		case MOVE_UP:
			return (! (map.get_walls()[(agent.getX())][agent.getY() - 1]) && !(breakableWalls[(agent.getX())][agent.getY() - 1]) );
		case MOVE_LEFT:
			return (! (map.get_walls()[(agent.getX() - 1)][agent.getY()]) && !(breakableWalls[(agent.getX() - 1)][agent.getY()]) );
		case MOVE_RIGHT:
			return (! (map.get_walls()[agent.getX() + 1][agent.getY()]) && !(breakableWalls[agent.getX() + 1][agent.getY()]) );
		case STOP:
			return true;
		case PUT_BOMB:
			return noBomb(agent.getX(), agent.getY());
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
				agent.setY(agent.getY() + 1);
			}
			;
			break;
		case MOVE_UP:
			if (isLegalMove(agent, action)) {
				agent.setY(agent.getY() - 1);
			}
			break;
		case MOVE_LEFT:
			if (isLegalMove(agent, action)) {
				agent.setX(agent.getX() - 1);
			}
			break;
		case MOVE_RIGHT:
			if (isLegalMove(agent, action)) {
				agent.setX(agent.getX() + 1);
			}
			break;
		case STOP:
			break;

		case PUT_BOMB:
			InfoBomb newbomb = new InfoBomb(agent.getX(), agent.getY(), agent.getPower(), agent.getId(), StateBomb.Step1);
			bombs.add(newbomb);
			break;
		default: 
			break;
		}

	} // END METHODE moveAgent

	public void damages(ArrayList<InfoBomb> bombsExploded) {
		ArrayList<Agent> deadsAgents = new ArrayList<>();

		for (InfoBomb bBoom : bombsExploded) {
			//Faire perdre une vie aux agents qui sont sur la range de la bombe
			for (int i=0; i<=bBoom.getRange(); i++) {
				for (Agent a : agents) {
					if (
					(a.getX() == (bBoom.getX())-i) && (a.getY() == bBoom.getY())
					|| (a.getX() == (bBoom.getX())+i) && (a.getY() == bBoom.getY())
					|| (a.getX() == (bBoom.getX())) && (a.getY() == bBoom.getY()-i)
					|| (a.getX() == (bBoom.getX())) && (a.getY() == bBoom.getY()+i)
					) {
						a.hitted();
						for (Agent a1 : agents) {
							if (a1.getId() == bBoom.getOwner())  {
								a1.hit();
							}
						}
						//Ceux qui n'ont plus de vie sont dead.
						if (a.getLife()==0) {
							deadsAgents.add(a);
							for (Agent a1 : agents) {
								if (a1.getId() == bBoom.getOwner())  {
									a1.kill();
								}
							}
						}
					}
				}

				//Casse les murs cassables sur la range.
				if ( breakableWalls[bBoom.getX()-i][bBoom.getY()] ){
					breakableWalls[bBoom.getX()-i][bBoom.getY()] = false;
					//Il y a une chance sur deux de créer un item quand un mur est cassé
					int randomCreateItem = (int) Math.round(Math.random() * 2);
					if (randomCreateItem == 0) {
						int randomNewItem = (int) Math.round(Math.random() * 6);
						ItemType newType;
						switch (randomNewItem) {
							case 0 : newType = ItemType.BOMB_DOWN; break;
							case 1 : newType = ItemType.BOMB_UP; break;
							case 2 : newType = ItemType.FIRE_DOWN; break;
							case 3 : newType = ItemType.FIRE_UP; break;
							case 4 : newType = ItemType.FIRE_SUIT; break;
							default : newType = ItemType.SKULL; break;
						}
						InfoItem newItem = new InfoItem(bBoom.getX()-i, bBoom.getY(), newType);
						items.add(newItem);
					}
					for (Agent a1 : agents) {
						if (a1.getId() == bBoom.getOwner())  {
							a1.break_wall();
						}
					}
				}
				if ( breakableWalls[bBoom.getX()+i][bBoom.getY()] ){
					breakableWalls[bBoom.getX()+i][bBoom.getY()] = false;
					int randomCreateItem = (int) Math.round(Math.random() * 2);
					if (randomCreateItem == 0) {
					
						int randomNewItem = (int) Math.round(Math.random() * 6.0);
						ItemType newType;
						switch (randomNewItem) {
							case 0 : newType = ItemType.BOMB_DOWN; break;
							case 1 : newType = ItemType.BOMB_UP; break;
							case 2 : newType = ItemType.FIRE_DOWN; break;
							case 3 : newType = ItemType.FIRE_UP; break;
							case 4 : newType = ItemType.FIRE_SUIT; break;
							default : newType = ItemType.SKULL; break;
						}
						InfoItem newItem = new InfoItem(bBoom.getX()+i, bBoom.getY(), newType);
						items.add(newItem);
					}
					for (Agent a1 : agents) {
						if (a1.getId() == bBoom.getOwner())  {
							a1.break_wall();
						}
					}
				}
				if ( breakableWalls[bBoom.getX()][bBoom.getY()-i] ){
					breakableWalls[bBoom.getX()][bBoom.getY()-i] = false;

					int randomCreateItem = (int) Math.round(Math.random() * 2);
					if (randomCreateItem == 0) {
						
						int randomNewItem = (int) Math.round(Math.random() * 6.0);
						ItemType newType;
						switch (randomNewItem) {
							case 0 : newType = ItemType.BOMB_DOWN; break;
							case 1 : newType = ItemType.BOMB_UP; break;
							case 2 : newType = ItemType.FIRE_DOWN; break;
							case 3 : newType = ItemType.FIRE_UP; break;
							case 4 : newType = ItemType.FIRE_SUIT; break;
							default : newType = ItemType.SKULL; break;
						}
						InfoItem newItem = new InfoItem(bBoom.getX(), bBoom.getY()-i, newType);
						items.add(newItem);
					}
					for (Agent a1 : agents) {
						if (a1.getId() == bBoom.getOwner())  {
							a1.break_wall();
						}
					}
				}
				if ( breakableWalls[bBoom.getX()][bBoom.getY()+i] ){
					breakableWalls[bBoom.getX()][bBoom.getY()+i] = false;
					int randomCreateItem = (int) Math.round(Math.random() * 2);
					if (randomCreateItem == 0) {
					
						int randomNewItem = (int) Math.round(Math.random() * 6.0);
						ItemType newType;
						switch (randomNewItem) {
							case 0 : newType = ItemType.BOMB_DOWN; break;
							case 1 : newType = ItemType.BOMB_UP; break;
							case 2 : newType = ItemType.FIRE_DOWN; break;
							case 3 : newType = ItemType.FIRE_UP; break;
							case 4 : newType = ItemType.FIRE_SUIT; break;
							default : newType = ItemType.SKULL; break;
						}
						InfoItem newItem = new InfoItem(bBoom.getX(), bBoom.getY()+i, newType);
						items.add(newItem);
					}
					for (Agent a1 : agents) {
						if (a1.getId() == bBoom.getOwner())  {
							a1.break_wall();
						}
					}
				}

			}

			for (Agent agentDead : deadsAgents) {
				agents.remove(agentDead);
			}

			for (InfoBomb b : bombsExploded) {
				if (bBoom.equals(b)) {
					bombs.remove(b);
					break;
				}
			}
		}

	}

	public void takeItem() {
		for (InfoItem item : items ) {
			for (Agent agent : agents) {
				if (item.getX()==agent.getX() && item.getY()==agent.getY()) {
					switch (item.getType()) {
						case FIRE_UP :
							agent.fireUp();
							break;
						case FIRE_DOWN :
							agent.fireDown();
							break;
						case BOMB_UP :
							agent.bombUp();
							break;
						case BOMB_DOWN :
							agent.bombDown();
							break;
						case FIRE_SUIT :
							agent.fireSuit();
							break;
						case SKULL :
							agent.getIll();
							break;
						default :
							break;
					}
				}
			}
		}
	}

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

	/**
	 * Méthode pour donner toutes les informations sur les agents à la vue.
	 * @return ArrayList<InfoAgent>, la liste de toutes les informations nécessaire à la vue, informations qui sont relatives aux agents.
	 */
	public ArrayList<InfoAgent> getInfoAgents() {
		ArrayList<InfoAgent> infoAgents = new ArrayList<>();
		AgentAction move;
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
				infoAgent = new InfoAgent(agent.getX(), agent.getY(), move, 'B', agent.getColor(), false, false);
				break;
			case "AgentBird":
				infoAgent = new InfoAgent(agent.getX(), agent.getY(), move, 'V', agent.getColor(), false, false);
				break;
			case "AgentEnnemy":
				infoAgent = new InfoAgent(agent.getX(), agent.getY(), move, 'E', agent.getColor(), false, false);
				break;
			case "AgentRaijon":
				infoAgent = new InfoAgent(agent.getX(), agent.getY(), move, 'R', agent.getColor(), false, false);
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