package web.compare.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.compare.modell.MetadataHelper;
import web.compare.modell.result.CompareResult;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class JsonUtil {

    private static Logger log = LoggerFactory.getLogger(JsonUtil.class);

    public static Map<String, MetadataHelper> loadElementJsons(String path) {
        var gson = new Gson();
        var out = FileUtils.listFiles(Path.of(path).toFile(), new String[]{"json"}, false).stream()
                .collect(Collectors.toMap(f -> f.getName(), f -> {
                    try {
                        return gson.fromJson(FileUtils.readFileToString(f, StandardCharsets.UTF_8), MetadataHelper.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }));
        return out;
    }

    public static MetadataHelper loadElementJson(String path, String file) throws IOException {
        var gson = new Gson();
        var out = FileUtils.getFile(Path.of(path).toFile(), file);
        return gson.fromJson(FileUtils.readFileToString(out, StandardCharsets.UTF_8), MetadataHelper.class);
    }

    public static void saveJson(Object compareResult, String name) {
        var json = makeJson(compareResult);
        try {
            FileUtils.writeStringToFile(Path.of(name + ".json").toFile(), json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to create: ");
        }
    }

    public static String makeJson(Object compareResult) {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(compareResult);
    }

    public static LinkedList<CompareResult> loadCompareResult(Path path,String end) {
        var gson = new Gson();
        AtomicInteger id = new AtomicInteger(1);
        var out = FileUtils.listFiles(path.toFile(), new String[]{"json"}, false).stream()
                .map(f -> {
                    try {
                        var result = gson.fromJson(FileUtils.readFileToString(f, StandardCharsets.UTF_8), CompareResult.class);
                        result.setId(id.get());
                        id.getAndIncrement();
                        return result;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).collect(Collectors.toCollection(LinkedList::new));

        return out;

    }
}
