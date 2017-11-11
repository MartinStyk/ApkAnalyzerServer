package sk.styk.martin.apkanalyzer.dao;

import org.hibernate.validator.constraints.NotEmpty;
import sk.styk.martin.apkanalyzer.entity.AppData;

import java.util.List;

public interface AppDataDao extends GenericDao<AppData, Long> {

    List<AppData> getForDevice(@NotEmpty String deviceId);
}