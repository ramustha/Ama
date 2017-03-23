package com.ramusthastudio.ama.model;

public class UserPersonality {
  private String id;
  private String personName;
  private String category;
  private String parentName;
  private double parentPercentile;
  private String childName;
  private double childPercentile;

  public UserPersonality() { }

  public UserPersonality(String aId, String aPersonName, String aCategory, String aParentName, double aParentPercentile, String aChildName, double aChildPercentile) {
    id = aId;
    personName = aPersonName;
    category = aCategory;
    parentName = aParentName;
    parentPercentile = aParentPercentile;
    childName = aChildName;
    childPercentile = aChildPercentile;
  }

  public String getId() { return id; }
  public String getPersonName() {    return personName;  }
  public String getCategory() { return category; }
  public String getParentName() { return parentName; }
  public double getParentPercentile() { return parentPercentile; }
  public String getChildName() { return childName; }
  public double getChildPercentile() { return childPercentile; }

  public UserPersonality setId(String aId) {
    id = aId;
    return this;
  }
  public UserPersonality setPersonName(String aPersonName) {
    personName = aPersonName;
    return this;
  }
  public UserPersonality setCategory(String aCategory) {
    category = aCategory;
    return this;
  }
  public UserPersonality setParentName(String aParentName) {
    parentName = aParentName;
    return this;
  }
  public UserPersonality setParentPercentile(double aParentPercentile) {
    parentPercentile = aParentPercentile;
    return this;
  }
  public UserPersonality setChildName(String aChildName) {
    childName = aChildName;
    return this;
  }
  public UserPersonality setChildPercentile(double aChildPercentile) {
    childPercentile = aChildPercentile;
    return this;
  }

  @Override public String toString() {
    return "UserPersonality{" +
        "id='" + id + '\'' +
        ", personName='" + personName + '\'' +
        ", category='" + category + '\'' +
        ", parentName='" + parentName + '\'' +
        ", parentPercentile=" + parentPercentile +
        ", childName='" + childName + '\'' +
        ", childPercentile=" + childPercentile +
        '}';
  }
}
