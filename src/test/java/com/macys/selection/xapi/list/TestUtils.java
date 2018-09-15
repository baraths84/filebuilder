package com.macys.selection.xapi.list;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.io.IOUtils;

public class TestUtils {

  private TestUtils() {
  }

  public static void main(String[] args) {
      try {
          System.out.println(URLDecoder.decode("14_2hj7z%2B4S9iwBH1fmxPwWxx0mJzauKTt3grN%2B7J%2Bgv%2FC2CSIn0CGHSdY3ZulnwVFIvZQKPhXsQu9D7mJJ9G7N%2Bw%3D%3D", "UTF-8"));

      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }
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
