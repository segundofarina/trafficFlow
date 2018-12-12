package ar.edu.itba.ss;

public class Car {

    private int id;
    private double position;
    private double velocity;


    public Car(int id, double position, double velocity) {
        this.id = id;
        this.position = position;
        this.velocity = velocity;
    }

    public double getPosition() {
        return position;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public double distanceTo(Car other){
        return this.position-other.position;
    }

    public void increaseVelocity(double dt){
        this.velocity+=dt;
    }

    public void decreaseVelocity(double dt){
        this.velocity-=dt;
        if(this.velocity<0){
            this.velocity=0;
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Car{" +
                "position=" + position +
                ", velocity=" + velocity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;

        Car car = (Car) o;

        return getId() == car.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
