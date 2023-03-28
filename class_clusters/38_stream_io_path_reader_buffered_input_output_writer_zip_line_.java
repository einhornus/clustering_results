package com.ernestas.gaya.Util;

import com.ernestas.gaya.Exceptions.FileHandlingException;
import com.ernestas.gaya.Exceptions.GayaException;
import com.ernestas.gaya.Exceptions.InvalidFileException;
import com.google.common.base.Joiner;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FileStringifier {

    private FileStringifier() {}

    public static String fileToString(String path) throws GayaException {
        return fileToString(new File(path));
    }

    public static String fileToString(File file) throws GayaException {
        BufferedReader reader;
        try {
            FileReader fr = new FileReader(file);
            reader = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            throw new InvalidFileException("File not found.");
        }

        if (!file.isFile()) {
            throw new InvalidFileException("File is actually not a file.");
        }

        if (!file.canRead()) {
            throw new InvalidFileException("Could not read file. Check file permissions.");
        }

        return readerToString(reader);
    }

    public static String readerToString(BufferedReader reader) throws GayaException {
        List<String> lines = new LinkedList<String>();
        try {
            String line;
            while((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            throw new FileHandlingException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new FileHandlingException(e);
            }
        }

        return Joiner.on("").skipNulls().join(lines);
    }

    public static String fileToString(InputStream resourceAsStream) throws GayaException {
        return readerToString(new BufferedReader(new InputStreamReader(resourceAsStream)));
    }
}

--------------------

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * public PrintWriter(OutputStream out)
 * 
 * Parameters: 
 * ----------
 * 
 * out - An output stream
 */
public class PrintWriterDemo
{

	public static void main(String[] args) throws IOException
	{
		FileOutputStream fileOutputStream = null;
		PrintWriter printWriter = null;
		try
		{
			fileOutputStream = new FileOutputStream("myoutputfile.txt");
			/*
			 * Creates a new PrintWriter, without automatic
			 * line flushing, from an existing OutputStream.
			 */
			printWriter = new PrintWriter(fileOutputStream);

			int intValue = 13;
			double doubleValue = 67.8;
			printWriter.printf("i = %d and k = %f", intValue, doubleValue);
			/*
			 * Flushes the stream.
			 */
			printWriter.flush();

			System.out.println("Successfully written to the file."
					+ "please check the file \'myoutputfile.txt\'");

		}
		finally
		{
			if (fileOutputStream != null)
			{
				fileOutputStream.close();
			}
			if (printWriter != null)
			{
				printWriter.close();
			}
		}

	}
}

--------------------

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;


public class TryTest  {

    public static void main(String[] args) {
        String [] testNames = {"01"};
        PrintStream out = System.out;
        for (String n : testNames) {
            try {
                System.out.println("***** TEST FILE " + n + ".in *****");
                System.out.print(fileData(n + ".in"));
                System.out.println("***** OUTPUT FOR " + n + ".in *****");
                String logData = tryTest(n);
                System.setOut(out);
                System.out.print(logData);

                String outData = fileData(n+".out");
                if(logData.equals(outData)) {
                    out.println("***** TEST " + n + " passed!");
                } else {
                    System.out.println("**** CORRECT OUTPUT FOR " + n + ".in *****");
                    System.out.print(outData);
                    out.println("***** TEST " + n + " failed!");
                }
                System.out.println("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String tryTest(String testName) {
        try {
            FileInputStream fin = new FileInputStream(new File(testName + ".in"));
            System.setIn(fin);
            String logName = testName + ".log";
            PrintStream flog = new PrintStream(new FileOutputStream(new File(logName)));
            System.setOut(new PrintStream(flog));

            WordLadder.main(null);

            return fileData(logName);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String fileData(String fileName) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Scanner fileScanner = new Scanner(bufferedReader);

        while (fileScanner.hasNextLine())
            stringBuffer.append(fileScanner.nextLine() + "\n");

        fileScanner.close();
        return stringBuffer.toString();
    }


}

--------------------

