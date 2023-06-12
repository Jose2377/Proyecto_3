import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Nodo para algoritmo de huffman
class Node_Huffman  
{  
    // Caracter
    Character ch;  
    // Frecuencia
    Integer Map;  
    //Hijos del arbol
    Node_Huffman left = null;   
    Node_Huffman right = null;   
    //Constructores 
    Node_Huffman(Character ch, Integer Map)  
    {  
        this.ch = ch;  
        this.Map = Map;  
    }    
    public Node_Huffman(Character ch, Integer Map, Node_Huffman left, Node_Huffman right)  
    {  
        this.ch = ch;  
        this.Map = Map;  
        this.left = left;  
        this.right = right;  
    }  
}  

// Nodo para informacion de XMLStore
class XMLStore{
    // Imformacion de las listas
    String Name; 
    int MountC = 0;
    List<String> Columns = new ArrayList<String>();
    // Constructor
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

    public static void Huffman(String text)  
    {  
        //Por si no hay texto
        if (text == null || text.length() == 0)   
        {  
            return;  
        }  
        //Con un map, contar la cantidad de veces que aparece las letras 
        Map<Character, Integer> Map = new HashMap<>();  
        for (char c: text.toCharArray())   
        {  
            //Guardar la cantidad de veces que aparece las letras
            Map.put(c, Map.getOrDefault(c, 0) + 1);  
        }  
        //Con una cola se almacene los nodos actuales del árbol de Huffman con su prioridad.  
        PriorityQueue<Node_Huffman> pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.Map));  
        //loop iterate over the Map and returns a Set view of the mappings contained in this Map  
        for (var entry: Map.entrySet())   
        {  
            //Se crea un nodo hoja y lo agrega a la cola
            pq.add(new Node_Huffman(entry.getKey(), entry.getValue()));  
        }  
        //Mientras el tamano de la raiz se mayor a 1
        while (pq.size() != 1)  
        {  
            //Se combinan los nodos
            Node_Huffman left = pq.poll();  
            Node_Huffman right = pq.poll();  
            //Se crea un nuevo nodo. Agregue el nuevo nodo a la cola de prioridad.
            //Y sumamos la frecuencia
            int sum = left.Map + right.Map;  
            //Se agrega ese nuevo nodo
            pq.add(new Node_Huffman(null, sum, left, right));  
        }  
        //La raíz almacena el puntero a la raíz del árbol de Huffman
        Node_Huffman root = pq.peek();  
        //Rastrear el árbol de Huffman y almacenar los códigos de Huffman en un mapa
        Map<Character, String> huffmanCode = new HashMap<>();  
        encodeData(root, "", huffmanCode);  
        //Los codigos huffman
        System.out.println("Codigos: " + huffmanCode);  
        //Creando una instancia StringBuilder class   
        StringBuilder sb = new StringBuilder();  
        //For para cada caracter
        for (char c: text.toCharArray())   
        {  
            //Imprime una cadena codificada
            sb.append(huffmanCode.get(c));  
        }   
        System.out.println("Codificado: " + sb);  
        System.out.print("Contraseña: ");  
        if (Hoja(root))  
        {  
            //En caso de solo haber una variable como a, aa, aaa, etc.  
            while (root.Map-- > 0)   
            {  
                System.out.print(root.ch);  
            }  
        }  
        else   
        {  
            //Atraviesa el árbol de Huffman nuevamente y decodifica la cadena codificada 
            int index = -1;  
            while (index < sb.length() - 1)   
            {  
                index = decodeData(root, index, sb);  
            }  
        }  
    }  
    // Codifica la contraseña
    public static void encodeData(Node_Huffman root, String str, Map<Character, String> huffmanCode)  
    {  
        if (root == null)   
        {  
            return;  
        }  
        // Comprueba si el nodo es un nodo hoja o no
        if (Hoja(root))   
        {  
            huffmanCode.put(root.ch, str.length() > 0 ? str : "1");  
        }  
        encodeData(root.left, str + '0', huffmanCode);  
        encodeData(root.right, str + '1', huffmanCode);  
    }  
    //Atraviesa el árbol de Huffman y decodificar la función de cadena codificada que decodifica los datos codificados
    public static int decodeData(Node_Huffman root, int index, StringBuilder sb)  
    {  
        //checks if the root node is null or not  
        if (root == null)   
        {  
            return index;  
        }   
        //checks if the node is a leaf node or not  
        if (Hoja(root))  
        {  
            System.out.print(root.ch);  
            return index;  
        }  
        index++;   
        root = (sb.charAt(index) == '0') ? root.left : root.right;  
        index = decodeData(root, index, sb);  
        return index;  
    }  
    //function to check if the Huffman Tree contains a single node  
    public static boolean Hoja(Node_Huffman root)   
    {  
        //returns true if both conditions return ture  
        return root.left == null && root.right == null;  
    }  

    // Funcion principal para el INSERT
    public static void CreateXML(String[] Words){
        try {
        // Verifica que si el XMLStore que queremos existe
            String XMLStore = "C:\\Users\\HP\\Documents\\GitHub\\Proyecto_3\\" + Words[2];
            File file = new File(XMLStore);
            if (file.exists()){ 
            // Iniciando variables de informacion
            XMLStore Actual = null;
            String root = Words[2];
            int no = 1;
            int id = 0;
            // Si existe empezara a buscar informacion del XML
            for (XMLStore Search : ListStore){
                if (Search.Name.equals(Words[2])){ //Si coincide el nombre, guarda informacion
                    no = Search.MountC;
                    Actual = Search;
                    break;
                }
            }
            // Iniciadores para construir el archivo
            DocumentBuilderFactory documentBuilderFactory = 
            DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = 
            documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement(root);
            document.appendChild(rootElement);
            // Busca el orden de las columnas donde se quiere agregar informacion
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
                // En caso de encontrar una columna equivalente, tomar la informacion, caso contrario vacio
                if (Dataid+1+no != Words.length){
                    data = Words[1+Dataid+no];
                }
                
                // Se agrega la columna
                Element em = document.createElement(Element);
                em.appendChild(document.createTextNode(data));
                rootElement.appendChild(em);
            }
            // Construccion final
            TransformerFactory transformerFactory = 
            TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            
            file = new File(XMLStore + "\\"+Words[2]+"_"+String.valueOf(id)+".xml");
            while(file.exists()){
                id++;
                file = new File(XMLStore + "\\"+Words[2]+"_"+String.valueOf(id)+".xml");
            }
            StreamResult result = new StreamResult(new File(XMLStore + "\\"+Words[2]+"_"+String.valueOf(id)+".xml"));
            transformer.transform(source, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error auxiliar INSERT");
        }
    }
    // Funcion auxiliar para el UPDATE
    public static void EditXML(File file,String[] Words,String[] Values){
        try{
        // Verifica que si el XMLStore que queremos existe
        String XMLStore = "C:\\Users\\HP\\Documents\\GitHub\\Proyecto_3\\" + Words[1];
        if (file.exists()){ 
            String name = file.getName();
            file.delete();
            // Iniciando variables de informacion
            XMLStore Actual = null;
            String root = Words[1];
            int no = 1;
            // Si existe empezara a buscar informacion del XML
            for (XMLStore Search : ListStore){
                if (Search.Name.equals(Words[1])){ //Si coincide el nombre, guarda informacion
                    no = Search.MountC;
                    Actual = Search;
                    break;
                }
            }
            // Iniciadores para construir el archivo
            DocumentBuilderFactory documentBuilderFactory = 
            DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = 
            documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement(root);
            document.appendChild(rootElement);
            // Busca el orden de las columnas donde se quiere agregar informacion
            for (int i = 1; i <= no; i++){
                String Element = Actual.Columns.get(i-1);
                String data = "";
                try {
                    data = Values[i-1];
                } catch (Exception e) {
                    
                }
                
                // Se agrega la columna
                Element em = document.createElement(Element);
                em.appendChild(document.createTextNode(data));
                rootElement.appendChild(em);
            }
            // Construccion final
            TransformerFactory transformerFactory = 
            TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(XMLStore + "\\" + name));
            transformer.transform(source, result);
        }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error auxiliar UPDATE");
        }

    }

    // Funcion para el DELETE FROM WHERE
    public static void DeleteXML(String[] path){
        try {
            // Verifica que si el XMLStore que queremos existe
            String folderpath = "C:\\Users\\HP\\Documents\\GitHub\\Proyecto_3\\" + path[2];
            File folder = new File(folderpath);
            if (folder.exists()){
                File[] files = folder.listFiles();
                XMLStore Actual = null;
                for (XMLStore Search : ListStore){
                    if (Search.Name.equals(path[2])){
                        Actual = Search;
                        break;
                    }
                }
                for (File file: files){
                    String value = ReadXML(folderpath+"\\"+file.getName(), path[2]);
                    String[] values = value.split("/",0);
                    int Dataid = 0;
                    for (int j=0;j < Actual.MountC; j++){
                        if (path[4].equals(Actual.Columns.get(j))){
                            break;
                        }
                        Dataid++;
                    }
                    if (path[5].equals("=")){
                        if (values[Dataid].equals(path[6])){
                            file.delete();
                        }
                    }
                    else if(path[5].equals("<")){
                        if (Integer.parseInt(values[Dataid]) < Integer.parseInt(path[6]) ){
                            file.delete();
                        }
                    }
                    else if(path[5].equals(">")){
                        if (Integer.parseInt(values[Dataid]) > Integer.parseInt(path[6]) ){
                            file.delete();
                        }
                    }
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error DELETE XML");
        }
    }
    public static void Update(String[] path){
        try {
            // Verifica que si el XMLStore que queremos existe
            String folderpath = "C:\\Users\\HP\\Documents\\GitHub\\Proyecto_3\\" + path[1];
            File folder = new File(folderpath);
            if (folder.exists()){
                File[] files = folder.listFiles();
                XMLStore Actual = null;
                for (XMLStore Search : ListStore){
                    if (Search.Name.equals(path[1])){
                        Actual = Search;
                        break;
                    }
                }
                int info = 0;
                int i = 3;
                while(true){
                        if (path[i].equals("WHERE")){
                            break;
                        }
                        info++;
                        i++;
                        if (info>10){
                            break;
                        }
                    }
                info = (int) info/3;
                System.out.println(info);
                for (File file: files){
                    String value = ReadXML(folderpath+"\\"+file.getName(), path[1]);
                    String[] values = value.split("/",0);
                    for (int a = 0; a < info;a++){
                    int Dataid = 0;
                    boolean Found = false;
                    for (int j=0;j < Actual.MountC; j++){
                        if (path[3].equals(Actual.Columns.get(j))){
                            Found = true;
                            break;
                        }
                        Dataid++;
                    }
                    if (Found == true){
                    if (path[5+3*info].equals("=")){
                        if (values[Dataid].equals(path[6+3*info])){
                            values[Dataid] = path[5+3*a];
                        }
                    }
                    else if(path[5+3*info].equals("<")){
                        if (Integer.parseInt(values[Dataid]) < Integer.parseInt(path[6+3*info]) ){
                            values[Dataid] = path[5+3*a];
                        }
                    }
                    else if(path[5+3*info].equals(">")){
                        if (Integer.parseInt(values[Dataid]) > Integer.parseInt(path[6+3*info]) ){
                            values[Dataid] = path[5+3*a];
                        }
                    }
                    }
                }
                    EditXML(file,path,values);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error UPDATE");
        }
    }
    // Funcion para el DELETE FROM *
    public static void DeleteAll(String Path){
        try {
        // Verifica que si el XMLStore que queremos existe
        String folderpath = "C:\\Users\\HP\\Documents\\GitHub\\Proyecto_3\\" + Path;
        File folder = new File(folderpath);
        if (folder.exists()){ // Si el XMLStore existe, borrara todos los archivos
            File[] files = folder.listFiles();
            for (File file: files){
                file.delete();
            }
        }
        } catch (Exception e) {
            
        }
    }
    public static void SelectAll(String Path){
        try {
        // Verifica que si el XMLStore que queremos existe
        String folderpath = "C:\\Users\\HP\\Documents\\GitHub\\Proyecto_3\\" + Path;
        File folder = new File(folderpath);
        if (folder.exists()){ // Si el XMLStore existe, borrara todos los archivos
            File[] files = folder.listFiles();
            String text = "";
            for (File file: files){
                text = text + ReadXML(folderpath+"\\"+file.getName(), Path);
            }
            String[] value = text.split("/",0);
            Create(Path, value);
        }
        } catch (Exception e) {
            
        }
    }
    // Funcion auxliar SELECT ALL
    public static void Create(String path, String[] values){
        try {
        // Verifica que si el XMLStore que queremos existe
            String XMLStore = "C:\\Users\\HP\\Documents\\GitHub\\Proyecto_3\\" + path;
            File file = new File(XMLStore);
            if (file.exists()){ 
            // Iniciando variables de informacion
            XMLStore Actual = null;
            int no = 1;
            // Si existe empezara a buscar informacion del XML
            for (XMLStore Search : ListStore){
                if (Search.Name.equals(path)){ //Si coincide el nombre, guarda informacion
                    no = Search.MountC;
                    Actual = Search;
                    break;
                }
            }
            // Iniciadores para construir el archivo
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Datos");
            doc.appendChild(rootElement);
            // Busca el orden de las columnas donde se quiere agregar informacion
            for (int j = 0; j < (int) values.length/no; j++){
                Element staff = doc.createElement("Prueba");
                rootElement.appendChild(staff);
                for (int i = 1; i <= no; i++){
                    String Element = "";
                    String data = "";
                    try {
                        Element = Actual.Columns.get(i-1);
                        System.out.println(Element);
                        data = values[i-1+(j*no)];
                        System.out.println(data);
                    } catch (Exception e) {
                    
                    }
                    Element name = doc.createElement(Element);
                    name.setTextContent(data);
                    staff.appendChild(name);
                }
            }
            // Construccion final
            TransformerFactory transformerFactory = 
            TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("Datos.xml"));
            transformer.transform(source, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error auxiliar SELECT ALL");
        }
    }
    // Funcion que lee la informacion de nodos de un XML
    public static String ReadXML(String file, String value){
        try{
            // Se crea un DocumentBuilderFactory para leer
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(new File(file));
            NodeList listavariables = documento.getElementsByTagName(value); //Buscamos por cada nodo

            String Result = "";
            //Buscamos por cada nodo, y subnodos, esta encontrar el elemento que queremos
            for (int i=0; i<listavariables.getLength();i++){
                Node nodo = listavariables.item(i);
                if(nodo.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) nodo;
                    NodeList hijos = e.getChildNodes();
                    for (int j = 0; j< hijos.getLength();j++){
                        Node hijo = hijos.item(j);
                        //Cuando lo encontramos, devuelve el resultado
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
        else if (Words[0].equals("SELECT") && Words[1].equals("*")){
            SelectAll(Words[3]);
        }
        else if (Words[0].equals("UPDATE") && Words[2].equals("SET")){
            Update(Words);
        }
        else if (Words[0].equals("DELETE") && Words[1].equals("FROM") && Words.length>3){
            DeleteXML(Words);
        }
        else if (Words[0].equals("DELETE") && Words[1].equals("FROM") && Words.length==3){
            DeleteAll(Words[2]);
        }
        else if (Words[0].equals("HUFFMAN")){
            Huffman(Words[1]);
        }
        else {
            System.out.println("Comando no reconocido");
        }
    }
    public static void main(String[] args) throws Exception{
        String function = "HUFFMAN Prueba";
        //funtionDetection(function);
        
        XMLStore Prueba = new XMLStore("Prueba");
        Prueba.AddColumn("Nombre");
        Prueba.AddColumn("Apellido");
        ListStore.add(Prueba);
        
        function = "SELECT * FROM Prueba";
        funtionDetection(function);
        /*
        function = "INSERT INTO Prueba Nombre Apellido VALUES 17 12";
        funtionDetection(function);
        function = "INSERT INTO Prueba Nombre Apellido VALUES 16 12";
        funtionDetection(function);
        function = "INSERT INTO Prueba Nombre Apellido VALUES 16 12";
        funtionDetection(function);

        function = "INSERT INTO Prueba Apellido Nombre VALUES 14 15";
        funtionDetection(function);
        function = "INSERT INTO Prueba Apellido Nombre VALUES 16 17";
        funtionDetection(function);
        function = "DELETE FROM Prueba WHERE Nombre < 10";
        funtionDetection(function);
        */
    }
}
