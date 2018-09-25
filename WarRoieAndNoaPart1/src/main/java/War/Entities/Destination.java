package War.Entities;

public class Destination {
    private String dest;

    public Destination(String dest){
        setDest(dest);
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    @Override
    public String toString() {
        return dest;
    }
}
