/* Implement this class. */

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.lang.model.type.NullType;

public class MyHost extends Host {
    
    // Priority Blocking Queue in order to work with thread operations
    PriorityBlockingQueue<Task> pQueue = new PriorityBlockingQueue<Task>(60, new TaskComparator());
    // Task that is being executed
    Task currentTask;
    boolean doing_task = false;
    boolean isRunning = true;
    long time = 0;
    long start_time = 0;

    // Comparator based or prio and than start for sorting the tasks in the queue
    class TaskComparator implements Comparator<Task>{
            public int compare(Task t1, Task t2) {
                if (t1.getPriority() < t2.getPriority()){
                    return 1;
                }else if (t1.getPriority() > t2.getPriority()){
                    return -1;
                }else if(t1.getStart() > t2.getStart()){
                    return 1;
                    }else{
                        return -1;
                    }
                }
    }

    @Override
    public void run() {
        while(isRunning == true){

            // If the queue is not empty it means a new task can be started
            if(pQueue.size() > 0){
                try {
                    // Takes highest prio task out and starts it
                    currentTask = pQueue.take();
                    doing_task = true;
                   // Saves duration left in time
                   // And current time in start_time
                    time = currentTask.getLeft();
                    start_time = System.currentTimeMillis();
                
                    // While there is time left :
                    while(currentTask.getLeft() > 0){
                        // The time left is lowered by how many miliseconds have passed
                        currentTask.setLeft(time - System.currentTimeMillis() + start_time);
                       // If there are other tasks in the queue it checks if the task is preemtable
                       // and if the new tasks in the queue have higher priorities it replaces the currentTask.
                        if(pQueue.size() > 0){
                            if(currentTask.isPreemptible() && pQueue.peek().getPriority() > currentTask.getPriority()){
                                break;
                            }
                        }
                    }

                    // Task is finished or replaced after the loop.
                    if(currentTask.getLeft() <= 0){
                        currentTask.finish();
                        doing_task = false;
                        }else if(currentTask.isPreemptible() && pQueue.peek().getPriority() > currentTask.getPriority()){
                            pQueue.add(currentTask);
                            doing_task = false;
                        }
                } catch (InterruptedException e) {

                }

            }

    }

    }

    @Override
    public void addTask(Task task) {
        pQueue.put(task);

    }

    @Override
    public synchronized int getQueueSize() {
        // Queue size + 1 if we have a running task

        if(!doing_task){
        return pQueue.size();
    }else {
        return pQueue.size() + 1;
        }

    }

    @Override
    public long getWorkLeft() {
       // Add all time left on all in the queue
       // and if there is an active task it
       // adds the time left for it too
        long duration = 0;
        for(Task task : pQueue){
            duration += task.getLeft();
        }
        if(doing_task){
            duration += currentTask.getLeft();
        }
        return duration;
    }

    @Override
    public void shutdown() {
        // Variable used in run to indicate the shutdown command
        isRunning = false;
    }


}
