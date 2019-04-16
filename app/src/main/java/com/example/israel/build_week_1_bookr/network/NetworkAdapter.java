package com.example.israel.build_week_1_bookr.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// TODO IMPORTANT. improve failed attempt reporting
public class NetworkAdapter {
    public static final int READ_TIMEOUT = 3000;
    public static final int CONNECT_TIMEOUT = 3000;

    @WorkerThread
    @Nullable
    public static String httpRequestGET(String urlStr) {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Connection error. Response code: " + Integer.toString(responseCode));
            }

            inputStream = httpURLConnection.getInputStream();
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String resStr;
                while ((resStr = reader.readLine()) != null) {
                    builder.append(resStr);
                }

                return builder.toString(); // success
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SecurityException e) { // for future reference. if ever i forgot to put internet permission again!!
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return null;
    }

    @WorkerThread
    @Nullable
    public static String httpRequestPOST(String urlStr, String requestBody) {
        HttpURLConnection httpURLConnection = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpURLConnection.setRequestMethod("POST");
            //httpURLConnection.connect();
            outputStream = httpURLConnection.getOutputStream();
            if (outputStream != null) {
                outputStream.write(requestBody.getBytes());
            }

            inputStream = httpURLConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String resStr;
                while ((resStr = reader.readLine()) != null) {
                    builder.append(resStr);
                }
            }

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Connection error. Response code: " + Integer.toString(responseCode));
            }

            return builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SecurityException e) { // for future reference. if ever i forgot to put internet permission again!!
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return null;

    }

    @WorkerThread
    @NonNull
    public static Result httpRequestPOSTJson(String urlStr, JSONObject jsonObject) {
        HttpURLConnection httpURLConnection = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        Result result = new Result();
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestMethod("POST");
            //httpURLConnection.connect();
            outputStream = httpURLConnection.getOutputStream();
            if (outputStream != null) {
                outputStream.write(jsonObject.toString().getBytes());
            }

            int responseCode = httpURLConnection.getResponseCode();
            result.responseCode = responseCode;
            if (responseCode < HttpURLConnection.HTTP_OK || responseCode > HttpURLConnection.HTTP_MULT_CHOICE - 1) {
                throw new IOException("Connection error. Response code: " + Integer.toString(responseCode));
            }

            inputStream = httpURLConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String resStr;
                while ((resStr = reader.readLine()) != null) {
                    builder.append(resStr);
                }
            }

            result.resultObj = builder.toString();
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SecurityException e) { // for future reference. if ever i forgot to put internet permission again!!
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return result;
    }

    @WorkerThread
    @Nullable
    public static Bitmap httpImageRequestGET(String urlStr) {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Connection error. Response code: " + Integer.toString(responseCode));
            }

            inputStream = httpURLConnection.getInputStream();
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SecurityException e) { // for future reference. if ever i forgot to put internet permission again!!
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return null;
    }

    public static class Result {
        /** The request didn't have a chance to get to a getResponseCode line*/
        public static final int INVALID_RESPONSE_CODE = -1;

        public int responseCode = INVALID_RESPONSE_CODE;
        public Object resultObj;
    }


}
