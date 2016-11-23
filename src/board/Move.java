package board;

public class Move {

    long positionFrom;
    long positionTo;
    MoveDirection direction;
    public boolean isCaptureMove = false;
    int affectedBullets = 0;

    public enum MoveDirection {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * Constructor.
     *
     * @param positionFrom position from which the bullet will move
     * @param direction direction in witch the bullet will get pushed
     */
    public Move(final long positionFrom, final MoveDirection direction) {
        this.positionFrom = positionFrom;
        this.direction = direction;
        switch (direction) {
            case UP: positionTo = positionFrom << 7;break;
            case DOWN: positionTo = positionFrom >> 7;break;
            case LEFT: positionTo = positionFrom << 1;break;
            case RIGHT: positionTo = positionFrom >> 1;break;
        }
    }

    /**
     * Constructor with the additional parameter isCaptureMove.
     *
     * @param positionFrom position from wich the bullet will move
     * @param direction direction in witch the bullet will get pushed
     * @param isCaptureMove true if the move throws an enemy or red bullet over the edge
     */
    public Move(final long positionFrom, final MoveDirection direction, boolean isCaptureMove) {
        this.positionFrom = positionFrom;
        this.direction = direction;
        this.isCaptureMove = isCaptureMove;
        switch (direction) {
            case UP: positionTo = positionFrom << 7;break;
            case DOWN: positionTo = positionFrom >> 7;break;
            case LEFT: positionTo = positionFrom << 1;break;
            case RIGHT: positionTo = positionFrom >> 1;break;
        }
    }

    @Override
    public String toString() {
        return "Move{" +
                "positionFrom = " + positionFrom +
                ", positionTo = " + positionTo +
                ", direction = " + direction +
                ", affectedBullets = " + affectedBullets +
                ", isCaptureMove = " + isCaptureMove +
                '}';
    }
}
