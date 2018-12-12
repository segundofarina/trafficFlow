package ar.edu.itba.ss;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Simulation {

    private double vmax;
    private double probabilityOfDescreasing;
    private Random random;
    private double dt;
    private Printer printer;
    private boolean periodic;
    private List<Lane> lanes;
    private Intersection intersection;

    public Simulation(int numberOfCars, double roadLength,double carSpace,double vmax,double probabilityOfDescreasing,boolean periodic) {
        this.vmax = vmax;
        this.random = new Random();
        this.probabilityOfDescreasing = probabilityOfDescreasing;
        this.printer = new Printer("./out/trafficFlow/animation_");
        this.dt = 1;
        this.periodic = periodic;

        lanes = new ArrayList<>();

        this.intersection = new Intersection(16,16);
        lanes.add(new Lane(LaneType.HORIZONTAL, 20, 67, vmax, probabilityOfDescreasing, dt, periodic, 16,intersection));
        lanes.add(new Lane(LaneType.VERTICAL, 10, 33, vmax, probabilityOfDescreasing, dt, periodic, 16,intersection));


    }


    public void run() {

        for(int i=0 ;i<500;i++){
            printer.appendToAnimation(lanes);
            simulataionStep();

        }

        printer.close();
    }


    public void simulataionStep() {
        lanes.forEach(Lane::updatePositions);
        allowToCross();
        lanes.forEach(Lane::advanceVehicles);
        checkIfCrossed();
    }

    private void checkIfCrossed() {
        intersection.getCrossing().ifPresent(car->{
            if(car.getPosition()>intersection.getPosition(car)){
                intersection.setCrossing(Optional.empty());
            }});

    }

    private void allowToCross() {
        if(intersection.getCrossing().isPresent()){
            System.out.print("Crossing is present [");
            System.out.println(intersection.getCrossing().get());
           return;
        }
        System.out.print("No one is crossing ");

        List<Car> crossingCars = Stream.of(intersection.getAboutToCrossH(),intersection.getAboutToCrossV())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        Collections.shuffle(crossingCars);

        Optional<Car> willCross = crossingCars.size()>0 ? Optional.ofNullable(crossingCars.get(0)): Optional.empty();

        Optional<Car> willStop = crossingCars.size()>1 ? Optional.ofNullable(crossingCars.get(1)): Optional.empty();

        System.out.print("Will Cross : [");
        willCross.ifPresent(System.out::print);
        System.out.print("] Will Stop:[");
        willStop.ifPresent(System.out::print);
        System.out.println("]");

        willCross.ifPresent(car -> {
            intersection.markAsCrossing(car);
            intersection.clearAboutToCross(car);
            car.setVelocity(dt);
        });

        willStop.ifPresent(car->{
            car.setVelocity(0);
        });

    }
}
