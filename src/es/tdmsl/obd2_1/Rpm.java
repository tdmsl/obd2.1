package es.tdmsl.obd2_1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Manu on 26/06/2016.
 */
public class Rpm extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.rpm);
        super.onCreate(savedInstanceState);
    }
    public void cerrar(View view){
        finish();
    }
}