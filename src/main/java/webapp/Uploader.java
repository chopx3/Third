package webapp;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Uploader extends HttpServlet

{
    TreeMap<Integer, String> errors = new TreeMap<>();
    String json = "";
    private static final String UPLOAD_DIRECTORY = "upload";
    private static final int THRESHOLD_SIZE = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
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
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        item.write(storeFile);
                        //Получили файл, записали в InputStream
                        FileInputStream fIP = new FileInputStream(storeFile);
                        XSSFWorkbook workbook = new XSSFWorkbook(fIP);
                        XSSFSheet sheet = workbook.getSheetAt(0);


                        ArrayList<String> lines = new ArrayList<>();

                        String txtfilePath = getServletContext().getRealPath("") + File.separator;
                        File fileWithCategories = new File(txtfilePath+"/fortest.txt");
                        try{
                            lines = get_arraylist_from_file(fileWithCategories);
                        }
                        catch(Exception e){
                            System.out.println("error");
                        }

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
                            String completeLine = "";
                            completeLine = (lineWithError.toString()).substring(0,lineWithError.length()-1);

                            if (!lines.contains(completeLine))
                            {
                                {
                                    errors.put(j+1, "Текст: " + completeLine);
                                }
                            }
                        }
                        ObjectMapper mapper = new ObjectMapper();

                        json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errors);
                        System.out.println(json);
                        Pattern p = Pattern.compile("\"([0-9]*)\" : \"([\\w:\\s/\\\")]*)");
                        Matcher match = p.matcher(json);
                        json = match.replaceAll("\tНомер строки - $1\n\t$2");  // number 46

                    }
                }
                //request.setAttribute("size", errors.size());
                request.setAttribute("message", json);
            } catch (Exception ex) {
                request.setAttribute("message", "There was an error: " + ex.getMessage());
            }

            getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);

        }
    }
    public static ArrayList<String> get_arraylist_from_file(File f)
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
