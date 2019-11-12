import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.Collections;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SkierServlet extends javax.servlet.http.HttpServlet {


  protected void doPost(javax.servlet.http.HttpServletRequest request,
                        javax.servlet.http.HttpServletResponse response) throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    //extract values from URL path params and request body
    //construct a LifeRide with the values
    //pass that object to DAO layer
    long postStart = System.nanoTime();

    String[] params = request.getPathInfo().split("/");
    Integer resortId = Integer.parseInt(params[1]);
    String seasonId = params[3];
    String dayId = params[5];
    Integer skierId = Integer.parseInt(params[7]);

    String bodyInfo = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    Gson gson = new Gson();

    Body body = gson.fromJson(bodyInfo, Body.class);

    Integer bodyTime = body.getTime();
    Integer bodyLiftId = body.getLiftID();

    LiftRide liftRide = new LiftRide(skierId, resortId, seasonId, dayId, bodyTime,
        bodyLiftId, bodyLiftId * 10);

    LiftRideDao dao = new LiftRideDao();
    try {
      dao.createLiftRide(liftRide);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_OK);

    long postEnd = System.nanoTime();
    long latencyNano = postEnd - postStart;
    int latency = (int) latencyNano / 1000000;

    RequestDao reqDao = new RequestDao();
    try {
      reqDao.createRequest(new Request("/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}", "POST", latency));
    } catch (SQLException e) {
      e.printStackTrace();
    }


  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("application/json");

    long getStart = System.nanoTime();

    String urlPath = req.getPathInfo();

    // check we have a URL!
    if (urlPath == null) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing paramterers");
      return;
    }



    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

    Integer vertical = null;

    LiftRideDao dao = new LiftRideDao();
    try {
      vertical = dao.getVertical(Integer.parseInt(urlParts[1]), urlParts[3], urlParts[5], Integer.parseInt(urlParts[7]));
    } catch (SQLException e) {
      e.printStackTrace();
    }


    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      res.getWriter().write(String.valueOf(vertical));
      long getEnd = System.nanoTime();
      long latencyNano = getEnd - getStart;
      int latency = (int) latencyNano / 1000000;

      RequestDao reqDao = new RequestDao();
      try {
        reqDao.createRequest(new Request("/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}", "GET", latency));
      } catch (SQLException e) {
        e.printStackTrace();
      }


    }
  }

  private boolean isUrlValid(String[] urlPath) {
    // TODO: validate the request url path according to the API spec
    // urlPath  = "/1/seasons/2019/day/1/skier/123"
    // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
    return true;
  }

  public static class Body {
    private Integer time;
    private Integer liftID;

    public Body(Integer time, Integer liftID) {
      this.time = time;
      this.liftID = liftID;
    }

    public Integer getTime() {
      return time;
    }

    public Integer getLiftID() {
      return liftID;
    }
  }

}
