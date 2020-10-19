package org.emberon.winscan.base;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}
