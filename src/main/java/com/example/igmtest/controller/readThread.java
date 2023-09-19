package com.example.igmtest.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class readThread extends Thread {

    private String nameArchIn, nameArchOut;

    public readThread() {
    }

    public readThread(String nameArchIn, String nameArchOut) {
        this.nameArchIn = nameArchIn;
        this.nameArchOut = nameArchOut;
    }

    @Override
    public void run() {
        File file = new File(nameArchOut);
        String text = "sample text";
        StringBuilder html = new StringBuilder();
        try (FileWriter fw = new FileWriter(file); BufferedWriter bf = new BufferedWriter(fw); FileReader fr = new FileReader(nameArchIn); BufferedReader br = new BufferedReader(fr); PrintWriter out = new PrintWriter(bf)) {

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
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }
}
