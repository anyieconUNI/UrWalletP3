package co.urwallet.utils;

import co.urwallet.model.UrWallet;
import co.urwallet.model.Usuario;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Persistencia {
    private static final String RUTA_ARCHIVO_USUARIOS = "src/main/resources/co/urwallet/Persistencia/Usuarios.txt";
//    private static final String LOG = "D:\\Programacion II\\PRE-PARCIAL\\persistencia\\persistencia\\log\\actividadLog.txt";

    public static void cargarDatosArchivos(UrWallet urWallet) throws FileNotFoundException, IOException {
        //cargar archivo de users
        ArrayList<Usuario> usersCargados = cargarUsers();
        if(usersCargados.size() > 0)
            urWallet.getListaUsers().addAll(usersCargados);

    }
    
    public static void guardarUsuario(ArrayList<Usuario> listaUsers) throws IOException {
        // TODO Auto-generated method stub
        String contenido = "";
        for(Usuario usuarios:listaUsers)
        {
            contenido += usuarios.getIdUsuario()+","+usuarios.getCedula()+","+usuarios.getNombreCompleto()+","+usuarios.getTelefono()
                    +","+usuarios.getCorreo()+","+usuarios.getContrasena()+","+usuarios.getDireccion()+","+usuarios.getSaldoDispo()+"\n";
        }
        ArchivoUtils.guardarArchivo(RUTA_ARCHIVO_USUARIOS, contenido, false);
    }

    public static ArrayList<Usuario> cargarUsers() throws FileNotFoundException, IOException
    {
        ArrayList<Usuario> users =new ArrayList<Usuario>();
        ArrayList<String> contenido = ArchivoUtils.leerArchivo(RUTA_ARCHIVO_USUARIOS);
        String linea="";
        for (int i = 0; i < contenido.size(); i++)
        {
            linea = contenido.get(i);//juan,arias,125454,Armenia,uni1@,12454,125444
            Usuario Usuario = new Usuario();
            Usuario.setIdUsuario(linea.split(",")[0]);
            Usuario.setCedula(linea.split(",")[1]);
            Usuario.setNombreCompleto(linea.split(",")[2]);
            Usuario.setTelefono(linea.split(",")[3]);
            Usuario.setCorreo(linea.split(",")[4]);
            Usuario.setContrasena(linea.split(",")[5]);
            Usuario.setDireccion(linea.split(",")[6]);
            Usuario.setSaldoDispo(Float.valueOf(linea.split(",")[7]));
            users.add(Usuario);
        }
        return users;
    }
}
