import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class XMLStore{
    String Name; 
    int id = 0; 
    int MountC = 0;
    List<String> Columns = new ArrayList<String>();
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
public class Prueba {
    static List<XMLStore> ListStore = new ArrayList<XMLStore>();

    public static void CreateXML(String[] Words) throws Exception{
        String XMLStore = "C:\\Users\\HP\\Documents\\GitHub\\Proyecto_3\\" + Words[2];
        File file = new File(XMLStore);
        if (file.exists()){
            XMLStore Actual = null;
            String root = Words[2];
            int no = 1;
            int id = 0;
            for (XMLStore Search : ListStore){
                if (Search.Name.equals(Words[2])){
                    no = Search.MountC;
                    id = Search.id;
                    Search.id ++;
                    Actual = Search;
                    break;
                }
            }
            DocumentBuilderFactory documentBuilderFactory = 
            DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = 
            documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement(root);
            document.appendChild(rootElement);
            System.out.println("Logrado");
            for (int i = 1; i <= no; i++){
                String Element = Actual.Columns.get(i-1);
                String data = "";
                int Dataid = 3;
                for (int j=1;j <= no; j++){
                    if (Words[Dataid].equals(Element)){
                        break;
                    }
                    Dataid++;
                }
                if (Dataid+1+no != Words.length){
                    data = Words[1+Dataid+no];
                }

                Element em = document.createElement(Element);
                em.appendChild(document.createTextNode(data));
                rootElement.appendChild(em);
                System.out.println("Prueba");
            }
            System.out.println("Final");
            TransformerFactory transformerFactory = 
            TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(XMLStore + "\\"+Words[2]+"_"+String.valueOf(id)+".xml"));
            transformer.transform(source, result);
        }

    }
    public static void DeleteXML(String path){
        try {
        String folderpath = "C:\\Users\\HP\\Documents\\GitHub\\Proyecto_3\\" + path;
        File folder = new File(folderpath);
        if (folder.exists()){
            File[] files = folder.listFiles();
            for (File file: files){
                String value = ReadXML(folderpath+"\\"+file.getName(), "path");
            }
        }
        } catch (Exception e) {
           
        }
    }
    public static void DeleteAll(String Path){
        String folderpath = "C:\\Users\\HP\\Documents\\GitHub\\Proyecto_3\\" + Path;
        File folder = new File(folderpath);
        if (folder.exists()){
            File[] files = folder.listFiles();
            for (File file: files){
                file.delete();
            }
        }
    }
    public static String ReadXML(String file, String value){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document documento = builder.parse(new File(file));
            NodeList listavariables = documento.getElementsByTagName(value);
            
            String Result = "";
            for (int i=0; i<listavariables.getLength();i++){
                Node nodo = listavariables.item(i);
                if(nodo.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) nodo;
                    NodeList hijos = e.getChildNodes();
                    for (int j = 0; j< hijos.getLength();j++){
                        Node hijo = hijos.item(j);
                        if (hijo.getNodeType() == Node.ELEMENT_NODE){
                            Result = Result + hijo.getTextContent() + "/";
                        }
                    }
                }
            }
            return Result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public static void funtionDetection(String function) throws Exception{
        String[] Words = function.split(" ",0);
        if (Words[0].equals("INSERT") && Words[1].equals("INTO")){
            CreateXML(Words);
        }
        else if (Words[0].equals("DELETE") && Words[1].equals("FROM") && Words.length>3){

        }
        else if (Words[0].equals("DELETE") && Words[1].equals("FROM") && Words.length==3){
            DeleteAll(Words[2]);
            System.out.println("Ejecucion");
        }
    }
    public static void main(String[] args) throws Exception{
        XMLStore Prueba = new XMLStore("Prueba");
        Prueba.AddColumn("Nombre");
        Prueba.AddColumn("Apellido");
        ListStore.add(Prueba);
        
        //String function = "DELETE FROM Prueba";
        //funtionDetection(function);
        //function = "INSERT INTO Prueba Nombre Apellido VALUES 12 13";
        //funtionDetection(function);
        //function = "INSERT INTO Prueba Apellido Nombr VALUES 14 15";
        //function = "DELETE FROM Prueba";
        
    }
}
