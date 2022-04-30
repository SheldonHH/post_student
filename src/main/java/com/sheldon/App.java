package com.sheldon;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class App {
    final static CloseableHttpClient httpClient = HttpClients.createDefault();
    public static void simpleGet() throws Exception{
        HttpGet request = new HttpGet("http://localhost:8080/Students");
        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
//        System.out.println(EntityUtils.toString(entity));

        if(response.getStatusLine().getStatusCode() != 200){
            System.out.println("Connection is bad" + response.getStatusLine().getStatusCode() );
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        List<Student> studList = new ArrayList<Student>();
        int counter = 0;

        studList = mapper.readValue(EntityUtils.toString(entity), new TypeReference<List<Student>>() {});
        counter = studList.size();
        System.out.println(counter + " Students found");

        for(Student student: studList){
            System.out.println(student.toString());
        }
    }
    public static void parameterizedGet(int studId) throws Exception{
        String URL = String.format("http://localhost:8080/Students?studId=%d", studId);
        HttpGet request = new HttpGet(URL);

        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        if(response.getStatusLine().getStatusCode() != 200){
            System.out.println("Connection is bad" + response.getStatusLine().getStatusCode() );
        }
    }

    public static void postAStudent() throws Exception{
        HttpPost request = new HttpPost("http://localhost:8080/Students");
        Student addStudent = new Student(12, "James", "Corey");

        ObjectMapper mapper = new ObjectMapper();
        StringEntity json = new StringEntity(mapper.writeValueAsString(addStudent), ContentType.APPLICATION_JSON);

        request.setEntity(json);
        CloseableHttpResponse response = httpClient.execute(request);

        if(response.getStatusLine().getStatusCode() != 200){
            System.out.println("Student is not added! "+response.getStatusLine().getStatusCode() );
        }

    }
//    https://youtu.be/aJc166sLjuQ
    public static void main(String []args) throws Exception{
//        simpleGet();
         postAStudent();
          parameterizedGet(42);
    }

}
