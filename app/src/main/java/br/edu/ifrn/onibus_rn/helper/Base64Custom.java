package br.edu.ifrn.onibus_rn.helper;

import android.util.Base64;

/**
 * Classe que será utilizada para criptografar
 * dados do usuário que serão usados como
 * chave primária no Firebase
 */

public class Base64Custom {

    /**
     * Método que será utilizado para fazer a codificação
     * da string. Por exemplo: um endereço de email
     * será criptografado e poderá ser usado como chave primária
     */
    public static String codificarBase64(String texto) {
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    /**
     * Método que será utilizado para fazer a decodificação
     * da string. Por exemplo: um endereço de email
     * já criptografado será descriptografado com este método
     * para que seja conhecido o endereço de email
     */
    public static  String decodificarBase64(String textoCodificado) {
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
