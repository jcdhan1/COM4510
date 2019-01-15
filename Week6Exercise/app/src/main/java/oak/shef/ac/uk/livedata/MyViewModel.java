/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.livedata;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import oak.shef.ac.uk.livedata.database.NumberData;

public class MyViewModel extends AndroidViewModel {
    private final MyRepository mRepository;

    private LiveData<NumberData> numberToDisplay;

    public MyViewModel (Application application) {
        super(application);
        // creation and connection to the Repository
        mRepository = new MyRepository(application);
        // connection to the live data
        numberToDisplay = mRepository.getNumberData();
    }


    /**
     * getter for the live data
     * @return
     */
    public LiveData<NumberData> getNumberDataToDisplay() {
        if (numberToDisplay == null) {
            numberToDisplay = new MutableLiveData<NumberData>();
        }
        return numberToDisplay;
    }

    /**
     * request by the UI to generate a new random number
     */
    public void generateNewNumber() {
        mRepository.generateNewNumber();
    }
}
