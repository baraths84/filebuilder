package com.macys.selection.xapi.list.data.converters;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class TestUtils {

  private TestUtils() {
  }

  public static String readFile(String filePath) throws IOException {
    InputStream in = null;
    try {
        in = ClassLoader.getSystemClassLoader().getResourceAsStream(filePath);
        return IOUtils.toString(in);
    } finally {
        if (in != null) { in.close();}
    }
  }

}
