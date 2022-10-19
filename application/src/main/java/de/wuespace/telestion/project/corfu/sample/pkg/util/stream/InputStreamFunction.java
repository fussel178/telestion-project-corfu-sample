package de.wuespace.telestion.project.corfu.sample.pkg.util.stream;

import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.InputStreamEmptyException;

public interface InputStreamFunction {
	Object invoke() throws InputStreamEmptyException;
}
