package guru.qa.rococo.controller.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ClientUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ClientUtils() {
    }

    public static <T> List<T> convertPageToTypedList(List<?> pageData, Class<T> type) {
        List<T> typedList = new ArrayList<>(pageData.size());
        for (Object item : pageData) {
            T instance = objectMapper.convertValue(item, type);
            typedList.add(instance);
        }
        return typedList;
    }
}
