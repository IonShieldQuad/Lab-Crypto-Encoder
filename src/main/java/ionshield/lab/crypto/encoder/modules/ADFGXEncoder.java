package ionshield.lab.crypto.encoder.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ADFGXEncoder implements CryptoEncoder {
    private char[] alphabet;
    private char[] rows;
    private char[] cols;
    private char[] keyword;

    private int alphabetSize;
    private int rowCount;
    private int colCount;

    public ADFGXEncoder() {
    }

    public ADFGXEncoder(List<String> keyLines) {
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

        int tableLength = rowCount + colCount + alphabetSize;

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < keyLines.size(); i++) {
            sb.append(keyLines.get(i));
            if (i < keyLines.size() - 1) {
                sb.append('\n');
            }
        }

        String data = sb.toString();

        if (data.length() < tableLength) return false;

        int ri = rowCount;
        int ci = colCount;

        rows = data.substring(0, ri).toCharArray();
        cols = data.substring(ri, ri + ci).toCharArray();
        alphabet = data.substring(ri + ci, tableLength).toCharArray();
        keyword = data.substring(tableLength).toCharArray();

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

    private char getRowChar(int row) {
        return rows[row];
    }

    private char getColChar(int row) {
        return cols[row];
    }

    @Override
    public List<String> encode(List<String> source) {
        List<List<Character>> columns = new ArrayList<>();
        List<List<Character>> columnsSorted = new ArrayList<>();
        List<Character> keyList = new ArrayList<>();

        List<String> out = new ArrayList<>();
        StringBuilder sb;

        for (String line : source) {
            sb = new StringBuilder();

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                int index = getCharIndex(c);
                if (index < 0) continue;

                char rc = getRowChar(getRow(index));
                char cc = getColChar(getCol(index));
                sb.append(rc);
                sb.append(cc);
            }

            out.add(sb.toString());
        }

        for (char c : keyword) {
            keyList.add(c);
            columns.add(new ArrayList<>());
        }

        int charNum = 0;
        int coli = 0;
        for (String line : out) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                columns.get(coli).add(c);
                charNum++;
                coli = (coli + 1) % columns.size();
            }
        }

        /*while (coli > 0) {
            columns.get(coli).add(' ');
            coli = (coli + 1) % columns.size();
        }*/

        while (!keyList.isEmpty()) {
            char min = keyList.get(0);
            int mini = 0;
            for (int i = 0; i < keyList.size(); i++) {
                char c = keyList.get(i);
                if (c < min) {
                    min = c;
                    mini = i;
                }
            }
            columnsSorted.add(columns.get(mini));
            columns.remove(mini);
            keyList.remove(mini);
        }

        out.clear();
        sb = new StringBuilder();
        /*for (int i = 0; i < charNum; i++) {
            sb.append(columnsSorted.get(i % keyword.length).get(i / keyword.length));
        }*/

        for (List<Character> column : columnsSorted) {
            for (char c : column) {
                sb.append(c);
            }
        }

        return Collections.singletonList(sb.toString());
    }
}
