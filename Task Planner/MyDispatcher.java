/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {
    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }

    // Variable for round robin -1 so first one sent will be on 0.
    int previous = -1;
    @Override
    public void addTask(Task task) {
        if(this.algorithm == SchedulingAlgorithm.ROUND_ROBIN){
            // Sends the first ones to 0 others ro last sent + 1
            // MODULO number of hosts.
            hosts.get((previous + 1) % hosts.size()).addTask(task);
            previous = (previous + 1) % hosts.size();
        }else if(this.algorithm == SchedulingAlgorithm.SHORTEST_QUEUE){
            int min = hosts.get(0).getQueueSize();
            int minId = 0;
            // Uses GetQueueSize to get the minimum size
            // of a queue and uses it.
            for(int i = 1; i < hosts.size(); i++){
               if(min > hosts.get(i).getQueueSize()){
                    min = hosts.get(i).getQueueSize();
                    minId = i;
               };
            }
            hosts.get(minId).addTask(task);

        }else if(this.algorithm == SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT){
           // Sends to 0, 1 ,2 based on type
            if(task.getType() == TaskType.SHORT){
                hosts.get(0).addTask(task);
            }else if(task.getType() == TaskType.MEDIUM){
                hosts.get(1).addTask(task);
            }else if(task.getType() == TaskType.LONG){
                hosts.get(2).addTask(task);
            }
        }else if(this.algorithm == SchedulingAlgorithm.LEAST_WORK_LEFT){
           // Uses getworkleft method to see
           // which host has the shorthest work left to do
           // and sends it to it.
            long min = hosts.get(0).getWorkLeft();;
            int minId = 0;
            for(int i = 1; i < hosts.size(); i++){
                if(min > hosts.get(i).getWorkLeft()){
                    min = hosts.get(i).getWorkLeft();
                    minId = i;
                }
            }

            hosts.get(minId).addTask(task);

        }
    }
}
