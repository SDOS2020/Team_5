package org.emberon.winscan.base;


import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Runs {@link UseCase}s using a {@link UseCaseScheduler}.
 */
@Singleton
public class UseCaseHandler {
    private final UseCaseScheduler mUseCaseScheduler;

    @Inject
    public UseCaseHandler(UseCaseScheduler useCaseScheduler) {
        mUseCaseScheduler = useCaseScheduler;
    }

    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValue> void execute(
            final UseCase<T, R> useCase, T values, UseCase.UseCaseCallback<R> callback) {
        useCase.setRequestValues(values);
        useCase.setUseCaseCallback(new UiCallbackWrapper(callback, this));
        mUseCaseScheduler.execute(useCase::run);
    }

    public <V extends UseCase.ResponseValue> void notifyResponse(final V response,
                                                                 final UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.notifyResponse(response, useCaseCallback);
    }

    private <V extends UseCase.ResponseValue> void notifyError(final String message,
                                                               final UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.onError(message, useCaseCallback);
    }

    private static final class UiCallbackWrapper<V extends UseCase.ResponseValue> implements
            UseCase.UseCaseCallback<V> {
        private final UseCase.UseCaseCallback<V> mCallback;
        private final UseCaseHandler mUseCaseHandler;

        public UiCallbackWrapper(UseCase.UseCaseCallback<V> callback,
                                 UseCaseHandler useCaseHandler) {
            mCallback = callback;
            mUseCaseHandler = useCaseHandler;
        }

        @Override
        public void onSuccess(V response) {
            mUseCaseHandler.notifyResponse(response, mCallback);
        }

        @Override
        public void onError(String message) {
            mUseCaseHandler.notifyError(message, mCallback);
        }
    }
}
