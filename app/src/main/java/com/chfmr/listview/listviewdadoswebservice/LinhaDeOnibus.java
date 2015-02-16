package com.chfmr.listview.listviewdadoswebservice; /**
 * Created by julianolustosa on 08/02/15.
 */

import java.io.Serializable;

public class LinhaDeOnibus implements Serializable {

    public String nome;
    public int numero;
    public String sentido_id;
    public String sentido_volda;
    public String imagem;

    public LinhaDeOnibus(String nome, int numero, String sentido_id,
                         String sentido_volda, String imagem){
        this.nome = nome;
        this.numero = numero;
        this.sentido_id = sentido_id;
        this.sentido_volda = sentido_volda;
        this.imagem = imagem;
    }

    @Override
    public String toString(){
        return nome + numero;
    }
}
