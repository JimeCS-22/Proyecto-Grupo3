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
import cr.ac.ucr.sga.model.entities.Tramite;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TramiteData {
    private static final String FILE_PATH = "data/tramites.json";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

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

    public TramiteData() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    public void saveAllTramites(LinkedList<Tramite> tramites) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(tramites, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Tramite> getAllTramites() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return new LinkedList<>();
            }

            try (FileReader reader = new FileReader(file)) {
                Type listType = new TypeToken<LinkedList<Tramite>>() {}.getType();
                LinkedList<Tramite> tramites = gson.fromJson(reader, listType);
                return tramites != null ? tramites : new LinkedList<>();
            }
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    public LinkedList<Tramite> getTramitesPendientes() throws ListException {
        LinkedList<Tramite> todos = getAllTramites();
        LinkedList<Tramite> pendientes = new LinkedList<>();

        for (Tramite t : todos.toList()) {
            if ("Pendiente".equals(t.getNombreEstado())) {
                pendientes.add(t);
            }
        }
        return pendientes;
    }

    public void addTramite(Tramite tramite) {
        LinkedList<Tramite> tramites = getAllTramites();
        tramites.add(tramite);
        saveAllTramites(tramites);
    }

    public void updateTramite(Tramite tramiteActualizado) throws ListException {
        LinkedList<Tramite> tramites = getAllTramites();

        for (int i = 0; i < tramites.size(); i++) {
            if (tramites.get(i).getId().equals(tramiteActualizado.getId())) {
                tramites.add(i, tramiteActualizado);
                saveAllTramites(tramites);
                return;
            }
        }
    }

    public Tramite getTramiteById(String id) {
        LinkedList<Tramite> tramites = getAllTramites();

        for (Tramite t : tramites.toList()) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }
}