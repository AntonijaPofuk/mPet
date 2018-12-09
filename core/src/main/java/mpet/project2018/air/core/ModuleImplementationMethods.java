package mpet.project2018.air.core;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import Retrofit.Model.Ljubimac;

public interface ModuleImplementationMethods {

    Fragment getModuleFragment();
    void validateCode(String code);
    void outputValidationStatus(boolean validationStatus);
    void showDataInFragment(FragmentActivity nowActivity, Ljubimac gotPet);

}
