package ionshield.lab.crypto.encoder.modules;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class RSAEncoder implements CryptoEncoder {

    private BigInteger n = BigInteger.ONE;
    private BigInteger e = BigInteger.ONE;
    private BigInteger d = BigInteger.ONE;
    private boolean forward = true;
    private int bitLength = 8;

    public RSAEncoder() {};

    public RSAEncoder(BigInteger n, BigInteger e, BigInteger d, int bitLength) {
        this.n = n;
        this.e = e;
        this.d = d;
        this.bitLength = bitLength;
    }

    @Override
    public boolean readKey(List<String> keyLines) {
        if (keyLines == null || keyLines.size() < 3) return false;

        String[] keyData = keyLines.get(0).split("\\s+");
        if (keyData.length < 2) return false;
        try {
            forward = Integer.parseInt(keyData[0]) != 0;
            bitLength = Integer.parseInt(keyData[1]);

            if (forward) {
                e = new BigInteger(keyLines.get(1));
            }
            else {
                d = new BigInteger(keyLines.get(1));
            }

            n = new BigInteger(keyLines.get(2));
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


    @Override
    public List<String> encode(List<String> source) {
        if (forward) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < source.size(); i++) {
                sb.append(source.get(i));
                if (i < source.size() - 1) {
                    sb.append(System.lineSeparator());
                }
            }

            char[] chars = sb.toString().toCharArray();
            StringBuilder out = new StringBuilder();

            for (char c : chars) {
                BigInteger value = new BigInteger(String.valueOf((int) c));
                value = Utils.modPow(value, e, n);
                out.append(value.toString()).append(" ");
            }
            if (out.length() == 0) return  new ArrayList<>();
            return Collections.singletonList(out.substring(0, out.length() - 1));
        }
        else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < source.size(); i++) {
                sb.append(source.get(i));
                if (i < source.size() - 1) {
                    sb.append(System.lineSeparator());
                }
            }

            String[] data = sb.toString().split("\\s+");
            for (String s : data) {
                BigInteger value = new BigInteger(s);
                //TODO
            }

            return Collections.singletonList("Not implemented");
        }
    }
}
