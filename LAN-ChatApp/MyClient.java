import java.net.*;
import java.io.*;
import java.util.*;

public class MyClient implements Runnable
{

	public static Socket s;
	String input = null;

	public void run()
	{
		try
		{
			InputStreamReader is = new InputStreamReader(s.getInputStream());
			BufferedReader br = new BufferedReader(is);

			do
			{
					input = br.readLine();
					System.out.println("Server: " + input);
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
			s = new Socket(InetAddress.getLocalHost(),6666);

			MyClient mc = new MyClient();
			Thread t1 = new Thread(mc);
			t1.start();

			OutputStreamWriter ow = new OutputStreamWriter(s.getOutputStream());
			BufferedWriter bw = new BufferedWriter(ow);

			String output = null;

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
		}
		catch(Exception e) { }
	}
}