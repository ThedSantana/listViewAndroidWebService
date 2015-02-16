package com.chfmr.listview.listviewdadoswebservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

/**
 * Created by carlosfm on 08/02/15.
 */

public class AppHttp {

    public static final String LINHAS_ONIBUS_URL_JSON = "http://192.168.1.100:8081/bragmobi/linhas-de-onibus/offset/0/limit/10";
    private static final Object APPBUS = "APPBUS";

    private static HttpURLConnection connect(String urlWebService) throws IOException {

        final int SEGUNDOS = 1000;
        URL url = new URL(urlWebService);
        HttpURLConnection conexao = (HttpURLConnection)url.openConnection();
        conexao.setReadTimeout(10 * SEGUNDOS);
        conexao.setConnectTimeout(15 * SEGUNDOS);
        conexao.setRequestMethod("GET");
        conexao.setDoInput(true);
        conexao.setDoOutput(false);
        conexao.connect();
        return conexao;
    }

    public static boolean hasConect(Context ctx){

        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();

        if(info != null && info.isConnected()){
            return true;
        } else {
            return false;
        }
    }

    private static String bytesToString (InputStream is) throws IOException {
        Log.i("APPBUS", "bytesToString");
        byte[] buffer = new byte[1024];

        ByteArrayOutputStream bufferMemory = new ByteArrayOutputStream();
        int bytesRead;

        while ((bytesRead = is.read(buffer)) != -1){
            bufferMemory.write(buffer, 0, bytesRead);
        }

        return new String(bufferMemory.toByteArray(), "UTF-8");
    }

    /* Metodos que precisam ser colocados na class LinhasDeOnibus */

    public static List<LinhaDeOnibus> carregarLinhaOnibusJson(){

        try{
            HttpURLConnection connecting = connect(LINHAS_ONIBUS_URL_JSON);
            int resposta = connecting.getResponseCode();

            Log.i("APPBUS", "resposta connect:" + resposta);
            Log.i("APPBUS", "HttpURLConnection.HTTP_OK:" + HttpURLConnection.HTTP_OK);

            if(resposta == HttpURLConnection.HTTP_OK){
                InputStream is = connecting.getInputStream();
                JSONObject json = new JSONObject(bytesToString(is));
                Log.i("APPBUS", "carregouLinhaOnibusJson" + json);
                return readJsonLineBus(json);
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<LinhaDeOnibus> readJsonLineBus(JSONObject json) throws JSONException {

        List<LinhaDeOnibus> listaDeLinhaDeOnibus = new ArrayList<LinhaDeOnibus>();

        JSONArray jsonLinhasDeOnibus = json.getJSONArray("linhas_de_onibus");

        Log.i("APPBUS", "jsonLinhasDeOnibus:" + jsonLinhasDeOnibus.length());

        for(int contador = 0; contador < jsonLinhasDeOnibus.length(); contador++){

            Log.i("APPBUS", "readJsonLineBus contador:" + contador);

            JSONObject objetoLinhaDeOnibus = jsonLinhasDeOnibus.getJSONObject(contador);

            LinhaDeOnibus linha = new LinhaDeOnibus(
                    objetoLinhaDeOnibus.getString("nome"),
                    objetoLinhaDeOnibus.getInt("numero"),
                    objetoLinhaDeOnibus.getString("sentido_ida"),
                    objetoLinhaDeOnibus.getString("sendito_volta"),
                    "http://www.guiadebraganca.com.br/public/imagens/icon_facebook.png"
                    // objetoLinhaDeOnibus.getString("imagem")
            );

            listaDeLinhaDeOnibus.add(linha);
        }

        return listaDeLinhaDeOnibus;
    }
}
