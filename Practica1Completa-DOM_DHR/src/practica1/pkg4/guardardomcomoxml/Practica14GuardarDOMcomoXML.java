/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package practica1.pkg4.guardardomcomoxml;

import java.io.File;
import javax.xml.transform.TransformerException;

/**
 *
 * @author David
 */
public class Practica14GuardarDOMcomoXML {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TransformerException {
        AccesoDOM a = new AccesoDOM();
        File f = new File("./archivos/Libros.xml");
        a.abrirXMLaDOM(f);
        a.recorreDOMyMuestra();
        a.insertarLibroEnDOM("Yerma", "Lorca", "1935");
        a.borrarNodo("Don Quijote");
        a.guardarDOMcomoArchivo("./archivos/LibrosDeDOM.xml");
    }
    
}
