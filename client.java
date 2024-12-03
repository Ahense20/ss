import java.util.Scanner;
import java.net.*;
import java.io.*;

public class client implements Runnable {

	private static String address;
	private static int port;
	private static String command;
	private static int numRequests;
	private static long[] turnaroundTimes;
	private int threadIndex;
	private static Socket clientSocket;
	private static PrintWriter writer;
	private static BufferedReader reader;

	public client(int threadIndex) {
		this.threadIndex = threadIndex;
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner scnr = new Scanner(System.in);

		//query user for IP address
		System.out.println("Please enter the IP address of the server: ");
		//fetch address
		String serverAddress = scnr.nextLine();


		//query user for port
		System.out.println("Please enter the server port: ");
		//fetch port
		int portAddress = Integer.parseInt(scnr.nextLine());


		//create socket
		Socket clientSocket = new Socket(serverAddress, portAddress);

		while(true) {
			//present menu of commands to run on server();
			System.out.println("Please select an option from the menu below (1-6): ");
			System.out.println("1. Date and Time\n 2. Uptime \n 3. Memory Use\n 4. Netstat \n 5.Current Users\n 6. Running Processes\n");
			//fetch command
			String command = scnr.nextLine();

			//query for number of requests on the server();
			System.out.println("Please enter number of requests to send (1, 5, 10, 15, 20 and 25): ");
			//fetch number
			int numRequests = scnr.nextInt();


			turnaroundTimes = new long[numRequests];
			Thread[] threads = new Thread[numRequests];

			//create threads
			for(int i = 0; i < numRequests; i++) {
				threads[i] = new Thread(new client(i));
			}

			//start threads
			for (Thread thread : threads) {
				thread.run();
			}

			//find turn around time per request
			//find total turn around time for all requests (sum them up)
			//find average turn around time
			long totalTime = 0;
			for (long time : turnaroundTimes) {
				System.out.println("The time per request is: " + time + " ms");
				totalTime= totalTime + time;
			}

			long averageTime = totalTime / numRequests;

			System.out.println("The total turn-around time is " + totalTime + " ms");
			System.out.println("The average turn-around time: " + averageTime + " ms");

		}//end while
	}//end main

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			threadHandler(command, threadIndex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}//end run

	private static void threadHandler(String command, int threadIndex) throws IOException {
		Socket socket = new Socket(address, port); //open socket
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //open buffered reader
		PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); //open PrintWriter

		long startTime = System.currentTimeMillis(); //initialize thread timer


		writer.println(command); // Send command to the server

		String response = reader.readLine(); // Read result from server

		long endTime = System.currentTimeMillis();
		long turnaroundTime = endTime - startTime;

		// Print the turnaround time for each thread
		System.out.println("Thread " + (threadIndex + 1) + ": Turnaround time = " + turnaroundTime + " ms");

		
		turnaroundTimes[threadIndex] = turnaroundTime;

		//close buffers
		reader.close();
		writer.close();
		socket.close();
	}


}//end class
