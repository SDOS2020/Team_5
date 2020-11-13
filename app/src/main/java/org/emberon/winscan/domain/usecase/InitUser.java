package org.emberon.winscan.domain.usecase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.emberon.winscan.base.UseCase;
import org.emberon.winscan.data.remote.api.ApiEndPoints;
import org.emberon.winscan.domain.entity.User;

import javax.inject.Inject;

public class InitUser extends UseCase<InitUser.RequestValues,
        InitUser.ResponseValue> {

    @Inject
    public InitUser() {
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String uid = FirebaseAuth.getInstance().getUid();
        db.document(ApiEndPoints.USERS + uid).set(new User(uid))
                .addOnSuccessListener(aVoid -> getUseCaseCallback().onSuccess(new ResponseValue()))
                .addOnFailureListener(e -> getUseCaseCallback().onError(e.getMessage()));
    }

    public static final class RequestValues implements UseCase.RequestValues {

        public RequestValues() {
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public ResponseValue() {
        }
    }
}
