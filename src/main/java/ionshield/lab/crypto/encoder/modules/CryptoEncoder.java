package ionshield.lab.crypto.encoder.modules;

import java.util.List;

public interface CryptoEncoder {
    boolean readKey(List<String> keyLines);
    List<String> encode(List<String> source);
}
