
import java.util.Scanner;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import java.util.*;
import java.io.*;

import com.google.gson.JsonElement;

class testSignal {


    public static void main(String[] args) {
        
		
		System.out.println("Ready"); // Display the string.

		final  boolean disconnectExpected  = false;
        // Connect to the server
       final HubConnection conn = new HubConnection("**** Your  Hub******");

        
        // Hub Proxy
       final HubProxy proxy = conn.createHubProxy("*** Hub Proxy Name ***");

		

		
		proxy.subscribe(new Object() {
            @SuppressWarnings("unused")
            public void ha(String HC, String AC) {

                lightsOut(HC, AC);
                System.out.println("Home Automation Command Recieved");

            }
        });

        conn.connected(new Runnable() {
            @Override
            public void run() {
                System.out.println("CONNECTED");
                proxy.invoke("Registerme", "HA");



            }
        });

        conn.received(new MessageReceivedHandler() {

            @Override
            public void onMessageReceived(JsonElement json) {
                System.out.println("RAW received message: " + json.toString());
            }
        });

		conn.closed(new Runnable() {
            @Override
            public void run() {
                if (disconnectExpected) {
                    // disconnect was expected, for example, application is being shut down
                    return;
                }
                new Timer(false).schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Attemmpted Disconnect.... Trying to Reconnect!");
                        conn.start().done(new Action<Void>() {
                            @Override
                            public void run(Void v) throws Exception {
                                //  listener.onConnectionReopened();
                            }
                        });
                    }
                }, 5000);
            }
        });


        // ON Error Call
        conn.error(new ErrorCallback() {

            @Override
            public void onError(Throwable error) {
                System.err.println("There was an error communicating with the server.");
                System.err.println("Error detail: " + error.toString());

                error.printStackTrace(System.err);
            }
        });
		
		conn.start()
			.done(new Action<Void>() {

                @Override
                public void run(Void obj) throws Exception {
                    System.out.println("Done Connecting!");
                }
            });




        try {
            while (true) {


                Thread.sleep(10 * 1000);

                proxy.invoke("heartupdate", "52369");

                Thread.sleep(300 * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		


		conn.stop();
   
    }


	
	public static void lightsOut(String HC, String AC){
	
	try{
		ProcessBuilder pb = new ProcessBuilder("./lights.sh", HC, AC);
        pb.redirectErrorStream(true);
        Process ps = pb.start();
         		
	    }
		 catch(IOException ioe)
		{
				System.out.println("Error Caught!! on input and output");
				System.out.println(ioe);
				
		}

	}

}
