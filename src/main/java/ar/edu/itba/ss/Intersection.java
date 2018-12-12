package ar.edu.itba.ss;

import java.util.Optional;

public class Intersection {


    private Optional<Car> crossing;
    private Optional<Car> aboutToCrossH;
    private Optional<Car> aboutToCrossV;
    private double horizontalPos;
    private double verticalPos;
    private Semaphore semaphore;

    public Intersection(double hoirzontalPos, double verticalPos,Semaphore semaphore) {
        this.crossing = Optional.empty();
        this.aboutToCrossH = Optional.empty();
        this.aboutToCrossV = Optional.empty();
        this.horizontalPos = hoirzontalPos;
        this.verticalPos = verticalPos;
        this.semaphore = semaphore;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void markAsAboutToCross(Car car, LaneType type) {
        if(type == LaneType.HORIZONTAL){
            aboutToCrossH = Optional.of(car);
        }else{
            aboutToCrossV = Optional.of(car);
        }
    }
    public void markAsCrossing(Car car ){
        if(crossing.isPresent()){
            throw new IllegalStateException();
        }
        crossing=Optional.of(car);
    }

    public Optional<Car> getCrossing() {
        return crossing;
    }

    public Optional<Car> getAboutToCrossH() {
        return aboutToCrossH;
    }

    public Optional<Car> getAboutToCrossV() {
        return aboutToCrossV;
    }

    public double getHorizontalPos() {
        return horizontalPos;
    }

    public double getVerticalPos() {
        return verticalPos;
    }

    public void setCrossing(Optional<Car> crossing) {
        this.crossing = crossing;
    }

    public double getPosition(Car car){
        if (car.getLaneType() == LaneType.HORIZONTAL){
            return verticalPos;
        }else {
            return horizontalPos;
        }
    }

    public void clearAboutToCross(Car car) {
        if(Optional.of(car).equals(aboutToCrossH)){
            aboutToCrossH = Optional.empty();
        }else if( Optional.of(car).equals(aboutToCrossV)){
            aboutToCrossV = Optional.empty();
        }else{
            throw new IllegalArgumentException();
        }

    }
}
