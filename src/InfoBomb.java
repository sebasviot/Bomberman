
public class InfoBomb {
	
	private int x;
	private int y;
	private int range;

	private int owner;
		
	StateBomb stateBomb;

	public InfoBomb(int x, int y, int range, int owner, StateBomb stateBomb) {
		this.x=x;
		this.y=y;
		this.range=range;
		this.owner=owner;
		this.stateBomb = stateBomb;
	}


	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}



	public StateBomb getStateBomb() {
		return stateBomb;
	}



	public void setStateBomb(StateBomb stateBomb) {
		this.stateBomb = stateBomb;
	}



	public int getRange() {
		return range;
	}


	public void setRange(int range) {
		this.range = range;
	}

	public int getOwner() {
		return owner;
	}


	
}
	