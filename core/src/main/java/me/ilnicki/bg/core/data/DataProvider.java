package me.ilnicki.bg.core.data;

public interface DataProvider {
    String getLocation();

    void setLocation(String path) throws DataWriteException;

    <T extends DataBean> T read(Class<T> beanClass);

    <T extends DataBean> void write(T dataCluster) throws DataWriteException;
}
