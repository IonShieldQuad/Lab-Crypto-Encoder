package ionshield.lab.crypto.encoder.modules;

import java.util.ArrayList;
import java.util.List;

public class CrossroadsEncoder implements CryptoEncoder {
    private int n;
    private boolean dir; //0 = clockwise, 1 - counterclockwise

    public CrossroadsEncoder() {
    }

    public CrossroadsEncoder(List<String> keyLines) {
        readKey(keyLines);
    }

    @Override
    public boolean readKey(List<String> keyLines) {
        if (keyLines == null || keyLines.size() < 1) return false;

        String[] sizes = keyLines.get(0).split("\\s+");
        if (sizes.length < 2) return false;
        try {
            dir = Integer.parseInt(sizes[0]) != 0;
            n = Integer.parseInt(sizes[1]);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> encode(List<String> source) {
        List<Character> top = new ArrayList<>();
        List<Character> mid = new ArrayList<>();
        List<Character> bot = new ArrayList<>();

        int it = 0, im = 0, ib = 0;


        int pos = 0;


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < source.size(); i++) {
            sb.append(source.get(i));
            if (i < source.size() - 1) {
                sb.append(System.lineSeparator());
            }
        }

        for (char c : sb.toString().toCharArray()) {
            switch (pos) {
                case 0:
                case 2: {
                    mid.add(c);
                    break;
                }
                case 1: {
                    top.add(c);
                    break;
                }
                case 3: {
                    bot.add(c);
                    break;
                }
            }
            if (!dir) {
                pos = (pos + 1) % 4;
            }
            else {
                pos = (pos - 1);
                while (pos < 0) {
                    pos += 4;
                }
            }
        }

        StringBuilder out = new StringBuilder();

        while (it < top.size() && im < mid.size() && ib < bot.size()) {
            for (int i = 0; i < n; i++, it++) {
                if (it < top.size()) {
                    out.append(top.get(it));
                }
                else {
                    out.append(" ");
                }
            }

            for (int i = 0; i < 2 * n; i++, im++) {
                if (im < mid.size()) {
                    out.append(mid.get(im));
                }
                else {
                    out.append(" ");
                }
            }

            for (int i = 0; i < n; i++, ib++) {
                if (ib < bot.size()) {
                    out.append(bot.get(ib));
                }
                else {
                    out.append(" ");
                }
            }

        }

        List<String> ol = new ArrayList<>();
        ol.add(out.toString());
        return ol;
    }

 }
