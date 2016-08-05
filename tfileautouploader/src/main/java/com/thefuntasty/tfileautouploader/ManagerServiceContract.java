package com.thefuntasty.tfileautouploader;

interface ManagerServiceContract<T> extends BaseManagerContract<T> {
	void updateItem(FileHolder<T> file, @ItemUpdate.UpdateType int updateType);
}