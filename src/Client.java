// A Java program for a Client
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    // initialize socket and input output streams


    //CONNECT 35.40.142.189 8533
    //35.40.130.127
    //port 8533

    private Socket socket = null;
    private DataInputStream input = null;
    private DataInputStream server = null; //newly defined
    private DataOutputStream out = null;
    private FileInputStream inputstream = null;

    // constructor to put ip address and port
    public void download() {
        String line = "";
        String filename = "test.txt";

        try {
            while (!(line = server.readUTF()).equals("Over:")) {
                System.out.println(line);
                File f = new File(line);


            }
        } catch (IOException e) {
        }

    }

    public void listen(){
        String line = "";
        try {
            while (!(line = server.readUTF()).equals("Over")) {

                System.out.println(line);

            }
        } catch (IOException i) {
            System.out.println(i);
        }
    }
    public void recieve(){
        String line = "";
        try {
            String fileName ="";
            fileName = server.readUTF();
            File files = new File("ClientFiles/" + fileName);
            PrintWriter writer = new PrintWriter("ClientFiles/"+fileName, "UTF-8");


            while (!(line = server.readUTF()).equals("Over")) {
                writer.println(line);
            }
            writer.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    private void returnFile(String filename, DataOutputStream out) throws IOException {
        //File dir = new File("Files");
        File file = null;
        //File [] list = dir.listFiles();
        //for (File f: list) {
        //if (f.getName().equals(filename)) {
        file = new File("ClientFiles/" + filename);
        //  }
        //}
        if (file !=null) {
            try {
                Scanner scan = new Scanner(file);
                out.writeUTF(filename);
                String l;
                while (scan.hasNextLine()) {
                    l = scan.nextLine();
                    //System.out.println(l);
                    out.writeUTF(l);

                }
                out.writeUTF("Over");


            } catch (Exception e) {
                System.out.print(e);
            }
        } else {
            out.writeUTF("File not found");
        }
    }

    public Client() {
        // establish a connection
        try {

            Scanner input = new Scanner(System.in);
            String data = input.nextLine();
            String split[] = data.split("\\s+");
            String command = split[0];
            String ip = split[1];
            int port;
            port = Integer.parseInt(split[2]);


            socket = new Socket(ip, port);
            //socket = new Socket(address, port);

            System.out.println("Connected");

            // takes input from terminal
            String termInput;
            server = new DataInputStream(socket.getInputStream());
            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("______________________________________________________________");
            System.out.println("ENTER A COMMAND:");

            while (!(termInput = input.nextLine()).equals("QUIT")) {


                out.writeUTF(termInput);

                if(termInput.contains("RETRIEVE")) {
                    recieve();
                }else
                if(termInput.equals("LIST")) {
                    listen();
                }else if(termInput.contains("STORE")){
                    String [] a = termInput.split("\\s+");
                    returnFile(a[1],out);
                    //System.out.println(a[1]);

                }
                if(termInput.equals("TERMINATE")){
                    //out.writeUTF(termInput);
                    System.exit(0);
                }
                System.out.println("______________________________________________________________");
                System.out.println("ENTER A COMMAND:");


            }
            socket.close();
            out.close();
            //out.writeUTF(termInput);


        } catch (Exception u) {
            System.out.println("An error occurred connecting to the server, please try again.");
            //System.out.println(u);

        }




        // string to read message from input
        //String line = "";

        // keep reading until "Over" is input


        // close the connection
        // try {
        //     // input.close();
        //     out.close();
        //     socket.close();

        // } catch (IOException i) {
        //     System.out.println(i);

        // }
        // String in;
        // while (true) {
        //     try {
        //         in = server.readUTF();
        //         if (input.equals("Hey")) {
        //             System.out.println("");

        //         }
        //     } catch (IOException e) {

        //     }
        // }

    }
    public static void main(String args[]) {

        while (true) {
            System.out.println("To Connect to a server, enter 'CONNECT' followed by the IP and server port");
            Client client = new Client();
        }

    }
}