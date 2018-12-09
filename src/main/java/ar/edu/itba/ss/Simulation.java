package ar.edu.itba.ss;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Simulation {

    private Set<Car> cars;
    private int roadLength;
    private int vmax;
    private double probabilityOfDescreasing;
    private Random random;
    private double dt;
    Printer printer;
    boolean periodic;

    public Simulation(int numberOfCars, int roadLength,int vmax,double probabilityOfDescreasing,boolean periodic) {
        this.cars = new HashSet<>();
        this.roadLength = roadLength;
        this.vmax = vmax;
        this.random = new Random();
        this.probabilityOfDescreasing = probabilityOfDescreasing;
        this.printer = new Printer("./out/trafficFlow/animation_");
        this.dt = 1.0/60;
        this.periodic = periodic;

        List<Integer> positions = IntStream.range(0,roadLength).boxed()
                .collect(Collectors.toList());

        Collections.shuffle(positions);

        positions.subList(0,numberOfCars)
                .forEach(pos ->this.cars.add(new Car(pos,0)));


    }


    public void run(){

        for(int i=0 ;i<1000;i++){
            simultaionStep();
            printer.appendToAnimation(cars);
        }

        printer.close();

    }


    public void simultaionStep(){

        setVelocitites();

        slowDownIfVehicleAtSite();

        randomDecrease();

        advanceVehicles();


    }

    public void setVelocitites(){
        cars.forEach(car -> {
            if(car.getVelocity()<vmax)
                car.increaseVelocity(dt);
        });
    }

    public void slowDownIfVehicleAtSite(){
        cars.forEach(car -> {
            double vel = car.getVelocity();
            double carPos= car.getPosition();
            boolean hasToslowdown= cars.stream()
                    .anyMatch(otherCar ->
                            carPos < otherCar.getPosition() &&otherCar.getPosition()<= carPos+vel
                                || carPos+vel-roadLength>0 && otherCar.getPosition() <carPos+vel-roadLength);
            if(hasToslowdown){
                car.decreaseVelocity(dt);
            }
        });
    }

    public void randomDecrease(){
        cars.forEach(car -> {if(random.nextDouble()> probabilityOfDescreasing)  car.decreaseVelocity(dt);} );
    }

    public void advanceVehicles(){
        cars.forEach(car -> {
            double pos = car.getPosition();
            double vel=  car.getVelocity();

            if(pos+(vel*dt) > roadLength){
                pos=pos+(vel*dt)-roadLength;
            }else{
                pos+=vel*dt;
            }
            car.setPosition(pos);
        });
    }


}
