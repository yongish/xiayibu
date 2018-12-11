package com.zhiyong.xiayibu;

public class Util {

    public static int responseInt(String response) {
        response = response.trim().toLowerCase();
        switch (response) {
            case "yes":
                return 0;
            case "no":
                return 1;
            case "never":
            case "don't show again":
                return 2;
        }
        throw new IllegalArgumentException("Invalid response string.");
    }

    public static String responseString(int response) {
        switch (response) {
            case 0:
                return "Yes";
            case 1:
                return "No";
            case 2:
                return "Never";
        }
        throw new IllegalArgumentException("Invalid response number.");
    }
}
