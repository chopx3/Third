package webapp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class Uploader extends HttpServlet

{

    private static final String UPLOAD_DIRECTORY = "upload";
    private static final int THRESHOLD_SIZE = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<Rows> outputRows = new ArrayList<>();
        JSONArray errors = new JSONArray();
        String outputString = "";
        if (!ServletFileUpload.isMultipartContent(request)) {
            PrintWriter writer = response.getWriter();
            writer.println("Bad request");
            writer.flush();
            return;
        } else {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(THRESHOLD_SIZE);
            factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setFileSizeMax(MAX_FILE_SIZE);
            upload.setSizeMax(MAX_REQUEST_SIZE);
            Random rn = new Random();
            String RandomNum =  Integer.toString(rn.nextInt(100));
            // constructs the directory path to store upload file
            String uploadPath = getServletContext().getRealPath("")
                    + File.separator + UPLOAD_DIRECTORY;
// creates the directory if it does not exist
            try {
                // parses the request's content to extract file data
                List formItems = upload.parseRequest(request);
                Iterator iter = formItems.iterator();
                // iterates over form's fields
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        String fileName = RandomNum + item.getName();
                        String WhichFormat = fileName.substring(fileName.lastIndexOf(".")+1);
                        String filePath = uploadPath + File.separator +  fileName;
                        File storeFile = new File(filePath);
                        item.write(storeFile);
                        FileInputStream fIP = new FileInputStream(storeFile);
                        ArrayList<String> lines = new ArrayList<>();

                        String txtfilePath = getServletContext().getRealPath("") + File.separator;
                        File fileWithCategories = new File(txtfilePath+"/fortest.txt");
                        try{
                            lines = get_arraylist_from_file(fileWithCategories);
                        }
                        catch(Exception e){
                            System.out.println("error");
                        }
                        //Выбор формата файла, XLSX или XLS
                        Workbook workbook;
                        Sheet sheet;
                        if (WhichFormat.equals("xlsx")) {
                            //Получили файл, записали в InputStream
                            workbook = new XSSFWorkbook(fIP);
                            sheet = workbook.getSheetAt(0);
                        }
                        else
                        {
                            workbook = new HSSFWorkbook(fIP);
                            sheet = workbook.getSheetAt(0);
                        }
                        //Пробег по категориям и поиск ошибки
                        for (int j=1;j<sheet.getPhysicalNumberOfRows();j++)
                        {
                            StringBuilder lineWithError = new StringBuilder();
                            for (int i = 3; i < 7; i++)
                            {
                                if (sheet.getRow(j).getCell(i) != null)
                                {
                                    if (!sheet.getRow(j).getCell(i).getStringCellValue().replaceAll(" ","").equals(""))
                                    {
                                        lineWithError.append(sheet.getRow(j).getCell(i).getStringCellValue());
                                        lineWithError.append("/");
                                    }
                                }
                            }
                            //формирование строк с ошибками
                            String completeLine = "";
                            completeLine = (lineWithError.toString()).substring(0,lineWithError.length()-1);

                            if (!lines.contains(completeLine))
                            {
                                JSONObject splitDetails = new JSONObject();
                                splitDetails.put("row", j+1);
                                splitDetails.put("text", completeLine);
                                errors.put(splitDetails);
                            }
                        }
                        outputString = errors.toString();
                        outputRows = new Gson().fromJson(outputString, new TypeToken<List<Rows>>(){}.getType());
                        Files.deleteIfExists(storeFile.toPath());
                    }
                }

                request.setAttribute("outputRows", outputRows);
            } catch (Exception ex) {
                request.setAttribute("message", "There was an error: " + ex.getMessage());
            }

            getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);

        }
    }


    private static ArrayList<String> get_arraylist_from_file(File f)
            throws FileNotFoundException {
        Scanner s;
        ArrayList<String> list = new ArrayList<>();
        s = new Scanner(f);
        while (s.hasNextLine()) {
            list.add(s.nextLine());
        }
        s.close();
        return list;
    }
}
