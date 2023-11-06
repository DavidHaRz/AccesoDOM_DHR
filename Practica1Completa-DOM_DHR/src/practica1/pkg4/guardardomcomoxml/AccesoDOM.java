/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practica1.pkg4.guardardomcomoxml;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author David
 */
public class AccesoDOM {

    Document doc;

//codigo escrito en Practica1
public int abrirXMLaDOM(File f) {
        try {
            System.out.println("Abriendo archivo XML file y generando DOM....");

            //creamos nuevo objeto DocumentBuilder al que apunta la variable factory 
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            //ignorar comentarios y espacios blancos
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            //DocumentBuilder tiene el método parse que es el que genera DOM en memoria

            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(f);
            // ahora doc apunta al arbol DOM y podemos recorrerlo
            System.out.println("DOM creado con éxito.");
            return 0;//si el método funciona
        } catch (Exception e) {
            System.out.println(e);
            return -1;//if the method aborta en algún punto
        }
    }
//añade el nuevo método
    public void recorreDOMyMuestra() {
        int nlibros = 0;
        String[] datos = new String[3];//lo usamos para la información de cada libro
        Node nodo = null;
        Node root = doc.getFirstChild();
        NodeList nodelist = root.getChildNodes(); //(1)Ver dibujo del árbol
        //recorrer el árbol DOM. El 1er nivel de nodos hijos del raíz
        for (int i = 0; i < nodelist.getLength(); i++) {
            nodo = nodelist.item(i);//node toma el valor de los hijos de raíz
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {//miramos nodos de tipo Element
                Node ntemp = null;
                int contador = 1;
                //sacamos el valor del atributo publicado
                datos[0] = nodo.getAttributes().item(0).getNodeValue();
                //sacamos los valores de los hijos de nodo, Titulo y Autor
                NodeList nl2 = nodo.getChildNodes();//obtenemos lista de hijos (2)
                for (int j = 0; j < nl2.getLength(); j++) {//iteramos en esa lista 
                    ntemp = nl2.item(j);
                    if (ntemp.getNodeType() == Node.ELEMENT_NODE){
                        //para conseguir el texto de titulo y autor, se
                        //puede hacer con getNodeValue(), también con
                        //getTextContent() si es ELEMENT
                        datos[contador] = ntemp.getTextContent();
                        // también datos[contador]=ntemp.getChildNodes().item(0).getNodeValue();		
                        contador++;
                    }
                }
                nlibros++;
                //el array de String datos[] tiene los valores que necesitamos
                System.out.println("\nLibro " + nlibros + ":" + "\n\tFecha de publicación: " + datos[0] + "\n\tAutor: " + datos[2] + "\n\tTítulo: " + datos[1]);
            }

        }
    }
    
    public int insertarLibroEnDOM (String titulo, String autor, String fecha) {
        try {
            System.out.println("Añadir libro al árbol DOM: " + titulo + ";" + autor + ";" + fecha);
            //Crea los nodos -> los añade al padre desde las hojas a la raíz
            //CREA TÍTULO con el texto en medio
            
            Node nTitulo = doc.createElement("Titulo"); //Crea etiquetas <Titulo>...</Titulo>
            Node nTitulo_text = doc.createTextNode(titulo); //Crea el nodo texto para el Titulo
            nTitulo.appendChild(nTitulo_text); //Añade el titulo a las etiquetas <Titulo>titulo</Titulo>            
            //CREA AUTOR
            //Otra manera de hacerlo
            //Node nAutor=doc.createElement("Autor").appendChild(doc.createTextNode(autor));
                
            Node nAutor = doc.createElement("Autor");
            Node nAutor_text = doc.createTextNode(autor);
            nAutor.appendChild(nAutor_text);
            
            //CREA LIBRO con atributo y nodos Titulo y Autor
            Node nLibro = doc.createElement("Libro");
            ((Element)nLibro).setAttribute("publicado", fecha);
            nLibro.appendChild(nTitulo);
            nLibro.appendChild(nAutor);

            nLibro.appendChild(doc.createTextNode("\n")); //Para insertar saltos de línea
            
            Node raiz = doc.getFirstChild(); //tb.doc.getChildNodes().item(0)
            raiz.appendChild(nLibro);
            System.out.println("Libro insertado en DOM");
            return 0;
        }catch (Exception e){
            System.out.println(e);
            return -1;
        }
    }
    
    public int borrarNodo(String tit) {
        System.out.println("Buscando el Libro " + tit + " para borrarlo");
            try{
                //Node root = doc.getFirstChild();
                Node raiz = doc.getDocumentElement();
                NodeList nl1 = doc.getElementsByTagName("Titulo");
                Node n1 = null;
                for (int i = 0; i < nl1.getLength(); i++) 
                    n1 = nl1.item(i);
                
            if (n1.getNodeType() == Node.ELEMENT_NODE) {  //Redundante por getElementsByTabName, no lo es si buscamos getChildNodes()
                if (n1.getChildNodes().item(0).getNodeValue().equals(tit)){
                    System.out.println("Borrando el nodo <Libro> con títulos " + tit);
                    //n1.getParentNode().removeChild(n1);
                    //Borra <Titulo> tit </Titulo>, pero deja Libro y Autor
                    n1.getParentNode().getParentNode().removeChild(n1.getParentNode());
                }
            }
                return 0;
            } catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
                return -1;
            }
    }
    
    //Crea un nuevo archivo xml del DOM en memoria
    void guardarDOMcomoArchivo(String nuevoArchivo) throws TransformerConfigurationException, TransformerException {
        try {
            Source src = new DOMSource(doc);  //Definidos el origen
            StreamResult rst = new StreamResult(new File(nuevoArchivo));  //Definimos el resultado
            //Declaramos el Transformer que tiene el método .transform() que necesitamos.
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            
            //Opción para indentar el archivo
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            transformer.transform(src, (javax.xml.transform.Result) rst);
            System.out.println("Archivo creado del DOM con éxito\n");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

