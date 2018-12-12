package ar.edu.itba.ss;

public class Semaphore {
    public enum STATE{
        RED,GREEN,YELLOW
    }


    private Semaphore.STATE state;
    private Semaphore.STATE nextState;
    private int time;
    private int greenDuration;
    private int redDuration;
    private int yellowDuration;
    private boolean ignoreYellow;

    public Semaphore(STATE state, int redDuration,int yellowDuration, int startingTime,boolean ignoreYellow) {
        this.state = state;
        this.time = startingTime;
        this.yellowDuration = yellowDuration;
        this.redDuration = redDuration;
        //this.greenDuration = greenDuration;
        this.greenDuration = redDuration - yellowDuration;
        this.ignoreYellow = ignoreYellow;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public void advanceSemaphore(){
        time++;
        if(time%getDuration()==0){
            switchSemaphore();
        }
    }

    private int getDuration(){
        int duration = 0;
        switch (state){
            case GREEN:
                duration = greenDuration;
                break;
            case RED:
                duration = redDuration;
                break;
            case YELLOW:
                duration = yellowDuration;
                break;
        }
        return duration;
    }

    private void switchSemaphore() {
        switch (state){
            case RED:
                state = STATE.GREEN;
                //nextState = STATE.GREEN;
                break;
            case GREEN:
                state = STATE.YELLOW;
                //nextState = STATE.RED;
                break;
            case YELLOW:
                state = STATE.RED;
                //Don't need to change nextState
                break;
        }

    }

    public boolean isRed(LaneType type){
        if(type == LaneType.HORIZONTAL){
            return this.state == STATE.RED;
        }else{
            return this.state == STATE.GREEN || this.state == STATE.YELLOW;
        }
    }

    public boolean isYellow(LaneType type) {
        if(type == LaneType.HORIZONTAL) {
            return this.state == STATE.YELLOW;
        } else {
            if(ignoreYellow){
                return false;
            }
            if(this.state == STATE.YELLOW || this.state == STATE.GREEN) {
                return false;
            }
            return (time % redDuration) > greenDuration;
        }
    }
}
