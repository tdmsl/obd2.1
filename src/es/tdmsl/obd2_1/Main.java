package es.tdmsl.obd2_1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.*;

public class Main extends Activity {

    private ProgressDialog progress;
    private ProgressDialog progresBar;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice dispositivo;
    private BluetoothSocket socket;
    private TextView info;
    private ScrollView scrollView;
    private TextView tv_respuesta;
    private TextView et_CMD;
    private static final int REQUEST_ENABLE_BT = 1;

    private Handler handlerProgreso = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //info.setText(info.getText()+"\n"+msg.obj.toString());
            info.append("\n" + msg.obj.toString());
            //Toast.makeText(getBaseContext(), "Estoy en el HandleMessage"+msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private Handler handlerRespuesta = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //tv_respuesta.setText(msg.obj.toString());
            tv_respuesta.append("\n" + msg.obj.toString());

        }
    };

    private Handler handlerAlert = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Main.this);
            alertDialog.setMessage(msg.obj.toString());
            alertDialog.show();
        }
    };

    private Handler handlerProgresBar = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);

            progress = new ProgressDialog(Main.this);
            progress.setMessage(msg.obj.toString());
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();

        }
    };

    Thread conexion = new Thread(new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            handlerAlert.sendMessage(msg);
            //BluetoothSocket socket = myDispositivo.createInsecureRfcommSocketToServiceRecord(uuid);
            //tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        }
    });


    public final ThreadLocal<BroadcastReceiver> bReceiver = new ThreadLocal<BroadcastReceiver>() {
        @Override
        protected BroadcastReceiver initialValue() {
            return new BroadcastReceiver() {
                boolean OBDII;

                @Override
                public void onReceive(final Context context, Intent intent) {
                    final String action = intent.getAction();
                    // final BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();


// Filtramos por la accion. Nos interesa detectar BluetoothAdapter.ACTION_STATE_CHANGED
                    if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                        // Solicitamos la informacion extra del intent etiquetada como BluetoothAdapter.EXTRA_STATE
// El segundo parametro indicara el valor por defecto que se obtendra si el dato extra no existe
                        final int estado = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                                BluetoothAdapter.ERROR);

                        switch (estado) {
                            // Apagado
                            case BluetoothAdapter.STATE_OFF: {
                                //((Button)findViewById(R.id.btnBluetooth)).setText(R.string.ActivarBluetooth);
                                //TextView info = (TextView) findViewById(R.id.info);
                                Message msg = new Message();
                                msg.obj = "conectando con un Thread y su Hanler case BluetoothAdapter.STATE_OFF:";
                                handlerProgreso.sendMessage(msg);
                                //activarBluetooth();
                                break;
                            }

                            // Encendido
                            case BluetoothAdapter.STATE_ON: {
                                //((Button)findViewById(R.id.btnBluetooth)).setText(R.string.DesactivarBluetooth);
                                //TextView info = (TextView) findViewById(R.id.info);
                                //info.setText("Bluetooth Activado");
                                Message msg = new Message();
                                msg.obj = "conectando con un Thread y su Hanler case BluetoothAdapter.STATE_ON:";
                                handlerProgreso.sendMessage(msg);
                                //conexion.start();
                       /* // Lanzamos un Intent de solicitud de visibilidad Bluetooth, al que añadimos un par
                        // clave-valor que indicara la duracion de este estado, en este caso 120 segundos
                        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
                        startActivity(discoverableIntent);*/

                        /*if(bAdapter.isEnabled())
                            bAdapter.disable();
                        else
                        {
                            // Lanzamos el Intent que mostrara la interfaz de activacion del
                            // Bluetooth. La respuesta de este Intent se manejara en el metodo
                            // onActivityResult
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        }*/


                                break;
                            }
                            default:
                                break;
                        }

                    }
                    // Cada vez que se descubra un nuevo dispositivo por Bluetooth, se ejecutara
                    // este fragmento de codigo
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                        // Acciones a realizar al descubrir un nuevo dispositivo
                        OBDII = false;
                        dispositivo = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if ("OBDII".equals(dispositivo.getName())) {
                            btAdapter.cancelDiscovery();
                            //TextView info = (TextView) findViewById(R.id.info);
                            //info.setText(info.getText() + "\nEncontrada la inteface " + dispositivo.getName() + "\n");
                            Message msg = new Message();
                            msg.obj = "encotrado dispositivo elm327 ";
                            handlerProgreso.sendMessage(msg);
                            // Finalizamos la busqueda de dispositivos
                            OBDII = true;
                            progress.dismiss();

                            // Iniciamos la conexion
                            msg = new Message();
                            msg.obj = "iniciando la Conexion ";
                            handlerProgresBar.sendMessage(msg);

                            conectar.start();

                        }


                    }
                    // Codigo que se ejecutara cuando el Bluetooth finalice la busqueda de dispositivos.
                    else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                        // Acciones a realizar al finalizar el proceso de descubrimiento
                        progress.dismiss();
                        // BluetoothDevice dispositivo = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if (OBDII == false)
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert")
                                    .setMessage("No se ha encontrado ningun interface\n ELM 327")
                                    .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            btAdapter.startDiscovery();
                                            progress = new ProgressDialog(context);
                                            progress.setMessage("Buscando interface ELM327 ");
                                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                            progress.setIndeterminate(true);
                                            progress.show();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        //Toast.makeText(Main.this, "ACTION_DISCOVERY_FINISHED", Toast.LENGTH_SHORT).show();
                    }

                }
            };
        }
    };


    Thread conectar = new Thread(new Runnable() {
        @Override
        public void run() {
        /*progresBar = new ProgressDialog(getBaseContext());
        progresBar.setMessage("iniciando conexion ");
        progresBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresBar.setIndeterminate(true);
        progresBar.show();*/

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService
            try {
                socket = dispositivo.createInsecureRfcommSocketToServiceRecord(uuid);
                socket.connect();
                Message msg = new Message();
                msg.obj = "conectado con " + socket.getRemoteDevice().getName();//OBD||
                handlerProgreso.sendMessage(msg);

                progress.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    });

    public void enviarCMD(View view) {
        //para hacer una lectura continua de crea un bucle infinito repitiendo el pid
        new Thread(new Runnable() {
            public void run() {
                /*//aqui hacemos un bucle infinito para una lectura continua
                new Thread(new Runnable() {
                    public void run() {
                        while (true){
                            String comando = et_CMD.getText().toString();
                            enviar(comando, tv_respuesta);
                        }
                    }
                }).start();*/
                //Aquí ejecutamos nuestras tareas costosas
                String comando = et_CMD.getText().toString();
                enviar(comando, tv_respuesta);
                //se envia informacion a IU
               /* Message msg = new Message();
                msg.obj = comando;
                handlerAlert.sendMessage(msg);*/
                ///////////////

            }
        }).start();
    }

    private void enviar(String cmd, TextView tv_respuesta) {
       /* Message msg = new Message();
        msg.obj = "enviando comando " + cmd;
        handlerProgreso.sendMessage(msg);*/

        try {
            for (int i = 0; i < cmd.length(); i++) {
                socket.getOutputStream().write(cmd.codePointAt(i));//Se escribe en el Puerto serie
                if ((i + 1) == cmd.length()) {
                    socket.getOutputStream().write(13);//Solamente necesita el retorno de carro,sin el salto de linea.
                    recibir(cmd, tv_respuesta);
                    //readResult(socket.getInputStream());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    ////////////////////////////////////////////////////////
    protected void readResult(InputStream paramInputStream)
            throws IOException {
        StringBuilder localStringBuilder = new StringBuilder();
        String respuesta;
        ArrayList<Integer> buffer = null;
        while (true) {
            int i = (byte) paramInputStream.read();
            if ((char) i == '>')
                break;
            if ((char) i != ' ')
                localStringBuilder.append((char) i);
        }
        respuesta = localStringBuilder.toString().trim();
        //buffer.clear();
        int j = 0;

        for (int k = 2; k <= respuesta.length(); k += 2) {
            String str = "0x" + respuesta.substring(j, k);
            Log.i("TAG", str);
            //String str = "0x" + respuesta;
            //buffer.add(Integer.decode(str));
            j = k;
        }
        Message msg = new Message();
        msg.obj = "---recibida respuesta " + respuesta.toString();
        handlerRespuesta.sendMessage(msg);
    }

    ////////////////////////////////////////////////////////////////
    private void recibir(String cmd, TextView tv_respuesta) {
        StringBuffer respuesta = new StringBuffer();
        String trama = ("no data");
        RespuestasPids rPids;

        Message msg;
        int caracter = 0;
        try {
        while ((char) caracter != (char) '>') {


                caracter = socket.getInputStream().read();//Se lee el Puerto serie

            respuesta.append((char) caracter);

        }
        Log.i("TAG", "respuesta a "+cmd+"\n" + respuesta);
////ojo aqui falta
        int pid = Integer.parseInt(respuesta.substring(0, 4),16);//averiguo si es un comando o un pid
//pid soportados 01,02,03,04,05,06,07,0F,13,17, st7
        //si es un Pid averiguo cual es
        switch (pid) {
            case 256://0100 muestra pid disponibles
                rPids = new RespuestasPids();
                msg = new Message();
                msg.obj = "---recibida respuesta " + cmd + "----\n" + respuesta+"\n"+
                       // "trama hexa \n"+trama+"\n"+
                        "Pids aceptados "+rPids.mostrarPidsDisponibles(respuesta);
                handlerRespuesta.sendMessage(msg);
                break;

            case 259://0103 el estado del sistema de combustible
                rPids = new RespuestasPids();
                msg = new Message();
                msg.obj = "---recibida respuesta " + cmd + "----\n" + respuesta+"\n"+
                        // "trama hexa \n"+trama+"\n"+
                        "valores "+rPids.estadoSistemaCombustible(respuesta);
                handlerRespuesta.sendMessage(msg);
                break;
            case 260://0104 carga del motor calculada
                rPids = new RespuestasPids();
                msg = new Message();
                msg.obj = "---recibida respuesta " + cmd + "----\n" + respuesta+"\n"+
                        // "trama hexa \n"+trama+"\n"+
                        "Carga Motor "+rPids.cargaMotor(respuesta);
                handlerRespuesta.sendMessage(msg);
                break;
            case 261://0105 temperatura refrigerante
                rPids = new RespuestasPids();
                msg = new Message();
                msg.obj = "---recibida respuesta " + cmd + "----\n" + respuesta+"\n"+
                        // "trama hexa \n"+trama+"\n"+
                        "Temperatura refrigerante "+rPids.temperaturaRefrigerante(respuesta);
                handlerRespuesta.sendMessage(msg);
                break;
            case 262 ://0106 mezcla combudtible
                rPids = new RespuestasPids();
                msg = new Message();
                msg.obj = "---recibida respuesta " + cmd + "----\n" + respuesta+"\n"+
                        // "trama hexa \n"+trama+"\n"+
                        "Mezcla "+rPids.relacionEstequiometrica(respuesta);
                handlerRespuesta.sendMessage(msg);
                break;
            default:
                msg = new Message();
                msg.obj = "---recibida respuesta no implementada " + cmd + "----\n" + respuesta + "\n" +
                        "trama hexa \n" + trama;
                handlerRespuesta.sendMessage(msg);

        }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e){
            msg = new Message();
            msg.obj = "---recibida respuesta desde el cath de la Excepcion " + cmd + "----\n" + respuesta + "\n" +
                    "trama hexa \n" + trama;
            handlerRespuesta.sendMessage(msg);


        }


    }



    private long timer() {
        Date temps2 = new Date();
        return temps2.getTime();
    }

    private void registrarEventosBluetooth() {
// Registramos los BroadcastReceiver que instanciamos previamente para
// detectar los distintos eventos que queremos recibir
        IntentFilter filtro = new IntentFilter();
        filtro.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filtro.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filtro.addAction(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(bReceiver.get(), filtro);

    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        info = (TextView) findViewById(R.id.info);
        tv_respuesta = (TextView) findViewById(R.id.tv_respuesta);
        et_CMD = (EditText) findViewById(R.id.etCMD);
        Button enviarCMD = (Button) findViewById(R.id.btnEnviarCMD);

        final Button ejecutarLista = (Button) findViewById(R.id.btnEnviarListaComandos);
        final Spinner spinnerCMD = (Spinner) findViewById(R.id.comboCMD);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,comandos );
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.comandos, android.R.layout.simple_spinner_item);
        spinnerCMD.setAdapter(adapter);
        //implementamos el listener del boton
        ejecutarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for (String item : getResources().getStringArray(R.array.comandos2)) {
                            String[] comando = item.split(",");
                            enviar(comando[0], tv_respuesta);
                        }
                        Message msg = new Message();
                        msg.obj = "Fin Lista CMD";
                        handlerProgreso.sendMessage(msg);

                    }
                }).start();

            }
        });
        //implementamos el listener del spinner
        spinnerCMD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if (socket != null) {
                    new Thread(new Runnable() {
                        String[] comando = spinnerCMD.getItemAtPosition(position).toString().split(",");

                        //String[] subcomando =spinnerCMD.getItemAtPosition(position).toString().split(",");
                        @Override

                        public void run() {
                            Log.i("TAG", "subcomando" + comando[0]);
                            enviar(comando[0], tv_respuesta);

                        }

                    }).start();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        // Obtenemos el adaptador Bluetooth. Si es NULL, significara que el
        // dispositivo no posee Bluetooth,
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            info.setText("Este dispositivo no posee bluetooth");
            return;
        }
        // Comprobamos si el Bluetooth esta activo y cambiamos el texto de info

        if (btAdapter.isEnabled()) {
            info.append("\nBluetooth activado");
            //info.setText("Bluetooth activado");
            btAdapter.startDiscovery();
            progress = new ProgressDialog(this);
            progress.setMessage("Buscando interface ELM327 ");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
            registrarEventosBluetooth();
        } else
            info.setText("Bluetooth desactivado");
    }

    // Ademas de realizar la destruccion de la actividad, eliminamos el registro del
// BroadcastReceiver.
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(bReceiver.get());


    }

    public static int hex2decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }

}
