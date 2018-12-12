package com.zhiyong.xiayibu;

public class Util {

    public static int responseInt(String response) {
        response = response.trim().toLowerCase();
        switch (response) {
            case "no":
                return 0;
            case "yes":
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
                return "No";
            case 1:
                return "Yes";
            case 2:
                return "Never";
        }
        throw new IllegalArgumentException("Invalid response number.");
    }
}
