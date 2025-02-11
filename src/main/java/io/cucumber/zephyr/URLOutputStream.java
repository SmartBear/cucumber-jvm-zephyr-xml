package io.cucumber.zephyr;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

public class URLOutputStream extends OutputStream {
    private final OutputStream out;

    public URLOutputStream(URL url) throws IOException {
        this(url, "PUT", Collections.emptyMap());
    }
    public URLOutputStream(URL url, String method, Map<String, String> headers) throws IOException {
        HttpURLConnection urlConnection;
        if (url.getProtocol().equals("file")) {
            File file = new File(url.getFile());
            ensureParentDirExists(file);
            out = new FileOutputStream(file);
        } else if (url.getProtocol().startsWith("http")) {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setDoOutput(true);
            for (Map.Entry<String, String> header : headers.entrySet()) {
                urlConnection.setRequestProperty(header.getKey(), header.getValue());
            }
            out = urlConnection.getOutputStream();
        } else {
            throw new IllegalArgumentException("URL Scheme must be one of file,http,https. " + url.toExternalForm());
        }
    }

    private void ensureParentDirExists(File file) throws IOException {
        if (file.getParentFile() != null && !file.getParentFile().isDirectory()) {
            boolean ok = file.getParentFile().mkdirs() || file.getParentFile().isDirectory();
            if (!ok) {
                throw new IOException("Failed to create directory " + file.getParentFile().getAbsolutePath());
            }
        }
    }

    @Override
    public void write(byte[] buffer, int offset, int count) throws IOException {
        out.write(buffer, offset, count);
    }

    @Override
    public void write(byte[] buffer) throws IOException {
        out.write(buffer);
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }
}
