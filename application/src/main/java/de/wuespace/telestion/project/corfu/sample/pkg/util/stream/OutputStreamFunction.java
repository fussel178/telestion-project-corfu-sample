package de.wuespace.telestion.project.corfu.sample.pkg.util.stream;

import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.OutputStreamFullException;

public interface OutputStreamFunction<T> {
	void invoke(T t) throws OutputStreamFullException;
}
