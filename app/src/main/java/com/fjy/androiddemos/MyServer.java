package com.fjy.androiddemos;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

public class MyServer extends NanoHTTPD {
    private final static int PORT = 8080;
    private Context mContext;
    public MyServer(Context context) throws IOException {
        super(PORT);
        this.mContext = context;
        start();
        System.out.println( "\nRunning! Point your browers to http://localhost:8080/ \n" );
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        File file = new File(mContext.getExternalFilesDir("unzip").getAbsolutePath()+uri);
        try {
            return newChunkedResponse(Response.Status.OK, MIME_HTML, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_HTML,get404page(uri));
    }
    String get404page(String uri){
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");
        builder.append("404 -- Sorry, Can't Found "+ uri + " !");
        builder.append("</body></html>\n");
        return builder.toString();
    }
}