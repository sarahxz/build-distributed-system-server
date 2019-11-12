import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "StatisticsServlet")
public class StatisticsServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");

    int postMean = 0;
    int postMax = 0;
    int getMean = 0;
    int getMax = 0;

    RequestDao reqDao = new RequestDao();

    try {
      ArrayList<Integer> res1 = reqDao.getPost();

      postMean = res1.get(0);
      postMax = res1.get(1);

      ArrayList<Integer> res2 = reqDao.getGet();
      getMean = res2.get(0);
      getMax = res2.get(1);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_OK);

    JSONObject jo1 = new JSONObject();
    jo1.put("URL", "/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}");
    jo1.put("operation", "POST");
    jo1.put("mean", postMean);
    jo1.put("max", postMax);


    JSONObject jo2 = new JSONObject();
    jo2.put("URL", "/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}");
    jo2.put("operation", "GET");
    jo2.put("mean", getMean);
    jo2.put("max", getMax);


    JSONArray ja = new JSONArray();
    ja.put(jo1);
    ja.put(jo2);

    JSONObject mainJo = new JSONObject();
    mainJo.put("endpointStats", ja);
    Gson gson = new Gson();

    response.setStatus(HttpServletResponse.SC_OK);

    response.getWriter().write(String.valueOf(mainJo));


  }
}
