public class AgentFactory {
    public Agent fabrique(int x, int y, ColorAgent color, char type){
        switch (type) {
            case 'B' : return new AgentBomberman(x, y, color);
            case 'R' : return new AgentRaijon(x, y, color);
            case 'V' : return new AgentBird(x, y, color);
            case 'E' : return new AgentEnnemy(x, y, color);
            default: return null;
        }
    }
}