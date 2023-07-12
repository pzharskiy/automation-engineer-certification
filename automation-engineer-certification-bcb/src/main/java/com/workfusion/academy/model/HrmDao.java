package com.workfusion.academy.model;

import lombok.*;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class HrmDao {
    private String first_name;
    private String middle_name;
    private String last_name;
    private String employee_id;
    private String other_id;
    private String driver_license_no;
    private String license_expiry_date;
    private String gender;
    private String marital_status;
    private String nationality;
    private String date_of_birth;
    private String address_street_1;
    private String address_street_2;
    private String city;
    private String state_province;
    private String zip_postal_code;
    private String country;
    private String home_telephone;
    private String mobile;
    private String work_telephone;
    private String work_email;
    private String other_email;

    public HrmDao() {
        first_name
                = middle_name
                = last_name
                = employee_id
                = other_id
                = driver_license_no
                = license_expiry_date
                = gender
                = marital_status
                = nationality
                = date_of_birth
                = address_street_1
                = address_street_2
                = city
                = state_province
                = zip_postal_code
                = country
                = home_telephone
                = mobile
                = work_telephone
                = work_email
                = other_email
                = StringUtils.EMPTY;
    }

    public String[] getAsInputRow() {
        return new String[]{
                first_name,
                middle_name,
                last_name,
                employee_id,
                other_id,
                driver_license_no,
                license_expiry_date,
                gender,
                marital_status,
                marital_status,
                nationality,
                date_of_birth,
                address_street_1,
                address_street_2,
                city,
                state_province,
                zip_postal_code,
                country,
                home_telephone,
                mobile,
                work_telephone,
                work_email,
                other_email
        };
    }
}
