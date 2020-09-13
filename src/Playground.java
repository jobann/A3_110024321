import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import graphs.*;
import memoryManagement.BTree;

/*
 * Assignment 3 
 * @author Jobanpreet Singh
 * Student Id: 110024321
 */
public class Playground {

	/*
	 * Task 1: Read a Directed Graph from a file and then run DFS on it and display
	 * vertices in pre and post order and compute CPU time in nanoseconds.
	 */
	void task1() {
		// Read file
		In in = new In("mediumDG.txt");
		Digraph digraph = new Digraph(in);
		// initializing variables to calculated CPU time
		long start = 0;
		long elapsed = 0;
		long average = 0;
		// initialize DepthFirstOrder first
		DepthFirstOrder depthFirstOrder = new DepthFirstOrder(digraph);
		// Display Header
		System.out.println("Vertices\tPre-order\tPost-order");
		for (int i = 0; i < digraph.V(); i++) {
			// noticing start time before putting random string into HashTable
			start = System.nanoTime();
			// Displaying vertices in pre and post order
			System.out.println(i + "\t\t" + depthFirstOrder.pre(i) + "\t\t" + depthFirstOrder.post(i));
			elapsed = System.nanoTime() - start;
			average += elapsed;
		}
		average = average / digraph.V();
		// Displaying CPU time
		System.out.println("Average time: " + average + " (nanoseconds)");

	}

	/*
	 * Task 2a Read a EdgeWeightedDigraph from a file and then finds the shortest
	 * path for all pairs of nodes using Dijkstra’s Algorithm and calculate CPU
	 * time.
	 */
	void task2a() {
		// Read a file
		In in = new In("10000EWG.txt");
		// Initializing EdgeWeightedDigraph
		EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(in);
		int s = 0;
		// Initializing Dijkstra's Object
		DijkstraSP dijkstraSP = new DijkstraSP(edgeWeightedDigraph, s);
		// Initializing variables for CPU time
		long start = 0;
		long elapsed = 0;
		long average = 0;

		// loop all the vertices of EdgeWeightedGraph
		for (int i = 0; i < edgeWeightedDigraph.V(); i++) {
			// Noting start time
			start = System.nanoTime();

			// if current node has path to i node then finds the shortest path for all pairs
			// of nodes
			if (dijkstraSP.hasPathTo(i)) {
				// Displaying the path
				System.out.print(s + " to " + i + " (" + (float) dijkstraSP.distTo(i) + ")\t");
				// Displaying the path for all pairs
				if (dijkstraSP.hasPathTo(i)) {
					for (DirectedEdge e : dijkstraSP.pathTo(i)) {
						System.out.print(e + " ");
					}
				}
				System.out.println();
			} else {
				// Displaying message if no path found
				System.out.println(s + " to " + i + " no path");
			}
			elapsed = System.nanoTime() - start;
			average += elapsed;
		}
		// Calculating the CPU time
		average = average / edgeWeightedDigraph.V();
		// Displaying the CPU time
		System.out.println("\nAverage time: " + average + " (nanoseconds)");
	}

	/*
	 * Task 2b Read EdgeWeightedGraph from a file and the find MST using KruskalMST
	 * and calculate CPU time
	 */
	void task2b() {

		// Initializing variables for CPU time
		long start = 0;
		long elapsed = 0;
		long average = 0;
		int size = 0;

		// Reading data from file
		In in = new In("10000EWG.txt");

		// Initializing EdgeWeightedGraph
		EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(in);

		// Initializing KruskalMST
		KruskalMST kruskalMST = new KruskalMST(edgeWeightedGraph);

		// Displaying MST for every edge
		for (Edge e : kruskalMST.edges()) {
			start = System.nanoTime();
			System.out.println(e);
			elapsed = System.nanoTime() - start;
			average += elapsed;
			size++;
		}

		// Displaying size and CPU time
		System.out.println(size);
		average = average / size;
		System.out.println("\nWeight: " + kruskalMST.weight());
		System.out.println("CPU time: " + average + " (nanoseconds)");
	}

