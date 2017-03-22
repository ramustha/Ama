package com.ramusthastudio.ama.model;

public class UserConsumption {
  private String twitterId;
  private String consumptionCategory;
  private String consumptionName;
  private double consumptionScore;

  public UserConsumption() { }
  public UserConsumption(String aTwitterId, String aConsumptionCategory, String aConsumptionName, double aConsumptionScore) {
    twitterId = aTwitterId;
    consumptionCategory = aConsumptionCategory;
    consumptionName = aConsumptionName;
    consumptionScore = aConsumptionScore;
  }

  public String getTwitterId() { return twitterId; }
  public String getConsumptionCategory() { return consumptionCategory; }
  public String getConsumptionName() { return consumptionName; }
  public double getConsumptionScore() { return consumptionScore; }

  public UserConsumption setTwitterId(String aTwitterId) {
    twitterId = aTwitterId;
    return this;
  }
  public UserConsumption setConsumptionCategory(String aConsumptionCategory) {
    consumptionCategory = aConsumptionCategory;
    return this;
  }
  public UserConsumption setConsumptionName(String aConsumptionName) {
    consumptionName = aConsumptionName;
    return this;
  }
  public UserConsumption setConsumptionScore(double aConsumptionScore) {
    consumptionScore = aConsumptionScore;
    return this;
  }

  @Override public String toString() {
    return "UserConsumption{" +
        "twitterId='" + twitterId + '\'' +
        ", consumptionCategory='" + consumptionCategory + '\'' +
        ", consumptionName='" + consumptionName + '\'' +
        ", consumptionScore=" + consumptionScore +
        '}';
  }
}
