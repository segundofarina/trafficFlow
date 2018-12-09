package ar.edu.itba.ss;

public class Car {

    private double position;
    private double velocity;


    public Car(double position, double velocity) {
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

    public void increaseVelocity(double dt){
        this.velocity+=dt;
    }

    public void decreaseVelocity(double dt){
        this.velocity-=dt;
        if(this.velocity<0){
            this.velocity=0;
        }
    }

    @Override
    public String toString() {
        return "Car{" +
                "position=" + position +
                ", velocity=" + velocity +
                '}';
    }
}
