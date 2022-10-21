package com.saber.spring_camel_mongodb_demo.utility;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.ULocale;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.util.Date;

@Component
public class DateUtility {

    public String convertPersianToGregorianDate(String persianDate) throws ParseException {

        ULocale persianLocale = new ULocale("fa");
        ULocale enLocale = new ULocale("en");
        SimpleDateFormat dateFormatPersian = new SimpleDateFormat("yyy/MM/dd",persianLocale);
        SimpleDateFormat dateFormatGregorian = new SimpleDateFormat("yyyy-MM-dd",enLocale);
        Date dateFormatted = dateFormatPersian.parse(persianDate);
        return persianToDecimal(dateFormatGregorian.format(dateFormatted));
    }
    private static String persianToDecimal(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }
}