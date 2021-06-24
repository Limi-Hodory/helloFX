package model;

import java.io.PrintWriter;
import java.net.Socket;

public class FlightConnector {


    private Socket socket = null;
    private PrintWriter out;

    public FlightConnector() {
        configureSocket();
    }


    private void configureSocket() {
        try {
            socket = new Socket("localhost", 5400);
            out = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void sendDataToFlight(String line) {
        if(socket != null&& out!=null){
            //send the line
            out.println(line);
            out.flush(); //clean buffer
        }
    }

}