	/*
	 * Task 3 Reading data from file(movies.txt) and find all connected components
	 * and calculate the CPU time
	 */

	void task3() {

		long start = 0;
		long elapsed = 0;

		// Initializing SymbolGraph using a file (movies.txt)
		SymbolGraph sg = new SymbolGraph("movies.txt", "/");
		Graph G = sg.G();

		/*
		 * Initializing CC class that represents a data type for determining the
		 * connected components in an undirected graph
		 */
		CC cc = new CC(G);

		// number of connected components
		int M = cc.count();
		System.out.println(M + " components");

		// compute list of vertices in each connected component
		@SuppressWarnings("unchecked")
		Queue<Integer>[] components = (Queue<Integer>[]) new Queue[M];
		start = System.nanoTime();
		for (int i = 0; i < M; i++) {
			components[i] = new Queue<Integer>();
		}
		for (int v = 0; v < G.V(); v++) {
			components[cc.id(v)].enqueue(v);
		}
		elapsed = System.nanoTime() - start;

		// Displaying results
		for (int i = 0; i < M; i++) {
			System.out.print(i + ": ");
			for (int v : components[i]) {
				System.out.print(v + " ");
			}
			System.out.println();
		}

		// Displaying CPU time
		System.out.println("CPU time: " + elapsed + " (nanoseconds)");

	}

	/*
	 * Task 4 Display movies starred by particular Actor or by two Actors
	 */
	void task4() {
		// Initializing SymbolGraph using a file (movies.txt)
		SymbolGraph sg = new SymbolGraph("movies.txt", "/");
		Graph G = sg.G();
		int input;

		// Displaying Menu and ask the user input to search movies starred by particular
		// actor or by two actors or to exit the menu
		do {
			System.out.println(
					"\n\nMenu\n1. Find movies starred by a particular actor:\n2. Find movies starred by two actors:\n3. Exit.\n");
			try {
				input = StdIn.readInt();
			} catch (Exception e) {
				input = 0;
			}

			// Switching cases as per user input
			switch (input) {
			// Search movies by a particular actor
			case 1:
				System.out.println("Enter Actor's name:");
				StdIn.readLine();
				String source = StdIn.readLine();
				// Check if SymbolGraph contains the movie(s) by the actor specifies by the user
				if (sg.contains(source)) {
					int s = sg.index(source);
					// Displaying all the movies by actor
					for (int v : G.adj(s)) {
						StdOut.println("   " + sg.name(v));
					}
				} else {
					StdOut.println("input does not contain '" + source + "'\n");
				}
				break;
			// Search movies by two actors
			case 2:
				// Asking for user input
				StdIn.readLine();
				System.out.println("Enter first Actor's name:");
				String source1 = StdIn.readLine();
				System.out.println("Enter second Actor's name:");
				String source2 = StdIn.readLine();
				int s1 = 0, s2 = 0;
				// Checking if SymbolGraph contains movies by the actors specified
				if (sg.contains(source1)) {
					s1 = sg.index(source1);
				}
				if (sg.contains(source2)) {
					s2 = sg.index(source2);
				}

				// Loop to check all the movies by first actor
				for (int v1 : G.adj(s1)) {
					// Loop to check all the movies by second actor
					for (int v2 : G.adj(s2)) {
						// Displaying common movies by both actors
						if (v1 == v2) {
							System.out.println(sg.name(v1));
						}
					}
				}
				break;
			// Exit the menu
			case 3:
				System.out.println("Bye...");
				break;
			// For invalid input
			default:
				System.out.println("Invalid input!");

			}
		} while (input != 3);
	}

