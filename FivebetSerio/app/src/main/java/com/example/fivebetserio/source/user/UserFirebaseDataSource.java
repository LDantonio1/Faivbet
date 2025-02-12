package com.example.fivebetserio.source.user;

import static com.example.fivebetserio.util.Constants.*;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.fivebetserio.model.User;
import com.example.fivebetserio.util.SharedPreferencesUtils;

public class UserFirebaseDataSource extends BaseUserDataRemoteDataSource{

    private final DatabaseReference databaseReference;
    private final SharedPreferencesUtils sharedPreferencesUtil;

    public UserFirebaseDataSource(SharedPreferencesUtils sharedPreferencesUtil) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
        databaseReference = firebaseDatabase.getReference().getRef(); // Inizializza il riferimento al database Firebase
        this.sharedPreferencesUtil = sharedPreferencesUtil; // Salva l'istanza di SharedPreferencesUtils
    }

    @Override
    public void saveUserData(User user) {
        // Verifica se l'utente esiste già nel database
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(user.getIdToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userResponseCallback.onSuccessFromRemoteDatabase(user); // Se l'utente esiste, ritorna successo
                } else {
                    // Se l'utente non esiste, salva i dati nel database
                    databaseReference.child(FIREBASE_USERS_COLLECTION).child(user.getIdToken()).setValue(user)
                            .addOnSuccessListener(aVoid -> {
                                userResponseCallback.onSuccessFromRemoteDatabase(user); // Notifica successo
                            })
                            .addOnFailureListener(e -> {
                                userResponseCallback.onFailureFromRemoteDatabase(e.getLocalizedMessage()); // Notifica errore
                            });
                }
            }

            @Override // Notifica errore se l'operazione viene annullata
            public void onCancelled(@NonNull DatabaseError error) {
                userResponseCallback.onFailureFromRemoteDatabase(error.getMessage());
            }
        });
    }

}
