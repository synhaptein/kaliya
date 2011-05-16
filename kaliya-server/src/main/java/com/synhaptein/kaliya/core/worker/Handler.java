package com.synhaptein.kaliya.core.worker;

import com.synhaptein.kaliya.core.Information;
import com.synhaptein.kaliya.core.Message;
import com.synhaptein.kaliya.core.Server;

import java.net.Socket;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * Handle the security resquest from the flash client.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class Handler {

	/**
	 * Handle the security resquest from the flash client. If it does not ask
	 * for the crossdomain.xml, we add the client to the server as a normal
	 * client. The client must send '\n' to be add to the client list
	 * 
	 * @param p_server
	 *            manager server
	 * @param p_threadPool
	 *            thread pool
	 * @param p_clientSocket
	 *            client socket
	 * @param p_communicationBuffer
	 *            communication buffer
	 * @throws java.io.IOException
	 *             If the stream with the client fails
	 */
	public Handler(WorkerServer p_server, ExecutorService p_threadPool,
			Socket p_clientSocket, BlockingQueue<Message> p_communicationBuffer)
			throws IOException, InterruptedException {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(p_clientSocket.getInputStream()));
		OutputStream out = p_clientSocket.getOutputStream();

		boolean isSecurityRequest = false;
        char[] tab = new char[1];
        while (in.read(tab, 0, 1) != -1) {
            if (tab[0] == '<') {
                isSecurityRequest = true;
            }
            break;
        }
        
		// Check if we get a security request
		if (isSecurityRequest) {
			if (Information.isDebug()) {
				System.out.println("Sending crossdomain.xml");
			}

			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(Information.getCrossDomain().getBytes()));
			byte[] buffer = new byte[dis.available()];
			dis.readFully(buffer);
			out.write(buffer);
			p_clientSocket.close();
		} else {
            Worker worker = new Worker(p_server, p_clientSocket,
					p_communicationBuffer);
			p_threadPool.execute(worker);
		}
	}
}
