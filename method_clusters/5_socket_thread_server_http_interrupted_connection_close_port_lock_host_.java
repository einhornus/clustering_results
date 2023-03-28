public void close () {
		Connection[] connections = this.connections;
		if (INFO && connections.length > 0) info("kryonet", "Closing server connections...");
		for (int i = 0, n = connections.length; i < n; i++)
			connections[i].close();
		connections = new Connection[0];

		ServerSocketChannel serverChannel = this.serverChannel;
		if (serverChannel != null) {
			try {
				serverChannel.close();
				if (INFO) info("kryonet", "Server closed.");
			} catch (IOException ex) {
				if (DEBUG) debug("kryonet", "Unable to close server.", ex);
			}
			this.serverChannel = null;
		}

		UdpConnection udp = this.udp;
		if (udp != null) {
			udp.close();
			this.udp = null;
		}

		// Select one last time to complete closing the socket.
		synchronized (updateLock) {
			selector.wakeup();
			try {
				selector.selectNow();
			} catch (IOException ignored) {
			}
		}
	}
--------------------

public void run()
	{
		String received;
		do
		{
			//menerima pesan dari client
			//melalui socket input stream..
			received = input.nextLine();
			//Menampilkan kembali pesan ke client
			//melalui socket output stream...
			output.println("ECHO: " + received);
			//ulangi sampai QUIT
		}while (!received.equalsIgnoreCase("QUIT"));
		try
		{
			if (client!=null)
			{
				System.out.println(
						"Menutup koneksi...sukses..");
				client.close();
			}
		}
		catch(IOException ioEx)
		{
			System.out.println("Gagal menutup koneksi!");
		}
	}
--------------------

public lookupHandlerThread (Socket socket) {
		super("lookupHandlerThread");
		this.socket = socket;
		this.sock = sock;
		System.out.println("Created new Look up Thread to handle client");
	}
--------------------

