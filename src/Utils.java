import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String parseName(String name){

        String id ="";
        int state = 0;

        for(int i = 0; i < name.length(); i++){

            switch(state){
                case 0:
                    if(name.charAt(i) == '['){
                        state = 1;
                    }
                    break;
                case 1:

                    if(name.charAt(i) == ']'){
                        state = 2;
                    }
                    else{
                        id = id + name.charAt(i);
                    }
                    break;
                case 2:
                    break;
            }
        }

        return id;
    }

    public static Boolean analyzeRegex(String s, String pattern){

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(s);

        if(m.find()){
            return true;
        }
        else{
            return false;
        }
    }
    
}