package guru.qa.rococo.controller;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;

public class Constants {
    private Constants() {
    }

    public static class Artist {
        public static String ID_1 = "dd7b419d-a8e0-45dd-b9f6-2ecf4720d87b";
        public static String ID_2 = "9142d482-18fe-4055-9def-c97bd5d4a48a";
        public static String ID_3 = "1ede2ab5-ada6-4701-939d-82a29aff4dfd";
    }

    public static class Museum {
        public static String ID_1 = "3b785453-0d5b-4328-8380-5f226cb4dd5a";
        public static String ID_2 = "4a2b5f3c-1d6b-4e8a-9c7f-2d3e4f5b6a7b";
    }

    public static class Painting {
        public static String ID_1 = "3b785453-0d5b-4328-8380-5f226cb4dd5a";
        public static String ID_2 = "27ba1fd3-855b-401c-80e9-b9632fc9b9ed";
    }

    public static String getResponse(Resource resource) {
        try {
            return Files.readString(resource.getFile().toPath())
                    .replace("Constants.Artist.ID_1", Constants.Artist.ID_1)
                    .replace("Constants.Artist.ID_2", Constants.Artist.ID_2)
                    .replace("Constants.Artist.ID_3", Constants.Artist.ID_3)
                    .replace("Constants.Museum.ID_1", Constants.Museum.ID_1)
                    .replace("Constants.Museum.ID_2", Constants.Museum.ID_2)
                    .replace("Constants.Painting.ID_1", Constants.Painting.ID_1)
                    .replace("Constants.Painting.ID_2", Constants.Painting.ID_2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
