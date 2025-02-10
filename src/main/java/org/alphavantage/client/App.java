package org.alphavantage.client;

import com.opencsv.CSVWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App
{

    private static final String baseUrl = "https://www.alphavantage.co/query";
    private static String apiKey = "QOGVHCSIFK7MYBC3";
    private static String theTickers = "CRYPTO:BTC";
    //private static String dateFrom = "20220501T0000";
    //private static String dateTo   = "20220601T0000";

    //private static String filePath =  "C:\\Users\\jrobes\\Desktop\\AphaVantage\\";
    private static String filePath =  "C:\\Users\\COTERENA\\Desktop\\AphaVantage\\";

    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello World!" );

        //getSentimentByDate(apiKey, theTickers, "20220501T0000", "20220601T0000");
        //getSentimentByDate(apiKey, theTickers, "20220601T0000", "20220701T0000");
        //getSentimentByDate(apiKey, theTickers, "20220701T0000", "20220801T0000");
        //getSentimentByDate(apiKey, theTickers, "20220801T0000", "20220901T0000");
        //getSentimentByDate(apiKey, theTickers, "20220901T0000", "20221001T0000");
        //getSentimentByDate(apiKey, theTickers, "20221001T0000", "20221101T0000");
        //getSentimentByDate(apiKey, theTickers, "20221101T0000", "20221201T0000");
        //getSentimentByDate(apiKey, theTickers, "20221201T0000", "20230101T0000");

        //String parametros = "?function=NEWS_SENTIMENT&tickers=CRYPTO:BTC&time_from=20240201T0130&time_TO=20240204T0130&limit=500&apikey=QOGVHCSIFK7MYBC3";
        //String parametros2 = "?function=NEWS_SENTIMENT&tickers=CRYPTO:BTC&time_from=20220101T0000&time_to=20220201T0000&limit=1000&apikey=QOGVHCSIFK7MYBC3";

        JSONObject inputJSON = readJSONFromFile(theTickers, "20220501T0000", "20220601T0000");
        writeJSON2File(inputJSON, theTickers, "20220501T0000", "20220601T0000");

    }

    private static void writeJSON2File(JSONObject inputJSON, String theTickers, String from, String to) throws IOException {
        String filePath = getCsvFilename(theTickers, from, to);
        CSVWriter writer = new CSVWriter(new FileWriter(filePath));
        JSONArray jsonArray = inputJSON.getJSONArray("feed");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject noticia = jsonArray.getJSONObject(i);
            String time = noticia.getString("time_published");
            String summary = noticia.getString("summary");
            JSONArray ticker_sentimentArray = noticia.getJSONArray("ticker_sentiment");
            System.out.println("ticker_sentimentArray: " + ticker_sentimentArray.length());

            for(int j = 0; j < ticker_sentimentArray.length(); j++){
                JSONObject scores = ticker_sentimentArray.getJSONObject(j);
                //System.out.println(scores);
                String relevance_score = scores.getString("relevance_score");
                String ticker_sentiment_score = scores.getString("ticker_sentiment_score");
                String ticker_sentiment_label = scores.getString("ticker_sentiment_label");
                writer.writeNext(new String[]{time, ticker_sentiment_label, ticker_sentiment_score, relevance_score, summary});
            }


        }

            writer.close();





    }

    private static JSONObject readJSONFromFile(String tickers, String fr, String to) {
        //String filePath = "ruta/a/tu/archivo.json";
        String filePath = getJSONFilename(tickers, fr, to);
        JSONObject jsonObject = null;
        try {
            // Leer archivo JSON y convertirlo en String
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Crear el JSONObject a partir del contenido del archivo
            jsonObject = new JSONObject(content);

            // Mostrar el JSONObject
            System.out.println("El contenido del JSON es:");
            System.out.println(jsonObject.toString(4)); // Formateo con sangría de 4 espacios
            System.out.println("Leido correctamente:");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al leer el archivo JSON.");
        }
        return jsonObject;
    }



    public static JSONObject getSentimentByDate(String token, String tickers, String dateFrom, String dateTo){
        String apiUrl = baseUrl + "?function=NEWS_SENTIMENT&tickers=" + tickers + "&time_from=" + dateFrom + "&time_to=" + dateTo + "&limit=1000&apikey=" + apiKey;
        System.out.println();
        System.out.println(apiUrl);
        // Crear un cliente HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Crear la solicitud GET
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                //.header("Content-Type", "application/json")
                //.header("Authorization", "Bearer tu_token_aqui") // Si es necesario
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    int statusCode = response.statusCode();
                    switch (statusCode) {
                        case 200:
                            //System.out.println("Respuesta exitosa: \n" + response.body());
                            JSONObject resp = new JSONObject(response.body());
                            try{
                                double items= resp.getDouble("items");
                                System.out.println("Numero de items: " + items);
                                String rutaArchivo = getJSONFilename(tickers, dateFrom, dateTo);

                                        // + ;

                                // Usamos try-with-resources para asegurarnos de que los recursos se cierren correctamente
                                try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
                                    // Escribimos la cadena en el archivo
                                    writer.write(response.body());

                                    // Opcional: Escribir una nueva línea después del contenido
                                    writer.newLine();

                                    System.out.println("Los datos se han guardado correctamente en el archivo.");
                                } catch (IOException e) {
                                    // Manejo de excepciones en caso de que ocurra un error
                                    System.err.println("Ocurrió un error al intentar guardar el archivo: " + e.getMessage());
                                }

                            }catch (JSONException e){
                                System.err.println(response.body());
                            }
                            break;
                        case 400:
                            System.out.println("Solicitud incorrecta (400)");
                            break;
                        case 401:
                            System.out.println("No autorizado (401)");
                            break;
                        case 500:
                            System.out.println("Error del servidor (500)");
                            break;
                        default:
                            System.out.println("Código de respuesta: " + statusCode);
                    }
                })
                .join();



        return null;
    }

    private static String getJSONFilename(String ticks, String from, String to) {
        String dateF = from.substring(0,8);
        String dateT = to.substring(0,8);
        String tick;
        if(ticks.contains(":")){
            tick = ticks.replace(":", "-");
        }
        else {
            tick = ticks;
        }

        return filePath + "SENTIMENT-" + tick + "-" + dateF + "-" + dateT + ".json";
    }

    private static String getCsvFilename(String ticks, String from, String to) {
        String dateF = from.substring(0,8);
        String dateT = to.substring(0,8);
        String tick;
        if(ticks.contains(":")){
            tick = ticks.replace(":", "-");
        }
        else {
            tick = ticks;
        }

        return filePath + "SENTIMENT-" + tick + "-" + dateF + "-" + dateT + ".csv";
    }
}
