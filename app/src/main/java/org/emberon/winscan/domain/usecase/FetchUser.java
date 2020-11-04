package org.emberon.winscan.domain.usecase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.emberon.winscan.base.UseCase;
import org.emberon.winscan.data.remote.api.ApiEndPoints;
import org.emberon.winscan.domain.entity.User;

import javax.inject.Inject;

public class FetchUser extends UseCase<FetchUser.RequestValues,
        FetchUser.ResponseValue> {

    @Inject
    public FetchUser() {
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String uid = FirebaseAuth.getInstance().getUid();
        db.document(ApiEndPoints.USERS + uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = null;
                    if (documentSnapshot.exists()) {
                        user = documentSnapshot.toObject(User.class);
                    }
                    getUseCaseCallback().onSuccess(new ResponseValue(user));
                })
                .addOnFailureListener(e -> getUseCaseCallback().onError(e.getMessage()));
    }

    public static final class RequestValues implements UseCase.RequestValues {

        public RequestValues() {
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final User user;

        public ResponseValue(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }
}
