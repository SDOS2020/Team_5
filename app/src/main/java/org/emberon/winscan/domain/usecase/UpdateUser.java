package org.emberon.winscan.domain.usecase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.emberon.winscan.base.UseCase;
import org.emberon.winscan.data.remote.api.ApiEndPoints;
import org.emberon.winscan.domain.entity.User;

import javax.inject.Inject;

public class UpdateUser extends UseCase<UpdateUser.RequestValues,
        UpdateUser.ResponseValue> {

    @Inject
    public UpdateUser() {
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String uid = FirebaseAuth.getInstance().getUid();
        db.document(ApiEndPoints.USERS + uid).set(requestValues.getUser())
                .addOnSuccessListener(aVoid -> getUseCaseCallback().onSuccess(new ResponseValue()))
                .addOnFailureListener(e -> getUseCaseCallback().onError(e.getMessage()));
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final User user;

        public RequestValues(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public ResponseValue() {
        }
    }
}
