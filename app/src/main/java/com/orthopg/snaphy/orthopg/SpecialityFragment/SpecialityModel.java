package com.orthopg.snaphy.orthopg.SpecialityFragment;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Speciality;

/**
 * Created by nikita on 23/3/17.
 */

public class SpecialityModel {

    private Speciality speciality;
    private boolean isSpecialitySelected;

    public SpecialityModel(Speciality speciality, boolean isSpecialitySelected){

        this.speciality = speciality;
        this.isSpecialitySelected = isSpecialitySelected;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public boolean isSpecialitySelected() {
        return isSpecialitySelected;
    }

    public void setSpecialitySelected(boolean specialitySelected) {
        isSpecialitySelected = specialitySelected;
    }
}
