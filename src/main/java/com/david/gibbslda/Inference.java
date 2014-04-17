package com.david.gibbslda;

import java.util.Map;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

class Inference {

    private int iter;
    private String modelFolder;
    private String inferFileName;

    public Inference (int iter, String modelFolder, String inferFileName) 
    {
        this.iter = iter;
        this.modelFolder = modelFolder;
        this.inferFileName = inferFileName;
    } 
    
    // Inference is used to infer the topic distribution of new docs 
    // based on the result of train data.
    public void run () {
        
        Data data = new Data ();
        int inferData [][] = data.loadInferData (modelFolder, inferFileName);
        Map <String, Integer> wordMapping = data.getWordMapping ();
        int M = inferData.length;

        // Load trained model: params & gibbs samping result
        Model model = new Model (modelFolder);

        int K = model.getK ();
        int V = model.getV ();
        float alpha = model.getAlpha ();
        float beta = model.getBeta ();

        System.out.println ("K=" + K + " V=" + V + " alpha=" + alpha + " beta=" + beta);
        System.out.println ("M=" + M + " Iter=" + iter);
        
        GibbsLDA gibbsLDA = new GibbsLDA (K, V, alpha, beta, iter, inferData);
        // Gibbs sampling on trained model
        gibbsLDA.samplingWithModel (model);
       
        String folderPath = MyUtil.getFolderPath (inferFileName);
        // Save z assign data 
        try { 
            File file = new File (folderPath + "/inf_zAssign.txt");
            if (!file.exists ()) {
                file.createNewFile ();
            }
            BufferedWriter writer = null;
            writer = new BufferedWriter (new FileWriter (file));

            int zAssign [][] = gibbsLDA.getZAssign ();
            int N = 0; // Doc size

            for (int m=0;m<M;++m) { // Doc
                N = zAssign [m].length; // Doc size
                for (int n=0;n<N;++n) {
                    writer.write (inferData [m][n] + ":" + zAssign [m][n] + " ");
                }
                writer.write ("\n");
            }
            writer.close ();
        } catch (IOException e) {
            e.printStackTrace ();
        }

        // Calculate theta 
        // Without calculating phi and topK words for each topic
        Calculate calculate = new Calculate (K, V, alpha, beta);
        int NWZ [][] = gibbsLDA.getNWZ ();
        int NMZ [][] = gibbsLDA.getNMZ ();
        int NZ [] = gibbsLDA.getNZ ();
        int NM [] = gibbsLDA.getNM ();
       
        String thetaFilePath = folderPath + "/inf_theta.txt"; 
        String phiFilePath = folderPath + "/inf_phi.txt"; 
        String topWordsFilePath = folderPath + "/inf_topWords.txt";

        calculate.calTheta (NMZ, NM, thetaFilePath);
        // I have not implemented the calPhi for inference.
        // So not use calPhi to calculate Phi and TopKWords for inference.
        return;
    }
}
