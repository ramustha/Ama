package com.ramusthastudio.ama;

import java.util.ArrayList;

public class exPercentile {
  public static void main(String[] args) {

    ArrayList<String> likes1 = new ArrayList<>();
    likes1.add("be sensitive to ownership cost when buying automobiles");
    likes1.add("prefer safety when buying automobiles");
    likes1.add("prefer quality when buying clothes");

    ArrayList<String> unlikes1 = new ArrayList<>();
    unlikes1.add("be sensitive to ownership cost when buying automobiles");
    unlikes1.add("prefer safety when buying automobiles");
    unlikes1.add("prefer quality when buying clothes");

    ArrayList<String> likes2 = new ArrayList<>();
    likes2.add("be sensitive to ownership cost when buying automobiles");
    likes2.add("prefer safety when buying automobiles");
    likes2.add("prefer quality when buying clothes");

    ArrayList<String> unlikes2 = new ArrayList<>();
    unlikes2.add("be sensitive to ownership cost when buying automobiles");
    unlikes2.add("prefer safety when buying automobiles");
    unlikes2.add("prefer quality when buying clothes");

    System.out.println(processMatches(likes1, likes2, 50));
    System.out.println(processMatches(unlikes1, unlikes2, 25));
    System.out.println(processMatches(unlikes1, unlikes2, 25));
  }

  private static int processMatches(ArrayList<String> aCandidate1, ArrayList<String> aCandidate2, int aOffset) {
    double likeCount = 0;
    for (String s1 : aCandidate1) {
      for (String s2 : aCandidate2) {
        if (s1.contains(s2)) {
          likeCount++;
        }
      }
    }
    return (int) Math.round((likeCount / aCandidate1.size()) * aOffset);
  }
}
