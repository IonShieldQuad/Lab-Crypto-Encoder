package ionshield.lab.crypto.encoder.modules;

import java.util.Collections;
import java.util.List;

public class XOREncoder implements CryptoEncoder {
    private char[] key;

    public XOREncoder() {
    }

    public XOREncoder(List<String> keyLines) {
        readKey(keyLines);
    }

    @Override
    public boolean readKey(List<String> keyLines) {
        if (keyLines == null || keyLines.isEmpty()) return false;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyLines.size(); i++) {
            sb.append(keyLines.get(i));
            if (i < keyLines.size() - 1) {
                sb.append('\n');
            }
        }

        String data = sb.toString();
        key = data.toCharArray();

        return true;
    }

    @Override
    public List<String> encode(List<String> source) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < source.size(); i++) {
            sb.append(source.get(i));
            if (i < source.size() - 1) {
                sb.append('\n');
            }
        }

        String data = sb.toString();
        char[] arr = data.toCharArray();

        int index = 0;

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (char) (arr[i] ^ key[index]);
            index = (index + 1) % key.length;
        }

        return Collections.singletonList(new String(arr));
    }
}
