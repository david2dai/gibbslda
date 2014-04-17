#gibbslda

This is a java version of gibbs lda implement by David2 Dai.
You can use or modify it as you wish.

### 1. Compile the project 
The jar files are already included in the lib folder, so you can use them 
without recompile. 

If you want to make some modification of the code and need recopile the 
project, you should install maven first and then execute the build.sh script
in the project root directory. The jars in the folder ./lib will be fresh.

### 2. How to use gibbslda
There is a script called gibbslda in the project root folder. In the linux 
terminal, you can type the following cmd to use it.

#### Usage
##### Estimate parameters on a train data
> gibbslda -est [-ntopics <int>] [-alpha <float>] [-beta <float>] 
		        [-niters <int>] [-file <string>]

##### Infer parameters on a unseen data based on the result of a train data.
> gibbslda -inf [-niters <int>] [-model <string>] [-file <string>]

#### Parameters
> -est            do estimate/train for a data file.

> -inf            do inference for a new unseen data file.

> -ntopics        indicate the topics number.

> -niters         indicate the iterate times.

> -alpha          hyper-parameter alpha.

> -beta           hyper-parameter beta.

> -file           indicate the data file for est/inf.

> -model          indicate the trained model for the inference of unseen data.

### 3. Input Data Format
Both data for training/estimating the model and new data (i.e., previously 
unseen data) have the same format as follows.
#### Data format
> [M]

> [document_1]

> [document_2]

> ...

> [document_M]

### 4. Output Data Format

#### z assign
> [word]:[z] [word]:[z]

> ... 

> [word]:[z] [word]:[z]

#### theta
> [theta_1_0] [theta_1_1] ... [theta_1_K-1]

> ...

> [theta_M_0] [theta_M_1] ... [theta_M_K-1]

#### phi 
> [phi_0_0] [phi_0_1] ... [phi_0_V]

> ...

> [phi_K-1_0] [phi_K-1_1] ... [phi_K-1_V]

#### top words for each topic 
> [Topic0]

> [word0]:[prob]

> [word1]:[prob]

> ...

> [wordk]:[prob]
