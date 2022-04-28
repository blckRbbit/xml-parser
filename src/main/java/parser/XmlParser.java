package parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import util.Util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 * В классе реализована логика парсинга данных из xml-файла.
 */
public class XmlParser {
    private static final Logger LOGGER = Logger.getLogger(XmlParser.class.getName());

    /**
     *
     * @param path - путь к файлу
     * @param tag - указатель результирующей сущности, которую необходимо распарсить
     * @return список массивов строк, содержащие все необходимые поля сущности, разделенные сепараторами
     * @throws ParserConfigurationException - обрабатывается в методе-обертке getRows()
     * @throws IOException - обрабатывается в методе-обертке getRows()
     * @throws SAXException - обрабатывается в методе-обертке getRows()
     */
    private static List<String[]> parse(String path, String tag) throws ParserConfigurationException, IOException, SAXException, DataFormatException {
        File xmlFile = new File(path);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFile);
        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName(tag);
        if (nodeList.getLength()==0) {
            LOGGER.log(Level.WARNING, "Такого тэга нет в документе!");
            throw new DataFormatException("Такого тэга нет в документе!");
        }
        Set<String> insertions = parseXML(nodeList);

        List<String[]> rows = new ArrayList<>();
        for (String s : insertions) {
            rows.add(s.split(Util.getSeparator()));
        }
        LOGGER.log(Level.INFO, "Парсинг файла успешно завершен...");
        return rows;
    }

    /**
     * Метод ищет необходимые данные по тегу в NodeList, парсит их в строки, которые добавляет в результирующий Set<String> insertions.
     * @param nodeList - аргумент, инстанс сущности-узла из xml-файла
     * @return - Set<String> insertions, коллекция извлеченных из xml-файла строк.
     */
    private static Set<String> parseXML(NodeList nodeList) {
        Set<String> insertions = new TreeSet<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String movieTitle =  element.getElementsByTagName("MovieTitle").item(0).getTextContent();
            String movieDirector =  element.getElementsByTagName("MovieDirector").item(0).getTextContent();
            String releaseYear =  element.getElementsByTagName("ReleaseYear").item(0).getTextContent();
            String separator = Util.getSeparator();
            insertions.add(String.format("%s%s%s%s%s", movieTitle, separator, releaseYear, separator, movieDirector));
        }
        return insertions;
    }

    /**
     * Метод-обертка над parse
     * @param path - путь к xml-файлу, который нужно распарсить
     * @param tag - тэг в xml-файле, который и является нужной сущностью (представленной таблицей в БД)
     * @return List<String> result - строка со всеми необходимыми полями результирующей сущности, разделенными сепаратором.
     * @throws ParserConfigurationException может произойти, если файл поврежден.
     * @throws IOException может произойти, если файл не найден, или поток чтения по какой-то причине был закрыт.
     * @throws SAXException - ошибка парсинга, возбуждаемая исключениями класса SaxParser
     */
    public static List<String[]> getRows(String path, String tag) {
        List<String[]> result;
        try {
            result = parse(path, tag);
            return result;
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.WARNING, "Ошибка синтаксиса в xml-файле.");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Файл не найден. Проверьте правильность пути.");
        } catch (SAXException e) {
            LOGGER.log(Level.WARNING, "Ошибка при парсинге файла.");
        } catch (DataFormatException e) {
            LOGGER.log(Level.WARNING, "Такого тэга нет в документе!");
        }
        return null;
    }
}
