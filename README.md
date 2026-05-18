This repository run a muiltple-goal recofnition problem.

The main class: experiments.MultipleGoals, gets a tar.bz2 file file that contaions of a recognition problem, and run the muiltiple-goals recoger on this problem.

The main gets 2 arguments:
	1) The problem file .pddl
	2) a path to a folder where all the temporary files the program uses will be saved.
The main:
	1) print to the consule all the details about the recognition process.
	2) creates 2 files: problem_file.tar.bz2ScoreStats.txt and problem_file.tar.bz2ProbStats.txt that 
	contains a summary of each score/probability each hypothesis was given at each iteration of the algorithem. 
	The file are saved at the save folder the original tar.bz2 file is saved.
	

1)In order to run the program.
	1) Nevigate to the root directory of this repository.
	2) Run the command: make run <tar_file_path.tar.bz2> <folder_for_saving_plan_files_path>
		ex: make run ./experiments/blockes/blocks_prob1.tar.bz2 ./experiments/blocks/blocks_prob1

2) How to run the script with make file:
    In order to run the script with make file you have to:
    1) Create your directories hierarchy of experiments under the folder "experiments_with_make_file".
    	The script exept any hierarchy, in which:
	a) Each experiment's tar.bz2 file saved in its own directory. (without more subdirectories with other experiments in that directory).  
    2) Copy the files Makefile and Makeflie.subdir that under the folder: "experiments_with_makefile" to the root directory of your experiments.
    4) run the command "nohup make -j <num_of_cores> &" from the root folder of your experiments.
	ex:  nohup make -j 20 &"

3) The tar.bz2 fil must contain:
