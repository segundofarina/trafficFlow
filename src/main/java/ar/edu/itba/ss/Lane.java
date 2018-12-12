package ar.edu.itba.ss;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lane {
    private LaneType type;
    private Set<Car> cars;
    private int cellsAmount;
    private double vmax;
    private double probabilityOfDescreasing;
    private Random random;
    private double dt;
    private boolean periodic;
    private double intersectionPos;
    private Intersection intersection;

    public Lane(LaneType type, int numberOfCars, int cellsAmount, double vmax, double probabilityOfDescreasing, double dt, boolean periodic, double lanePosition,Intersection intersection) {
        this.type = type;
        this.cellsAmount = cellsAmount;
        this.vmax = vmax;
        this.probabilityOfDescreasing = probabilityOfDescreasing;
        this.dt = dt;
        this.periodic = periodic;
        this.cars = new HashSet<>();
        this.random = new Random();
        this.intersectionPos = lanePosition;
        this.intersection = intersection;

        List<Integer> positions = IntStream.range(0,(int) cellsAmount).boxed()
                .filter(i -> i != intersectionPos)
                .collect(Collectors.toList());

        Collections.shuffle(positions);

        int carId = LaneType.HORIZONTAL == type? 1000:0;


        positions.subList(0, numberOfCars)
                .forEach(pos ->this.cars.add(new Car(carId + pos, pos,0,type)));
        //cars.stream().sorted(Comparator.comparingDouble(Car::getPosition)).forEach(System.out::println);

    }

    public void updatePositions() {
        setVelocitites();
        slowDownIfVehicleAtSite();
        //randomDecrease();
        slowDownIfAboutToCross();
//        advanceVehicles();
    }

    private void slowDownIfAboutToCross() {
        Optional<Car> attemptToCross = cars.stream()
                .filter(car-> car.getPosition()<intersectionPos && car.getPosition()+car.getVelocity()>= intersectionPos)
                .findFirst();

        attemptToCross.ifPresent(car ->{
            car.setVelocity(intersectionPos-car.getPosition()-dt);
        });

        Optional<Car> aboutToCross = cars.stream()
                .filter(car-> car.getPosition() == intersectionPos - dt)
                .findFirst();

        aboutToCross.ifPresent(car ->{
            intersection.markAsAboutToCross(car,type);
            car.setVelocity(0);
        });

    }


    private void setVelocitites(){
        // set to 0 if is not selected car in intersection
        cars.forEach(car -> {
            if(car.getVelocity()<vmax)
                car.increaseVelocity(dt);
        });

    }

    private void slowDownIfVehicleAtSite(){
        cars.forEach(car -> {
            Optional<Car> oneAhead = cars.stream().filter(carO-> carO.getId()!=car.getId()).min((car1,car2)-> {
                double dist1 = car1.distanceTo(car);
                double dist2= car2.distanceTo(car);

                dist1 = dist1>0 ?dist1:dist1+ cellsAmount;
                dist2 =dist2>0 ?dist2:dist2+ cellsAmount;
                return (int)Math.signum(dist1-dist2);
            });

            oneAhead.ifPresent( ahead-> {
                double breach = ahead.getPosition()-car.getPosition();
                breach = breach>0 ?breach: breach+ cellsAmount;
                if (breach-dt<car.getVelocity()) {
                    car.setVelocity(breach-dt);
                }
            });
        });
    }



    private void randomDecrease(){
        cars.forEach(car -> {if(random.nextDouble()< probabilityOfDescreasing)  car.decreaseVelocity(dt);} );
    }

    public void advanceVehicles(){
        cars.forEach(car -> {
            double pos = car.getPosition();
            double vel=  car.getVelocity();

            if(pos+(vel*dt) > cellsAmount){
                pos=pos+(vel*dt)- cellsAmount;
            }else{
                pos+=vel*dt;
            }
            car.setPosition(pos);
        });
    }

    public Set<Car> getCars() {
        return cars;
    }

    public LaneType getType() {
        return type;
    }

    public double getIntersectionPos() {
        return intersectionPos;
    }
}
