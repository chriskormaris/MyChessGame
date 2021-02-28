package chess;

import java.util.ArrayList;
import java.util.List;

/* A class describing a move in the board
 * Every produced child corresponds to a move
 * and we need to keep the moves as well as the states.
 */
public class Move {
	
	private double value;

    // In this list we are going to keep 2 String objects.
    // The first one is starting position and
    // the second one is the ending position.
    private List<String> positions;

    public Move() {
    	this.positions = new ArrayList<String>();
		this.positions.add("A1");
		this.positions.add("A1");
		this.value = 0;
	}
	
	public Move(List<String> positions) {
		this.positions = new ArrayList<String>(positions);
		this.value = 0;
	}
	
    public Move(String startingPosition, String endingPosition) {
        this.positions = new ArrayList<String>();
        this.positions.add(startingPosition);
        this.positions.add(endingPosition);
        this.value = 0;
    }
    
    public Move(String startingPosition, String endingPosition, double value) {
        this.positions = new ArrayList<String>();
        this.positions.add(startingPosition);
        this.positions.add(endingPosition);
        this.value = value;
    }
    
	public Move(int value) {
		this.positions = new ArrayList<String>();
		this.positions.add("A1");
		this.positions.add("A1");
		this.value = value;
	}
	
	public Move(List<String> positions, double value) {
		this.positions = new ArrayList<>(positions);
		this.value = value;
	}

    public Move(Move otherMove) {
        this.positions = new ArrayList<String>(otherMove.positions);
        this.value = otherMove.value;
    }

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

    public List<String> getPositions() {
        return positions;
    }

    public void setPositions(List<String> positions) {
        this.positions = positions;
    }
    
    @Override
    public String toString() {
		return "startingPosition: " + positions.get(0) + ", endingPosition: " + positions.get(1) + ", value: " + value;
    }

}
