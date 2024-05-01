package br.com.fiap.bug_milenium;

import jakarta.ejb.Stateless;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Stateless
public class BeanDataHora {

    public String getDataHoraDoServidor(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }
}