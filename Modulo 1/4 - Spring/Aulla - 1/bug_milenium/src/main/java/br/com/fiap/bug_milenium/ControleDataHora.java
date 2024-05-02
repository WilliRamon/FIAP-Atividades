package br.com.fiap.bug_milenium;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named(value = "controleDataHora")
@RequestScoped
public class ControleDataHora {

    @EJB
    private BeanDataHora beanDataHora;

    public ControleDataHora(){}

    public BeanDataHora getBeanDataHora(){
        return beanDataHora;
    }

    public void setBeanDataHora(BeanDataHora beanDataHora) {
        this.beanDataHora = beanDataHora;
    }
}
