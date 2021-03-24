package br.edu.ifrn.onibus_rn.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    /*Método para retornar a data atual*/
    public static String dataAtual() {
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        return dataString;
    }

}
