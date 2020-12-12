package com.viditjain.gridsearchpk;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseClient
{
    private static FirebaseFirestore db;

    static FirebaseFirestore getFirebaseDbInstance()
    {
        if(db==null)
        {
            db=FirebaseFirestore.getInstance();
        }
        return db;
    }
}
