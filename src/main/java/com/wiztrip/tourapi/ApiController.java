package com.wiztrip.tourapi;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@RestController
@RequestMapping("/tour")
public class ApiController {

    @GetMapping("/api")
    public String allowBasic() {
        StringBuilder result = new StringBuilder();
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/KorService1/areaBasedList1");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=oQJZLhk4tLq3xQz%2BVsJ%2BZaaOuqTdiYjEoX%2Fy2Y8ULJatKQ1StfLkBcJ8HDSt%2BSi7DSnAn4Nkl%2BbbelVkeRYl3Q%3D%3D");
          //  urlBuilder.append("&"+ URLEncoder.encode("pageNo","UTF-8") + URLEncoder.encode("1","UTF-8"));
        //    urlBuilder.append("&"+ URLEncoder.encode("numOfRows","UTF-8") + URLEncoder.encode("10","UTF-8"));
            urlBuilder.append("&MobileOS=ETC&MobileApp=AppTest");
            urlBuilder.append("&_type=json");
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <=300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String line;
            while((line = rd.readLine()) != null) {
                result.append(line + "\n");
            }
            rd.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result + " ";
    }
}
