package ar.edu.itba.ss;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Simulation {

    //private Set<Car> cars;
    //private double roadLength;
    private double vmax;
    private double probabilityOfDescreasing;
    private Random random;
    private double dt;
    private Printer printer;
    private boolean periodic;
    private List<Lane> lanes;
    private List<Intersection> intersections;

    public Simulation(int numberOfCars, double roadLength,double carSpace,double vmax,double probabilityOfDescreasing,boolean periodic) {
        //this.cars = new HashSet<>();
        //this.roadLength = roadLength;
        this.vmax = vmax;
        this.random = new Random();
        this.probabilityOfDescreasing = probabilityOfDescreasing;
        this.printer = new Printer("./out/trafficFlow/animation_");
        this.dt = 1;
        this.periodic = periodic;

        /*List<Integer> positions = IntStream.range(0,(int)roadLength).boxed()
                .collect(Collectors.toList());

        Collections.shuffle(positions);

        positions.subList(0,numberOfCars)
                .forEach(pos ->this.cars.add(new Car(pos,0)));
*/
        lanes = new ArrayList<>();
        intersections = new ArrayList<>();

        Intersection intersection = new Intersection(2,1,16, 16);
        intersections.add(intersection);

        List<Intersection> lane1Intersections = new ArrayList<>();
        List<Intersection> lane2Intersections = new ArrayList<>();
        lane1Intersections.add(intersection);
        lane2Intersections.add(intersection);
        lanes.add(new Lane(1, 20, 67, vmax, probabilityOfDescreasing, dt, periodic, lane1Intersections, 16));
        lanes.add(new Lane(2, 10, 33, vmax, probabilityOfDescreasing, dt, periodic, lane2Intersections, 16));


    }


    public void run() {

        for(int i=0 ;i<1000;i++){
            printer.appendToAnimation(lanes);
            simulataionStep();

        }

        printer.close();
    }


    public void simulataionStep() {
        lanes.forEach(Lane::updatePositions);

        updateIntersectionsPriority();
    }

    private void updateIntersectionsPriority() {
        intersections.forEach(intersection -> {
            if(intersection.getCrossingCar().isPresent()) {
                return;
            }

            if(!intersection.getVerticalCar().isPresent()) {
                intersection.setSelectedCar(intersection.getHorizontalCar());
                intersection.setUnSelectedCar(intersection.getVerticalCar());
            } else if(!intersection.getHorizontalCar().isPresent()) {
                intersection.setSelectedCar(intersection.getVerticalCar());
                intersection.setUnSelectedCar(intersection.getHorizontalCar());
            }else{
                if(random.nextDouble() < 0.5) {
                    intersection.setSelectedCar(intersection.getHorizontalCar());
                    intersection.setUnSelectedCar(intersection.getVerticalCar());
                } else {
                    intersection.setSelectedCar(intersection.getVerticalCar());
                    intersection.setUnSelectedCar(intersection.getHorizontalCar());
                }
            }



        });
    }

    /*
    public void setVelocitites(){
        cars.forEach(car -> {
            if(car.getVelocity()<vmax)
                car.increaseVelocity(dt);
        });
    }

    public void slowDownIfVehicleAtSite(){
        cars.forEach(car -> {
            Optional<Car> oneAhead = cars.stream().min((car1,car2)-> {
                double dist1 = car1.distanceTo(car);
                double dist2= car2.distanceTo(car);

                dist1 = dist1>0 ?dist1:dist1+roadLength;
                dist2 =dist2>0 ?dist2:dist2+roadLength;
                return (int)Math.signum(dist1-dist2);
            });

            oneAhead.ifPresent( ahead-> {
                double breach = ahead.getPosition()-car.getPosition();
                breach = breach>0 ?breach: breach+roadLength;
                if (breach<car.getVelocity()) {
                    car.setVelocity(breach-dt);
                }
            });
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

*/
}
