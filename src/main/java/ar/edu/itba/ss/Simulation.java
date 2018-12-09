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
    Printer printer;

    public Simulation(int numberOfCars, int roadLength,int vmax,double probabilityOfDescreasing) {
        this.cars = new HashSet<>();
        this.roadLength = roadLength;
        this.vmax = vmax;
        this.random = new Random();
        this.probabilityOfDescreasing = probabilityOfDescreasing;
        this.printer = new Printer("./out/trafficFlow/animation_");

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
                car.increaseVelocity();
        });
    }

    public void slowDownIfVehicleAtSite(){
        cars.forEach(car -> {
            int vel = car.getVelocity();
            int carPos= car.getPosition();
            boolean hasToslowdown= cars.stream()
                    .anyMatch(otherCar ->
                            carPos < otherCar.getPosition() &&otherCar.getPosition()<= carPos+vel
                                || carPos+vel-roadLength>0 && otherCar.getPosition() <carPos+vel-roadLength);
            if(hasToslowdown){
                car.decreaseVelocity();
            }
        });
    }

    public void randomDecrease(){
        cars.forEach(car -> {if(random.nextDouble()> probabilityOfDescreasing)  car.increaseVelocity();} );
    }

    public void advanceVehicles(){
        cars.forEach(car -> {
            int pos = car.getPosition();
            int vel=  car.getVelocity();

            if(pos+vel > roadLength){
                pos=pos+vel-roadLength;
            }else{
                pos+=vel;
            }
            car.setPosition(pos);
        });
    }


}
