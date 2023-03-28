package com.atomic.parallel.dfs;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Processor extends Thread {
	AtomicInteger[] load;
	AtomicBoolean[] waiting;
	AtomicBoolean done;
	private int threadNumber;
	private Graph graph;
	private int numberOfProcessor;
	public Processor(Graph graph,int numberOfProcessor,int id){
		load = new AtomicInteger[numberOfProcessor];
		waiting = new AtomicBoolean[numberOfProcessor];
		for(int i = 0;i<numberOfProcessor;i++){
			load[i] = new AtomicInteger();
			waiting[i] = new AtomicBoolean();
		}
		done = new AtomicBoolean();
		done.set(false);
		this.threadNumber = id;
		this.numberOfProcessor = numberOfProcessor; 
		this.graph = graph;
	}
	
	@Override
	public long getId() {
		return threadNumber;
	}

	@Override
	public void run() {
		while(done.get()==false){
			load[threadNumber].set(graph.dfs());
			for(int i = 1;load[i-1].get()==0 && i<numberOfProcessor;i++){
				if(i==(numberOfProcessor-1) && load[i].get()==0)
					done.set(true);
			}
			if(load[threadNumber].get()==0){
				waiting[threadNumber].set(true);
			}else{
				for(int i = 0;i<numberOfProcessor;i++){//sets waiting to false for all threads
					waiting[i].set(false);
				}
			}
			while(waiting[threadNumber].get()==true && done.get()==false){
				System.out.println("Im waiting - " + getThreadNumber());
			}
		}
	}

	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

	public int getNumberOfProcessor() {
		return numberOfProcessor;
	}

	public void setNumberOfProcessor(int numberOfProcessor) {
		this.numberOfProcessor = numberOfProcessor;
	}
	
}

--------------------

package Lab3;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by badams on 2/9/15.
 */
public class CollectorStream extends Stream {
    Queue<Producer> producers = new LinkedList<Producer>();
    TCFrame t = null;
    Notifier n = null;
    double value = 0;

    public CollectorStream(){}

    public CollectorStream(Notifier n, TCFrame t) {
        // TODO: Add logic for this constructor
        this.t = t;
        this.n = n;
    }

    public void add(Producer p){
        producers.add(p);
    }

    public void start(){
        for (Producer p : producers){
            t.area.append("Collector: got " + p.ident + "\n");
            Subscriber s = (Subscriber)p.next();
            value += s.stock_value;
        }
    }

    synchronized public Object next () {
        n.putValue(new IntObject((int) value));
        return n;
    }
}

--------------------

import java.util.concurrent.Callable;

public class CallableTask implements Callable<Integer>
{
	private int taskNumber;

	CallableTask(int taskNumber)
	{
		this.taskNumber = taskNumber;
	}

	public Integer call()
	{
		for (int i = 0; i <= 100; i += 20)
		{
			// Perform some work ...
			System.out.println(Thread.currentThread().getName()
					+ " taskNumber : " + taskNumber + ", percent complete: "
					+ i);
			try
			{
				Thread.sleep((int) (Math.random() * 1000));
			}
			catch (InterruptedException e)
			{
			}
		}
		return (taskNumber);
	}
}
--------------------

