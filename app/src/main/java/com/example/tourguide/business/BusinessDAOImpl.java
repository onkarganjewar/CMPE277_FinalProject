package com.example.tourguide.business;

import java.util.List;

/**
 * Created by Onkar on 7/30/2016.
 */
public class BusinessDAOImpl implements BusinessDAO {


    @Override
    public List<BusinessModel> findbyCityName(String name) {
        return null;
    }

    @Override
    public BusinessModel findByName(String name) {
        return null;
    }

    @Override
    public BusinessModel findByLocation(double latitude, double longitude) {
        return null;
    }

    @Override
    public boolean insertRecord(BusinessModel businessModel) {
        return false;
    }

    @Override
    public boolean updateRecord(BusinessModel businessModel) {
        return false;
    }

    @Override
    public boolean deleteRecord(BusinessModel businessModel) {
        return false;
    }
}
