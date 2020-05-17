import jdk.swing.interop.SwingInterOpUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.LSOutput;
import org.xml.sax.SAXException;

import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import javax.crypto.spec.PSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Dictionary{


    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        HashMap<String, String> Dictionary = new HashMap<String, String>();
        ArrayList<LookUpHistory> listLUWord = null;//list looked up words
        ArrayList<String> listFavor = null;
        listLUWord=importFile();
        Scanner input = new Scanner(System.in);
        boolean flag=true;
        int choose;
        do {
            while (flag == true) {
                System.out.println("TRA CUU TU DIEN");
                System.out.println("1. Tra cuu tu dien Anh-Viet");
                System.out.println("2. Tra cuu tu dien Viet-Anh");
                System.out.println("3. Thong ke tan suat tra cuu");
                System.out.println("0. Thoat chuong trinh");
                System.out.print("Nhap lua chon: ");
                choose = Integer.parseInt(input.nextLine());
                if(choose>=0&&choose<=3)
                {
                    switch (choose)
                    {
                        case 1:
                            readXML(Dictionary,"Anh_Viet.xml");
                            showMenu(Dictionary,listLUWord);
                            writeXML(Dictionary,"Anh_Viet.xml");
                            break;
                        case 2:
                            readXML(Dictionary,"Viet_Anh.xml");
                            showMenu(Dictionary,listLUWord);
                            writeXML(Dictionary,"Viet_Anh.xml");
                            break;
                        case 3:
                            listLUWord=importFile();
                            Statistic(listLUWord);
                            break;
                        case 0:
                            flag=false;
                            break;
                    }
                }
            }
        }while (flag==true);
    }


    //read file xml
    public static void readXML(HashMap<String, String> AVHashMap, String filepath) throws ParserConfigurationException, IOException, SAXException
    {
        File xmlFile = new File(filepath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try{
            dBuilder= dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList NList = doc.getElementsByTagName("record");
            ArrayList<AnhVietList> avList = new ArrayList<>();
            for (int i = 0; i < NList.getLength(); i++) {
                avList.add(getAVList(NList.item(i)));
            }
            for (AnhVietList avlist : avList) {
                AVHashMap.put(avlist.getWord(),avlist.getMeaning());
            }
        }catch(SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }
    }
    //function get AVList
    private static AnhVietList getAVList(Node node)
    {
        //XMLReaderDOM domReader = new XMLReaderDOM();
        AnhVietList avList = new AnhVietList();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            avList.setWord(getTagValue("word", element));
            avList.setMeaning(getTagValue("meaning", element));
        }
        return avList;
    }
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
    //write file xml
    public static void writeXML(HashMap<String, String> AVHashMap, String filepath)
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try{
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            //add elements to Document
            Element root = doc.createElement("dictionary");
            doc.appendChild(root);
            for(String key : AVHashMap.keySet())
            {
                Element record = doc.createElement("record");
                root.appendChild(record);
                Element word = doc.createElement("word");
                word.appendChild(doc.createTextNode(key));
                record.appendChild(word);
                Element meaning = doc.createElement("meaning");
                meaning.appendChild(doc.createTextNode(AVHashMap.get(key)));
                record.appendChild(meaning);
            }
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(filepath));
            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
    //read file favourite list
    public static void readTXT(ArrayList<String> dsyt) throws IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("./dsyeuthich.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                dsyt.add(str);
                //Or split your read string here as you wish.
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
    }
    //write file favourite list
    public static void writeTXT(ArrayList<String> dsyt) throws IOException {
        BufferedWriter writer = null;
        try{
            File txt = new File("dsyeuthich.txt");
            writer = new BufferedWriter(new FileWriter(txt));
            for(String str : dsyt)
            {
                writer.write(str);
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }
    }
    //import tu file csv
    public  static ArrayList<LookUpHistory> importFile() throws IOException
    {
        ArrayList<LookUpHistory> listLUWord = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try{
            FileReader fr = new FileReader("./thongke.csv");
            bufferedReader = new BufferedReader(fr);
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                String[] temp = line.split(",");
                LocalDate date = LocalDate.parse(temp[0]);
                String word = temp[1];
                int count = Integer.parseInt(temp[2]);
                listLUWord.add(new LookUpHistory(date,word,count));
            }
        }catch (IOException exc)
        {
            System.out.println("Error open file.");
        }
        bufferedReader.close();
        return listLUWord;
    }
    //export ra file csv
    public static void exportFile(ArrayList<LookUpHistory> listLUWord) throws IOException
    {
        BufferedWriter bufferedWriter = null;
        try{
            FileWriter fw = new FileWriter("./thongke.csv");
            bufferedWriter = new BufferedWriter(fw);

        }catch(IOException exc)
        {
            System.out.println("Error open file !");
            return;
        }
        try{
            for(LookUpHistory list : listLUWord)
            {
                bufferedWriter.write(list.getLookUpDate().toString());
                bufferedWriter.write(",");
                bufferedWriter.write(list.getLookUpWord());
                bufferedWriter.write(",");
                bufferedWriter.write(Integer.toString(list.getCount()));
                bufferedWriter.write("\n");
            }
        }catch (IOException exc)
        {
            System.out.println("Error write file !");
            return;
        }
        finally {
            bufferedWriter.close();
        }
    }
    //show menu
    public static void showMenu(HashMap<String,String> Dic, ArrayList<LookUpHistory> listLUWord) throws IOException {

        Scanner input = new Scanner(System.in);
        int choose;
        boolean flag=true;
        do{
                System.out.println("1. Tra cuu tu");
                System.out.println("2. Them tu moi(cung voi nghia) vao tu dien");
                System.out.println("3. Xoa mot tu(cung voi nghia) ra khoi tu dien");
                System.out.println("0. Quay lai");
                System.out.print("Nhap lua chon: ");
                choose=Integer.parseInt(input.nextLine());
                switch (choose){
                    case 1:
                        searchWord(Dic,listLUWord);
                        exportFile(listLUWord);
                        break;
                    case 2:
                        addWord(Dic);
                        break;
                    case 3:
                        removeWord(Dic);
                        break;
                    case 4:

                    case 0:
                        flag=false;
                        break;
                }
        }while (flag==true);
    }
    //search a word from dictionary
    public static void searchWord(HashMap<String,String> Dic, ArrayList<LookUpHistory> listLUWord) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.print("Nhap tu can tra: ");
        String tmp = input.nextLine();
        if(Dic.containsKey(tmp)){
            System.out.println("Tu: " + tmp);
            System.out.println("Nghia: "+ Dic.get(tmp));
            System.out.println("");
            //update times looked up words
            LocalDate current = LocalDate.now();
            if(listLUWord.isEmpty())
            {
                listLUWord.add(new LookUpHistory(current,tmp,1));
            }
            else {
                if(listLUWord.toString().contains(tmp)) {
                    for (int i = 0; i < listLUWord.size(); i++) {
                        if (listLUWord.get(i).getLookUpWord().equals(tmp)) {
                            if (listLUWord.get(i).getLookUpDate().isEqual(current)) {
                                int count = listLUWord.get(i).getCount();
                                count++;
                                listLUWord.get(i).setCount(count);
                            } else {
                                int count = listLUWord.get(i).getCount();
                                count++;
                                listLUWord.get(i).setCount(count);
                            }
                        }
                    }
                }
                else
                    listLUWord.add(new LookUpHistory(current,tmp,1));
            }
            System.out.print("Ban co muon luu tu \"" + tmp + "\" vao danh sach yeu thich khong?(y/n) ");
            String yn = input.next();
            System.out.println(yn);
            if(yn.equals("y"))
            {
                ArrayList<String> dsyt = new ArrayList<>();
                readTXT(dsyt);
                if(dsyt.contains(tmp))
                    System.out.println("Tu da ton tai trong danh sach!");
                else
                {
                    dsyt.add(tmp);
                    Collections.sort(dsyt);
                    writeTXT(dsyt);
                    System.out.println("Luu vao danh sach yeu thich thanh cong!");
                }
            }
            else
            {
                System.out.println("Luu khong thanh cong!");
            }
        }
        else
            System.out.println("Khong tim thay tu can tra!");
    }
    //add a word to dictionary
    public static void addWord(HashMap<String,String> Dic){
        Scanner input = new Scanner(System.in);
        System.out.print("Nhap tu can them: ");
        String word = input.nextLine();
        if(Dic.containsKey(word))
        {
            System.out.println("Tu da ton tai!");
        }
        else
        {
            System.out.println("Nhap nghia cua tu can them: ");
            String meaning = input.nextLine();
            Dic.put(word,meaning);
            System.out.println("Them thanh cong!");
        }
    }
    //remove a word from dictionary
    public static void removeWord(HashMap<String,String> Dic){
        Scanner input =new Scanner(System.in);
        System.out.print("Nhap tu can xoa khoi tu dien: ");
        String word = input.nextLine();
        if(Dic.containsKey(word))
        {
            System.out.print("Ban co chac muon xoa tu " + word + " khoi tu dien (y/n): ");
            String tmp = input.nextLine();
            if(tmp.equals("y")) {
                Dic.remove(word);
                System.out.println("Xoa tu thanh cong!");
            }
            else{
                System.out.println("Xoa khong thanh cong!");
            }
        }
        else
        {
            System.out.println("Tu khong ton tai!");
        }
    }
    //Statistics frequency lookup of words
    public static void Statistic(ArrayList<LookUpHistory> listLUWord)
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Nhap ngay bat dau(yyyy-mm-dd): ");
        LocalDate date1 = LocalDate.parse(input.nextLine());
        System.out.print("Nhap ngay ket thuc(yyyy-mm-dd): ");
        LocalDate date2 = LocalDate.parse(input.nextLine());
        HashMap<String,Integer> LookedUpWord = new HashMap<String, Integer>();
        System.out.println("-------------------------------------------THONG KE TAN SUAT CAC TU DA TRA-------------------------------------------");
        System.out.format("%30s | ", "Tu");
        System.out.format("%20s | ", date1);
        System.out.format("%20s | \n", date2);
        for(LookUpHistory statis : listLUWord)
        {
            if((statis.getLookUpDate().isEqual(date1)||statis.getLookUpDate().isAfter(date1))
            && (statis.getLookUpDate().isEqual(date2)|| statis.getLookUpDate().isBefore(date2)))
            {
                if(LookedUpWord.isEmpty())
                    LookedUpWord.put(statis.getLookUpWord(),statis.getCount());
                else
                {
                    if(LookedUpWord.containsKey(statis.getLookUpWord()))
                    {
                        LookedUpWord.replace(statis.getLookUpWord(),LookedUpWord.get(statis.getLookUpWord()),
                                LookedUpWord.get(statis.getLookUpWord())+statis.getCount());
                    }
                    else
                        LookedUpWord.put(statis.getLookUpWord(),statis.getCount());
                }
                System.out.format("%30s | ", statis.getLookUpWord());
                System.out.format("%20s   ", LookedUpWord.get(statis.getLookUpWord())+"(lan)");
                System.out.format("%20s | \n","");
            }
        }
    }
}
