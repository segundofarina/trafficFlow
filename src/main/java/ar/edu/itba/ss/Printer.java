package ar.edu.itba.ss;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
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


    private String generateFileString(Set<Car> allParticles) {
        Set<Car> limits = new HashSet<>();
        //addBorders(limits);
        StringBuilder builder = new StringBuilder()
                .append(allParticles.size() + limits.size())
                .append("\r\n")
                .append("// X\t vel\t \r\n");

        appendParticles(allParticles, builder);
        appendParticles(limits, builder);
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

    private void appendParticles(Set<Car> cars, StringBuilder builder) {
        for (Car current : cars) {
            double vel = current.getVelocity();
            builder.append(current.getPosition())
                    .append(" ")
                    .append(new Double(vel).floatValue())
                    .append("\r\n");


        }
    }


    public void appendToAnimation(Set<Car> allParticles){
        appendToFile(generateFileString(allParticles));
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
