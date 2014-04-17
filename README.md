gibbslda
========

This is a java version of gibbs lda implement by David2 Dai.
You can use or modify it as you wish.

### 1. Compile the project 
The jar files are already included in the lib folder, so you can use them 
without recompile. 
If you want to make some modification of the code and need recopile the 
project, you should install maven first and then execute the build.sh script
in the project root directory. The jars in the folder ./lib will be fresh.

### 2. How to use gibbslda
There is a script called gibbslda in the project root folder. In the linux terminal,
type the following cmd and you will the useage infomation of gibbslda.
    
		./gibbslda

		gibbslda -est [-ntopics <int>] [-alpha <float>] [-beta <float>] 
		    [-niters <int>] [-file <string>]

		gibbslda -inf [-niters <int>] [-model <string>] [-file <string>]

"gibbslda -est ..." is used to estimate parameters on a train data.
"gibbslda -inf ..." is used to infer parameters on a new unseen data based the 
result of a train data.

Parameters:
		-est            do estimate/train for a data file.
		-inf            do inference for a new unseen data file.
		-ntopics        indicate the topics number.
		-niters         indicate the iterate times.
		-alpha          hyper-parameter alpha.
		-beta           hyper-parameter beta.
		-file           indicate the data file for est/inf.
		-model          indicate the trained model (folder) for inference of unseen data.

### 3. Input Data Format
Both data for training/estimating the model and new data (i.e., previously 
unseen data) have the same format as follows:

> [M]
> [document_1]
> [document_2]
> ...
> [document_M]
