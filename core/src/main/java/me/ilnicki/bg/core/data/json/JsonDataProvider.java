package me.ilnicki.bg.core.data.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ilnicki.bg.core.data.DataBean;
import me.ilnicki.bg.core.data.DataProvider;
import me.ilnicki.bg.core.data.DataWriteException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class JsonDataProvider implements DataProvider {
    private Path dataPath;
    private final Gson gson;

    public JsonDataProvider() throws DataWriteException {
        this.setLocation("./data");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting()
                .registerTypeAdapter(DataBean.class, new DataBeanSerializer());

        gson = gsonBuilder.create();
    }

    @Override
    public String getLocation() {
        return this.dataPath.toString();
    }

    @Override
    public void setLocation(String path) throws DataWriteException {
        File dataDir = new File(path);

        try {
            dataDir.mkdirs();
            this.dataPath = Paths.get(dataDir.getCanonicalPath());
        } catch (SecurityException | IOException se) {
            throw new DataWriteException(se);
        }
    }

    @Override
    public <T extends DataBean> T read(Class<T> beanClass) {
        return null;
    }


    @Override
    public void write(DataBean dataCluster) {

    }

    private String makeDataFileName(String dataName) {
        return Paths.get(this.getLocation(), dataName + ".json").toString();
    }
}
