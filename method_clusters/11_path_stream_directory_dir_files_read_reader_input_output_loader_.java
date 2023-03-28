public static void writeToChannel(ByteBuffer byteBuffer, WritableByteChannel channel) throws IOException {
        if (byteBuffer.isDirect() || (byteBuffer.remaining() <= WRITE_CHUNK_SIZE)) {
            while (byteBuffer.hasRemaining()) {
                channel.write(byteBuffer);
            }
        } else {
            // duplicate the buffer in order to be able to change the limit
            ByteBuffer tmpBuffer = byteBuffer.duplicate();
            try {
                while (byteBuffer.hasRemaining()) {
                    tmpBuffer.limit(Math.min(byteBuffer.limit(), tmpBuffer.position() + WRITE_CHUNK_SIZE));
                    while (tmpBuffer.hasRemaining()) {
                        channel.write(tmpBuffer);
                    }
                    byteBuffer.position(tmpBuffer.position());
                }
            } finally {
                // make sure we update byteBuffer to indicate how far we came..
                byteBuffer.position(tmpBuffer.position());
            }
        }
    }
--------------------

public void set(int index, String value) throws IOException {
			Writer writer = null;
			try {
				writer = new OutputStreamWriter(newOutputStream(index), Util.UTF_8);
				writer.write(value);
			} finally {
				Util.closeQuietly(writer);
			}
		}
--------------------

public void getCodeRead(){
		File file = new File("D:/workE/Kodilitek/src/zadanie/Zadanie.java");
		 
		try (FileInputStream fis = new FileInputStream(file)) {
 
			System.out.println("Total file size to read (in bytes) : "+ fis.available());
 
			int content;
			while ((content = fis.read()) != -1) {
				// convert to char and display it
				System.out.print((char) content);
			}
 
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
--------------------

