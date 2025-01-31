package com.devilish.granja.utils;

public class Validator {

    public static boolean isValidCpf(String cpf) {

        cpf = cpf.replaceAll("[^0-9]", "");


        if (cpf.length() != 11) {
            return false;
        }


        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }


        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Integer.parseInt(cpf.substring(i, i + 1)) * (10 - i);
        }
        int remainder = sum % 11;
        int digit1 = (remainder < 2) ? 0 : 11 - remainder;


        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Integer.parseInt(cpf.substring(i, i + 1)) * (11 - i);
        }
        remainder = sum % 11;
        int digit2 = (remainder < 2) ? 0 : 11 - remainder;


        return cpf.substring(9).equals(String.valueOf(digit1) + String.valueOf(digit2));
    }

    private static int calculateDigit(String str, int[] weights) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            sum += Integer.parseInt(str.substring(i, i + 1)) * weights[i];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    public static boolean isValidRegistration(String registration) {

        registration = registration.replaceAll("[^0-9]", "");


        if (registration.length() != 6) {
            return false;
        }


        if (registration.matches("0{6}")) {
            return false;
        }


        if (!registration.matches("[1-9]\\d{5}")) {
            return false;
        }

        return true;
    }

}
