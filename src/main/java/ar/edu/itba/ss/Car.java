package ar.edu.itba.ss;

public class Car {

    private int position;
    private int velocity;


    public Car(int position, int velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public int getPosition() {
        return position;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void increaseVelocity(){
        this.velocity++;
    }

    public void decreaseVelocity(){
        this.velocity--;
    }

    @Override
    public String toString() {
        return "Car{" +
                "position=" + position +
                ", velocity=" + velocity +
                '}';
    }
}
