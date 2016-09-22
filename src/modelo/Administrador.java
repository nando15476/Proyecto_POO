package modelo;

import controlador.IAdministrador;

/**
 * Created by notengobattery on 14/09/16.
 */
public class Administrador implements IAdministrador {
    @Override
    public boolean isAutorizado() {
        return true;
    }
}
