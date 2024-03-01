Task planning in a datacenter - Radulescu Matei

The implementation is based into two files a dispatcher and a host.

The dispatcher checks which planification policy is being used and sends the tasks to the hosts
based on the following criteria:

1) Round Robin : 
The first one is always sent to host 0, after that the next one is sent to previous host + 1 MODULO number
of hosts.

2) Shortest queue:
Uses the host's getQueueSize method to see which host has the least amounts of elementso in it's queue and
sends it to that one.

3)SizeIntervalTaskAssignment:
Checks it's size and sends it to a host based on it.

4)LeastWorkLeft:
Uses hosts getWork to check which host has the lowest amount of work left to do and sends the task to that one.

The host implements the following methods:
1)run: Loops infinetly if the shut down signal is not given. In the loops it checks if the priority queue used
(explained at addTask) is empty, if it is not that means a new task can be executed, it takes the task out of 
the priority queue(it is already sorted). While the task has time left it decreases it's time left with a 
counter using the command System.currentTimeMillis(); by saving the starting time of the task and using the
diffrence between them to determine how much time has passed. Inside the loop there is a break condition
which checks were the task is preemtable, if it is and a task with a higher priority has arrived it is
replaced in execution.
After the loop the tasked in marked as finished, or sent back into the queue if it was replaced by another.


2)addTask:
Adds the received task to a PriorityBlockQueue used to sort them with a comparator based on priorirty first and
than start value as a second compare value. The blocking queue is used to not have any data problems with 
multithreadding as it does not accept null vallues.

3)getQueuesize: Returns the size of the priority queue, or it's size + 1 if a task is being executed right now.

4)getWorkLeft: Returns the sum of the time left for executing each task including the currently running one if 
there is one.

5)shutdown:
Changes the value of a boolean which stops the infinite loop from the run method.