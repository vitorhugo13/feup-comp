import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


public class Utils {
	
	public static InputStream toInputStream(String text) {
        try {
            return new ByteArrayInputStream(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     *
     * @param name
     * @return first part of name. Ex: given INTEGER[20] returns INTEGER
     */
    public static String getFirstPartOfName(String name){
	    int finalChar=1;
        for(int i = 0; i < name.length(); i++){
            if(name.charAt(i)== '[')
                finalChar=i;
        }
        return name.substring(0, finalChar);
    }

    public static String parseName(String name) {
        return name.substring(name.indexOf("[") + 1, name.indexOf("]"));
    }
    
}