package com.david.gibbslda;

import java.util.Map;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

class Estimate {

    private int K;
    private int iter;
    private float alpha;
    private float beta;
    private String trainFileName;

    public Estimate (int K, int iter, float alpha, float beta, String trainFileName) {
        this.K = K;
        this.iter = iter;
        this.alpha = alpha;
        this.beta = beta;
        this.trainFileName = trainFileName;
    }

    public void run () {

        String folderPath = MyUtil.getFolderPath (trainFileName);
        
        Data data = new Data ();
        int trainData [][] = data.loadTrainData (trainFileName);
        int V = data.getV (); // Vocabulary size
        int M = trainData.length; // Documents count

        // Save train params
        try { 
            File file = new File (folderPath + "/params.txt");
            
            if (!file.exists ()) {
                file.createNewFile ();
            }

            BufferedWriter writer = null;
            writer = new BufferedWriter (new FileWriter (file));

            writer.write ("topics=" + K + "\n");
            writer.write ("words=" + V + "\n");
            writer.write ("documents=" + M + "\n");
            writer.write ("alpha=" + alpha + "\n");
            writer.write ("beta=" + beta + "\n");
            writer.write ("iter=" + iter + "\n");
            writer.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }

        // Gibbs sampling
        GibbsLDA gibbsLDA = new GibbsLDA (K, V, alpha, beta, iter, trainData);
        gibbsLDA.sampling ();
       
        // Save zAssign result of train
        try { 
            File file = new File (folderPath + "/est_zAssign.txt");
            
            if (!file.exists ()) {
                file.createNewFile ();
            }

            BufferedWriter writer = null;
            writer = new BufferedWriter (new FileWriter (file));

            int zAssign [][] = gibbsLDA.getZAssign (); // z assigned for each word of docs
            int N = 0; // Doc size

            for (int m=0;m<M;++m) { // Doc
                N = zAssign [m].length; // Doc size
                for (int n=0;n<N;++n) {
                    writer.write (trainData [m][n] + ":" + zAssign [m][n] + " ");
                }
                writer.write ("\n");
            }
            writer.close ();
        } catch (IOException e) { 
            e.printStackTrace ();
        }

        // Calculate theta, phi and topK words for each topics
        Calculate calculate = new Calculate (K, V, alpha, beta);
        int NWZ [][] = gibbsLDA.getNWZ ();
        int NMZ [][] = gibbsLDA.getNMZ ();
        int NZ [] = gibbsLDA.getNZ ();
        int NM [] = gibbsLDA.getNM ();

        String thetaFilePath = folderPath + "/est_theta.txt"; 
        String phiFilePath = folderPath + "/est_phi.txt"; 
        String topWordsFilePath = folderPath + "/est_topWords.txt";

        Map <String, Integer> wordMapping = data.getWordMapping ();
        calculate.calTheta (NMZ, NM, thetaFilePath);
        calculate.calPhi (NWZ, NZ, wordMapping, 20, phiFilePath, topWordsFilePath);

        return;
    }
}
