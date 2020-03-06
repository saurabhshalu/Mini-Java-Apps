import java.net.*;
import java.io.*;
import java.util.*;

public class MyServer implements Runnable
{
	public static Socket s;

	public void run()
	{
		try{
			InputStreamReader is = new InputStreamReader(s.getInputStream());
			BufferedReader br = new BufferedReader(is);
			String input=null;
			
			do
			{
					input = br.readLine();
					System.out.println("Client: " + input);
			}
			while(!(input.equals("bye")));

			br.close();
			is.close();
		}
		catch(Exception e) { }
	}
	public static void main(String args[])
	{
		try
		{
			
			ServerSocket ss = new ServerSocket(6666);
			s = ss.accept();
	
			MyServer ms = new MyServer();
			Thread t1 = new Thread(ms);
			t1.start();

			OutputStreamWriter ow = new OutputStreamWriter(s.getOutputStream());
			BufferedWriter bw = new BufferedWriter(ow);

			String output;

			Scanner sc = new Scanner(System.in);

			do
			{
				output = sc.nextLine();
				bw.write(output,0,output.length());
				bw.write("\n");
				bw.flush();
			}
			while(!output.equals("bye"));

			sc.close();
			bw.close();
			ow.close();
			s.close();
			ss.close();
		}
		catch(Exception e) { }
	}
}