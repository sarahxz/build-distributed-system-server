public class LiftRide {
  Integer liftID;
  Integer time;
  Integer resortId;
  String seasonId;
  String dayId;
  Integer skierId;
  Integer vertical;


  public LiftRide(Integer skierId, Integer resortId, String seasonId, String dayId, Integer time, Integer liftID,
                  Integer vertical) {
    this.liftID = liftID;
    this.time = time;
    this.resortId = resortId;
    this.seasonId = seasonId;
    this.dayId = dayId;
    this.skierId = skierId;
    this.vertical = vertical;
  }

  public Integer getLiftId() {
    return liftID;
  }

  public Integer getTime() {
    return time;
  }

  public Integer getResortId() {
    return resortId;
  }

  public String getSeasonId() {
    return seasonId;
  }

  public String getDayId() {
    return dayId;
  }

  public Integer getSkierId() {
    return skierId;
  }

  public Integer getVertical() {
    return vertical;
  }

  public void setLiftId(Integer liftID) {
    this.liftID = liftID;
  }

  public void setTime(Integer time) {
    this.time = time;
  }

  public void setResortId(Integer resortId) {
    this.resortId = resortId;
  }

  public void setSeasonId(String seasonId) {
    this.seasonId = seasonId;
  }

  public void setDayId(String dayId) {
    this.dayId = dayId;
  }

  public void setSkierId(Integer skierId) {
    this.skierId = skierId;
  }

  public void setVertical(Integer vertical) {
    this.vertical = vertical;
  }

  @Override
  public String toString() {
    return "LiftRide{" +
        "liftId=" + liftID +
        ", time=" + time +
        ", resortId='" + resortId + '\'' +
        ", seasonId='" + seasonId + '\'' +
        ", dayId='" + dayId + '\'' +
        ", skierId=" + skierId +
        '}';
  }

}
