package at.wu.ac.declare.appl.design.model;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import minerful.logmaker.MinerFulLogMaker;
import minerful.logmaker.params.LogMakerCmdParameters;
import nl.tue.declare.domain.model.AssignmentModel;
import at.wu.ac.declare.appl.design.gui.LogGenerationDialog;
import at.wu.ac.declare.util.OriginalDeclareMapDecoder;

public class LogCoordinator implements ILogGenerator {
	private JFrame mainFrame;
	private LogGenerationDialog logGenDialog;
	private MinerFulLogMaker logMak;
	private AssignmentModel assignmentModel;

	public LogCoordinator(JFrame mainFrame, AssignmentModel assignmentModel) {
		this.mainFrame = mainFrame;
		logGenDialog = new LogGenerationDialog(mainFrame, "", this);
		this.assignmentModel = assignmentModel;
	}
	
	public JFrame getFrame() {
		return mainFrame;
	}

	public void generateLog() {
		logGenDialog.setVisible(true);
	}

	@Override
	public boolean saveLog(Long logsize, Integer minTraceLength,
			Integer maxTraceLength, String eventLogType, File eventLogFile) {
		LogMakerCmdParameters logMakParams = new LogMakerCmdParameters(minTraceLength, maxTraceLength, logsize, eventLogFile, LogMakerCmdParameters.Encoding.valueOf(LogMakerCmdParameters.Encoding.class, eventLogType));
		this.logMak = new MinerFulLogMaker(logMakParams);
		
		this.logMak.createLog(new OriginalDeclareMapDecoder(assignmentModel).createMinerFulProcessModel());
		try {
			this.logMak.storeLog();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
}