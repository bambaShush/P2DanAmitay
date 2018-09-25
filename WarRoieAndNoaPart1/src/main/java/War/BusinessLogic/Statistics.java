package War.BusinessLogic;


public class Statistics{
    private int numOfLaunchedMissiles;
    private int numOfDestructedMissiles;
    private int numOfHits;
    private int numOfDestructedLaunchers;
    private double totalDamage;

    public Statistics(){
        numOfLaunchedMissiles = 0;
        numOfDestructedMissiles = 0;
        numOfHits = 0;
        numOfDestructedLaunchers = 0;
        totalDamage = 0;
    }

    public Statistics(int numbOfLaunchedMissiles, int numOfDestructedMissiles, int numOfHits, int numOfDestructedLaunchers, double totalDamage) {
        setNumOfLaunchedMissiles(numbOfLaunchedMissiles);
        setNumOfDestructedMissiles(numOfDestructedMissiles);
        setNumOfHits(numOfHits);
        setNumOfDestructedLaunchers(numOfDestructedLaunchers);
        setTotalDamage(totalDamage);
    }

    public synchronized void increaseNumOfLaunchedMissiles(){
        setNumOfLaunchedMissiles(getNumOfLaunchedMissiles() + 1);
    }

    public synchronized void increaseNumOfDestructedMissiles(){
        setNumOfDestructedMissiles(getNumOfDestructedMissiles() + 1);
    }

    public synchronized void increaseNumOfHits(){
        setNumOfHits(getNumOfHits() + 1);
    }

    public synchronized void increaseNumOfDestructedLaunchers(){
        setNumOfDestructedLaunchers(getNumOfDestructedLaunchers() + 1);
    }

    public synchronized void raiseDamage(double damage){
        setTotalDamage(getTotalDamage() + damage);
    }

    public int getNumOfLaunchedMissiles() {
        return numOfLaunchedMissiles;
    }

    private void setNumOfLaunchedMissiles(int numOfLaunchedMissiles) {
        this.numOfLaunchedMissiles = numOfLaunchedMissiles;
    }

    public int getNumOfDestructedMissiles() {
        return numOfDestructedMissiles;
    }

    private void setNumOfDestructedMissiles(int numOfDestructedMissiles) {
        this.numOfDestructedMissiles = numOfDestructedMissiles;
    }

    public int getNumOfHits() {
        return numOfHits;
    }

    private void setNumOfHits(int numOfHits) {
        this.numOfHits = numOfHits;
    }

    public int getNumOfDestructedLaunchers() {
        return numOfDestructedLaunchers;
    }

    private void setNumOfDestructedLaunchers(int numOfDestructedLaunchers) {
        this.numOfDestructedLaunchers = numOfDestructedLaunchers;
    }

    public double getTotalDamage() {
        return totalDamage;
    }

    private void setTotalDamage(double totalDamage) {
        this.totalDamage = totalDamage;
    }

    @Override
    public String toString() {
        return "Statistics:" +
                "\nnumOfLaunchedMissiles: " + numOfLaunchedMissiles +
                ", \nnumOfDestructedMissiles: " + numOfDestructedMissiles +
                ", \nnumOfHits: " + numOfHits +
                ", \nnumOfDestructedLaunchers: " + numOfDestructedLaunchers +
                ", \ntotalDamage: " + totalDamage +
                '\n';
    }
}
