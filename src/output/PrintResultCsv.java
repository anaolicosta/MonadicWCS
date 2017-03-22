package output;

public class PrintResultCsv extends PrintResults {

	public PrintResultCsv(final String descriptionFilePath){
		super(descriptionFilePath, "csv");
	}

	public void println(final String line){
		this.getDescriptionFileWriter().println(line);
	}
	
	public void print(final String line){
		this.getDescriptionFileWriter().print(line + ",");
	}
}
