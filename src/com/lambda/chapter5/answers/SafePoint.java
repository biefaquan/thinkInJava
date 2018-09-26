
package com.lambda.chapter5.answers;

public class SafePoint {
    private Integer x;
    private Integer y;

    private SafePoint(int x,int y) {
        this.x = x;
        this.y = y;
    }
    public SafePoint(SafePoint safePoint){
        synchronized (safePoint){
            this.x=safePoint.x;
            this.y=safePoint.y;
        }
    }

    public synchronized int[] getXy(){
        return new int[]{x,y};
    }

    public synchronized  void setPoint(int x,int y){
        this.x=x;
        try{
            Thread.sleep(100*100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.y=y;
    }


    public static void main(String[] args) {
        final SafePoint rePoint=new SafePoint(1,1);
        new Thread(()->{
            rePoint.setPoint(2,2);
            System.out.println(rePoint.toString());
        }).start();
        new Thread(()->{
            SafePoint newPoint=new SafePoint(rePoint);
            System.out.println(newPoint.toString());
        }).start();
    }

    @Override
    public String toString() {
        return this.x+"---"+this.y;
    }
}
