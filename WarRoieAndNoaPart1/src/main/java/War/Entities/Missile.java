package War.Entities;

public class Missile extends Destructible {
    private static int idGenerator = 1;

    private Destination destination;
    private long launchTime;
    private long flightTime;
    private double damage;

    public Missile(String id, double damage, Destination destination, int flightTime, long launchTime) {
        super(id);
        missileInit(damage, destination, flightTime, launchTime);
    }

    public Missile(double damage, Destination destination, long flightTime, long launchTime) {
        super( "M" + idGenerator++);
        missileInit(damage, destination, flightTime, launchTime);
    }

    private void missileInit(double damage, Destination destination, long flightTime, long launchTime){
        setDamage(damage);
        setDestination(destination);
        setFlightTime(flightTime);
        setLaunchTime(launchTime);
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public long getLaunchTime() {
        return launchTime;
    }

    private void setLaunchTime(long launchTime) {
        this.launchTime = launchTime;
    }

    public long getFlightTime() {
        return flightTime;
    }

    private void setFlightTime(long flightTime) {
        this.flightTime = flightTime;
    }

    public double getDamage() {
        return damage;
    }

    private void setDamage(double damage) {
        this.damage = damage;
    }

    @Override
    public String toString() {
        return super.toString() +
                " destination=" + destination +
                ", launchTime=" + launchTime +
                ", flightTime=" + flightTime +
                ", damage=" + damage;
    }

	@Override
	public boolean destruct() {
		isDestructed.set(true);
		return true;
	}
}
