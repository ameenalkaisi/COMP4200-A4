package com.example.ameenal_kaisiassignment4;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileHandler {
    public static void writeData(Context context, ArrayList<String> items) {
        try {
            FileOutputStream fos = context.openFileOutput(context.getString(R.string.filename),
                    Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(items);

            oos.close();
            fos.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readData(Context context) {
        ArrayList<String> data;

        try {
            FileInputStream fis = context.openFileInput(context.getString(R.string.filename));
            ObjectInputStream ois = new ObjectInputStream(fis);

            data = (ArrayList<String>) ois.readObject();

            ois.close();
            fis.close();
        } catch(IOException e) {
            data = new ArrayList<>();
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            // could have errors here as well
            data = new ArrayList<>();
            e.printStackTrace();
        }

        return data;
    }
}
