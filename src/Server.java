import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server
{
    //initialize socket and input stream 
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream in       =  null;

    // constructor with port 
    public Server(int port)
    {
        // starts server and waits for a connection 
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            // takes input from the client socket 
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            String line = "";

            // reads message from client until "Over" is sent 
            while (!line.equals("QUIT"))
            {
//                try
//                {
                String lines[];
                String File;
                line = in.readUTF();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                if (line.contains("LIST")){

                    listFiles(out);

                    //returnFile("testPlan.txt", out, socket);
                }
                if (line.contains("RETRIEVE")) {
                    String [] a = line.split("\\s+");
                    returnFile(a[1], out);

                }
                if (line.contains("STORE")) {
                    out.writeUTF(line.substring(line.indexOf(" ")));
                    recieve();
                }

                System.out.println(line);

//                }
//                catch(IOException i)
//                {
//                    System.out.println(i);
//                }
            }
            System.out.println("Closing connection");

            // close connection
            server.close();

        }
        catch(IOException i)
        {

        }
    }

    public static void main(String args[])
    {
        Server server;
        while (true) {
            server = new Server(8533);
            System.out.println("Closing connection");
        }
    }
    public void listFiles(DataOutputStream out) throws IOException{
        File dir = new File("ServerFiles");

        out.writeUTF("Select a file: \n");
        File [] list = dir.listFiles();
        for (File file : list) {
            System.out.println(file.getName());
            out.writeUTF(file.getName() + '\n');

        }
        out.writeUTF("Over");

    }
    private void returnFile(String filename, DataOutputStream out) throws IOException {
        File dir = new File("ServerFiles");
        File file = null;
        File [] list = dir.listFiles();
        for (File f: list) {
            if (f.getName().equals(filename)) {
                file = new File("ServerFiles/" + filename);
                System.out.println("got here1");
            }

        }
        if (file !=null) {

            try {
                Scanner scan = new Scanner(file);
                out.writeUTF(filename);
                while (scan.hasNextLine()) {

                    out.writeUTF(scan.nextLine() + "\n");

                }
                out.writeUTF("Over");
            } catch (Exception e) {

                return;
            }
        } else {
            out.writeUTF("File not found");
        }
        System.out.println("got here");
        //out.writeUTF("Over");

    }
    public void recieve(){
        String line = "";
        System.out.println("1");
        try {
            String fileName ="";

            fileName = in.readUTF();

            File files = new File("ServerFiles/" + fileName);
            PrintWriter writer = new PrintWriter("ServerFiles/"+fileName, "UTF-8");


            while (!(line = in.readUTF()).equals("Over")) {
                writer.println(line);
                System.out.println(line);
            }
            System.out.println("Finished");
            writer.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }
} 