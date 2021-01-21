package com.integracao.producers.dto;




public class Message {

    private  String Fila;

    public String getFila() {
        return Fila;
    }

    public void setFila(String fila) {
        Fila = fila;
    }



    private  String Json;

    public Message() {

    }


    public String getJson() {
        return Json;
    }

    public void setJson(String json) {
        Json = json;
    }
}
