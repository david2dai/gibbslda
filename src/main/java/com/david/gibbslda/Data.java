package com.david.gibbslda;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map.Entry;

public class Data {

    private Map <String, Integer> wordMapping = null;
    
    public Map <String, Integer> getWordMapping () {
        return this.wordMapping;
    }

    public int getV () {
        return this.wordMapping.size ();
    }

    private void saveWordMapping (String fileName, Map <String, Integer> wordMapping) {
        
        try {
            File file = new File (fileName);

            if (!file.exists ()) {
                file.createNewFile ();
            }

            BufferedWriter writer = null; 
            writer = new BufferedWriter (new FileWriter (file));
            
            for (Entry <String, Integer> entry : wordMapping.entrySet ()) {
                writer.write (entry.getKey () + " " + (entry.getValue ()).intValue () + "\n");
            }
            
            writer.close ();
        } catch (IOException e) {

            e.printStackTrace ();
        }
        return;
    }

    private void loadWordMapping (String fileName) {

        if (null != wordMapping) {
            return;
        }
        
        wordMapping = new HashMap <String, Integer> ();

        try {
            File file = new File (fileName);
            BufferedReader reader = null;
            reader = new BufferedReader (new FileReader (file));

            String line = null;
            String word = null;
            Integer wordId = null;

            while ((line = reader.readLine ()) != null) {
                String [] items = line.split (" ");
                word = items [0];
                wordId = Integer.valueOf (items[1]);
                wordMapping.put (word, wordId);
            }

            reader.close ();
        } catch (IOException e) {
            e.printStackTrace ();
        }

        return;
    }

    public int [][] loadTrainData (String fileName) {

        System.out.println ("Load Train Data Start!");
        wordMapping = new HashMap <String,Integer> ();
        BufferedReader reader = null;
        int trainData [][] = null;
        
        try {

            File file = new File (fileName);
            reader = new BufferedReader (new FileReader (file));
            String line = null;
            String word = null;
            int count = 0;
            int M = 0; // Doc count
            int m = 0; // a doc id
            int N = 0; // Doc size
            int wordId = 0;
            
            // First line is the total count of the Doc
            line = reader.readLine ();
            M = Integer.valueOf (line);
            System.out.println ("Doc cnt is " + M);
            trainData = new int [M][];

            while ((line = reader.readLine ())!= null) {
                String [] words = line.split (" ");
                N = words.length;
                trainData [m] = new int [N];
                
                for (int n=0;n<N;++n) {
                    word = words [n];
                    Integer wordId_ = null;
                    if ((wordId_ = wordMapping.get (word)) == null) {
                        trainData [m][n] = wordId;
                        wordId_ = new Integer (wordId);
                        wordMapping.put (word, wordId_);
                        ++ wordId;
                    } else {
                        trainData [m][n] = wordId_.intValue ();
                    }
                }
                ++ m;
            }
            
            reader.close ();
        } catch (IOException e) {
            e.printStackTrace ();
        } finally {
            if (reader != null) {
                try {
                    reader.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }

        if (0 != wordMapping.size ()) {
            String folderPath = MyUtil.getFolderPath (fileName);
            saveWordMapping (folderPath + "/wordMapping.txt", wordMapping);
        }

        System.out.println ("Word Dict size is " + wordMapping.size ());
        System.out.println ("Doc cout " + trainData.length);
       
        return trainData;
    }

    public int [][] loadInferData (String modelFolder, String fileName) {

        System.out.println ("Load Infer Data Start!");
        int inferData [][] = null; 
        BufferedReader reader = null;
        
        if (null == wordMapping) {
            loadWordMapping (modelFolder + "/wordMapping.txt");
            System.out.println ("Load wordMapping!");
        }
        
        try {
            File file = new File (fileName);
            reader = new BufferedReader (new FileReader (file));
            
            String line = null;
            String word = null;
            int count = 0;
            int M = 0; // Doc count
            int m = 0; // a doc id
            int N = 0; // Doc size
            int tmpN = 0; // Tmp doc size
            int wordId = 0;
            // First line is the total count of the Doc
            line = reader.readLine ();
            M = Integer.valueOf (line);
            System.out.println ("Doc cnt is " + M);
            inferData = new int [M][];

            while ((line = reader.readLine ())!= null) {
                String [] words = line.split (" ");
                tmpN = words.length;
                N = 0;
                
                // Some words in infer data may not appear in train
                // Get the N of a doc (with unseen word filterd)
                Vector <Integer> wordIds = new Vector <Integer> ();
                
                for (int i=0;i<tmpN;++i) {
                    word = words [i];
                    Integer wordId_ = null;

                    if ((wordId_ = wordMapping.get (word)) != null) {
                        wordIds.add (wordId_);
                    }
                }

                N = wordIds.size ();
                if (N == 0) {
                    System.out.println ("Doc size is Zero, please remove this doc, line =" + (m + 2));
                }

                inferData [m] = new int [N];
                
                for (int n=0;n<N;++n) {
                    wordId = (wordIds.get (n)).intValue ();
                    inferData [m][n] = wordId;
                }
                
                ++ m;
            }
            
            reader.close ();
        } catch (IOException e) {
            e.printStackTrace ();
        } finally {
            if (reader != null) {
                try {
                    reader.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }

        System.out.println ("Word Dict size is " + wordMapping.size ());
        System.out.println ("Infer Doc cout " + inferData.length);

        return inferData;
    }
}
