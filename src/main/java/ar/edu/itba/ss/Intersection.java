package ar.edu.itba.ss;

import java.util.Optional;

public class Intersection {
    private int verticalLane;
    private int horizontalLane;
    private double verticalLanePosition;
    private double horizontalLanePosition;
    private Optional<Car> verticalCar;
    private Optional<Car> horizontalCar;
    private Optional<Car> selectedCar;
    private Optional<Car> unSelectedCar;
    private Optional<Car> crossingCar;

    public Intersection(int verticalLane, int horizontalLane, double verticalLanePosition, double horizontalLanePosition) {
        this.verticalLane = verticalLane;
        this.horizontalLane = horizontalLane;
        this.verticalLanePosition = verticalLanePosition;
        this.horizontalLanePosition = horizontalLanePosition;
        this.verticalCar = Optional.empty();
        this.horizontalCar = Optional.empty();
        this.selectedCar = Optional.empty();
        this.unSelectedCar = Optional.empty();
        crossingCar = Optional.empty();
    }

    public int getVerticalLane() {
        return verticalLane;
    }

    public void setVerticalLane(int verticalLane) {
        this.verticalLane = verticalLane;
    }

    public int getHorizontalLane() {
        return horizontalLane;
    }

    public void setHorizontalLane(int horizontalLane) {
        this.horizontalLane = horizontalLane;
    }

    public double getVerticalLanePosition() {
        return verticalLanePosition;
    }

    public void setVerticalLanePosition(double verticalLanePosition) {
        this.verticalLanePosition = verticalLanePosition;
    }

    public double getHorizontalLanePosition() {
        return horizontalLanePosition;
    }

    public void setHorizontalLanePosition(double horizontalLanePosition) {
        this.horizontalLanePosition = horizontalLanePosition;
    }

    public Optional<Car> getVerticalCar() {
        return verticalCar;
    }

    public void setVerticalCar(Optional<Car> verticalCar) {
        this.verticalCar = verticalCar;
    }

    public Optional<Car> getHorizontalCar() {
        return horizontalCar;
    }

    public void setHorizontalCar(Optional<Car> horizotnalCar) {
        this.horizontalCar = horizotnalCar;
    }

    public Optional<Car> getSelectedCar() {
        return selectedCar;
    }

    public void setSelectedCar(Optional<Car> selectedCar) {
        this.selectedCar = selectedCar;
    }

    public Optional<Car> getUnSelectedCar() {
        return unSelectedCar;
    }

    public void setUnSelectedCar(Optional<Car> unSelectedCar) {
        this.unSelectedCar = unSelectedCar;
    }

    public Optional<Car> getCrossingCar() {
        return crossingCar;
    }

    public void setCrossingCar(Optional<Car> crossingCar) {
        this.crossingCar = crossingCar;
    }
}
