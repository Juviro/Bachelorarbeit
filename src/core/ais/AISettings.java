package core.ais;


public class AISettings {
    public boolean alphaBetaPruningEnabled;
    public boolean moveSortEnabled;
    public boolean timeManagementEnabled;
    public boolean quiescenceEnabled;
    double[] weights;

    public AISettings(boolean alphaBetaPruningEnabled, boolean moveSortEnabled, boolean timeManagementEnabled, boolean quiescenceEnabled, double[] weights) {
        this.alphaBetaPruningEnabled = alphaBetaPruningEnabled;
        this.moveSortEnabled = moveSortEnabled;
        this.timeManagementEnabled = timeManagementEnabled;
        this.quiescenceEnabled = quiescenceEnabled;
        this.weights = weights;
    }

    public AISettings(boolean alphaBetaPruningEnabled, boolean moveSortEnabled, boolean timeManagementEnabled, boolean quiescenceEnabled) {
        this.alphaBetaPruningEnabled = alphaBetaPruningEnabled;
        this.moveSortEnabled = moveSortEnabled;
        this.timeManagementEnabled = timeManagementEnabled;
        this.quiescenceEnabled = quiescenceEnabled;
        this.weights = new double[] {1, 0.5, 1, 0.5};
    }
}
