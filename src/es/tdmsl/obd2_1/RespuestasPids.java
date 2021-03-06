package es.tdmsl.obd2_1;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Manu on 20/06/2016.
 */
public class RespuestasPids {
    String trama;
    public RespuestasPids() {
    }

    public RespuestasPids(String trama) {
        this.trama = trama;
    }

    public StringBuffer mostrarPidsDisponibles(StringBuffer respuesta) {
        StringBuffer tramaBinaria = new StringBuffer();
        StringBuffer tramaPidSoportados =  new StringBuffer();
        String trama = ("no data");
        Map<String, String> tablaHexBin = new HashMap<String, String>();
        tablaHexBin.put("0", "0000");
        tablaHexBin.put("1", "0001");
        tablaHexBin.put("2", "0010");
        tablaHexBin.put("3", "0011");
        tablaHexBin.put("4", "0100");
        tablaHexBin.put("5", "0101");
        tablaHexBin.put("6", "0110");
        tablaHexBin.put("7", "0111");
        tablaHexBin.put("8", "1000");
        tablaHexBin.put("9", "1001");
        tablaHexBin.put("A", "1010");
        tablaHexBin.put("B", "1011");
        tablaHexBin.put("C", "1100");
        tablaHexBin.put("D", "1101");
        tablaHexBin.put("E", "1110");
        tablaHexBin.put("F", "1111");
        Map<String, String> tablaBinHex = new HashMap<String, String>();
        tablaBinHex.put("1","01");
        tablaBinHex.put("2","02");
        tablaBinHex.put("3","03");
        tablaBinHex.put("4","04");
        tablaBinHex.put("5","05");
        tablaBinHex.put("6","06");
        tablaBinHex.put("7","07");
        tablaBinHex.put("8","08");
        tablaBinHex.put("9","09");
        tablaBinHex.put("10","0A");
        tablaBinHex.put("11","0B");
        tablaBinHex.put("12","0C");
        tablaBinHex.put("13","0D");
        tablaBinHex.put("14","0E");
        tablaBinHex.put("15","0F");
        tablaBinHex.put("16","10");
        tablaBinHex.put("17","11");
        tablaBinHex.put("18","12");
        tablaBinHex.put("19","13");
        tablaBinHex.put("20","14");
        tablaBinHex.put("21","15");
        tablaBinHex.put("22","16");
        tablaBinHex.put("23","17");
        tablaBinHex.put("24","18");
        tablaBinHex.put("25","19");
        tablaBinHex.put("26","1A");
        tablaBinHex.put("27","1B");
        tablaBinHex.put("28","1C");
        tablaBinHex.put("29","1D");
        tablaBinHex.put("30","1E");
        tablaBinHex.put("31","1F");
        tablaBinHex.put("32","20");
        Log.i("TAG", "TRAMA  =  " + respuesta);
        //formateamos la respuesta//quitamos pid y cabeceras
        trama = respuesta.toString();
        trama = trama.replace("SEARCHING...", "");
        trama = trama.replace("0100", "");
        trama = trama.replace("41 00 ", "");
        trama = trama.replace(">", "");
        trama = trama.replace("NO DATA", "");
        trama = trama.replaceAll("\\s", "");

        //trama = trama.replace("\n\r","");
        Log.i("TAG", "TRAMA  =  " + trama);
        Log.i("TAG", "longitud TRAMA  =  " + trama.length());
        int longitud = trama.length();
        int x = 0;
        //se pasa de hex a bin
        for (int l=1;l<=longitud;l++) {
            tramaBinaria.append(tablaHexBin.get(trama.substring(x, x+1))) ;
            x++;
        }

                /*for (int i=0;i<=tramaBinaria.length()-1;i++){
                    stringBufferPidSoportados.append(tramaBinaria.charAt(i));
                    //Log.i("TAG","datos vector pidSoportados Nª "+i+" = "+pidSoportados.get(i));
                }*/
        Log.i("TAG","trama binaria  "+tramaBinaria);
        Log.i("TAG","longitud cadena  "+tramaBinaria.length());
        int p1 =1;
        for(int i=1;i<=tramaBinaria.length()-1;i++){
            //tramaPidSoportados.append(i) ;

            if (tramaBinaria.charAt(i)=='1'){
                tramaPidSoportados.append(tablaBinHex.get(Integer.toString(p1))+",") ;
            }
            p1++;
        }
        /*int p2 = 1;
        for(int i=33;i<=64;i++){

            if (tramaBinaria.charAt(i)=='1'){
                tramaPidSoportados.append(tablaBinHex.get(Integer.toString(p2))+",") ;
            }
            p2++;
        }*/
        /*int p3 = 1;
        for(int i=65;i<96;i++){

            if (tramaBinaria.charAt(i)=='1'){
                tramaPidSoportados.append(tablaBinHex.get(Integer.toString(p3))+",") ;
            }
            p3++;
        }*/
        Log.i("TAG","pid soportados "+tramaPidSoportados);
        Log.i("TAG","numero de PID  "+tramaPidSoportados.length());

        Log.i("TAG", "---------------------------------------------------------------------------------------");

        return tramaPidSoportados;
    }

