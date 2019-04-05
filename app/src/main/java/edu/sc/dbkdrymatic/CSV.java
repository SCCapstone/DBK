package edu.sc.dbkdrymatic;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSV extends IOException{
    /**
     * this class is to be used for creating the CSV file that contains all the data related to
     * the boost boxes
     */
    File folder = new File(Environment.getExternalStorageDirectory() + "/Folder");

    }


