import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

class XMLStore{
    String Name; 
    int id = 0; 
    List<String> Columns = new ArrayList<String>();
    int MountC;
    public XMLStore(String name)
   {
        this.Name = name;
   }
   public void AddColumn(String Column)
   {
        this.Columns.add(Column);
        this.MountC++;
   }
};
public class Main {
    static List<XMLStore> ListStore = new ArrayList<XMLStore>();

    public static void CreateXML(String[] Words) throws Exception{
        String XMLStore = "C:\\Users\\HP\\Documents\\GitHub\\Proyecto_3\\" + Words[2];
        File file = new File(XMLStore);
        if (file.exists()){
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter number to add elements in your XML file: ");
            String str = bf.readLine();
            int no = Integer.parseInt(str);
            System.out.print("Enter root: ");
            String root = bf.readLine();
            DocumentBuilderFactory documentBuilderFactory = 
            DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = 
            documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement(root);
            document.appendChild(rootElement);
            for (int i = 1; i <= no; i++){
                System.out.print("Enter the element: ");
                String element = bf.readLine();
                System.out.print("Enter the data: ");
                String data = bf.readLine();
                Element em = document.createElement(element);
                em.appendChild(document.createTextNode(data));
                rootElement.appendChild(em);
            }
            TransformerFactory transformerFactory = 
            TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(XMLStore + "\\Data.xml"));
            transformer.transform(source, result);
        }

    }
    public static void DeleteXML(){
        File file = new File("PruebaData.xml");
        if (file.exists()){
            file.delete();
        }
        else {
            System.out.println("No existe el archivo");
        }
    }
    public static void DeleteAll(){
        
    }
    public static void funtionDetection(String function) throws Exception{
        String[] Words = function.split(" ",0);
        if (Words[0].equals("INSERT") && Words[1].equals("INTO")){
            CreateXML(Words);
        }
    }
    public static void main(String[] args) throws Exception{
        XMLStore Prueba = new XMLStore("Prueba");
        Prueba.AddColumn("Nombre");
        Prueba.AddColumn("Apellido");
        ListStore.add(Prueba);

        String function = "INSERT INTO Prueba Apellido Nombre VALUES Vindas Jose";
        funtionDetection(function);
    }
}
