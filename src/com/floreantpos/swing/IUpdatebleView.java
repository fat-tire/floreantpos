package com.floreantpos.swing;

public interface IUpdatebleView<E> {
	void initView(E e);
	boolean updateModel(E e);
}
