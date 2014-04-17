package com.david.gibbslda;

import java.util.Map;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

public class Start {

    enum CmdType {
        ESTIMATE, INFERENCE, INVALID
    }

    public static void uasge () {
        String usage1 = "gibbslda -est [-ntopics <int>] [-alpha <float>] [-beta <float>]" + 
            " [-niters <int>] [-file <string>]";
        String usage2 = "gibbslda -inf [-niters <int>] [-model <string>] [-file <string>]";
        System.out.println (usage1);
        System.out.println (usage2);
    }

    public static void main (String [] args) {

        Option opHelp = new Option ("h", "Help");
        Option opEstimate = new Option ("est", "Do estimate");
        Option opInference = new Option ("inf", "Do inference");

        Option opTopic = OptionBuilder.hasArg ().create ("ntopics");
        Option opAlpha = OptionBuilder.hasArg ().create ("alpha");
        Option opBeta = OptionBuilder.hasArg ().create ("beta");
        Option opIterate = OptionBuilder.hasArg ().create ("niters");
        Option opFile = OptionBuilder.hasArg ().create ("file");
        Option opModel = OptionBuilder.hasArg ().create ("model");

        Options options = new Options ();
        options.addOption (opHelp); 
        options.addOption (opEstimate); 
        options.addOption (opInference); 
        options.addOption (opFile); 
        options.addOption (opTopic); 
        options.addOption (opAlpha); 
        options.addOption (opBeta); 
        options.addOption (opIterate); 
        options.addOption (opModel); 

        CommandLineParser parser = new BasicParser();
        
        try {
            // parse the command line arguments
            CommandLine line = parser.parse( options, args );
            
            // Default value
            String file = null;
            CmdType cmdType = CmdType.INVALID;
            int K = 50;
            int iter = 1000;
            float alpha = 1.0f;
            float beta = 0.1f;

            if(line.hasOption("h")) {
                uasge (); 
                return;
            } 

            if(line.hasOption("est")) {
                System.out.println ("LDA estimate");
                cmdType = CmdType.ESTIMATE; 
            } else if (line.hasOption("inf")) {
                System.out.println ("LDA inference");
                cmdType = CmdType.INFERENCE; 
            } else {
                uasge (); 
                return;
            }

            if (line.hasOption("file")) {
                file = line.getOptionValue("file");
            } else {
                System.out.println("No data file. Use -h to see help info.");
                return;
            }
            
            if (line.hasOption("niters")) {
                iter = Integer.valueOf (line.getOptionValue("niters"));
            }

            System.out.println ("the parameters are:");
            System.out.println ("K=" + K);
            System.out.println ("alpha=" + alpha);
            System.out.println ("beta=" + beta);
            System.out.println ("iter=" + iter);
            System.out.println ("file=" + file);

            switch (cmdType) {
                case ESTIMATE:
                    if (line.hasOption("ntopics")) {
                        K = Integer.valueOf (line.getOptionValue("ntopics"));
                    }

                    if (line.hasOption("alpha")) {
                        alpha = Float.valueOf (line.getOptionValue("alpha"));
                    } else {
                        alpha = 50.0f/K;
                    }

                    if (line.hasOption("beta")) {
                        beta = Float.valueOf (line.getOptionValue("beta"));
                    }

                    Estimate estimate = new Estimate (K, iter, alpha, beta, file);
                    estimate.run (); 
                    break;
                    
                case INFERENCE:
                    String model = null; 
                    if (line.hasOption("model")) {
                        model = line.getOptionValue("model");
                    }

                    Inference inference = new Inference (iter, model, file);
                    inference.run ();
                    break;

                default:
                    break;
            }
        } catch( ParseException exp ) {
         
            System.err.println("Parsing failed: " + exp.getMessage());
            uasge (); 
        } 
    }
}
