package com.david.gibbslda;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

class Model {

    private int NWZ [][]; // row word, col z
    private int NMZ [][]; // row doc, col z 
    private int NZ []; // the count of words are assigned to z
    private int NM []; // the count of words in a doc

    private int K;
    private int V;
    private int docCnt;
    private float alpha;
    private float beta;
    private int iter;

    public int getK () {
        return this.K;
    }

    public int getV () {
        return this.V;
    }

    public float getAlpha () {
        return this.alpha;
    }
    
    public float getBeta () {
        return this.beta;
    }
    
    public Model (String folderPath) {

        try {

            File file = new File (folderPath + "/params.txt");
            BufferedReader reader = new BufferedReader (new FileReader (file));

            String line = null;
            while ((line = reader.readLine ()) != null) {
                String items [] = line.split ("=");
                
                if (items [0].equals ("topics")) {
                    K = Integer.valueOf (items [1]);
                } else if (items [0].equals ("words")) {
                    V = Integer.valueOf (items [1]);
                } else if (items [0].equals ("documents")) {
                    docCnt = Integer.valueOf (items [1]);
                } else if (items [0].equals ("alpha")) {
                    alpha = Float.valueOf (items [1]);
                } else if (items [0].equals ("beta")) {
                    beta = Float.valueOf (items [1]);
                } else if (items [0].equals ("iter")) {
                    iter = Integer.valueOf (items [1]);
                }

            }

            reader.close ();

            NWZ = new int [V][K];
            NMZ = new int [docCnt][K];
            NZ = new int [K];
            NM = new int [docCnt];
            
            for (int i=0;i<V;++i) {
                for (int j=0;j<K;++j) {
                    NWZ [i][j] = 0;
                }
            }

            for (int i=0;i<docCnt;++i) {
                for (int j=0;j<K;++j) {
                    NMZ [i][j] = 0;
                }
            }
            
            for (int i=0;i<K;++i) {
                NZ [i] = 0;
            }

            for (int i=0;i<docCnt;++i) {
                NM [i] = 0;
            }


            file = new File (folderPath + "/zAssign.txt");
            reader = new BufferedReader (new FileReader (file));
            line = null;
           
            int wordId = 0;
            int docId = 0;
            int docSize = 0;
            int z = 0;
            while ((line = reader.readLine ()) != null) {
                String items [] = line.split (" ");
                docSize = items.length;
                for (int i=0;i<docSize;++i) {
                    String subItems [] = items [i].split (":");
                    
                    wordId = Integer.valueOf (subItems [0]);
                    z = Integer.valueOf (subItems [1]);
                    NWZ [wordId][z] += 1;
                    NMZ [docId][z] += 1;
                    NZ [z] += 1;
                }
                NM [docId] += docSize;
                ++ docId;
            }
        } catch (IOException e) {

        }
    }

    public int [][] getNWZ () {
        return this.NWZ;
    }

    public int [][] getNMZ () {
        return this.NMZ;
    }

    public int [] getNZ () {
        return this.NZ;
    }

    public int [] getNM () {
        return this.NM;
    }

}
