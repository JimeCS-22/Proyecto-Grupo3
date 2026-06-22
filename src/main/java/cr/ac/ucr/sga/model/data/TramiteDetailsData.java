package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.Comentario;
import cr.ac.ucr.sga.model.entities.TramiteDetails;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TramiteDetailsData {

    private static final String FILE_PATH = "data/tramite-details.json";

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                @Override
                public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.format(formatter));
                }
            })
            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                        throws JsonParseException {
                    return LocalDateTime.parse(json.getAsString(), formatter);
                }
            })
            .create();

    public TramiteDetailsData() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    public void saveAllDetails(LinkedList<TramiteDetails> detailsList) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(detailsList, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LinkedList<TramiteDetails> getAllDetails() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return new LinkedList<>();
            }

            try (FileReader reader = new FileReader(file)) {
                Type listType = new TypeToken<LinkedList<TramiteDetails>>() {
                }.getType();
                LinkedList<TramiteDetails> details = gson.fromJson(reader, listType);
                return details != null ? details : new LinkedList<>();
            }
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    public TramiteDetails getDetailsByTramiteId(String tramiteId) {
        for (TramiteDetails details : getAllDetails().toList()) {
            if (details != null && tramiteId.equals(details.getTramiteId())) {
                return details;
            }
        }
        return new TramiteDetails(tramiteId);
    }

    public void saveDetails(TramiteDetails details) throws ListException {
        LinkedList<TramiteDetails> all = getAllDetails();

        for (int i = 1; i < all.size(); i++) {
            TramiteDetails current = all.get(i);
            if (current != null && details.getTramiteId().equals(current.getTramiteId())) {
                all.add(i, details);
                saveAllDetails(all);
                return;
            }
        }

        all.add(details);
        saveAllDetails(all);
    }

}
