package ionshield.lab.crypto.encoder.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class VariantEncoder implements CryptoEncoder {
    private int charsPerRow = 2;
    private  int charsPerCol = 2;

    private char[] alphabet;
    private char[] rows;
    private char[] cols;

    private int alphabetSize;
    private int rowCount;
    private int colCount;

    public VariantEncoder() {
    }

    public VariantEncoder(List<String> keyLines) {
        readKey(keyLines);
    }


    @Override
    public boolean readKey(List<String> keyLines) {
        if (keyLines == null || keyLines.size() < 2) return false;

        String[] sizes = keyLines.get(0).split("\\s+");
        if (sizes.length < 3) return false;
        try {
            rowCount = Integer.parseInt(sizes[0]);
            colCount = Integer.parseInt(sizes[1]);
            alphabetSize = Integer.parseInt(sizes[2]);
        }
        catch (NumberFormatException e) {
            return false;
        }

        int totalLength = rowCount * charsPerRow + colCount * charsPerCol + alphabetSize;

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < keyLines.size(); i++) {
            sb.append(keyLines.get(i));
            if (i < keyLines.size() - 1) {
                sb.append('\n');
            }
        }

        String data = sb.toString();

        if (data.length() < totalLength) return false;

        int ri = rowCount * charsPerRow;
        int ci = colCount * charsPerCol;

        rows = data.substring(0, ri).toCharArray();
        cols = data.substring(ri, ri + ci).toCharArray();
        alphabet = data.substring(ri + ci, totalLength).toCharArray();

        return true;
    }

    private int getRow(int i) {
        return i / colCount;
    }

    private int getCol(int i) {
        return i % colCount;
    }

    private int getCharIndex(char c) {
        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] == c) {
                return i;
            }
        }
        return -1;
    }

    private int getRow(char c) {
        return getRow(getCharIndex(c));
    }

    private int getCol(char c) {
        return  getCol(getCharIndex(c));
    }

    private char getRowChar(int row, int offset) {
        return rows[row * charsPerRow + offset];
    }

    private char getColChar(int row, int offset) {
        return cols[row * charsPerCol + offset];
    }

    @Override
    public List<String> encode(List<String> source) {
        List<String> out = new ArrayList<>();
        StringBuilder sb;
        Random rnd = new Random();

        for (String line : source) {
            sb = new StringBuilder();

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                int index = getCharIndex(c);
                if (index < 0) continue;

                char rc = getRowChar(getRow(index), rnd.nextInt(charsPerRow));
                char cc = getColChar(getCol(index), rnd.nextInt(charsPerCol));

                if (rnd.nextBoolean()) {
                    sb.append(rc);
                    sb.append(cc);
                }
                else {
                    sb.append(cc);
                    sb.append(rc);
                }
            }

            out.add(sb.toString());
        }

        return out;
    }
}
