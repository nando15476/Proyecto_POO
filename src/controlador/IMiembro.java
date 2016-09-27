package controlador;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

import excepciones.CambioDenegadoException;
import excepciones.NoEsUnCorreoValidoException;
import excepciones.NoEsUnNombreRealException;
import excepciones.PermisoDenegadoException;
import excepciones.ValorInvalidoException;
import modelo.Miembro;

/**
 * Una interfaz que permite comunicarse con cualquiera que implemente sus métodos. Esta interfaz
 * existe para promover el <a href="https://en.wikipedia.org/wiki/Loose_coupling">Acoplamiento
 * flojo</a>. Todos los herederos de {@link Miembro} implementan esta interfaz.
 * <p>
 * Además utilizar la interfaz en lugar de la clase concreta <b>prohibe</b> exitosamente la
 * creación arbitraria de objetos {@link Miembro} y sus herederos Multitones.
 */
public interface IMiembro extends AutoCloseable {
    /**
     * Devuelve el índice del {@link Miembro} en su respectiva tabla en la Base de Datos.
     * Este índice determina la validez del {@link Miembro}.
     *
     * @return el índice en la Base de Datos del {@link Miembro} o -1 si el {@link Miembro} no está
     * en la Base de Datos
     */
    int getSqlID();

    /**
     * Devuelve el ID del {@link Miembro}, si es -1 es que no se ha configurado un ID para el
     * {@link Miembro}.
     *
     * @return el ID del {@link Miembro}
     */
    int getIdentificador();

    /**
     * Cambia el ID del {@link Miembro}. Genera una excepción si el ID es 0, negativo o si el
     * {@link Miembro} ya está validado.
     *
     * @param identificador el nuevo ID del objeto
     *
     * @throws CambioDenegadoException si el valor del ID es inválido o si el {@link Miembro} ya
     *                                 está validado
     */
    void setIdentificador(final int identificador) throws CambioDenegadoException;

    /**
     * Devuelve los nombres del {@link Miembro}.
     *
     * @return los nombres del {@link Miembro}
     */
    @Nullable
    String getNombres();

    /**
     * Valida un nuevo nombre para el {@link Miembro} y luego asigna el nombre al {@link
     * Miembro}.
     *
     * @param nombres los nuevos nombres del {@link Miembro}
     *
     * @throws NoEsUnNombreRealException si el nombre parece no ser un nombre válido
     * @throws CambioDenegadoException   cuando no se puede realizar el cambio de nombres
     */
    void setNombres(@NonNls @NotNull final String nombres)
            throws NoEsUnNombreRealException, CambioDenegadoException;

    /**
     * Devuelve el primer apellido del {@link Miembro}.
     *
     * @return el primer apellido del {@link Miembro}
     */
    @Nullable
    String getPrimerApellido();

    /**
     * Valida el nuevo primer apellido del {@link Miembro} y luego asigna el primer apellido
     * al {@link Miembro}.
     *
     * @param primerApellido el nuevo primer apellido del {@link Miembro}
     *
     * @throws NoEsUnNombreRealException si el primer apellido parece no ser un apellido válido
     * @throws CambioDenegadoException   si el {@link Miembro} ya estaba validado
     */
    void setPrimerApellido(@NonNls @NotNull final String primerApellido)
            throws NoEsUnNombreRealException, CambioDenegadoException;

    /**
     * Devuelve el segundo apellido del {@link Miembro}.
     *
     * @return el segundo apellido del {@link Miembro}
     */
    @Nullable
    String getSegundoApellido();

    /**
     * Valida el nuevo segundo apellido del {@link Miembro} y luego asigna el segundo
     * apellido al {@link Miembro}.
     *
     * @param segundoApellido el segundo apellido del {@link Miembro}
     *
     * @throws NoEsUnNombreRealException si el segundo apellido parece no ser un apellido válido
     * @throws CambioDenegadoException   si el {@link Miembro} ya estaba validado
     */
    void setSegundoApellido(@NonNls @NotNull final String segundoApellido)
            throws NoEsUnNombreRealException, CambioDenegadoException;

    /**
     * Devuelve el host de correo electrónico personal del {@link Miembro}.
     *
     * @return el host de correo electrónico
     */
    @Nullable
    String getCorreoHost();

    /**
     * Valida el correo electrónico personal del {@link Miembro} y luego asigna el Host y el
     * Nombre de Usuario al {@link Miembro}. Tanto el Nombre de Usuario como el Host son
     * {@link Nullable}, es decir, pueden ser {@code null}. Esto hará que el {@link Miembro}
     * no tenga correo electrónico personal.
     *
     * @param correoHost    host del correo electrónico (como gmail.com o yahoo.com)
     * @param correoUsuario nombre de usuario (la parte que va antes de la arroba)
     *
     * @throws NoEsUnCorreoValidoException cuando no se puede determinar la validez del correo
     * @throws CambioDenegadoException     si el {@link Miembro} ya estaba validado
     */
    void setCorreo(@NonNls @Nullable final String correoHost,
            @NonNls @Nullable final String correoUsuario)
            throws NoEsUnCorreoValidoException, CambioDenegadoException;

    /**
     * Devuelve el nombre de usuario de correo electrónico personal del {@link Miembro}.
     *
     * @return el nombre de usuario de correo
     */
    @Nullable
    String getCorreoUsuario();

    /**
     * Devuelve el nombre de usuario de correo electrónico de la Universidad del {@link
     * Miembro}.
     *
     * @return el nombre de usuario de correo
     */
    @Nullable
    String getCorreoUniversidad();

    /**
     * Agrega el correo de la Universidad. En este caso, solo se usa el <b>nombre de usuario</b>,
     * pues el dominio de la universidad es constante y el mismo para todos los {@link
     * Miembro}s.
     *
     * @param correoUniversidad nombre de usuario de la Universidad
     *
     * @throws NoEsUnCorreoValidoException si el correo de la Universidad no pasa la prueba de
     *                                     validez
     * @throws CambioDenegadoException     si el {@link Miembro} ya estaba validado
     */
    void setCorreoUniversidad(@NonNls @NotNull String correoUniversidad)
            throws NoEsUnCorreoValidoException, CambioDenegadoException;

    /**
     * Genera un nombre de usuario para el correo electrónico de la Universidad.
     *
     * @throws CambioDenegadoException     si el {@link Miembro} ya estaba validado
     * @throws NoEsUnCorreoValidoException si por alguna razón no se puede generar automáticamente
     *                                     un nombre de usuario
     */
    void makeCorreoU() throws CambioDenegadoException, NoEsUnCorreoValidoException;

    /**
     * Guarda el objeto acutal en la Base de Datos, y una vez almacenado en la base de
     * datos, el objeto se sincroniza de nuevo con la base de datos, es decir, se valida.
     *
     * @param admin Un {@link IAdministrador} con autoriadad para modificar la base de datos
     *
     * @throws SQLException             cuando la petición SQL falla
     * @throws PermisoDenegadoException cuando el {@link IAdministrador} no tiene atoridad para
     *                                  realizar la acción
     * @throws ValorInvalidoException   cuando cualquier valor del Miembro recuperado es inválido.
     *                                  Esto implica un grave error pues la única fomrma de
     *                                  solucionarlo es corregir la base de datos
     */
    void guardarEnBaseDeDatos(final IAdministrador admin)
            throws SQLException, PermisoDenegadoException, ValorInvalidoException,
            CambioDenegadoException;

    boolean isValido();
}
