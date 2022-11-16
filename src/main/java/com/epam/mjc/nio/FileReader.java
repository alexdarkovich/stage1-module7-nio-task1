package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class FileReader {

    public static String returnValueFromMap(String str, Map<String, String> map) {

        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        String toReturn = null;

        for (Map.Entry<String, String> pair : entrySet) {
            if (str.equals(pair.getKey())) {
                toReturn = pair.getValue();
            }
        }
        return toReturn;
    }

    public Profile getDataFromFile(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        try (RandomAccessFile aFile = new RandomAccessFile(file, "r");
             FileChannel inChannel = aFile.getChannel();) {

            long fileSize = inChannel.size();

            ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
            inChannel.read(buffer);
            buffer.flip();

            for (int i = 0; i < fileSize; i++) {
                stringBuilder.append( (char) buffer.get());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str = stringBuilder.toString();
        String[] lines = str.split("\r?\n|\r");
        Map<String, String> map = new HashMap<>();

        for (String line : lines) {
            String[] newL = line.split(": ");
            map.put(newL[0], newL[1]);
        }

        return new Profile(returnValueFromMap("Name", map),
                Integer.parseInt(returnValueFromMap("Age", map)),
                returnValueFromMap("Email", map),
                (long) Integer.parseInt(returnValueFromMap("Phone", map)));

    }
}
