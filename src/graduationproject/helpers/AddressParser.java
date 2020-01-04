/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.helpers;

/**
 *
 * @author cloud
 */
public class AddressParser {

    public String getNetworkIp(String address, String netmask) {
        if (address != null) {
            String[] addressOctetValues = address.split("\\.");

            String[] netmaskOctetValues = netmask.split("\\.");
            DataConverter converter = new DataConverter();

            int octetSize = 8;
            boolean[] temp1, temp2;
            boolean[] temp = new boolean[octetSize];
            StringBuilder resultBuilder = new StringBuilder();

            for (int i = 0; i < addressOctetValues.length; i++) {
                temp1 = converter.convertDecimalToBinary(Integer.parseInt(addressOctetValues[i]), octetSize);
                temp2 = converter.convertDecimalToBinary(Integer.parseInt(netmaskOctetValues[i]), octetSize);

                for (int j = 0; j < octetSize; j++) {
                    temp[j] = temp1[j] && temp2[i];
                }

                resultBuilder.append(String.valueOf(converter.convertBinaryToDecimal(temp)));
                if (i != addressOctetValues.length - 1) {
                    resultBuilder.append(".");
                }
            }
//        System.out.println("ADDRESS FOR PARSE: " + address);
//        System.out.println("NETWORK ADDRESS: " + resultBuilder.toString());
            return resultBuilder.toString();
        }
        return null;
    }

    public String getNetworkIp(String address, short prefix) {
        if (address != null) {
            String[] octetValues = address.split("[.]");
            int count = 0;
            int octetSize = 8;
            boolean[] temp = new boolean[octetSize];
            boolean[] temp1;
            StringBuilder resultBuilder = new StringBuilder();
            DataConverter converter = new DataConverter();

            for (int i = 0; i < octetValues.length; i++) {
                temp1 = converter.convertDecimalToBinary(Integer.parseInt(octetValues[i]), octetSize);

                for (int j = 0; j < octetSize; j++) {
                    count++;
                    if (count > prefix) {
                        temp[j] = false;
                    } else {
                        temp[j] = temp1[j];
                    }
                }

                resultBuilder.append(String.valueOf(converter.convertBinaryToDecimal(temp)));
                if (i != octetValues.length - 1) {
                    resultBuilder.append(".");
                }
            }

            return resultBuilder.toString();
        }
        return null;
    }

    public static String normalizeMac(String mac) {        //ensure each octet always contain 2 bit
        StringBuilder result = new StringBuilder();
        try {
            String[] values = mac.split(":");
            if (values.length < 6) {
                values = mac.split("-");
            }

            for (int i = 0; i < values.length - 1; i++) {
                result.append(normalizeOctet(values[i]));
                result.append(":");
            }
            result.append(normalizeOctet(values[values.length - 1]));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public static String normalizeOctet(String value) {
        if (value.length() == 2) {
            return value;
        } else if (value.length() == 1) {
            return '0' + value;
        }
        return null;
    }
}
