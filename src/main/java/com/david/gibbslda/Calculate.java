package com.david.gibbslda;

import java.util.Stack;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

class Calculate {

    private int K,V;
    private float alpha, beta;
    private double theta [][];
    private double phi [][];

    public Calculate (int K, int V, float alpha, float beta) {

        this.K = K;
        this.V = V;
        this.alpha = alpha;
        this.beta = beta;
    }

    class WordProb {
        int wordId;
        double probWZ;

        public WordProb (int wordId, double probWZ) {
            this.wordId = wordId;
            this.probWZ = probWZ;
        }
    }

    public void calTheta (int NMZ [][], int NM [], String thetaFilePath) {
        try {

            File file = new File (thetaFilePath);
            if (!file.exists ()) {
                file.createNewFile ();
            }

            BufferedWriter writer = new BufferedWriter (new FileWriter (file));
            int M = NMZ.length; 
            double probZ = 0.0;
            for (int m=0;m<M;++m) {
                for (int k=0;k<K;++k) {
                    probZ = (NMZ [m][k] + alpha) / (NM [m] + K*alpha);
                    writer.write (probZ + " ");
                }

                writer.write ("\n");
            }
            writer.close ();
        } catch (IOException e) {

        }
        return;
    }

    public void calPhi (int NWZ [][], int NZ [], Map <String, Integer> wordMapping, int topK,
            String phiFilePath, String topWordsFilePath) {
        
        try {
            String [] wordMappingR = new String [wordMapping.size ()];
            
            for (Entry <String, Integer> entry : wordMapping.entrySet ()) {
                wordMappingR [(entry.getValue ()).intValue ()] = entry.getKey ();
            }

            File fileTheta = new File (phiFilePath);
            File fileTopKWords = new File (topWordsFilePath);
            
            if (!fileTheta.exists ()) {
                fileTheta.createNewFile ();
            }
            
            BufferedWriter writerTheta = null;
            writerTheta = new BufferedWriter (new FileWriter (fileTheta));

            if (!fileTopKWords.exists ()) {
                fileTopKWords.createNewFile ();
            }
            
            BufferedWriter writerTopKWords = null;
            writerTopKWords = new BufferedWriter (new FileWriter (fileTopKWords));

            Comparator <WordProb> cmp = new Comparator <WordProb> () {
                public int compare (WordProb a, WordProb b) {
                    if (a.probWZ > b.probWZ) {
                        return 1;
                    } else if (a.probWZ < b.probWZ) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            };
            
            double pWZ = 0.0;
            for (int k=0;k<K;++k) {
                Queue <WordProb> pq = new PriorityQueue <WordProb> (topK, cmp);
                for (int j=0;j<V;++j) {
                    pWZ = (NWZ [j][k] + beta) / (NZ [k] + V*beta);
                    writerTheta.write (pWZ + " ");
                    
                    if (pq.size () < topK) {
                        pq.add (new WordProb (j, pWZ));
                    } else {
                        WordProb peek = pq.peek ();
                        if (peek.probWZ < pWZ) {
                            pq.poll ();
                            pq.add (new WordProb (j, pWZ));
                        }
                    }
                }
                 
                writerTheta.write ("\n");
                // Top K words
                Stack <WordProb> wordProbStack = new Stack <WordProb> ();
                writerTopKWords.write ("Topic " + k + ":\n");
                
                while (!pq.isEmpty ()) {
                    WordProb wordProb = pq.poll ();
                    wordProbStack.push (wordProb);
                }

                while (!wordProbStack.empty ()) {
                    WordProb wordProb = wordProbStack.pop ();
                    String word = wordMappingR [wordProb.wordId];
                    writerTopKWords.write (word + ":" + wordProb.probWZ + "\n");
                }
                
                writerTopKWords.write ("\n");
            }

            writerTheta.close ();
            writerTopKWords.close ();   
        } catch (IOException e) {
        
        } 
    }
}
