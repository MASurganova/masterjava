package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

public class CityTestData {
    public static City MNSK;
    public static City SPB;
    public static City MOW;
    public static City KIV;
    public static List<City> CITIES;

    public static void init() {
        MNSK = new City("mnsk", "Минск");
        SPB = new City("spb", "Санкт-Петербург");
        KIV = new City("kiv", "Киев");
        MOW = new City("mow", "Москва");
        CITIES = ImmutableList.of(KIV, MNSK, MOW, SPB);
    }

    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            CITIES.forEach(dao::insert);
        });
    }
}
