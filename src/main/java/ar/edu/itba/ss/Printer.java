package ar.edu.itba.ss;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Printer {

    private BufferedWriter bw;

    public Printer(String outPath) {

        try {
            bw = new BufferedWriter(new FileWriter(outPath  + LocalDateTime.now() + ".txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (bw != null) try {
            bw.flush();
            bw.close();
        } catch (IOException ioe2) {
            // just ignore it
        }
    }


    private String generateFileString(List<Lane> lanes) {
        Set<Car> limits = new HashSet<>();

        int totalCars = lanes.stream().map(lane -> lane.getCars().size()).reduce(0, (a, b) -> a+b);

        //addBorders(limits);
        StringBuilder builder = new StringBuilder()
                .append(totalCars + limits.size())
                .append("\r\n")
                .append("// X\t Y\t vel\t Radius\t\r\n");

        appendParticles(lanes, builder);
        //appendParticles(limits, direction, lanePosition, builder);
        return builder.toString();
    }

//    private int addBorders(Set<Car> limits) {
//        double delta= 1/10.0;
//        int count=1;
//        double radius = 0.15;
//        for(double i =-radius ; i< L+radius;i+=delta){
//            limits.add(new Car(-count++,Vector.of(-radius,i),Vector.ZERO,radius,0,0,Vector.ZERO));
//            limits.add(new Car(-count++,Vector.of(W+radius,i),Vector.ZERO,radius,0,0,Vector.ZERO));
//        }
//        for (double j= -radius; j< W+radius;j+=delta){
//            if(j < W/2 - door/2 || j > (W/2 + door/2)){
//                limits.add(new Particle(-count++,Vector.of(j,-radius),Vector.ZERO,radius,0,0,Vector.ZERO));
//            }
//            limits.add(new Particle(-count++,Vector.of(j,L+radius),Vector.ZERO,radius,0,0,Vector.ZERO));
//
//        }
//        return count;
//    }

    private void appendParticles(List<Lane> lanes, StringBuilder builder) {
        lanes.forEach(lane -> {
            lane.getCars().forEach(car -> {
                if(lane.getId() == 1) {
                    builder.append(car.getPosition()*7.5)
                            .append(" ")
                            .append(lane.getLanePosition() * 7.5);
                } else {
                    builder.append(lane.getLanePosition() * 7.5)
                            .append(" ")
                            .append(car.getPosition()*7.5);
                }

                builder.append(" ")
                        .append(new Double(car.getVelocity()).floatValue())
                        .append(" ")
                        .append(2.5)
                        .append("\r\n");
            });
        });

/*
        for (Car current : cars) {
            double vel = current.getVelocity();

            if(direction == LaneDirection.HORIZONTAL) {
                builder.append(current.getPosition()*7.5)
                        .append(" ")
                        .append(lanePosition);
            } else {
                builder.append(lanePosition)
                        .append(" ")
                        .append(current.getPosition()*7.5);
            }
            //builder.append(current.getPosition()*7.5)
              builder.append(" ")
                    .append(new Double(vel).floatValue())
                    .append(" ")
                    .append(2.5)
                    .append("\r\n");


        }*/
    }


    public void appendToAnimation(List<Lane> lanes){
        appendToFile(generateFileString(lanes));
    }
    public void appendToFile( String data) {
        try {
            bw.write(data);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void flush() {
        try {
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
