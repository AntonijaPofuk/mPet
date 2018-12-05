package mpet.project2018.air.core;

import android.support.v4.app.Fragment;

public interface ModuleImplementationMethods {

    Fragment getModuleFragment();
    void validateCode();
    void outputValidationStatus(boolean validationStatus);
    void showDataInFragment();

}
