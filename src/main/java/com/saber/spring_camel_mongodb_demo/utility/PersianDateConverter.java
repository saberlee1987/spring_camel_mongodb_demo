package com.saber.spring_camel_mongodb_demo.utility;


import com.saber.spring_camel_mongodb_demo.exceptions.BadRequestException;
import org.apache.camel.Header;
import org.springframework.stereotype.Component;

@Component
public class PersianDateConverter {

    private final DateUtility dateUtility;

    public PersianDateConverter(DateUtility dateUtility) {
        this.dateUtility = dateUtility;
    }

    public String convertPersianDateToGeorgian(@Header("country") String country,@Header("language") String language,@Header("birthDate") String birthDate) {
        if (country != null && language != null) {
            if (country.trim().toLowerCase().startsWith("ir") &&
                    (language.toLowerCase().trim().startsWith("per") || language.toLowerCase().trim().startsWith("fa"))) {
                try {
                    birthDate = dateUtility.convertPersianToGregorianDate(birthDate);
                } catch (Exception ex) {
                    throw new BadRequestException("birthDate","Please enter correct birthDate Format(valid format yyyy/MM/dd)");
                }
            }
        }
        return birthDate;
    }
}
