package com.ramusthastudio.ama;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class exString {
  public static void main(String[] args) {
    String text1 = "ini    twitter ramustha";
    String text2 = " twitter    ramustha dwdwd dwdwdw ";
    String text3 = "nih     twitter     ramustha wdw dw d wd";
    String text4 = "ini   twitter     ramus2tha bro dwdwdwd";
    String text5 = "w    twitterramustha ?";
    String text6 = "  dwdw twitter:         ramu2stha dwdwd ";
    String text7 = "s332dwd twitter;        ramu2stha#2 wdw";
    String text8 = " dwdw twitter      .ramu2stha@#2 dwad";

    String find = "twitter";

    predictWord(text1, find);
    predictWord(text2, find);
    predictWord(text3, find);
    predictWord(text4, find);
    predictWord(text5, find);
    predictWord(text6, find);
    predictWord(text7, find);
    predictWord(text8, find);
  }

  private static void predictWord(String aText1, String aFind) {
    Pattern word = Pattern.compile(aFind);
    Matcher match = word.matcher(aText1);
    String result;
    while (match.find()) {
      // System.out.println("Found " + aFind + " at index " + match.start() + " - " + (match.end() - 1));
      String predictAfterKey = removeAnySymbol(aText1.substring(match.end(), aText1.length())).trim();

      if (predictAfterKey.length() > 0) {
        if (predictAfterKey.contains(" ")) {
          String[] predictAfterKeySplit = predictAfterKey.split(" ");
          result = predictAfterKeySplit[0];
        } else {
          result = predictAfterKey;
        }
        System.out.println("Result " + result);
      }
    }
  }

  private static String removeAnySymbol(String s) {
    Pattern pattern = Pattern.compile("[^a-z A-Z^0-9]");
    Matcher matcher = pattern.matcher(s);
    return matcher.replaceAll(" ");
  }
}
