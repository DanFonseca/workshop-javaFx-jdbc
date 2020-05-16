package Model.excpetions;

import java.util.HashMap;
import java.util.Map;

public class ValidationsException extends RuntimeException {
    private Map<String, String> errors = new HashMap<>();

    public  ValidationsException (String e ){
        super(e);
    }

    public Map<String, String> getErros (){
        return errors;
    }

    public void addError (String fieldName, String error){
        errors.put(fieldName, error);
    }
}
