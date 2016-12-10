package at.wu.ac.declare.appl.design.model;

import java.io.File;

public interface ILogGenerator {
	public abstract boolean saveLog(Long logsize, Integer minTraceLength, Integer maxTraceLength, String eventLogType, File eventLogFile);
}