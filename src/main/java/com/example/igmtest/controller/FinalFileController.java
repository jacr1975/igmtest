package com.example.igmtest.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * Class that contains the controller methods
 * @author: JACarvajal
 * @version: 1.0.0    09/23
 * @see 
 */

@RestController
public class FinalFileController {

    private final Random random = new Random();

 /**
 * This method REST endpoint allows the process of taking several html fragments (cloned 
 * with independent threads) and putting them together in a Result.html file
 * using independent threads with a synchronized code block.
 * @param mils This variable keeps a miliseconds value
 * @return The String is a phrase displayed in the browser.
 */
    @GetMapping("/{mils}")
    public String process(@PathVariable int mils) {
        if (mils > 76) {
            java.util.Properties prop = new Properties();

            try {
                prop.loadFromXML(new FileInputStream("properties.xml"));
            } catch (IOException ie) {

            }
//System.out.println(prop.getProperty("propF")); 

            Thread myThreads[] = new Thread[prop.size()];

            Collection<Object> list = prop.values();

            List<String> listStr = list.stream()
                    .map(object -> Objects.toString(object, null))
                    .toList();

            Set<String> ts1 = new TreeSet<>();

            for (int x = 0; x < listStr.size(); x++) {
                ts1.add(listStr.get(x));
            }

            // System.out.println(ts1);
            List<String> toList = new ArrayList<>(ts1);
            //toList.forEach(s -> System.out.println(s));

            List<String> listArchOut = new ArrayList<>();
            String myst = "", myst2 = "";
            for (String ban : toList) {
                myst = ban.substring(5);
                int x = 0;
                while ((x < myst.length()) && (myst.charAt(x) != '.')) {
                    myst2 = myst2 + String.valueOf(myst.charAt(x));
                    ++x;
                }

                listArchOut.add(ban.substring(0, 5) + myst2 + ".txt");
                myst2 = "";
            }

            // listArchOut.forEach(s -> System.out.println(s));
            for (int j = 0; j < ts1.size(); j++) {
                myThreads[j] = new Thread(new readThread(toList.get(j), listArchOut.get(j)));
                myThreads[j].start();
            }

            File arch = new File("Result.html");
            if (arch.exists()) {
                arch.delete();
            }

            deleteForExtension("/", "txt");

            finalArch far = new finalArch();
            Thread myThreads2[] = new Thread[listArchOut.size()];
            for (int y = 0; y < listArchOut.size(); y++) {

                myThreads[y] = new Thread(new finalFileThread(listArchOut.get(y), "Result.html", far));
                myThreads[y].start();
            }

            return "Job done..";
        }
        return "Status 429. Problem with the request..The process failed..";
    }

 /**
 * This method REST endpoint allows you to call the endpoint http://localhost:8080/ several times, 
 * trying to complete the process correctly. For this, use the Backoff.java class 
 * which implements exponential backoff methods.
 * @return The String is a phrase displayed in the console or cmd.
 * @see http://localhost:8080/ REST endpoint
 */
    @GetMapping("/retrieveFile")
    public String retrieveFile() {

        RestTemplate restTemplate = new RestTemplate();

        int msecs2 = random.nextInt(100);
        String msecs = String.valueOf(msecs2);
        String url = "http://localhost:8080/" + msecs;

        Backoff backf = new Backoff(3, 460);
        String msg = "";
        boolean flag = false;
        int cont = 1;
        if (msecs2 <= 76) {
            while (backf.shouldRetry()) {

                msecs2 = random.nextInt(100);
                if (msecs2 > 76) {
                    break;
                }

                System.err.println("*** Retrying in this moment " + cont + " of 3 attempts***");
                ++cont;
                backf.error();

            }

            if (!backf.shouldRetry()) {
                msg = "Technical Problem, Status 429\nThe process failed..\n";
                backf.doNotRetry();
                flag = true;
            }
        }
        String result = "";

        if (flag) {
            System.out.println(msg);
            url = "http://localhost:8080/32";
            result = restTemplate.getForObject(url, String.class);
        } else {
            System.out.println(" Process ended with success..\n");
            msecs = String.valueOf(msecs2);
            url = "http://localhost:8080/" + msecs;
            result = restTemplate.getForObject(url, String.class);
        }

        //-------------------------------
        return result;
    }

/**
 * Methods that receives two params and delete files with a particular extension
 * @param path Path where the files are
 * @param extension File extension of files to be deleted
 * @see 
 */
    public void deleteForExtension(String path, final String extension) {
        File[] archivos = new File(path).listFiles(new FileFilter() {
            public boolean accept(File archivo) {
                if (archivo.isFile()) {
                    return archivo.getName().endsWith('.' + extension);
                }
                return false;
            }
        });
        for (File archivo : archivos) {
            archivo.delete();
        }
    }

}
