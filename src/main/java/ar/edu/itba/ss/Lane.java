package ar.edu.itba.ss;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lane {
    private int id;
    private LaneType type;
    private Set<Car> cars;
    private int cellsAmount;
    private double vmax;
    private double probabilityOfDescreasing;
    private Random random;
    private double dt;
    private double lanePosition;
    private List<Intersection> intersections;

    public Lane(int id ,LaneType type, int numberOfCars, int cellsAmount, double vmax, double probabilityOfDescreasing, double dt, double lanePosition, List<Intersection> intersections) {
        this.type = type;
        this.cellsAmount = cellsAmount;
        this.vmax = vmax;
        this.probabilityOfDescreasing = probabilityOfDescreasing;
        this.dt = dt;
        this.cars = new HashSet<>();
        this.random = new Random();
        this.lanePosition = lanePosition;
        //this.intersectionPos = lanePosition;
        this.intersections = intersections;
        this.id = id;


        List<Double> intersetctionPos = intersections.stream().map(intersection ->
            type == LaneType.HORIZONTAL ? intersection.getVerticalPos() : intersection.getHorizontalPos()
        ).collect(Collectors.toList());

        List<Integer> positions = IntStream.range(0,(int) cellsAmount).boxed()
                .filter(i -> !intersetctionPos.contains(i.doubleValue()))
                .collect(Collectors.toList());

        Collections.shuffle(positions);

        int carId = 1000*this.id;


        positions.subList(0, numberOfCars)
                .forEach(pos ->this.cars.add(new Car(carId + pos, pos,0, type)));
        //cars.stream().sorted(Comparator.comparingDouble(Car::getPosition)).forEach(System.out::println);

    }

    private Lane(int id, LaneType type, Set<Car> cars, List<Intersection> intersections) {
        this.id = id;
        this.type = type;
        this.cars = cars;
        this.intersections = intersections;
    }

    public void updatePositions() {
        setVelocitites();
        slowDownIfVehicleAtSite();
        randomDecrease();
        //slowDownIfAboutToCross();
        stopIfRedLight();
//        advanceVehicles();
    }

    private void slowDownIfAboutToCross() {
        intersections.forEach(intersection -> {
            double intersectionPos = type == LaneType.HORIZONTAL ? intersection.getVerticalPos() : intersection.getHorizontalPos();

            Optional<Car> attemptToCross = cars.stream()
                    .filter(car-> car.getPosition() < intersectionPos && car.getPosition() + car.getVelocity() * dt >= intersectionPos)
                    .findFirst();

            attemptToCross.ifPresent(car ->{
                car.setVelocity(intersectionPos - car.getPosition() - dt);
            });

            Optional<Car> aboutToCross = cars.stream()
                    .filter(car-> car.getPosition() == intersectionPos - dt)
                    .findFirst();

            aboutToCross.ifPresent(car ->{
                intersection.markAsAboutToCross(car, type);
                car.setVelocity(0);
            });
        });
    }

    private void stopIfRedLight(){
        intersections.forEach(intersection -> {
            double intersectionPos = type == LaneType.HORIZONTAL ? intersection.getVerticalPos() : intersection.getHorizontalPos();

            Optional<Car> attemptToCross = cars.stream()
                    .filter(car -> car.getPosition() < intersectionPos && car.getPosition() + car.getVelocity() * dt >= intersectionPos)
                    .findFirst();
            if(intersection.getSemaphore().isRed(type) || intersection.getSemaphore().isYellow(type)){
                attemptToCross.ifPresent(car->car.setVelocity(intersectionPos-car.getPosition()-dt));
            }

            attemptToCross.ifPresent(car -> getOneAhead(car).ifPresent(ahead->{
                if(ahead.getPosition() == intersectionPos+1){
                    car.setVelocity(intersectionPos-car.getPosition()-dt);
                }
            } ));


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
            Optional<Car> oneAhead = getOneAhead(car);

            oneAhead.ifPresent( ahead-> {
                double breach = ahead.getPosition()-car.getPosition();
                breach = breach>0 ?breach: breach+ cellsAmount;
                if (breach-dt<car.getVelocity()) {
                    car.setVelocity(breach-dt);
                }
            });
        });
    }

    private Optional<Car> getOneAhead(Car car) {
        return cars.stream().filter(carO-> carO.getId()!=car.getId()).min((car1,car2)-> {
            double dist1 = car1.distanceTo(car);
            double dist2= car2.distanceTo(car);

            dist1 = dist1>0 ?dist1:dist1+ cellsAmount;
            dist2 =dist2>0 ?dist2:dist2+ cellsAmount;
            return (int)Math.signum(dist1-dist2);
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
        return lanePosition;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public Lane clone(Set<Car> cars){
        return new Lane(this.id,this.type,cars,this.intersections);
    }

    public int getCellsAmount() {
        return cellsAmount;
    }
}
