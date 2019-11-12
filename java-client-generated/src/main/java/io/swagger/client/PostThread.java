package io.swagger.client;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import io.swagger.client2.part2.MyFileWriter;

public class PostThread implements Runnable {

  private CountDownLatch endSignal;
  private Integer startSkierId;
  private Integer idRange;
  private Integer startTime;
  private Integer endTime;
  private Integer numSkiers;
  private Integer numThreads;
  private Integer numRuns;
  private Integer phase;
  MyFileWriter myFileWriter;


  public PostThread(CountDownLatch endSignal, Integer startSkierId, Integer idRange,
                    Integer startTime, Integer endTime, Integer numSkiers, Integer numThreads,
                    Integer numRuns, Integer phase, MyFileWriter myFileWriter) {
    this.endSignal = endSignal;
    this.startSkierId = startSkierId;
    this.idRange = idRange;
    this.startTime = startTime;
    this.endTime = endTime;
    this.numSkiers = numSkiers;
    this.numThreads = numThreads;
    this.numRuns = numRuns;
    this.phase = phase;
    this.myFileWriter = myFileWriter;
  }

  public void run() {
    SkiersApi apiInstance = new SkiersApi();
    ApiClient client = apiInstance.getApiClient();
    String basePath = "http://new-load-balancer-280919244.us-east-1.elb.amazonaws.com:8080/bsds_war";
//    String basePath = "http://localhost:8080";
//    String basePath = "http://52.202.226.19:8080/bsds_war";
    client.setBasePath(basePath);
    Integer localSuccess = 0;
    Integer localFailures = 0;

    int runs;

    if (phase!=2) {
      runs = (int)((numRuns / 10) * ((double)numSkiers / (numThreads / 4)));
    } else {
      runs = (int)((numRuns * 0.8) * (double)numSkiers / numThreads);
    }

    for (int i = 0; i < runs; i++) {
      long postStart = System.nanoTime();
      try {
        LiftRide body = new LiftRide();

        Integer randomTime = ThreadLocalRandom.current().nextInt(startTime, endTime);
        body.time(randomTime);

        Integer randomLiftId = ThreadLocalRandom.current().nextInt(5, 61);
        body.liftID(randomLiftId);
        Integer resortID = 10;
        String seasonID = "2019";
        String dayID = "1";

        Integer randomSkierId = ThreadLocalRandom.current().nextInt(startSkierId, startSkierId + idRange + 1);
        apiInstance.writeNewLiftRide(body, resortID, seasonID, dayID, randomSkierId);
        localSuccess++;

        long postEnd = System.nanoTime();
        long latencyNano = postEnd - postStart;
        double latency = (double) latencyNano / 1000000;

        Main.latencies.add(latency);

        myFileWriter.addResult(Main.result, postStart, "POST", latency, 200);

        if (phase == 3) {
          try {
            long getStart = System.nanoTime();
            apiInstance.getSkierDayVertical(resortID, seasonID, dayID, randomSkierId);
            long getEnd = System.nanoTime() - getStart;
            double latencyGet = (double) getEnd / 1000000;
            Main.latencies.add(latencyGet);
            localSuccess++;
          } catch (ApiException e) {
            long getStart = System.nanoTime();
            long getEnd = System.nanoTime() - getStart;
            double latencyGet = (double) getEnd / 1000000;
            Main.latencies.add(latencyGet);
            localFailures++;
          }
        }

      } catch (ApiException e) {

        Integer respCode = e.getCode();
        long postEnd = System.nanoTime();
        long latencyNano = postEnd - postStart;
        double latency = (double) latencyNano / 1000000;
        Main.latencies.add(latency);


        myFileWriter.addResult(Main.result, postStart, "POST", latency, respCode);

        System.err.println("Exception when calling SkeirsApi#getResorts");
        localFailures++;
        e.printStackTrace();
      }
    }


    endSignal.countDown();

    Main.totalSuccess.getAndAdd(localSuccess);
    Main.totalFailures.getAndAdd(localFailures);

}
}