    public String estadoSistemaCombustible(StringBuffer respuesta){
        //formateamos la respuesta//quitamos pid y cabeceras
        String trama = ("no data");
        trama = respuesta.toString();
        //trama = trama.replace("SEARCHING...", "");
        trama = trama.replace("0103", "");
        trama = trama.replace("41 03 ", "");
        trama = trama.replace(">", "");
        trama = trama.replaceAll("\\s", "");
        trama = trama.replace("\n\r","");
        //trama = trama
        Log.i("TAG", "TRAMA  =  " + trama);
        Log.i("TAG", "longitud TRAMA  =  " + trama.length());

        return trama;
    }

    public double cargaMotor(StringBuffer respuesta){
        //formateamos la respuesta//quitamos pid y cabeceras
        String trama = ("no data");
        trama = respuesta.toString();
        //trama = trama.replace("SEARCHING...", "");
        trama = trama.replace("0104", "");
        trama = trama.replace("41 04 ", "");
        trama = trama.replace(">", "");
        trama = trama.replaceAll("\\s", "");
        trama = trama.replace("\n\r","");
        double carga = Integer.parseInt(trama,16);
        carga = carga/2.55;
        carga =  Math.round(carga);
        Log.i("TAG", "TRAMA  =  " + trama);
        Log.i("TAG", "Porcentaje de carga  =  " + carga);
        Log.i("TAG", "longitud TRAMA  =  " + trama.length());

        return carga;
    }

    public double temperaturaRefrigerante(StringBuffer respuesta) {
        //formateamos la respuesta//quitamos pid y cabeceras
        String trama = ("no data");
        trama = respuesta.toString();
        //trama = trama.replace("SEARCHING...", "");
        trama = trama.replace("0105", "");
        trama = trama.replace("41 05 ", "");
        trama = trama.replace(">", "");
        trama = trama.replaceAll("\\s", "");
        trama = trama.replace("\n\r","");
        double temp=Integer.parseInt(trama,16);
        temp = temp-40;
        Log.i("TAG", "TRAMA  =  " + trama);
        Log.i("TAG", "Temperatura  =  " + trama);
        Log.i("TAG", "longitud TRAMA  =  " + trama.length());
        return temp;
    }

    public double relacionEstequiometrica(StringBuffer respuesta) {
        //formateamos la respuesta//quitamos pid y cabeceras
        String trama = ("no data");
        trama = respuesta.toString();
        //trama = trama.replace("SEARCHING...", "");
        trama = trama.replace("0106", "");
        trama = trama.replace("41 09 ", "");
        trama = trama.replace(">", "");
        trama = trama.replaceAll("\\s", "");
        trama = trama.replace("\n\r","");
        double mezcla=Integer.parseInt(trama,16);
        mezcla = (mezcla/1.28)-100;
        Log.i("TAG", "TRAMA  =  " + trama);
        Log.i("TAG", "relacionEstequiometrica  =  " + trama);
        Log.i("TAG", "longitud TRAMA  =  " + trama.length());
        return mezcla;
    }
}
