package com.herringbone.utility;

import com.herringbone.exception.StockException;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

@UtilityClass
@Log
public class PrintUtility {

    public String fileToString(InputStream is) throws IOException
    {
        @Cleanup InputStreamReader isr = new InputStreamReader(is);
        @Cleanup BufferedReader br = new BufferedReader(isr);
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            buffer.append(line);
            log.log(Level.INFO, line);
        }
        return buffer.toString();
    }

    @SneakyThrows
    public InputStream getFileAsIOStream(final String fileName)
    {
        InputStream ioStream = Class.forName("com.herringbone.utility.PrintUtility")
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (ioStream == null) {
            throw new StockException(fileName + " is not found");
        }
        return ioStream;
    }
}
