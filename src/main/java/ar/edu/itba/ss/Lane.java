package ar.edu.itba.ss;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lane {
    private int id;
    private Set<Car> cars;
    private int cellsAmount;
    private double vmax;
    private double probabilityOfDescreasing;
    private Random random;
    private double dt;
    private boolean periodic;
    private List<Intersection> intersections;
    private double lanePosition;

    public Lane(int id, int numberOfCars, int cellsAmount, double vmax, double probabilityOfDescreasing, double dt, boolean periodic, List<Intersection> intersections, double lanePosition) {
        this.id = id;
        this.cellsAmount = cellsAmount;
        this.vmax = vmax;
        this.probabilityOfDescreasing = probabilityOfDescreasing;
        this.dt = dt;
        this.periodic = periodic;
        this.cars = new HashSet<>();
        this.random = new Random();
        this.intersections = intersections;
        this.lanePosition = lanePosition;

        List<Integer> positions = IntStream.range(0,(int) cellsAmount).boxed()
                .collect(Collectors.toList());

        Collections.shuffle(positions);

        int carId = 1000 * id;

        positions.subList(0, numberOfCars)
                .forEach(pos ->this.cars.add(new Car(carId + pos, pos,0)));
        //cars.stream().sorted(Comparator.comparingDouble(Car::getPosition)).forEach(System.out::println);

    }

    public void updatePositions() {
        setVelocitites();
        slowDownIfVehicleAtSite();
        intersectionSlowDown();
        randomDecrease();
        advanceVehicles();
    }


    private void setVelocitites(){
        // set to 0 if is not selected car in intersection
        cars.forEach(car -> {
            if(car.getVelocity()<vmax)
                car.increaseVelocity(dt);
        });

        intersections.forEach(intersection -> {
            intersection.getUnSelectedCar().ifPresent(car -> {
                car.setVelocity(0);
            });
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

    private void intersectionSlowDown() {
        intersections.forEach(intersection -> {
            double intersectionPosition;
            if(intersection.getVerticalLane() == id) {
                intersectionPosition = intersection.getVerticalLanePosition();
            } else {
                intersectionPosition = intersection.getHorizontalLanePosition();
            }

            if(intersection.getCrossingCar().isPresent() && intersection.getCrossingCar().get().getPosition() >= intersectionPosition + dt) {
                intersection.setCrossingCar(Optional.empty());
            }

            Optional<Car> intersectionCar = cars.stream().filter(car ->
                 car.getPosition() < intersectionPosition && car.getPosition() + car.getVelocity() * dt >= intersectionPosition
            ).findFirst();

            intersectionCar.ifPresent(car -> {
                double newVelocity = intersectionPosition - car.getPosition() - dt;
                newVelocity = newVelocity < 0 ? 0 : newVelocity;
                car.setVelocity(newVelocity);

                if(intersection.getSelectedCar().isPresent() && car.equals(intersection.getSelectedCar().get())) {
                        car.increaseVelocity(dt);
                        intersection.setCrossingCar(Optional.of(car));
                }
            });

            if(!intersection.getSelectedCar().equals(intersectionCar)) {
                if(intersection.getVerticalLane() == id) {
                    intersection.setVerticalCar(intersectionCar);
                } else {
                    intersection.setHorizontalCar(intersectionCar);
                }
            } else {
                if(intersection.getVerticalLane() == id) {
                    intersection.setVerticalCar(Optional.empty());
                } else {
                    intersection.setHorizontalCar(Optional.empty());
                }
            }
            intersection.setSelectedCar(Optional.empty());
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

    public int getId() {
        return id;
    }

    public double getLanePosition() {
        return lanePosition;
    }
}
