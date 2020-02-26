package javacore.volume2.io;

import java.io.*;

public class InputStreamOutputStream {
    public static void main(String[] args) throws IOException {
        // 读入一个字节
        System.out.println("读入一个字节：");
        InputStream inputStream0 = getInputStream();
        OutputStream outputStream0 = getOutputStream(0);
        int i;
        while ((i = inputStream0.read()) > -1) {
            System.out.print(i + " ");
            outputStream0.write(i);
        }

        outputStream0.close();
        inputStream0.close();

        // 读入一个字节数组
        System.out.println("\n读入一个字节数组");
        InputStream inputStream1 = getInputStream();
        OutputStream outputStream1 = getOutputStream(1);
        byte[] bytes = new byte[1024];
        int len;
        while ((len = inputStream1.read(bytes)) > -1) {
            for (int l = 0; l < len; l++) {
                System.out.print(bytes[l] + " ");
            }
            outputStream1.write(bytes, 0, len);
        }
        outputStream1.close();
        inputStream1.close();

    }

    static InputStream getInputStream() throws IOException {
        return new FileInputStream("javacore-volume2/javacore-volume2-02io/src/main/resources/InputStreamOutputStream.txt");
    }

    static OutputStream getOutputStream(int num) throws IOException {
        return new FileOutputStream("javacore-volume2/javacore-volume2-02io/src/main/resources/InputStreamOutputStream_copy" + num + ".txt");
    }
}
