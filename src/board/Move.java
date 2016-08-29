package board;

/**
 * Created by Juviro on 12.08.2016.
 */
public class Move {

    public long positionFrom;
    public long positionTo;
    public MoveDirection direction;
    public boolean isCaptureMove = false;
    public int affectedBullets = 0;

    public enum MoveDirection {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * @param positionFrom
     * @param direction
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

    @Override
    public String toString() {
        return "Move{" +
                "positionFrom=" + positionFrom +
                ", positionTo=" + positionTo +
                ", direction=" + direction +
                ", isCaptureMove=" + isCaptureMove +
                '}';
    }
}
