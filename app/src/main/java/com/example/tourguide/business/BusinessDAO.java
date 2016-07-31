package com.example.tourguide.business;

import java.util.List;

/**
 * Created by Onkar on 7/30/2016.
 */
public interface BusinessDAO {
    public List<BusinessModel> findbyCityName(String name);
    public BusinessModel findByName(String name);
    public BusinessModel findByLocation(double latitude, double longitude);
    public boolean insertRecord(BusinessModel businessModel);
    public boolean updateRecord(BusinessModel businessModel);
    public boolean deleteRecord(BusinessModel businessModel);

}
