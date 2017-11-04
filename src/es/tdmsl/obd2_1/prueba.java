package es.tdmsl.obd2_1;

import android.os.Handler;

/**
 * Created by Manu on 19/05/2016.
 */
public class prueba {
    private Thread thread;

    public prueba(Thread thread) {
        //this.thread = thread;
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        sleep(1000);
                        handler.post(this);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    Handler handler = new Handler();
    final Runnable r = new Runnable() {
        public void run() {
            // tv.append("Hello World");
            handler.postDelayed(this, 1000);
        }
    };
    /*Handler handler = new Handler();
    Runnable r = new Runnable() {
        public void run() {
            //tv.append("Hello World");
        } };
    handler.postDelayed(r, 1000);
}*/



}