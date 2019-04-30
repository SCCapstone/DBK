package edu.sc.dbkdrymatic;


/**

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import edu.sc.dbkdrymatic.internal.BoostBox;

public class CSV {

    public void writeDataLine(String Filepath) //todo add filepath then delete later
    {
        File ToSend = new File(Filepath);

        try {
            FileWriter outputfile = new FileWriter(ToSend); //Todo fix ToSend needs to be written to a file to be emailed

            CSVWriter writer = new CSVWriter(outputfile);

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[]{"VALUE", "DATA"});//for header
            for (BoostBox boostBox; List < boostBox >) {
                CSVWriter.
                (getClass(boostBox.getAddress(), boostBox.getAirInTemp(), boostBox.getAirOutTarget(),
                        boostBox.getAirOutTemp(), boostBox.getAirOutThreshold(), boostBox.getCumulativeEnergy()
                        , boostBox.getCurrent(), boostBox.getHours(), boostBox.getJobId(), boostBox.getPower(), boostBox.isAutoRestart(),
                        boostBox.getName(), boostBox.isRunning(), boostBox.getVoltage());
            }
            writer.writeAll(data);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
**/