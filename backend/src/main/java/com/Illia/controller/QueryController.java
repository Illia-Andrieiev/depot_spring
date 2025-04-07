package com.Illia.controller;

import com.Illia.dto.QueryDTO;
import com.Illia.config.DatabaseConfig;
import com.Illia.dao.QueryDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class QueryController extends HttpServlet {
    private final QueryDAO queryService = new QueryDAO();
    private static final Logger logger = LogManager.getLogger(TripController.class);
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("Query controller loaded!");
        logger.info("Query controller loaded!");
    }
    private void sendJsonResponse(HttpServletResponse response, String status, String message) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> responseMap = new HashMap<>();
    responseMap.put("status", status);
    responseMap.put("message", message);
    String jsonResponse = mapper.writeValueAsString(responseMap);
    response.getWriter().write(jsonResponse);
}
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true"); 
        response.setStatus(HttpServletResponse.SC_OK);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Query POST request received");
        System.out.println("Query POST request received");
        request.setCharacterEncoding("UTF-8");
        BufferedReader reader = request.getReader();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> requestData = objectMapper.readValue(reader, Map.class);
        logger.info(requestData.toString());
        System.out.println(requestData.toString());
        String email = requestData.get("email");
        String type = requestData.get("type");
        System.out.println(type);
        logger.info(type);

        if (email == null || email.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Email is required");
            return;
        }

        if(type.equals("get_id")){
              try (Connection conn = DatabaseConfig.getConnection()) {
                logger.info("Connection opened");
            System.out.println("Connection opened");
            QueryDTO query = queryService.getQueryByID(conn, Integer.parseInt(requestData.get("editId")));

            if(query != null){ System.out.println("query received: " + query.toString());};

            if (query == null) {
                System.out.println("query not registered in DB");
                logger.info("query not registered in DB");
                sendJsonResponse(response, "error", "Wrong id.");
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                System.out.println("Sending query");
                logger.info("Sending query");
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                response.getWriter().write(mapper.writeValueAsString(query));
            }

            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                sendJsonResponse(response, "error", "SQL Error: " + e.getMessage());
            }
            
        }else if( type.equals("get_email")){
            try (Connection conn = DatabaseConfig.getConnection()) {
                System.out.println("Connection opened");
                logger.info("Connection opened");
                ArrayList<QueryDTO> query = queryService.getQueryByEmail(conn, email);
    
                if(query != null){ System.out.println("query received: " + query.toString());};
    
                if (query == null) {
                    logger.info("query is empty");
                    System.out.println("query is empty");
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(new ArrayList<QueryDTO>()));
                } else {
                    logger.info("Sending queries");
                    System.out.println("Sending queries");
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.registerModule(new JavaTimeModule());
                    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                    response.getWriter().write(mapper.writeValueAsString(query));
                }
    
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    sendJsonResponse(response, "error", "SQL Error: " + e.getMessage());
                }
        }else if( type.equals("get_new")){
            try (Connection conn = DatabaseConfig.getConnection()) {
                System.out.println("Connection opened");
                logger.info("Connection opened");
                ArrayList<QueryDTO> query = queryService.getNew(conn);
                if(query != null){ System.out.println("query received: " + query.toString());
                logger.info("query received: " + query.toString());};
    
                if (query == null) {
                    System.out.println("query is empty");
                    logger.info("query is empty");
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(new ArrayList<QueryDTO>()));
                } else {
                    logger.info("Sending queries");
                    System.out.println("Sending queries");
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.registerModule(new JavaTimeModule());
                    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                    response.getWriter().write(mapper.writeValueAsString(query));
                }
    
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    sendJsonResponse(response, "error", "SQL Error: " + e.getMessage());
                }
        }else if(type.equals("add")){
                QueryDTO newQuery = new QueryDTO();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                System.out.println("0");
                newQuery.setCarType(requestData.get("car_type"));
                System.out.println("1");
                newQuery.setDestination(requestData.get("destination"));
                System.out.println("2");
                newQuery.setEmail(email);
                System.out.println("3");
                newQuery.setDriverExperience(Integer.parseInt(requestData.get("driver_experience")));
                System.out.println("4");
                newQuery.setMaxCarMileage(Integer.parseInt(requestData.get("max_car_mileage")));
                System.out.println("5");
                newQuery.setStatus("new");
                System.out.println("6");
                newQuery.setStartDatetime(LocalDateTime.parse((requestData.get("start_datetime")), formatter));
                System.out.println("7");
                newQuery.setAddDatetime(LocalDateTime.now());
                System.out.println("8");
                newQuery.setStartLocation(requestData.get("start_location"));
                System.out.println("created driver_query: " + newQuery.toString());
                logger.info("created driver_query: " + newQuery.toString());
                if(newQuery.getAddDatetime().isAfter(newQuery.getStartDatetime())) {
                    System.out.println("Query already expired!");
                    logger.info("Query already expired!");
                } else {
                    queryService.createQuery(newQuery);
                }
               
    
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("Query created successfully");
        
        }else if(type.equals("delete")){
                System.out.println("trying delete");
                logger.info("trying delete");
                int id = Integer.parseInt(requestData.get("deleteId"));
                System.out.println("deleteId: " + id);
                logger.info("deleteId: " + id);
                queryService.deleteQuery(id);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Query deleted successfully");
        }else if(type.equals("update")){
            QueryDTO newQuery = new QueryDTO();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            System.out.println("0");
            newQuery.setCarType(requestData.get("car_type"));
            System.out.println("1");
            newQuery.setDestination(requestData.get("destination"));
            System.out.println("2");
            newQuery.setEmail(email);
            System.out.println("3");
            newQuery.setDriverExperience(Integer.parseInt(requestData.get("driver_experience")));
            System.out.println("4");
            newQuery.setMaxCarMileage(Integer.parseInt(requestData.get("max_car_mileage")));
            System.out.println("5");
            newQuery.setStatus("new");
            System.out.println("6");
            newQuery.setStartDatetime(LocalDateTime.parse((requestData.get("start_datetime")), formatter));
            System.out.println("7");
            newQuery.setAddDatetime(LocalDateTime.now());
            System.out.println("8");
            newQuery.setStartLocation(requestData.get("start_location"));
            System.out.println("9");
            newQuery.setId(Integer.parseInt(requestData.get("editId")));
            System.out.println("updated driver_query: " + newQuery.toString());
            logger.info("updated driver_query: " + newQuery.toString());
            if(newQuery.getAddDatetime().isAfter(newQuery.getStartDatetime())) {
                System.out.println("Query already expired!");
                logger.info("Query already expired!");
            } else {
                queryService.updateQuery(newQuery);
            }
           

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("Query created successfully");
    }
    }
}