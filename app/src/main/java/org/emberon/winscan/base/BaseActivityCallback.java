package org.emberon.winscan.base;

public interface BaseActivityCallback {

    void showProgressDialog(String message);

    void hideProgressDialog();

    void cancelProgressDialog();
}