	/*
	 * Task 5 Reading one million check-seq reads from the file then partition them
	 * into four equal sub lists and store them into files (A.dat, B.dat, C.dat,
	 * D.dat respectively). then sort each sublists and store them into (AS.dat,
	 * BS.dat,CS.dat,DS.dat respectively) and at last merge all the sorted sublists
	 * and store them in a new file (Chip-seq-reads-1M-sorted.dat)
	 */
	void task5() {
		// Reading data from file
		In in = new In("./src/ChIP-seq-reads-1M.dat");
		ArrayList<String> list = new ArrayList<String>();

		long start = System.currentTimeMillis();
		// adding data into list
		while (!in.isEmpty()) {
			list.add(in.readLine());
		}

		int size = list.size();
		// Partition the list into four sublists
		ArrayList<String> list1 = new ArrayList<String>(list.subList(0, size / 4));
		ArrayList<String> list2 = new ArrayList<String>(list.subList(size / 4, size / 2));
		ArrayList<String> list3 = new ArrayList<String>(list.subList(size / 2, size / 2 + size / 4));
		ArrayList<String> list4 = new ArrayList<String>(list.subList(size / 2 + size / 4, size));

		// Storing the sublists into files
		saveToFile("./src/A.dat", list1);
		saveToFile("./src/B.dat", list2);
		saveToFile("./src/C.dat", list3);
		saveToFile("./src/D.dat", list4);

		// Sorting the sublists
		Collections.sort(list1);
		Collections.sort(list2);
		Collections.sort(list3);
		Collections.sort(list4);

		// Saving the sorted sublists into files
		saveToFile("./src/AS.dat", list1);
		saveToFile("./src/BS.dat", list2);
		saveToFile("./src/CS.dat", list3);
		saveToFile("./src/DS.dat", list4);

		// Merging the Sublists into a single list
		ArrayList<String> sortedList = new ArrayList<String>();
		sortedList.addAll(list1);
		sortedList.addAll(list2);
		sortedList.addAll(list3);
		sortedList.addAll(list4);

		Collections.sort(sortedList);

		// Saving the sorted list into a file
		saveToFile("./src/Chip-seq-reads-1M-sorted.dat", sortedList);

		long elapsed = System.currentTimeMillis() - start;

		// Displaying the CPU time
		System.out.println("CPU time: " + elapsed + " (milliseconds)");

	}

	/*
	 * Task 6 Reading the chip-seq reads from a file and store them into a BTree as
	 * they appear. List the BTree in in-order traversal and store the output into a
	 * file (BTree.dat) and display CPU time
	 */
	void task6() {
		// Reading data from file
		In in = new In("./src/ChIP-seq-reads-1M.dat");

		// Initializing variables and objects
		BTree<Integer, String> bTree = new BTree<Integer, String>();
		ArrayList<String> list = new ArrayList<String>();
		int i = 0, j = 0;

		long start = System.currentTimeMillis();

		// Reading and putting every read from file into BTree
		while (!in.isEmpty()) {
			bTree.put(i++, in.readLine());
		}

		// Traversing BTree nodes in in-order and adding them into a list
		while (j < i) {
			list.add(bTree.get(j++));
		}

		// Saving the list into a file
		saveToFile("./src/B-tree.dat", list);
		long elapsed = System.currentTimeMillis() - start;

		// Displaying CPU time
		System.out.println("CPU time: " + elapsed + " (milliseconds)");

	}

	// main function
	public static void main(String[] args) {
		Playground playground = new Playground();
		System.out.println("\nTask 1");
		playground.task1();
		System.out.println("\nTask 2a");
		playground.task2a();
		System.out.println("\nTask 2b");
		playground.task2b();
		System.out.println("\nTask 3");
		playground.task3();
		System.out.println("\nTask 4");
		playground.task4();
		System.out.println("\nTask 5");
		playground.task5();
		System.out.println("\nTask 6");
		playground.task6();
	}

	/*
	 * @param path to store the file including file name
	 * 
	 * @param list to store it into the file.This method is to save a list into a
	 * file
	 *
	 */
	void saveToFile(String path, ArrayList<String> list) {
		try {
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.close();
			fos.close();

			System.out.println("File saved successfully to " + path + "!");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
