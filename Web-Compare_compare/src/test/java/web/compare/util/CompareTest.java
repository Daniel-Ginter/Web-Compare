package web.compare.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.compare.util.comparer.ComparerProzess;


import java.util.HashMap;
public class CompareTest {
    private static Logger log = LoggerFactory.getLogger(CompareTest.class);

    private static final String uiTestDir = "/Users/Speick/Desktop/Result";

    public static void main(String[] args){
        String test = "Chrome_Res_blablablaa-a-0.json";
        var config = new HashMap<String, String>();
        config.put("sourceDirectory","/Users/Speick/Desktop/Result");
        config.put("resultDirectory","/Users/Speick/Desktop/Result");
        config.put("test","Test ToDoMVc\\Nothing");
        config.put("mainEnviroment","Chrome_1900x700");
        config.putIfAbsent("serverDirectory","c:\\Users\\Speick\\OneDrive\\Webanwendung\\server\\public");
        new ComparerProzess(config).doit();

    }


}
