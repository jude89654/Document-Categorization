import java.io.*;
import java.util.ArrayList;


import com.ust.util.FileUtilities;
import liblinear.InvalidInputDataException;
import org.apache.commons.io.FileUtils;

/**
 * Main class of the system
 */
public class Main {
    /**
     * The root folder of the Categorizer
     */
    static String categorizedFilesPath="CategorizedDocuments";

    /**
     * Main method of the program
     * @param args
     */
    public static void main(String[] args) {


        // static String[] categories
        try {

            //Method used to create an OCR File of the test and training data
            FileUtilities.createOCRFile(new File("project/dev"), new File("tempFolder/dev"));
            FileUtilities.createOCRFile(new File("project/train"), new File("tempFolder/train"));

            //Copy the class_name file and the dev_label file
            FileUtils.copyFile(new File("project/class_name.txt"),new File("tempFolder/class_name.txt"));
            FileUtils.copyFile(new File("project/dev_label.txt"), new File("tempFolder/dev_label.txt"));

            //Start training using the training data
            Train.train();

            //Start Classifying all the testData
            Test.StartTesting();

            //Create the root folder and the subfolders for the categorized files
            initializeFolders();

            //create an array of the files and move it to its respective folders using the result.txt
            File [] originalFiles=new File("project/dev").listFiles();
            ArrayList<Integer> results = getResults(new File("result.txt"));

            //copy the files.
            moveCategorizedFiles(originalFiles,results);




        }catch(IOException io){
            System.out.println("ERROR IN INITIALIZING FILES");
            io.printStackTrace();
        } catch (InvalidInputDataException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for moving the categorized files based on the results.txt
     * @param categorizedfile Array of files
     * @param results
     */
    public static void moveCategorizedFiles(File[] categorizedfile, ArrayList<Integer> results){
        String folders[] ={"letter/application/","letter/complaint/","letter/resignation"};
        for (int index = 0; index < results.size(); index++) {
            try {
                switch (results.get(index)) {
                    case 0:
                        FileUtils.copyFile(categorizedfile[index], new File(categorizedFilesPath + "/" + folders[results.get(index)]+categorizedfile[index].getName()));
                        break;
                    case 1:
                        FileUtils.copyFile(categorizedfile[index], new File(categorizedFilesPath + "/" + folders[results.get(index)]+categorizedfile[index].getName()));
                        break;
                    case 2:
                        FileUtils.copyFile(categorizedfile[index], new File(categorizedFilesPath + "/" + folders[results.get(index)]+categorizedfile[index].getName()));
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * create an ArrayList of integers of the result of the categorization of the current file.
     * @param result
     * @return
     */
    public static ArrayList<Integer> getResults(File result){
        ArrayList<Integer> resultArrayList= new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(result));
            String line;
            while((line=bufferedReader.readLine())!=null){
                resultArrayList.add(Integer.parseInt(line.split(" ")[1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultArrayList;
    }


    /**
     * method to create a folder for creating the root folder and subfolders for the Categorized files.
     */
    public static void initializeFolders(){
        System.out.println("INITIALIZING FOLDERS");
        File mainFolder= new File(categorizedFilesPath);
        mainFolder.mkdir();

        String[] subFolders = new String[]{"reports","memorandum","letter","letter/application","letter/complaint", "letter/resignation"};
        for (String currentFolderName:
             subFolders) {
            File file = new File("CategorizedDocuments/"+currentFolderName);
            file.mkdir();
        }

    }



}


