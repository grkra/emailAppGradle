package krawczyk.grzegorz.controllers.persistence;

import krawczyk.grzegorz.controllers.BaseController;

import java.util.Base64;

/**
 * Class is used to encode and decode password saved in the local file
 */
public class Encoder {

    private static Base64.Encoder encoder = Base64.getEncoder();
    private static Base64.Decoder decoder = Base64.getDecoder();

    /**
     * Method encodes passed String as base44
     * @param textToEncode String to encode
     * @return String - encoded text
     */
    public String encode (String textToEncode) {
        try {
            return this.encoder.encodeToString(textToEncode.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    };

    /**
     * Method decodes passed String from base64
     * @param textToDecode String to decode
     * @return String - decoded String
     */
    public String decode (String textToDecode) {
        try {
            return new String(decoder.decode(textToDecode));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
