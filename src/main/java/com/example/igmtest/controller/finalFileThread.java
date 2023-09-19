package com.example.igmtest.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class finalArch {

    void execFinalArch(String nameArchIn, String nameArchOut) {
        synchronized (this) {
            File file = new File(nameArchOut);
            String text = "sample text";
            StringBuilder html = new StringBuilder();
            try (FileWriter fw = new FileWriter(file, true); BufferedWriter bf = new BufferedWriter(fw); FileReader fr = new FileReader(nameArchIn); BufferedReader br = new BufferedReader(fr); PrintWriter out = new PrintWriter(bf)) {

                String val;

                while ((val = br.readLine()) != null) {

                    out.print(val);
                    out.println();
                }

                br.close();
            } catch (IOException ex) {

                System.out.println(ex.getMessage());
            }
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }//end of the method    
}


public class finalFileThread extends Thread {

    private String nameArchIn, nameArchOut;
    private finalArch fArch;

    public finalFileThread() {
    }

    public finalFileThread(String nameArchIn, String nameArchOut, finalArch fArch) {
        this.nameArchIn = nameArchIn;
        this.nameArchOut = nameArchOut;
        this.fArch = fArch;
    }

    @Override
    public void run() {

        fArch.execFinalArch(nameArchIn, nameArchOut);

    }
}
