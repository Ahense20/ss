import java.net.*;
import java.util.Date;
import java.util.Scanner;
import java.io.*;
import java.lang.management.ManagementFactory;

public class server {


	public static void main(String args[]) throws IOException
	{
		Scanner scnr = new Scanner(System.in);

		//query user for port number
		System.out.println("Please enter the port number: ");
		//fetch port number
		int port = scnr.nextInt();

		//create server socket with port
		try (ServerSocket serverSocket = new ServerSocket(port);){
			System.out.println("Socket successfully created at port " + port);

			while (true) {
				// Accept connection from client
				Socket clientSocket = serverSocket.accept();
				System.out.println("New client connected");

				// Open buffers
				try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

					// Fetch command client sent
					String command = in.readLine();
					if (command != null) {
						System.out.println("Received command: " + command); // Debug print
					}
					else {
						System.out.print("No command recieved");
					}
					String response;

					switch (command) {
					case "1":
						System.out.print("reaching switch");
						response = new Date().toString();
						break;
					case "2":
						response = getUptime();
						break;
					case "3":
						response = runCommand("systeminfo | findstr /C:\"Available Physical Memory\"");
						break;
					case "4":
						response = runCommand("netstat");
						break;
					case "5":
						response = runCommand("query user");
						break;
					case "6":
						response = runCommand("tasklist");
						break;
					default:
						response = "Invalid command";
					}

					// Sending the response back to the client
					out.println(response);

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					// Closing the client connection
					try {
						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}	

	private static String getUptime() {
		long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
		return "Uptime in ms: " + uptime;
	}


	private static String runCommand(String command) 
	{
		StringBuilder newOutput = new StringBuilder();
		try 
		{
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) 
			{
				newOutput.append(line).append("\n");
			}//end inner while
		} //end try
		catch (IOException e) 
		{
			return "Error executing system command.";
		}//end catch
		return newOutput.toString();
	}//endnrunSystemCommand

	//socket.close();
}//end class
