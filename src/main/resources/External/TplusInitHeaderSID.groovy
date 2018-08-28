package External
/**
 * Created by haijia on 6/25/18.
 */
def getAuthSid(String key1, String key2, String s){
    if(null != s && 0 < s.length()){
        String[] array = s.split(";");
        if(null != array && array.length == 2){
            String token = array[0];
            String sid = array[1];
            token = "token" + " " + token;
            Map<String, String> map = new HashMap<String, String>();
            map.put(key1, token);
            map.put(key2, sid);
            return map;
        }
    }
}



