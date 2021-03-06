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
    private List<Lane> lanes;
    private List<Intersection> intersections;

    public Simulation(int numberOfCars, double roadLength,double carSpace,double vmax,double probabilityOfDescreasing) {
        this.vmax = vmax;
        this.random = new Random();
        this.probabilityOfDescreasing = probabilityOfDescreasing;
        this.printer = new Printer("./out/trafficFlow/animation_");
        this.dt = 1;

        lanes = new ArrayList<>();
        intersections = new ArrayList<>();

        Intersection intersection1 = new Intersection(16,16,new Semaphore(Semaphore.STATE.GREEN, 30,3,0,false));
        Intersection intersection2 = new Intersection(16,32,new Semaphore(Semaphore.STATE.GREEN, 30,3,0,false));
        Intersection intersection3 = new Intersection(16,48,new Semaphore(Semaphore.STATE.GREEN, 30,3,0,false));
        intersections.add(intersection1);
        intersections.add(intersection2);
        intersections.add(intersection3);

        Intersection intersection1L2 = new Intersection(17,16,new Semaphore(Semaphore.STATE.GREEN, 30,3,0,true));
        Intersection intersection2L2 = new Intersection(17,32,new Semaphore(Semaphore.STATE.GREEN, 30,3,0,true));
        Intersection intersection3L2 = new Intersection(17,48,new Semaphore(Semaphore.STATE.GREEN, 30,3,0,true));
        intersections.add(intersection1L2);
        intersections.add(intersection2L2);
        intersections.add(intersection3L2);


        List<Intersection> laneH1Intersections = new ArrayList<>();
        laneH1Intersections.add(intersection1);
        laneH1Intersections.add(intersection2);
        laneH1Intersections.add(intersection3);
        List<Intersection> laneV1Intersections = new ArrayList<>();
        laneV1Intersections.add(intersection1);
        laneV1Intersections.add(intersection1L2);
        List<Intersection> laneV2Intersections = new ArrayList<>();
        laneV2Intersections.add(intersection2);
        laneV2Intersections.add(intersection2L2);
        List<Intersection> laneV3Intersections = new ArrayList<>();
        laneV3Intersections.add(intersection3);
        laneV3Intersections.add(intersection3L2);

        List<Intersection> laneH2Intersections = new ArrayList<>();
        laneH2Intersections.add(intersection1L2);
        laneH2Intersections.add(intersection2L2);
        laneH2Intersections.add(intersection3L2);

        lanes.add(new Lane(1,LaneType.HORIZONTAL, 40, 67, vmax, probabilityOfDescreasing, dt,  16, laneH1Intersections));
        lanes.add(new Lane(2,LaneType.HORIZONTAL, 40, 67, vmax, probabilityOfDescreasing, dt,  17, laneH2Intersections));
        lanes.add(new Lane(3,LaneType.VERTICAL, 10, 33, vmax, probabilityOfDescreasing, dt,  16, laneV1Intersections));
        lanes.add(new Lane(4,LaneType.VERTICAL, 10, 33, vmax, probabilityOfDescreasing, dt, 32, laneV2Intersections));
        lanes.add(new Lane(5,LaneType.VERTICAL, 10, 33, vmax, probabilityOfDescreasing, dt, 48, laneV3Intersections));
    }


    public void run() {

        for(int i=0 ;i<500;i++){
            printer.appendToAnimation(lanes);
            System.out.print("Iter: " + i + " ");
            simulationStep();
        }

        printer.close();
    }


    public void simulationStep() {
        lanes.forEach(Lane::updatePositions);
        //allowToCross();
        lanes.forEach(Lane::advanceVehicles);
        //checkIfCrossed();
        intersections.stream()
                .map(Intersection::getSemaphore)
                .forEach(Semaphore::advanceSemaphore);


        intersections.stream()
                .map(Intersection::getSemaphore)
                .forEach(semaphore -> System.out.println(semaphore.getState()));

    }

    private void checkIfCrossed() {
        intersections.forEach(intersection -> {
            intersection.getCrossing().ifPresent(car -> {
                if(car.getPosition()>intersection.getPosition(car)){
                    intersection.setCrossing(Optional.empty());
                }
            });
        });
    }

    private void allowToCross() {
        intersections.forEach(intersection -> {
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

            Optional<Car> willCross = crossingCars.size() > 0 ? Optional.ofNullable(crossingCars.get(0)) : Optional.empty();

            Optional<Car> willStop = crossingCars.size() > 1 ? Optional.ofNullable(crossingCars.get(1)) : Optional.empty();

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

            willStop.ifPresent(car -> {
                car.setVelocity(0);
            });
        });
    }
}
