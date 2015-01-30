package test;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class TestQueue {
	private String name;
	private int population;

	public TestQueue(String name, int population) {
		this.name = name;
		this.population = population;
	}

	public String getName() {
		return this.name;
	}

	public int getPopulation() {
		return this.population;
	}

	public String toString() {
		return getName() + " - " + getPopulation();
	}

	public static void main(String args[]) {
		Comparator<TestQueue> OrderIsdn = new Comparator<TestQueue>() {
			public int compare(TestQueue o1, TestQueue o2) {
				// TODO Auto-generated method stub
				int numbera = o1.getPopulation();
				int numberb = o2.getPopulation();
				if (numberb > numbera) {
					return 1;
				} else if (numberb < numbera) {
					return -1;
				} else {
					return 0;
				}

			}

		};
		Queue<TestQueue> priorityQueue = new PriorityQueue<TestQueue>(11, OrderIsdn);

		TestQueue t1 = new TestQueue("t1", 5);
		TestQueue t3 = new TestQueue("t3", 3);
		TestQueue t2 = new TestQueue("t2", 2);
		TestQueue t4 = new TestQueue("t4", 0);
		priorityQueue.add(t1);
		priorityQueue.add(t3);
		priorityQueue.add(t2);
		priorityQueue.add(t4);
		System.out.println(priorityQueue.poll().toString());
	}
}
