package edu.sc.dbkdrymatic;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSV {

    /**
     * this class is to be used for creating the CSV file that contains all the data related to
     * the boost boxes


    private final String CSV_File = "./DBK.csv";

    public void main(String[] args)
    {
       write(CSV_File);
    }

    public void write(String filePath){

        File file=new File(filePath);
        try{

        FileWriter outputfile=new FileWriter(file);


        CSVWriter writer=new CSVWriter(outputfile);


        String[]header={"Data from Boost Box"};
        writer.writeNext(header);

        String[]line1={"Time = 1038"};
        writer.writeNext(line1);

        String[]line2={"HoursRun = 32"};
        writer.writeNext(line2);

        String[]line3={"NTC1 = 2979 K"};
        writer.writeNext(line3);

        writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        }

    public void read (String file)
    {

        try {


            FileReader filereader = new FileReader(file);


            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;


            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
     */
}
