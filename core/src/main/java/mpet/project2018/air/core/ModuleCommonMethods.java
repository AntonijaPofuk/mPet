package mpet.project2018.air.core;

import android.support.v4.app.FragmentActivity;

import Retrofit.Model.Ljubimac;

public interface ModuleCommonMethods {

   boolean validateCodeFormat(String codeToValidate);
   Class getContainerActivity();
   void showPetDataFragment(FragmentActivity nowActivity, Ljubimac gotPet);

}
