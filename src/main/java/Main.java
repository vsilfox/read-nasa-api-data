import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import java.io.*;

public class Main {

    public static void main(String[] args) {

        try {
            CloseableHttpClient client = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)
                            .setSocketTimeout(30000)
                            .setRedirectsEnabled(false)
                            .build()
                    )
                    .build();

            HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=iM33jhXEs4sVOEE3axbySnOCeY2geNkl2fAh3WIN");
            CloseableHttpResponse response = client.execute(request);
            NasaMedia nasaMedia = new ObjectMapper().readValue(response.getEntity().getContent(), NasaMedia.class);

            String url = nasaMedia.getUrl();
            String fileName = url.substring(url.lastIndexOf('/') + 1);
            String imageFormat = fileName.substring(fileName.lastIndexOf('.') + 1);

            HttpGet imageRequest = new HttpGet(url);
            CloseableHttpResponse imageResponse = client.execute(imageRequest);
            InputStream inputStream = imageResponse.getEntity().getContent();

            File file = new File(fileName);
            file.createNewFile();
            ImageIO.write(ImageIO.read(inputStream), imageFormat, file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
