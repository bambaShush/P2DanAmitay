package War.Entities;

import java.util.Objects;

public abstract class Weapon {
    private String id;

    public Weapon(String id) {
        setId(id);
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Weapon weapon = (Weapon) o;
        return Objects.equals(getId(), weapon.getId());
    }

    @Override
    public String toString() {
        return String.format("%-5s %-20s",getId(), getClass().getSimpleName());
    }


}
