package br.com.estruturart.utility;

public class StringUtilsPad
{
    private StringUtilsPad() {}

    // pad with " " to the right to the given length (n)
    public static String padRight(String s, int n, String to)
    {
      return String.format("%1$-" + n + "s", s).replaceAll(" ", to);
    }

    // pad with " " to the left to the given length (n)
    public static String padLeft(String s, int n, String to)
    {
      return String.format("%1$" + n + "s", s).replaceAll(" ", to);
    }
}