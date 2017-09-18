Assumptions:
1)	Json file is static and it is being read once during application start and not with polling.
2)	Scheduling is interpreted as current assignment and not forecasted job- printer assignment for all jobs (because polling is involved).
3)	Processing time is considered as seconds.
4)	If processing time is 0/ -ve considered as dummy and skipped to next job.
5)	Id, material and times in Json are assumed to be integers other formats will throw an error.
6)	Initial state of all printers in set to idle so Json will not be read for state.
7)	Change material is called only when required material is different from printer material.
8)	(Printer, Job) pairs which are currently printing will be logged, if printer is free or is in material change state will not  logged
 
The problem is a classic manufacturing load balancing problem, where we must achieve fast production by maximizing machine utilization. For solving part, A I’ve approached the problem writing my initial code with minimum complexity. But to optimize considering part B I’ve to make some changes to my code.

So, these are two approaches required to optimize the running time.
1)	Distributing jobs into 20 equal weighted pools based on  processing times of jobs and to be assigned across the 20 printers.
2)	Printers are down due to the material change. our algorithm should schedule with minimum material changes. So, whenever the printer is free we check through the pending jobs for same material type and assign it. Through this we will be able reduce material changing instances.

My initial code took 57 seconds (100% utilization till 40 seconds), same code with material change took 67 seconds. Modified code with material change as per 2nd way took 62 seconds for the sample Json. Together with 1st approach will result in most optimal running time and it can result in predictive scheduling however since the question is based on scheduling based on polling I’ve adapted 2nd approach alone.

I’ve written some Junit tests for my printer methods which will be used by algorithm, they include
1.	Printer accepting new job and changing state.
2.	Printer not accepting new job when printing.
3.	Printer becoming idle when print time is done.
4.	Printer changing material when job need different material.
5.	Printer staring the pending job when material is changed.

Execution: 
Additional libraries required: json.jar, Junit dependencies
The Json file path is hardcoded to the application so new data should be replaced with the sample data in the resources folder. Junit tests should be executed independently.



