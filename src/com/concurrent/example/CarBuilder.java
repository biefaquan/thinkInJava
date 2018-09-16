package com.concurrent.example;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * P744（仿真这一块的都看的不是很明白了？？？）
 * 分发工作，用于汽车的机器人组装线。（创建底盘，安装发动机、车厢和轮子）
 */
class Car1 {
    private final int id;
    private boolean engine = false, driveTrain = false, wheels = false;
    public Car1 (int idn) {
        id = idn;
    }
    // Empty Car1 object
    public Car1 () {
        id = -1;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized void addEngine () {
        engine = true;
    }

    public synchronized void addDriverTrain() {
        driveTrain = true;
    }

    public synchronized void addWheels () {
        wheels = true;
    }

    @Override
    public String toString() {
        return "Car1 " + id + " [" + " engine: " + engine + " driverTrain: " + driveTrain + " wheels: " + wheels + "]";
    }
}

class  CarQueue extends LinkedBlockingQueue<Car1> {}

class ChassisBuilder implements Runnable {
    private CarQueue carQueue;
    private int counter = 0;
    public ChassisBuilder(CarQueue cq) {
        carQueue = cq;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(500);
                //Make chassis
                Car1 c = new Car1(counter++);
                System.out.println("ChassisBuilder created " + c);
                // Inset into queue
                carQueue.put(c);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted: ChassisBuilder");
        }
        System.out.println("ChassisBuilder off");
    }
}

class Assembler implements Runnable {
    private CarQueue chassisQueue, finishingQueue;
    private Car1 Car1;
    private CyclicBarrier barrier = new CyclicBarrier(4);
    private RobotPool robotPool;
    public Assembler(CarQueue cq, CarQueue fq, RobotPool rp) {
        chassisQueue = cq;
        finishingQueue = fq;
        robotPool = rp;
    }
    public Car1 Car1() {
        return Car1;
    }

    public CyclicBarrier barrier() {
        return barrier;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //Blocks until chassis is availble
                Car1 = chassisQueue.take();
                //Hire robots to perform work
                robotPool.hire(EngineRobot.class, this);
                robotPool.hire(DriverTrainRobot.class, this);
                robotPool.hire(WheelRobot.class, this);
                barrier.await(); //Until the robots finish
                //Put Car1 into finishingQueue for further work
                finishingQueue.put(Car1);
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting Assemble via interrupt");
        } catch (BrokenBarrierException e) {
            //This one we want to konw about
            throw new RuntimeException(e);
        }
        System.out.println("Assemble off");
    }
}

class Reporter implements Runnable {
    private CarQueue carQueue;
    public Reporter(CarQueue cq) {
        carQueue = cq;
    }

    @Override
    public void run() {
        try{
            while (!Thread.interrupted()) {
                System.out.println(carQueue.take());
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting Reporter via interrupt");
        }
        System.out.println("Reporter off");
    }
}

abstract class Robot implements Runnable {
    private RobotPool pool;
    public Robot(RobotPool p) {
        pool = p;
    }
    protected Assembler assembler;
    public Robot assignAssembler(Assembler assembler) {
        this.assembler = assembler;
        return this;
    }
    private boolean engage = false;
    public synchronized void engage() {
        engage = true;
        notifyAll();
    }
    //This part of run() that's diffenent for each about
    abstract protected void performService();

    @Override
    public void run() {
        try {
            powerDown(); //Wait until needed
            while (!Thread.interrupted()) {
                performService();
                assembler.barrier().await();//synchronize
                //We're done with that job...
                powerDown();
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting " + this + "via interrupt");
        } catch (BrokenBarrierException e) {
            //This one we want to konw about
            throw new RuntimeException(e);
        }
        System.out.println(this + " off");
    }

    private synchronized void powerDown() throws InterruptedException {
        engage = false;
        assembler = null;//Disconnect from the Assemble
        //Put ourselves back in the available pool
        pool.release(this);
        while (engage == false) { //Power down
            wait();
        }
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}

class EngineRobot extends Robot {
    public EngineRobot(RobotPool pool) {
        super(pool);
    }

    @Override
    protected void performService() {
        System.out.println(this + " installing engine");
        assembler.Car1().addEngine();
    }
}

class DriverTrainRobot extends Robot {
    public DriverTrainRobot(RobotPool pool) {
        super(pool);
    }

    @Override
    protected void performService() {
        System.out.println(this + " installing driverTrain");
        assembler.Car1().addDriverTrain();
    }
}

class WheelRobot extends Robot {
    public WheelRobot(RobotPool pool) {
        super(pool);
    }

    @Override
    protected void performService() {
        System.out.println(this + " installing wheel");
        assembler.Car1().addWheels();
    }
}

class RobotPool {
    //Quitely prevents identical entries
    private Set<Robot> pool = new HashSet<>();
    public synchronized void add(Robot r) {
        pool.add(r);
        notifyAll();
    }
    public synchronized void hire(Class<? extends  Robot> robotType, Assembler d) throws InterruptedException {
        for (Robot r : pool) {
            if (r.getClass().equals(robotType)) {
                pool.remove(r);
                r.assignAssembler(d);
                r.engage(); //Power it up to do the task
                return;
            }
            wait();//None available
            hire(robotType, d);//Try again, recursively
        }
    }

    public synchronized void release(Robot r) {
        add(r);
    }
}

public class CarBuilder {
    public static void main(String[] args) throws  Exception{
        CarQueue chassisQueue = new CarQueue(),
                finishingQueue = new CarQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        RobotPool robotPool = new RobotPool();
        exec.execute(new EngineRobot(robotPool));
        exec.execute(new DriverTrainRobot(robotPool));
        exec.execute(new WheelRobot(robotPool));
        exec.execute(new Assembler(chassisQueue, finishingQueue, robotPool));
        exec.execute(new Reporter(finishingQueue));
        //Start everything running by producing chassis
        exec.execute(new ChassisBuilder(chassisQueue));
        TimeUnit.SECONDS.sleep(7);
        exec.shutdownNow();
    }
}
